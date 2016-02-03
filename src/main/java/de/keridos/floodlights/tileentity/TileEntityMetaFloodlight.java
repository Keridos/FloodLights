package de.keridos.floodlights.tileentity;

import de.keridos.floodlights.handler.ConfigHandler;
import de.keridos.floodlights.init.ModBlocks;
import de.keridos.floodlights.reference.Names;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ITickable;

import static de.keridos.floodlights.util.GeneralUtil.getPosFromPosFacing;
import static de.keridos.floodlights.util.MathUtil.rotate;

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
        this.worldObj.markBlockForUpdate(this.pos);
    }

    public void toggleInverted() {
        inverted = !inverted;
        active = !active;
        this.setState((byte) (this.active ? 1 : 0));
        this.worldObj.markBlockForUpdate(this.pos);
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
        if (worldObj.getBlockState(pos).getBlock() == ModBlocks.blockUVLightBlock) {
            return;
        }
        if (worldObj.setBlockState(pos, ModBlocks.blockPhantomLight.getDefaultState())) {
            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(pos);
            light.addSource(this.pos);
            worldObj.markBlockRangeForRenderUpdate(pos, pos);
        } else {
            this.toggleUpdateRun();
        }

    }

    @Override
    public void update() {
        
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
                setInventorySlotContents(slot, ItemStack.loadItemStackFromNBT(item));
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        super.writeToNBT(nbtTagCompound);
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
    public ItemStack getStackInSlot(int i) {
        worldObj.markBlockForUpdate(this.pos);
        return inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        ItemStack itemstack = getStackInSlot(slot);

        if (itemstack != null) {
            if (itemstack.stackSize <= count) {
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
        if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
            itemstack.stackSize = getInventoryStackLimit();
        }
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D, this.pos.getZ() + 0.5D) <= 64;
    }
    

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
    public IChatComponent getDisplayName() {
        return null;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    public void straightSource(boolean remove) {
        for (int i = 1; i <= ConfigHandler.rangeStraightFloodlight; i++) {
            int x = this.pos.getX() + this.orientation.getFrontOffsetX() * i;
            int y = this.pos.getY() + this.orientation.getFrontOffsetY() * i;
            int z = this.pos.getZ() + this.orientation.getFrontOffsetZ() * i;
            if (remove) {
                if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                    TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                    light.removeSource(this.pos);
                }
            } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isAir(worldObj, new BlockPos(x,y,z))) {
                setLight(new BlockPos(x,y,z));
            } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                light.addSource(this.pos);
            } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isOpaqueCube()) {
                break;
            }
        }
    }

    public void wideConeSource(boolean remove) {
        boolean[] failedBeams = new boolean[9];

        if (!remove && worldObj.getBlockState(getPosFromPosFacing(this.pos, this.orientation)).getBlock().isOpaqueCube()) {
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
                    int[] rotatedCoords = rotate(i, b, c, this.orientation);
                    int x = this.pos.getX() + rotatedCoords[0];
                    int y = this.pos.getY() + rotatedCoords[1];
                    int z = this.pos.getZ() + rotatedCoords[2];
                    if (remove) {
                        if (worldObj.getBlockState(pos).getBlock() == ModBlocks.blockPhantomLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                            light.removeSource(this.pos);
                        }
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isAir(worldObj, new BlockPos(x,y,z))) {
                        setLight(new BlockPos(x,y,z));
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                        light.addSource(this.pos);
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isOpaqueCube()) {
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
                    int[] rotatedCoords = rotate(i, b, c, this.orientation);
                    int x = this.pos.getX() + rotatedCoords[0];
                    int y = this.pos.getY() + rotatedCoords[1];
                    int z = this.pos.getZ() + rotatedCoords[2];
                    if (remove) {
                        if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                            light.removeSource(this.pos);
                        }
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isAir(worldObj, new BlockPos(x,y,z))) {
                        setLight(new BlockPos(x,y,z));
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                        light.addSource(this.pos);
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isOpaqueCube()) {
                        break;
                    }
                }
            }
        }
    }

    public void narrowConeSource(boolean remove) {
        boolean[] failedBeams = new boolean[9];    // for the additional beam to cancel when the main beams fail.
        for (int j = 0; j <= 16; j++) {
            if (j <= 8) {     // This is the main beams
                for (int i = 1; i <= ConfigHandler.rangeConeFloodlight; i++) {
                    // for 1st light:
                    if (i == 1) {
                        int x = this.pos.getX() + this.orientation.getFrontOffsetX();
                        int y = this.pos.getY() + this.orientation.getFrontOffsetY();
                        int z = this.pos.getZ() + this.orientation.getFrontOffsetZ();
                        if (remove) {
                            if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                                TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                                light.removeSource(this.pos);
                            }
                        } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isAir(worldObj, new BlockPos(x,y,z))) {
                            setLight(new BlockPos(x,y,z));
                        } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                            light.addSource(this.pos);
                        } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isOpaqueCube()) {
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
                    int[] rotatedCoords = rotate(a, b, c, this.orientation); // rotate the coordinate to the correct spot in the real world :)
                    int x = this.pos.getX() + rotatedCoords[0];
                    int y = this.pos.getY() + rotatedCoords[1];
                    int z = this.pos.getZ() + rotatedCoords[2];
                    if (remove) {
                        if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                            light.removeSource(this.pos);
                        }
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isAir(worldObj, new BlockPos(x,y,z))) {
                        setLight(new BlockPos(x,y,z));
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                        light.addSource(this.pos);
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isOpaqueCube()) {
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
                    int[] rotatedCoords = rotate(a, b, c, this.orientation);
                    int x = this.pos.getX() + rotatedCoords[0];
                    int y = this.pos.getY() + rotatedCoords[1];
                    int z = this.pos.getZ() + rotatedCoords[2];
                    if (remove) {
                        if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                            TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                            light.removeSource(this.pos);
                        }
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isAir(worldObj, new BlockPos(x,y,z))) {
                        setLight(new BlockPos(x,y,z));
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock() == ModBlocks.blockPhantomLight) {
                        TileEntityPhantomLight light = (TileEntityPhantomLight) worldObj.getTileEntity(new BlockPos(x,y,z));
                        light.addSource(this.pos);
                    } else if (worldObj.getBlockState(new BlockPos(x,y,z)).getBlock().isOpaqueCube()) {
                        break;
                    }
                }
            }
        }
    }

}
