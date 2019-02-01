package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.reference.Names;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Keridos on 01.10.14.
 * This Class is the base for all TileEntities within this mod.
 */
public class TileEntityPhantomLight extends TileEntity {

    /**
     * Map of floodlights owning this phantom light. Each position is associated with an
     * {@link TileEntityMetaFloodlight#floodlightId} to confirm ownership.
     */
    private Map<BlockPos, Integer> floodlights = new HashMap<>();
    private Block lightBlock;

    void addSource(BlockPos pos, int floodlightId) {
        if (floodlights.containsKey(pos))
            return;

        floodlights.put(pos, floodlightId);
        markDirty();
    }

    void removeSource(BlockPos pos) {
        removeSource(pos, true);
    }

    private void removeSource(BlockPos pos, boolean updateSources) {
        if (updateSources) {
            floodlights.remove(pos);
            markDirty();
        }
        if (floodlights.isEmpty() && hasWorld())
            world.setBlockToAir(this.pos);
    }

    public void setLightBlock(Block lightBlock) {
        this.lightBlock = lightBlock;
    }

    public void invalidateSources() {
        if (!hasWorld() || world.isRemote)
            return;

        if (!floodlights.isEmpty()) {
            Iterator<BlockPos> iter = floodlights.keySet().iterator();
            while (iter.hasNext()) {
                BlockPos pos = iter.next();
                TileEntity te = world.getTileEntity(pos);
                boolean remove;
                if (te instanceof TileEntityMetaFloodlight) {
                    TileEntityMetaFloodlight floodlight = (TileEntityMetaFloodlight) te;
                    int floodlightId = floodlights.get(pos);
                    remove = lightBlock != floodlight.lightBlock || !floodlight.hasLight() || floodlightId != floodlight.floodlightId;
                } else
                    remove = true;

                if (remove) {
                    removeSource(pos, false);
                    iter.remove();
                }
            }
        } else
            world.setBlockToAir(pos);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.SOURCES)) {
            NBTTagList list = nbtTagCompound.getTagList(Names.NBT.SOURCES, Constants.NBT.TAG_INT_ARRAY);
            for (int i = 0; i < list.tagCount(); i++) {
                int[] array = list.getIntArrayAt(i);
                if (array.length < 4) {
                    // Phantom lights using the old storage format (without floodlight id) shouldn't exist.
                    continue;
                }
                BlockPos pos = new BlockPos(array[0], array[1], array[2]);
                floodlights.put(pos, array[3]);
            }
        }

        if (nbtTagCompound.hasKey(Names.NBT.LIGHT_BLOCK))
            lightBlock = Block.getBlockFromName(nbtTagCompound.getString(Names.NBT.LIGHT_BLOCK));
        else
            lightBlock = Blocks.STONE;

        invalidateSources();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    @Nonnull
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeToNBT(nbtTagCompound);
        if (!this.floodlights.isEmpty()) {
            NBTTagList SourceList = new NBTTagList();
            for (BlockPos source : floodlights.keySet()) {
                int[] array = new int[] {source.getX(), source.getY(), source.getZ(), floodlights.get(source)};
                SourceList.appendTag(new NBTTagIntArray(array));
            }
            nbtTagCompound.setTag(Names.NBT.SOURCES, SourceList);
        }

        nbtTagCompound.setString(Names.NBT.LIGHT_BLOCK, lightBlock.getRegistryName().toString());
        return nbtTagCompound;
    }
}
