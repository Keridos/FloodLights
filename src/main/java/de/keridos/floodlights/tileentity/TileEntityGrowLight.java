package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.util.GeneralUtil;
import de.keridos.floodlights.util.RandomUtil;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Keridos on 04.05.2015.
 * This Class is the tile entity for the small floodlight.
 */

public class TileEntityGrowLight extends TileEntityFLElectric {

    private long nextGrowTick = 0;

    public TileEntityGrowLight() {
        mode = LIGHT_MODE_STRAIGHT;
        rangeStraight = 1;
        energyUsage = ConfigHandler.energyUsageGrowLight;
        updateEnergyUsage();
    }

    public void update() {
        super.update();

        if (world.isRemote || !isReady() || !active && !hasEnergy())
            return;

        // GrowLight-only logic below
        if (world.getWorldTime() > nextGrowTick) {
            BlockPos blockPosTarget = new BlockPos(
                    pos.getX() + getOrientation().getFrontOffsetX() * 2,
                    pos.getY() + getOrientation().getFrontOffsetY() * 2,
                    pos.getZ() + getOrientation().getFrontOffsetZ() * 2
            );
            BlockPos blockPosFront = new BlockPos(
                    pos.getX() + getOrientation().getFrontOffsetX(),
                    pos.getY() + getOrientation().getFrontOffsetY(),
                    pos.getZ() + getOrientation().getFrontOffsetZ()
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

    @Override
    public EnumFacing getOrientation() {
        return EnumFacing.DOWN;
    }
}
