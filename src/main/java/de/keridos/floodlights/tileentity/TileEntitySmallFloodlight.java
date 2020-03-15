package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.MathUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

/**
 * Created by Keridos on 04.05.2015.
 * This Class is the tile entity for the small floodlight.
 */

public class TileEntitySmallFloodlight extends TileEntityFLElectric {
    private boolean rotationState;

    public TileEntitySmallFloodlight() {
        super();
        rotationState = false;
        mode = LIGHT_MODE_STRAIGHT;
        energyUsage = ConfigHandler.energyUsageSmallFloodlight;
        updateEnergyUsage();
    }

    @Override
    public void readOwnFromNBT(NBTTagCompound nbtTagCompound) {
        super.readOwnFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.ROTATION_STATE)) {
            this.rotationState = nbtTagCompound.getBoolean(Names.NBT.ROTATION_STATE);
        }
    }

    @Override
    public NBTTagCompound writeOwnToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeOwnToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(Names.NBT.ROTATION_STATE, rotationState);
        return nbtTagCompound;
    }

    public boolean getRotationState() {
        return rotationState;
    }

    public void setRotationState(boolean rotationState) {
        this.rotationState = rotationState;
    }

    @Override
    public void straightSource(boolean remove) {
        LightSwitchExecutor executor = new LightSwitchExecutor(remove);
        for (int i = 0; i < 5; i++) {
            int a = 0;
            int b = 0;
            int c = 0;
            if (i == 0) {
                a = 1;
                b = c = 0;
            } else if (i == 1) {
                a = c = 0;
                b = 1;
            } else if (i == 2) {
                a = c = 0;
                b = -1;
            } else if (i == 3) {
                a = b = 0;
                c = 1;
            } else if (i == 4) {
                a = b = 0;
                c = -1;
            }
            int[] rotatedCoords = MathUtil.rotate(a, b, c, this.orientation);
            int x = this.pos.getX() + rotatedCoords[0];
            int y = this.pos.getY() + rotatedCoords[1];
            int z = this.pos.getZ() + rotatedCoords[2];

            executor.add(new BlockPos(x, y, z));
        }

        executor.execute();
    }

    @Override
    public void setMode(int mode) {
        // Mode cannot be changed
    }

    @Override
    public void changeMode(EntityPlayer player) {
        // Mode cannot be changed
        rotationState = !rotationState;
        //this.world.markBlocksDirtyVertical(this.pos.getX(), this.pos.getZ(), this.pos.getX(), this.pos.getZ());
        markDirty();
    }
}
