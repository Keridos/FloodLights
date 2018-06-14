package de.keridos.floodlights.client.gui.container;

import de.keridos.floodlights.client.gui.slot.ElectricSlot;
import de.keridos.floodlights.core.network.message.MessageTileEntityFL;
import de.keridos.floodlights.handler.PacketHandler;
import de.keridos.floodlights.tileentity.TileEntityFLElectric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import static de.keridos.floodlights.util.GeneralUtil.isItemStackValidElectrical;

/**
 * Created by Keridos on 09/10/2014.
 * This Class describes the Container for the Carbon Floodlight.
 */
public class ContainerElectricFloodlight extends Container {
    private TileEntityFLElectric electricFloodlight;

    public ContainerElectricFloodlight(InventoryPlayer invPlayer, TileEntityFLElectric entity) {
        this.electricFloodlight = entity;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 9; x++) {
                this.addSlotToContainer(new Slot(invPlayer, 9 + x + y * 9, 8 + x * 18, 58 + y * 18));
            }
        }
        for (int x = 0; x < 9; x++) {
            this.addSlotToContainer(new Slot(invPlayer, x, 8 + x * 18, 116));
        }

        this.addSlotToContainer(new ElectricSlot(entity, 0, 26, 22));
    }

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int i) {
        Slot slot = getSlot(i);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack = slot.getStack();
            ItemStack result = itemstack.copy();
            if (!isItemStackValidElectrical(itemstack)) {
                return null;
            }
            if (i >= 36) {
                if (!mergeItemStack(itemstack, 0, 36, false)) {
                    return null;
                }
            } else if (!mergeItemStack(itemstack, 36, 36 + 1, false)) {
                return null;
            }
            if (itemstack.getCount() == 0) {
                slot.putStack(null);
            } else {
                slot.onSlotChanged();
            }
            slot.onTake(player, itemstack);
            return result;
        }
        return null;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player) {
        return electricFloodlight.isUsableByPlayer(player);  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();
        for (IContainerListener listener:listeners){
            if (listener instanceof EntityPlayerMP) {
                PacketHandler.INSTANCE.sendTo(new MessageTileEntityFL(electricFloodlight), (EntityPlayerMP) listener);
            }
        }
    }
}
