package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.block.BlockFLColorableMachine;
import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 01.10.14.
 * This Class is the electric floodlight TileEntity.
 */

public class TileEntityElectricFloodlight extends TileEntityFLElectric {
    @Override
    public void update() {
        super.update();
        World world = this.getWorld();
        if (!world.isRemote) {
            int realEnergyUsage = ConfigHandler.energyUsage * (_mode == LIGHT_MODE_STRAIGHT ? 1 : 4);
            tryDischargeItem(inventory.getStackInSlot(0));
            if (timeout > 0) {
                timeout--;
                return;
            }
            if (active && (energy.getEnergyStored() >= realEnergyUsage)) {
                if (update) {
                    removeSource(this._mode);
                    addSource(this._mode);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                    update = false;
                } else if (!wasActive) {
                    addSource(this._mode);
                    world.setBlockState(this.pos, world.getBlockState(this.pos).withProperty(BlockFLColorableMachine.ACTIVE, true), 2);
                }

                energy.extractEnergy(realEnergyUsage, false);
                wasActive = true;
            } else if ((!active || energy.getEnergyStored() < realEnergyUsage) && wasActive) {
                removeSource(this._mode);
                world.setBlockState(this.pos, world.getBlockState(this.pos).getBlock().getStateFromMeta(this.getOrientation().ordinal()), 2);
                wasActive = false;
                timeout = ConfigHandler.timeoutFloodlights;
                update = false;
            }
        }
    }

    private void addSource(int mode) {
        if (mode == -1) {
            mode = this._mode;
        }
        if (mode == 0) {
            straightSource(false);
        } else if (mode == 1) {
            narrowConeSource(false);
        } else if (mode == 2) {
            wideConeSource(false);
        }
    }

    public void removeSource(int mode) {
        if (mode == -1) {
            mode = this._mode;
        }
        if (mode == 0) {
            straightSource(true);
        } else if (mode == 1) {
            narrowConeSource(true);
        } else if (mode == 2) {
            wideConeSource(true);
        }
    }

    public void changeMode(EntityPlayer player) {
        World world = this.getWorld();
        if (!world.isRemote) {
            int realEnergyUsage = ConfigHandler.energyUsage * (_mode == LIGHT_MODE_STRAIGHT ? 1 : 4);

            removeSource(this._mode);
            _mode = (_mode == LIGHT_MODE_WIDE_CONE ? LIGHT_MODE_STRAIGHT : _mode + 1);
            if (active && energy.getEnergyStored() >= realEnergyUsage) {
                addSource(this._mode);
            }
            String modeString = "";
            switch (_mode) {
                case LIGHT_MODE_STRAIGHT:
                    modeString = Names.Localizations.STRAIGHT;
                    break;
                case LIGHT_MODE_NARROW_CONE:
                    modeString = Names.Localizations.NARROW_CONE;
                    break;
                case LIGHT_MODE_WIDE_CONE:
                    modeString = Names.Localizations.WIDE_CONE;
                    break;
            }
            player.sendMessage(new TextComponentString(safeLocalize(Names.Localizations.MODE) + ": " + safeLocalize(modeString)));
        }
    }
}
