package de.keridos.floodlights.client.gui.container;

import de.keridos.floodlights.tileentity.TileEntityFLElectric;
import javafx.util.Pair;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

import static de.keridos.floodlights.util.GeneralUtil.isItemStackValidElectrical;

public class ElectricFloodlightContainer extends BlockFloodlightContainer<TileEntityFLElectric> {

    private boolean cloak;

    private ElectricFloodlightContainer(InventoryPlayer invPlayer, TileEntityFLElectric entity, boolean cloak) {
        super(invPlayer, entity);
        this.cloak = cloak;
    }

    @Override
    void initialize() {
        super.initialize();

        IItemHandler inventory = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
        addSlotToContainer(new SlotItemHandler(inventory, 0, 26, cloak ? 16 : 22) {
            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                return isItemStackValidElectrical(stack);
            }

            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                entity.markDirty();
            }
        });

        if (cloak) {
            addSlotToContainer(getCloakSlot(inventory, 1, 26, 41));
        }
    }

    @Override
    protected Pair<Integer, Integer> getHotbarOffset() {
        return new Pair<>(8, cloak ? 132 : 116);
    }

    @Override
    protected Pair<Integer, Integer> getInventoryOffset() {
        return new Pair<>(8, cloak ? 74 : 58);
    }

    public static ElectricFloodlightContainer create(InventoryPlayer inventoryPlayer, TileEntityFLElectric entity, boolean cloak) {
        ElectricFloodlightContainer container = new ElectricFloodlightContainer(inventoryPlayer, entity, cloak);
        container.initialize();
        return container;
    }
}
