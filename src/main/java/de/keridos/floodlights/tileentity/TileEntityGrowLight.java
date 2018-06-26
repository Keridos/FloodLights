package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.util.GeneralUtil;
import de.keridos.floodlights.util.RandomUtil;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Keridos on 04.05.2015.
 * This Class is the tile entity for the small floodlight.
 */

public class TileEntityGrowLight extends TileEntityFLElectric {
    private long nextGrowTick = 0;

    /*@Override
    public boolean canConnectEnergy(EnumFacing from) {
        return (from.getOpposite().ordinal() == orientation.ordinal());
    }*/

    public TileEntityGrowLight() {
        mode = LIGHT_MODE_STRAIGHT;
        rangeStraight = 1;
        energyUsage = ConfigHandler.energyUsageGrowLight;
    }


    /*@SuppressWarnings("ConstantConditions")
    @Override
    public void straightSource(boolean remove) {
        int[] rotatedCoords = MathUtil.rotate(1, 0, 0, this.orientation);

        int x = this.pos.getX() + rotatedCoords[0];
        int y = this.pos.getY() + rotatedCoords[1];
        int z = this.pos.getZ() + rotatedCoords[2];
        BlockPos blockPos = new BlockPos(x, y, z);
        if (remove) {
            if (world.getBlockState(blockPos).getBlock() == ModBlocks.blockPhantomLight) {
                TileEntityPhantomLight light = (TileEntityPhantomLight) world.getTileEntity(blockPos);
                light.removeSource(this.pos);
            }
        } else if (world.getBlockState(blockPos).getBlock().isAir(world.getBlockState(blockPos), world, blockPos)) {
            setLight(blockPos);
            world.setBlockState(blockPos, world.getBlockState(blockPos).withProperty(UPDATE, false));
        } else if (world.getBlockState(blockPos).getBlock() == ModBlocks.blockPhantomLight) {
            TileEntityPhantomLight light = (TileEntityPhantomLight) world.getTileEntity(blockPos);
            light.addSource(this.pos);
        }

    }*/

    public void update() {
        super.update();

        if (world.isRemote || !isReady() || !active && !hasEnergy())
            return;

        // GrowLight-only logic below
        if (world.getWorldTime() > nextGrowTick) {
            BlockPos blockPosTarget = new BlockPos(
                    pos.getX() + orientation.getFrontOffsetX() * 2,
                    pos.getY() + orientation.getFrontOffsetY() * 2,
                    pos.getZ() + orientation.getFrontOffsetZ() * 2
            );
            BlockPos blockPosFront = new BlockPos(
                    pos.getX() + orientation.getFrontOffsetX(),
                    pos.getY() + orientation.getFrontOffsetY(),
                    pos.getZ() + orientation.getFrontOffsetZ()
            );
            Block block = world.getBlockState(blockPosTarget).getBlock();
            Block blockFront = world.getBlockState(blockPosFront).getBlock();
            if (GeneralUtil.isBlockValidGrowable(block, world, blockPosTarget) && blockFront.isAir(world.getBlockState(blockPosFront), world, blockPosFront)) {
                ((IGrowable) block).grow(world, RandomUtil.random, blockPosTarget, world.getBlockState(blockPosTarget));
            }

            nextGrowTick = world.getWorldTime() + RandomUtil.getRandomTickTimeoutFromFloatChance(ConfigHandler.chanceGrowLight);
        }
    }

    @Override
    public void setMode(int mode) {
        // Mode cannot be changed
    }

    @Override
    public void changeMode(EntityPlayer player) {
        // Mode cannot be changed
    }
}
