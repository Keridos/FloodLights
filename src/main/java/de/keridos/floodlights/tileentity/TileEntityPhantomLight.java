package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.reference.Names;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
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
public class TileEntityPhantomLight extends TileEntity {
    private ArrayList<BlockPos> sources;

    public TileEntityPhantomLight() {
        super();
        sources = new ArrayList<>();
    }



    void addSource(BlockPos pos) {

        if (sources.contains(pos)) {
            return;
        }
        sources.add(pos);
    }

    private void removeSource(Iterator<BlockPos> iter, boolean remove) {
        iter.remove();
        if (sources.isEmpty() && this.hasWorldObj() && remove) {
            worldObj.setBlockToAir(this.pos);
        }
    }

    void removeSource(BlockPos pos) {
        sources.remove(pos);
        if (sources.isEmpty() && this.hasWorldObj()) {
            worldObj.setBlockToAir(this.pos);
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
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.SOURCES)) {
            NBTTagList list = nbtTagCompound.getTagList(Names.NBT.SOURCES, Constants.NBT.TAG_INT_ARRAY);
            for (int i = 0; i < list.tagCount(); i++) {
                sources.add(getPosFromIntArray(list.getIntArrayAt(i)));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeToNBT(nbtTagCompound);
        if (!this.sources.isEmpty()) {
            NBTTagList SourceList = new NBTTagList();
            for (BlockPos source : sources) {
                SourceList.appendTag(new NBTTagIntArray(getIntArrayFromPos(source)));
            }
            nbtTagCompound.setTag(Names.NBT.SOURCES, SourceList);
        }
        return nbtTagCompound;
    }
}
