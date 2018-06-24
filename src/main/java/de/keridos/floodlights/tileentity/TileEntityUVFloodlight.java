package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.handler.ConfigHandler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static de.keridos.floodlights.init.ModBlocks.blockPhantomUVLight;

/**
 * Created by Keridos on 15/09/2015.
 * This Class
 */
public class TileEntityUVFloodlight extends TileEntityFLElectric {

    public TileEntityUVFloodlight() {
        super();
    }

    @Override
    public void setLight(BlockPos pos) {
        if (world.setBlockState(pos, blockPhantomUVLight.getDefaultState())) {
            TileEntityPhantomUVLight light = (TileEntityPhantomUVLight) world.getTileEntity(pos);
            light.addSource(this.pos);
        }
    }

    public void uvSource(boolean remove) {
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
                    uvSource(true);
                    uvSource(false);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    update = false;
                } else if (!wasActive) {
                    uvSource(false);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                }

                energy.extractEnergy(realEnergyUsage, false);
                wasActive = true;
            } else if ((!active || energy.getEnergyStored() < realEnergyUsage) && wasActive) {
                uvSource(true);
                world.setBlockState(this.pos, world.getBlockState(this.pos).getBlock().getStateFromMeta(this.getOrientation().ordinal()), 2);
                wasActive = false;
            }
        }
    }
}

