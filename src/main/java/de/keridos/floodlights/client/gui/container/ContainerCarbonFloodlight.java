package de.keridos.floodlights.client.gui.container;

import de.keridos.floodlights.tileentity.TileEntityCarbonFloodlight;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

/**
 * Created by Keridos on 09/10/2014.
 * This Class
 */
public class ContainerCarbonFloodlight extends Container {
    private TileEntityCarbonFloodlight carbonFloodlight;

    public ContainerCarbonFloodlight(InventoryPlayer invPlayer, TileEntityCarbonFloodlight entity) {
        this.carbonFloodlight = entity;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(invPlayer, 9 + x + y * 9, 8 + x * 18, 58 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(invPlayer, x, 8 + x * 18, 116));
        }

        this.addSlotToContainer(new Slot(entity, 0, 26, 22));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        Slot slot = getSlot(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            ItemStack result = itemstack.copy();

            if (i >= 36) {
                if (!mergeItemStack(itemstack, 0, 36, false)) {
                    return null;
                }
            } else if (!mergeItemStack(itemstack, 36, 36 + 1, false)) {
                return null;
            }

            if (itemstack.stackSize == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            slot.onPickupFromSlot(player, itemstack);
            return result;
        }
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return carbonFloodlight.isUseableByPlayer(player);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
