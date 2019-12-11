package de.keridos.floodlights.client.gui.container;

import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import de.keridos.floodlights.util.Pair;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

@SuppressWarnings("WeakerAccess")
public abstract class BaseFloodlightContainer<T extends TileEntityMetaFloodlight> extends Container {

    protected InventoryPlayer invPlayer;
    protected T entity;

    BaseFloodlightContainer(InventoryPlayer invPlayer, T entity) {
        this.invPlayer = invPlayer;
        this.entity = entity;
    }

    void initialize() {
        Pair<Integer, Integer> hotbarOffset = getHotbarOffset();
        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(invPlayer, x, hotbarOffset.getFirst() + x * 18, hotbarOffset.getSecond()));
        }

        Pair<Integer, Integer> inventoryOffset = getInventoryOffset();
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(invPlayer, 9 + x + y * 9, inventoryOffset.getFirst() + x * 18, inventoryOffset.getSecond() + y * 18));
            }
        }

        entity.onInventoryOpen(invPlayer.player);
    }

    protected abstract boolean mergeStack(int slotId, ItemStack itemStack);

    @Override
    public boolean canInteractWith(@Nonnull EntityPlayer playerIn) {
        return true;
    }

    @Override
    public void onContainerClosed(EntityPlayer playerIn) {
        super.onContainerClosed(playerIn);
        entity.onInventoryClose(playerIn);
    }

    @Nonnull
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int slotId) {
        ItemStack ret = ItemStack.EMPTY;

        Slot slot = getSlot(slotId);
        if (slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            ret = itemstack.copy();

            if (slotId >= 36) {
                if (!mergeItemStack(itemstack, 0, 36, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!mergeStack(slotId, itemstack)) {
                return ItemStack.EMPTY;
            }

            if (itemstack.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }
            slot.onTake(player, itemstack);
        }

        return ret;
    }

    // Dimensions in pixels
    protected abstract Pair<Integer, Integer> getHotbarOffset();

    protected abstract Pair<Integer, Integer> getInventoryOffset();
}
