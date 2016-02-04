package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.reference.Names;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;

import static de.keridos.floodlights.util.GeneralUtil.getIntArrayFromPos;
import static de.keridos.floodlights.util.GeneralUtil.getPosFromIntArray;

/**
 * Created by Keridos on 01.10.14.
 * This Class is the base for all TileEntities within this mod.
 */
public class TileEntityPhantomLight extends TileEntity implements ITickable {
    protected ArrayList<BlockPos> sources = new ArrayList<BlockPos>();
    protected boolean update = true;
    protected boolean removeLightOnUpdate = true;

    public TileEntityPhantomLight() {
        super();
    }

    public void addSource(BlockPos pos) {
        for (BlockPos source : sources) {
            if (source.equals(pos)) {
                return;
            }
        }
        sources.add(pos);
        removeLightOnUpdate = false;
        update = false;
    }

    public void removeSource(BlockPos pos, boolean remove) {
        for (BlockPos source : sources) {
            if (source.equals(pos)) {
                sources.remove(source);
                break;
            }
        }
        if (sources.isEmpty() && this.hasWorldObj() && remove) {
            if (!worldObj.setBlockToAir(this.pos)) {
                update = true;
            } else {
                removeLightOnUpdate = true;
                worldObj.markBlockForUpdate(this.pos);
            }
        }
    }

    public void removeSource(BlockPos pos) {
        for (BlockPos source : sources) {
            if (source.equals(pos)) {
                sources.remove(source);
                break;
            }
        }
        if (sources.isEmpty() && this.hasWorldObj()) {
            if (!worldObj.setBlockToAir(this.pos)) {
                update = true;
            } else {
                removeLightOnUpdate = true;
                worldObj.markBlockForUpdate(this.pos);
            }
        }
    }

    public void updateAllSources(boolean remove) {
        for (BlockPos source : sources) {
            TileEntity te = worldObj.getTileEntity(source);
            if (te != null && te instanceof TileEntityMetaFloodlight) {
                ((TileEntityMetaFloodlight) te).toggleUpdateRun();
            } else {
                this.removeSource(source, remove);
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.SOURCES)) {
            NBTTagList list = nbtTagCompound.getTagList(Names.NBT.SOURCES, Constants.NBT.TAG_INT_ARRAY);
            for (int i = 0; i < list.tagCount(); i++) {
                sources.add(getPosFromIntArray(list.getIntArrayAt(i)));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        if (!this.sources.isEmpty()) {
            NBTTagList SourceList = new NBTTagList();
            for (BlockPos source : sources) {
                SourceList.appendTag(new NBTTagIntArray(getIntArrayFromPos(source)));
            }
            nbtTagCompound.setTag(Names.NBT.SOURCES, SourceList);
        }
    }

    @Override
    public void update() {
        if (!worldObj.isRemote && update &&worldObj.getWorldTime() % 20 == 11) {
            if (removeLightOnUpdate) {
                if (worldObj.setBlockToAir(this.pos)) {
                    worldObj.markBlockForUpdate(this.pos);
                    return;
                }
            }
            if (sources.isEmpty()) {
                if (worldObj.setBlockToAir(this.pos)) {
                    worldObj.markBlockForUpdate(this.pos);
                    update = false;
                }
            } else {
                update = false;
            }
        }
    }
}
