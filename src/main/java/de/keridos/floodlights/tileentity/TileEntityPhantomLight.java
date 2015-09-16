package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.reference.Names;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;

/**
 * Created by Keridos on 01.10.14.
 * This Class is the base for all TileEntities within this mod.
 */
public class TileEntityPhantomLight extends TileEntity {
    protected ArrayList<int[]> sources = new ArrayList<int[]>();

    public TileEntityPhantomLight() {
        super();
    }

    public void addSource(int x, int y, int z) {
        for (int[] source : sources) {
            if (source[0] == x && source[1] == y && source[2] == z) {
                return;
            }
        }
        sources.add(new int[]{x, y, z});
    }

    public void removeSource(int x, int y, int z) {
        for (int[] source : sources) {
            if (source[0] == x && source[1] == y && source[2] == z) {
                sources.remove(source);
                break;
            }
        }
        if (sources.isEmpty()) {
            worldObj.setBlockToAir(this.xCoord, this.yCoord, this.zCoord);
            worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }
    }

    public void updateAllSources() {
        for (int[] source : sources) {
            TileEntity te = worldObj.getTileEntity(source[0], source[1], source[2]);
            if (te != null && te instanceof TileEntityMetaFloodlight) {
                ((TileEntityMetaFloodlight) te).toggleUpdateRun();
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.SOURCES)) {
            NBTTagList list = nbtTagCompound.getTagList(Names.NBT.ITEMS, Constants.NBT.TAG_INT_ARRAY);
            for (int i = 0; i < list.tagCount(); i++) {
                sources.add(list.func_150306_c(i));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
        if (!this.sources.isEmpty()) {
            NBTTagList SourceList = new NBTTagList();
            for (int[] source : sources) {
                SourceList.appendTag(new NBTTagIntArray(source));
            }
            nbtTagCompound.setTag(Names.NBT.SOURCES, SourceList);
        }
    }

    @Override
    public boolean canUpdate() {
        return false;
    }
}
