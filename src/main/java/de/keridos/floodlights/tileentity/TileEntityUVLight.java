package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.handler.ConfigHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static de.keridos.floodlights.block.BlockPhantomLight.UPDATE;
import static de.keridos.floodlights.init.ModBlocks.blockUVLightBlock;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class TileEntityUVLight extends TileEntityFLElectric {

    public TileEntityUVLight() {
        super();
    }

    public void setLightUV(BlockPos pos) {
        if (world.setBlockState(pos, blockUVLightBlock.getDefaultState())) {
            TileEntityUVLightBlock light = (TileEntityUVLightBlock) world.getTileEntity(pos);
            light.addSource(this.pos);
        }
    }

    public void setSource(BlockPos blockPos, boolean remove, Integer setSourceUpdate) {
        if (remove) {
            if (world.getBlockState(blockPos).getBlock() == blockUVLightBlock && setSourceUpdate == 0) {
                TileEntityPhantomLight light = (TileEntityPhantomLight) world.getTileEntity(blockPos);
                light.removeSource(this.pos);
            } else if (world.getBlockState(blockPos).getBlock() == blockUVLightBlock) {
                world.setBlockState(blockPos, world.getBlockState(blockPos).withProperty(UPDATE, false), 4);
            }
        } else if (world.getBlockState(blockPos).getBlock() == blockUVLightBlock && setSourceUpdate == 2) {
            world.setBlockState(blockPos, world.getBlockState(blockPos).withProperty(UPDATE, false), 4);
        } else if (world.getBlockState(blockPos).getBlock() == blockUVLightBlock && setSourceUpdate == 0) {
            world.setBlockState(blockPos, world.getBlockState(blockPos).withProperty(UPDATE, true), 4);
        } else if (world.getBlockState(blockPos).getBlock() == blockUVLightBlock) {
            TileEntityUVLightBlock light = (TileEntityUVLightBlock) world.getTileEntity(blockPos);
            light.addSource(this.pos);
        } else if (world.getBlockState(blockPos).getBlock().isAir(world.getBlockState(blockPos), world, blockPos) && setSourceUpdate == 1) {
            setLightUV(blockPos);
        }
    }

    public void UVSource(boolean remove) {
        for (int k = (remove ? 1 : 2); k >= 0; k--) {
            for (int i = 1; i <= ConfigHandler.rangeUVFloodlight; i++) {
                BlockPos tempPos = new BlockPos(this.pos.getX() + this.orientation.getFrontOffsetX() * i,
                        this.pos.getY() + this.orientation.getFrontOffsetY() * i,
                        this.pos.getZ() + this.orientation.getFrontOffsetZ() * i);
                setSource(tempPos, remove, k);
                if (world.getBlockState(tempPos).getBlock().isOpaqueCube(world.getBlockState(tempPos))) {
                    break;
                }
            }
        }
    }

    @Override
    public void update() {
        super.update();
        World world = this.getWorld();
        if (!world.isRemote) {
            int realEnergyUsage = ConfigHandler.energyUsageUVFloodlight;
            tryDischargeItem(inventory.getStackInSlot(0));
            if (active && energy.getEnergyStored() >= realEnergyUsage) {
                if (update) {
                    UVSource(true);
                    UVSource(false);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    update = false;
                } else if (!wasActive) {
                    UVSource(false);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                }

                energy.extractEnergy(realEnergyUsage, false);
                wasActive = true;
            } else if ((!active || energy.getEnergyStored() < realEnergyUsage) && wasActive) {
                UVSource(true);
                world.setBlockState(this.pos, world.getBlockState(this.pos).getBlock().getStateFromMeta(this.getOrientation().ordinal()), 2);
                wasActive = false;
            }
        }
    }
}

