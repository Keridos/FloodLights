package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.MathUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;

import static de.keridos.floodlights.block.BlockPhantomLight.UPDATE;
import static de.keridos.floodlights.util.GeneralUtil.getPosFromPosFacing;

/**
 * Created by Keridos on 06.05.2015.
 * This Class
 */
@SuppressWarnings("WeakerAccess")
public abstract class TileEntityMetaFloodlight extends TileEntityFL implements ITickable {
    protected boolean active;
    protected boolean wasActive;
    protected boolean update = true;
    protected int timeout;
    protected ItemStackHandler inventory;

    public TileEntityMetaFloodlight() {
        super();
        this.wasActive = false;

        inventory = new ItemStackHandler(1) {
            @Override
            protected void onContentsChanged(int slot) {
                super.onContentsChanged(slot);
                markDirty();
            }
        };
    }

    public void setRedstone(boolean b) {
        active = b ^ inverted;
        this.setState((byte) (this.active ? 1 : 0));
        this.world.markBlocksDirtyVertical(this.pos.getX(), this.pos.getZ(), this.pos.getX(), this.pos.getZ());
    }

    public void toggleInverted() {
        inverted = !inverted;
        active = !active;
        this.setState((byte) (this.active ? 1 : 0));
        this.world.markBlocksDirtyVertical(this.pos.getX(), this.pos.getZ(), this.pos.getX(), this.pos.getZ());
    }

    public boolean getWasActive() {
        return wasActive;
    }

    public void setWasActive(boolean wasActive) {
        this.wasActive = wasActive;
    }

    public void toggleUpdateRun() {
        update = true;
    }

    @SuppressWarnings("ConstantConditions")
    public void setLight(BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == ModBlocks.blockUVLightBlock) {
            return;
        }
        if (world.setBlockState(pos, ModBlocks.blockPhantomLight.getDefaultState(), 3)) {
            TileEntityPhantomLight light = (TileEntityPhantomLight) world.getTileEntity(pos);
            light.addSource(pos);
        } else {
            this.toggleUpdateRun();
        }
    }

    @Override
    public void update() {
        if (!world.isRemote && update && !active) {
            update = false;
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        super.readFromNBT(nbtTagCompound);
        if (nbtTagCompound.hasKey(Names.NBT.STATE)) {
            this.active = (nbtTagCompound.getInteger(Names.NBT.STATE) != 0);
        }
        if (nbtTagCompound.hasKey(Names.NBT.WAS_ACTIVE)) {
            this.wasActive = nbtTagCompound.getBoolean(Names.NBT.WAS_ACTIVE);
        }
        if (nbtTagCompound.hasKey(Names.NBT.ITEMS))
            inventory.deserializeNBT(nbtTagCompound.getCompoundTag(Names.NBT.ITEMS));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(Names.NBT.WAS_ACTIVE, wasActive);
        nbtTagCompound.setByte(Names.NBT.STATE, state);
        nbtTagCompound.setTag(Names.NBT.ITEMS, inventory.serializeNBT());
        return nbtTagCompound;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) inventory : super.getCapability(capability, facing);
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @SuppressWarnings("ConstantConditions")
    public void setSource(BlockPos blockPos, boolean remove, Integer setSourceUpdate) {
        if (remove) {
            if (world.getBlockState(blockPos).getBlock() == ModBlocks.blockPhantomLight && setSourceUpdate == 0) {
                TileEntityPhantomLight light = (TileEntityPhantomLight) world.getTileEntity(blockPos);
                light.removeSource(this.pos);
            } else if (world.getBlockState(blockPos).getBlock() == ModBlocks.blockPhantomLight) {
                world.setBlockState(blockPos, world.getBlockState(blockPos).withProperty(UPDATE, false), 4);
            }
        } else if (world.getBlockState(blockPos).getBlock() == ModBlocks.blockPhantomLight && setSourceUpdate == 2) {
            world.setBlockState(blockPos, world.getBlockState(blockPos).withProperty(UPDATE, false), 4);

        } else if (world.getBlockState(blockPos).getBlock() == ModBlocks.blockPhantomLight && setSourceUpdate == 0) {
            world.setBlockState(blockPos, world.getBlockState(blockPos).withProperty(UPDATE, true), 4);

        } else if (world.getBlockState(blockPos).getBlock() == ModBlocks.blockPhantomLight) {
            TileEntityPhantomLight light = (TileEntityPhantomLight) world.getTileEntity(blockPos);
            light.addSource(this.pos);
        } else if (world.getBlockState(blockPos).getBlock().isAir(world.getBlockState(blockPos), world, blockPos) && setSourceUpdate == 1) {
            setLight(blockPos);
        }
    }

    public void straightSource(boolean remove) {
        for (int k = (remove ? 1 : 2); k >= 0; k--) {
            for (int i = 1; i <= ConfigHandler.rangeStraightFloodlight; i++) {

                int x = this.pos.getX() + this.orientation.getFrontOffsetX() * i;
                int y = this.pos.getY() + this.orientation.getFrontOffsetY() * i;
                int z = this.pos.getZ() + this.orientation.getFrontOffsetZ() * i;
                setSource(new BlockPos(x, y, z), remove, k);

                if (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube() && !remove)
                    break;
            }
        }
    }

    public void wideConeSource(boolean remove) {
        for (int k = (remove ? 1 : 2); k >= 0; k--) {
            boolean[] failedBeams = new boolean[9];

            if (!remove && world.getBlockState(getPosFromPosFacing(this.pos, this.orientation)).isOpaqueCube()) {
                return;
            }
            for (int j = 0; j <= 16; j++) {
                if (j <= 8) {
                    for (int i = 1; i <= ConfigHandler.rangeConeFloodlight / 4; i++) {
                        int b = 0;
                        int c = 0;
                        switch (j) {
                            case 0:
                                b += i;
                                break;
                            case 1:
                                b -= i;
                                break;
                            case 2:
                                c += i;
                                break;
                            case 3:
                                c -= i;
                                break;
                            case 4:
                                b += i;
                                c += i;
                                break;
                            case 5:
                                b += i;
                                c -= i;
                                break;
                            case 6:
                                b -= i;
                                c += i;
                                break;
                            case 7:
                                b -= i;
                                c -= i;
                                break;
                        }
                        int[] rotatedCoords = MathUtil.rotate(i, b, c, this.orientation);
                        int x = this.pos.getX() + rotatedCoords[0];
                        int y = this.pos.getY() + rotatedCoords[1];
                        int z = this.pos.getZ() + rotatedCoords[2];
                        setSource(new BlockPos(x, y, z), remove, k);
                        if (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube() && !remove) {
                            if (i < 4) {   //This is for canceling the long rangs beams
                                failedBeams[j] = true;
                            }
                            break;
                        }
                    }
                } else if (!failedBeams[j - 9] || remove) { // This is for the inner beams at longer range
                    for (int i = 4; i <= ConfigHandler.rangeConeFloodlight / 4; i++) {
                        int b = 0;
                        int c = 0;
                        switch (j) {
                            case 9:
                                b += i / 2;
                                break;
                            case 10:
                                b -= i / 2;
                                break;
                            case 11:
                                c += i / 2;
                                break;
                            case 12:
                                c -= i / 2;
                                break;
                            case 13:
                                b += i / 2;
                                c += i / 2;
                                break;
                            case 14:
                                b += i / 2;
                                c -= i / 2;
                                break;
                            case 15:
                                b -= i / 2;
                                c += i / 2;
                                break;
                            case 16:
                                b -= i / 2;
                                c -= i / 2;
                                break;
                        }
                        int[] rotatedCoords = MathUtil.rotate(i, b, c, this.orientation);
                        int x = this.pos.getX() + rotatedCoords[0];
                        int y = this.pos.getY() + rotatedCoords[1];
                        int z = this.pos.getZ() + rotatedCoords[2];
                        setSource(new BlockPos(x, y, z), remove, k);
                        if (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube() && !remove) {
                            break;
                        }
                    }
                }
            }
        }
    }

    public void narrowConeSource(boolean remove) {
        for (int k = (remove ? 1 : 2); k >= 0; k--) {
            boolean[] failedBeams = new boolean[9];    // for the additional beam to cancel when the main beams fail.
            for (int j = 0; j <= 16; j++) {
                if (j <= 8) {     // This is the main beams
                    for (int i = 1; i <= ConfigHandler.rangeConeFloodlight; i++) {
                        // for 1st light:
                        if (i == 1) {
                            int x = this.pos.getX() + this.orientation.getFrontOffsetX();
                            int y = this.pos.getY() + this.orientation.getFrontOffsetY();
                            int z = this.pos.getZ() + this.orientation.getFrontOffsetZ();
                            setSource(new BlockPos(x, y, z), remove, k);
                            if (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube() && !remove) {
                                return;
                            }
                        }
                        int b = 0;
                        int c = 0;
                        switch (j) {
                            case 0:
                                b += i / 2;
                                break;
                            case 1:
                                b -= i / 2;
                                break;
                            case 2:
                                c += i / 2;
                                break;
                            case 3:
                                c -= i / 2;
                                break;
                            case 4:
                                b += i / 2;
                                c += i / 2;
                                break;
                            case 5:
                                b += i / 2;
                                c -= i / 2;
                                break;
                            case 6:
                                b -= i / 2;
                                c += i / 2;
                                break;
                            case 7:
                                b -= i / 2;
                                c -= i / 2;
                                break;
                        }
                        int[] rotatedCoords = MathUtil.rotate(i, b, c, this.orientation); // rotate the coordinate to the correct spot in the real world :)
                        int x = this.pos.getX() + rotatedCoords[0];
                        int y = this.pos.getY() + rotatedCoords[1];
                        int z = this.pos.getZ() + rotatedCoords[2];
                        setSource(new BlockPos(x, y, z), remove, k);
                        if (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube() && !remove) {
                            if (i < 8) {   //This is for canceling the long rangs beams
                                failedBeams[j] = true;
                            }
                            break;
                        }
                    }
                } else if (!failedBeams[j - 9] || remove) { // This is for the inner beams at longer range
                    for (int i = 8; i <= ConfigHandler.rangeConeFloodlight; i++) {
                        int b = 0;
                        int c = 0;
                        switch (j) {
                            case 9:
                                b += i / 4;
                                break;
                            case 10:
                                b -= i / 4;
                                break;
                            case 11:
                                c += i / 4;
                                break;
                            case 12:
                                c -= i / 4;
                                break;
                            case 13:
                                b += i / 4;
                                c += i / 4;
                                break;
                            case 14:
                                b += i / 4;
                                c -= i / 4;
                                break;
                            case 15:
                                b -= i / 4;
                                c += i / 4;
                                break;
                            case 16:
                                b -= i / 4;
                                c -= i / 4;
                                break;
                        }
                        int[] rotatedCoords = MathUtil.rotate(i, b, c, this.orientation);
                        int x = this.pos.getX() + rotatedCoords[0];
                        int y = this.pos.getY() + rotatedCoords[1];
                        int z = this.pos.getZ() + rotatedCoords[2];
                        setSource(new BlockPos(x, y, z), remove, k);
                        if (world.getBlockState(new BlockPos(x, y, z)).isOpaqueCube() && !remove) {
                            break;
                        }
                    }
                }
            }
        }
    }
}
