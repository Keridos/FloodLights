package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.util.MathUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;

import static de.keridos.floodlights.block.BlockPhantomLight.UPDATE;
import static de.keridos.floodlights.util.GeneralUtil.getPosFromPosFacing;

/**
 * Created by Keridos on 06.05.2015.
 * This Class
 */
public class TileEntityMetaFloodlight extends TileEntityFL implements ISidedInventory, ITickable {
    protected boolean active;
    protected boolean wasActive;
    protected boolean update = true;
    protected int timeout;
    protected ItemStack[] inventory;

    public TileEntityMetaFloodlight() {
        super();
        this.wasActive = false;
        inventory = new ItemStack[1];
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

    public void setLight(BlockPos pos) {
        if (world.getBlockState(pos).getBlock() == ModBlocks.blockUVLightBlock) {
            return;
        }
        if (world.setBlockState(pos, ModBlocks.blockPhantomLight.getDefaultState(), 3)) {
            TileEntityPhantomLight light = (TileEntityPhantomLight) world.getTileEntity(pos);
            light.addSource(this.pos);
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
        if (nbtTagCompound.hasKey(Names.NBT.ITEMS)) {
            NBTTagList list = nbtTagCompound.getTagList(Names.NBT.ITEMS, 10);
            NBTTagCompound item = list.getCompoundTagAt(0);
            int slot = item.getByte(Names.NBT.ITEMS);
            if (slot >= 0 && slot < getSizeInventory()) {
                setInventorySlotContents(slot, new ItemStack(item));

            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound) {
        nbtTagCompound = super.writeToNBT(nbtTagCompound);
        nbtTagCompound.setBoolean(Names.NBT.WAS_ACTIVE, wasActive);
        nbtTagCompound.setByte(Names.NBT.STATE, state);
        NBTTagList list = new NBTTagList();
        ItemStack itemstack = getStackInSlot(0);
        if (itemstack != null) {
            NBTTagCompound item = new NBTTagCompound();
            item.setByte(Names.NBT.ITEMS, (byte) 0);
            itemstack.writeToNBT(item);
            list.appendTag(item);
        }
        nbtTagCompound.setTag(Names.NBT.ITEMS, list);
        return nbtTagCompound;
    }

    @Override
    public int[] getSlotsForFace(EnumFacing facing) {
        return new int[]{0};
    }

    @Override
    public boolean canInsertItem(int i, ItemStack itemstack, EnumFacing facing) {
        return true;
    }

    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, EnumFacing facing) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return inventory.length;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        world.markBlocksDirtyVertical(this.pos.getX(), this.pos.getZ(), this.pos.getX(), this.pos.getZ());
        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack itemstack = getStackInSlot(slot);

        if (itemstack != null) {
            if (itemstack.getCount() <= count) {
                setInventorySlotContents(slot, null);
            } else {
                itemstack = itemstack.splitStack(count);
                markDirty();
            }
        }
        return itemstack;
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack itemstack = getStackInSlot(slot);
        setInventorySlotContents(slot, null);
        return itemstack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        inventory[slot] = itemstack;
        if (itemstack != null && itemstack.getCount() > getInventoryStackLimit()) {
            itemstack.setCount(getInventoryStackLimit());
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

//    @Override
//    public boolean isUseableByPlayer(EntityPlayer player) {
//        return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64;
//    }


    @Override
    public void openInventory(EntityPlayer player) {

    }

    @Override
    public void closeInventory(EntityPlayer player) {

    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {

    }

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {

    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ITextComponent getDisplayName() {
        return null;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

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
                if (world.getBlockState(new BlockPos(x, y, z)).getBlock().isOpaqueCube(world.getBlockState(new BlockPos(x, y, z))) && !remove) {
                    break;
                }
            }
        }
    }

    public void wideConeSource(boolean remove) {
        for (int k = (remove ? 1 : 2); k >= 0; k--) {
            boolean[] failedBeams = new boolean[9];

            if (!remove && world.getBlockState(getPosFromPosFacing(this.pos, this.orientation)).getBlock().isOpaqueCube(world.getBlockState(getPosFromPosFacing(this.pos, this.orientation)))) {
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
                        if (world.getBlockState(new BlockPos(x, y, z)).getBlock().isOpaqueCube(world.getBlockState(new BlockPos(x, y, z))) && !remove) {
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
                        if (world.getBlockState(new BlockPos(x, y, z)).getBlock().isOpaqueCube(world.getBlockState(new BlockPos(x, y, z))) && !remove) {
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
                            if (world.getBlockState(new BlockPos(x, y, z)).getBlock().isOpaqueCube(world.getBlockState(new BlockPos(x, y, z))) && !remove) {
                                return;
                            }
                        }
                        int a = i;
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
                        int[] rotatedCoords = MathUtil.rotate(a, b, c, this.orientation); // rotate the coordinate to the correct spot in the real world :)
                        int x = this.pos.getX() + rotatedCoords[0];
                        int y = this.pos.getY() + rotatedCoords[1];
                        int z = this.pos.getZ() + rotatedCoords[2];
                        setSource(new BlockPos(x, y, z), remove, k);
                        if (world.getBlockState(new BlockPos(x, y, z)).getBlock().isOpaqueCube(world.getBlockState(new BlockPos(x, y, z))) && !remove) {
                            if (i < 8) {   //This is for canceling the long rangs beams
                                failedBeams[j] = true;
                            }
                            break;
                        }
                    }
                } else if (!failedBeams[j - 9] || remove) { // This is for the inner beams at longer range
                    for (int i = 8; i <= ConfigHandler.rangeConeFloodlight; i++) {
                        int a = i;
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
                        int[] rotatedCoords = MathUtil.rotate(a, b, c, this.orientation);
                        int x = this.pos.getX() + rotatedCoords[0];
                        int y = this.pos.getY() + rotatedCoords[1];
                        int z = this.pos.getZ() + rotatedCoords[2];
                        setSource(new BlockPos(x, y, z), remove, k);
                        if (world.getBlockState(new BlockPos(x, y, z)).getBlock().isOpaqueCube(world.getBlockState(new BlockPos(x, y, z))) && !remove) {
                            break;
                        }
                    }
                }
            }
        }
    }
}
