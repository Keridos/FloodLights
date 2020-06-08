package de.keridos.floodlights.client.gui.container;

import de.keridos.floodlights.tileentity.TileEntityCarbonFloodlight;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static de.keridos.floodlights.util.GeneralUtil.getBurnTime;

public class CarbonFloodlightContainer extends BlockFloodlightContainer<TileEntityCarbonFloodlight> {

    private CarbonFloodlightContainer(InventoryPlayer invPlayer, TileEntityCarbonFloodlight entity) {
        super(invPlayer, entity, true);
    }

    @Override
    void initialize() {
        super.initialize();

        IItemHandler inventory = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        addSlotToContainer(new SlotItemHandler(inventory, 0, 26, 16) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return getBurnTime(stack) > 0;
            }

            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                entity.markDirty();
            }
        });
        addSlotToContainer(getCloakSlot(inventory, 1, 26, 41));
    }

    public static CarbonFloodlightContainer create(InventoryPlayer inventoryPlayer, TileEntityCarbonFloodlight entity) {
        CarbonFloodlightContainer container = new CarbonFloodlightContainer(inventoryPlayer, entity);
        container.initialize();
        return container;
    }
}
