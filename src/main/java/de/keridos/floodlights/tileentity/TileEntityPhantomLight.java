package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.reference.Names;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.Iterator;

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
        removeLightOnUpdate = false;
        update = false;
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
        return false;
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

    public void removeSource(Iterator<BlockPos> iter, boolean remove) {
        iter.remove();
        if (sources.isEmpty() && this.hasWorldObj() && remove) {
            if (!worldObj.setBlockToAir(this.pos)) {
                update = true;
                removeLightOnUpdate = true;
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
                removeLightOnUpdate = true;
            }
        }
    }

    public void updateAllSources(boolean remove) {
        Iterator<BlockPos> iter = sources.iterator();
        while (iter.hasNext()) {
            BlockPos pos = iter.next();
            if (worldObj != null) {
                TileEntity te = worldObj.getTileEntity(pos);
                if (te != null && te instanceof TileEntityMetaFloodlight) {
                    ((TileEntityMetaFloodlight) te).toggleUpdateRun();
                } else {
                    this.removeSource(iter, remove);
                }
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        if (nbtTagCompound.hasKey(Names.NBT.SOURCES)) {
            NBTTagList list = nbtTagCompound.getTagList(Names.NBT.SOURCES, Constants.NBT.TAG_INT_ARRAY);
            for (int i = 0; i < list.tagCount(); i++) {
                sources.add(getPosFromIntArray(list.getIntArrayAt(i)));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
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
        if (!worldObj.isRemote && update && worldObj.getWorldTime() % 20 == 11) {
            if (removeLightOnUpdate) {
                if (worldObj.setBlockToAir(this.pos)) {
                    return;
                }
            } else {
                Iterator<BlockPos> iter = sources.iterator();
                while (iter.hasNext()) {
                    BlockPos pos = iter.next();
                    if (worldObj != null) {
                        TileEntity te = worldObj.getTileEntity(pos);
                        if (te != null && te instanceof TileEntityMetaFloodlight && ((TileEntityMetaFloodlight) te).getWasActive()) {
                            this.removeSource(iter, true);
                        }
                    }
                }
            }
            if (sources.isEmpty()) {
                if (worldObj.setBlockToAir(this.pos)) {
                    update = false;
                }
            } else {
                update = false;
            }
        }
    }
}
