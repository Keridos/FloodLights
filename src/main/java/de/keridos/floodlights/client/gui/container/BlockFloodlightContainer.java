package de.keridos.floodlights.client.gui.container;

import de.keridos.floodlights.tileentity.TileEntityMetaFloodlight;
import javafx.util.Pair;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public abstract class BlockFloodlightContainer<T extends TileEntityMetaFloodlight> extends BaseFloodlightContainer<T> {

    BlockFloodlightContainer(InventoryPlayer invPlayer, T entity) {
        super(invPlayer, entity);
    }

    @Override
    protected boolean mergeStack(int slotId, ItemStack itemStack) {
        // slotId = sourceSlot
        if (slotId < 2) {
            return mergeItemStack(itemStack, 0, 2, false);
        } else {
            return mergeItemStack(itemStack, 36, 37, false)
                    || mergeItemStack(itemStack, 37, 38, false);
        }
    }

    @SuppressWarnings("SameParameterValue")
    SlotItemHandler getCloakSlot(IItemHandler inventory, int index, int x, int y) {
        return new SlotItemHandler(inventory, index, x, y) {

            @Override
            public boolean isItemValid(@Nonnull ItemStack stack) {
                if (stack.getItem() instanceof ItemBlock) {
                    IBlockState state = ((ItemBlock) stack.getItem()).getBlock().getBlockState().getBaseState();
                    return state.isOpaqueCube() && state.isBlockNormalCube() && state.isFullCube() && !state.isTranslucent();
                }

                return false;
            }

            @Override
            public int getSlotStackLimit() {
                return 1;
            }

            @Override
            public int getItemStackLimit(@Nonnull ItemStack stack) {
                return 1;
            }

            @Override
            public void onSlotChanged() {
                super.onSlotChanged();
                entity.markDirty();
            }
        };
    }

    protected Pair<Integer, Integer> getHotbarOffset() {
        return new Pair<>(8, 132);
    }

    protected Pair<Integer, Integer> getInventoryOffset() {
        return new Pair<>(8, 74);
    }
}
