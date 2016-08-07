package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.GeneralUtil;
import de.keridos.floodlights.util.RandomUtil;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;

import static de.keridos.floodlights.block.BlockPhantomLight.UPDATE;
import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;
import static de.keridos.floodlights.util.MathUtil.rotate;

/**
 * Created by Keridos on 04.05.2015.
 * This Class is the tile entity for the small floodlight.
 */

public class TileEntityGrowLight extends TileEntityFLElectric {
    private long nextGrowTick = 0;

    @Override
    public boolean canConnectEnergy(EnumFacing from) {
        return (from.getOpposite().ordinal() == orientation.ordinal());
    }


    public void growSource(boolean remove) {
        int[] rotatedCoords = rotate(1, 0, 0, this.orientation);
        int x = this.pos.getX() + rotatedCoords[0];
        int y = this.pos.getY() + rotatedCoords[1];
        int z = this.pos.getZ() + rotatedCoords[2];
        BlockPos blockPos = new BlockPos(x, y, z);
        if (remove) {
            if (worldObj.getBlockState(blockPos).getBlock() == ModBlocks.blockPhantomLight) {
                TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(blockPos);
                light.removeSource(this.pos);
            }
        } else if (worldObj.getBlockState(blockPos).getBlock().isAir(worldObj.getBlockState(blockPos), worldObj, blockPos)) {
            setLight(blockPos);
            worldObj.setBlockState(blockPos, worldObj.getBlockState(blockPos).withProperty(UPDATE, false));
        } else if (worldObj.getBlockState(blockPos).getBlock() == ModBlocks.blockPhantomLight) {
            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(blockPos);
            light.addSource(this.pos);
        }

    }

    public void update() {
        super.update();
        World world = this.getWorld();
        if (!world.isRemote) {
            int realEnergyUsage = ConfigHandler.energyUsageGrowLight;
            tryDischargeItem(inventory[0]);
            if (timeout > 0) {
                timeout--;
                return;
            }
            if (active && (storage.getEnergyStored() >= realEnergyUsage || storageEU >= (double) realEnergyUsage / 8.0D)) {
                if (world.getTotalWorldTime() > nextGrowTick) {
                    BlockPos blockPosTarget = new BlockPos(this.pos.getX() + this.orientation.getFrontOffsetX() * 2, this.pos.getY() + this.orientation.getFrontOffsetY() * 2, this.pos.getZ() + this.orientation.getFrontOffsetZ() * 2);
                    BlockPos blockPosFront = new BlockPos(this.pos.getX() + this.orientation.getFrontOffsetX(), this.pos.getY() + this.orientation.getFrontOffsetY(), this.pos.getZ() + this.orientation.getFrontOffsetZ());
                    Block block = worldObj.getBlockState(blockPosTarget).getBlock();
                    Block blockFront = worldObj.getBlockState(blockPosFront).getBlock();
                    if (GeneralUtil.isBlockValidGrowable(block, world, blockPosTarget) && blockFront.isAir(worldObj.getBlockState(blockPosFront), world, blockPosFront)) {
                        if (block instanceof IPlantable) {
                            for (int i= 0;i<5;i++){
                                block.randomTick(world, blockPosTarget, world.getBlockState(blockPosTarget), RandomUtil.random);
                            }
                        } else if (block instanceof IGrowable)
                            ((IGrowable) block).grow(world, RandomUtil.random, blockPosTarget, world.getBlockState(blockPosTarget));
                    }
                    nextGrowTick = world.getTotalWorldTime() + RandomUtil.getRandomTickTimeoutGrowing();
                }
                if (update) {
                    if (mode == 0) {
                        growSource(true);
                        growSource(false);
                    }
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 3);
                    update = false;
                } else if (!wasActive) {
                    if (mode == 0) {
                        growSource(false);
                    }
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 3);
                }
                if (storageEU >= (double) realEnergyUsage / 8.0D) {
                    storageEU -= (double) realEnergyUsage / 8.0D;
                } else {
                    storage.modifyEnergyStored(-realEnergyUsage);
                }
                wasActive = true;
            } else if ((!active || (storage.getEnergyStored() < realEnergyUsage && storageEU < (double) realEnergyUsage / 8.0D)) && wasActive) {
                if (mode == 0) {
                    growSource(true);
                }
                world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, false), 3);
                wasActive = false;
                timeout = ConfigHandler.timeoutFloodlights;
                update = false;
            }
        }
    }

    public void changeMode(EntityPlayer player) {
        World world = this.getWorld();
        if (!world.isRemote) {
            if (mode == 0) {
                growSource(true);
            }
            mode = (mode == 1 ? 0 : mode + 1);
            if (active && (storage.getEnergyStored() >= ConfigHandler.energyUsage || storageEU >= ConfigHandler.energyUsage / 8.0D) && mode == 0) {
                growSource(false);
            }
            String modeString = (mode == 0 ? Names.Localizations.LIGHTING : Names.Localizations.DARK_LIGHT);
            player.addChatMessage(new TextComponentString(safeLocalize(Names.Localizations.MODE) + ": " + safeLocalize(modeString)));
        }
    }
}
