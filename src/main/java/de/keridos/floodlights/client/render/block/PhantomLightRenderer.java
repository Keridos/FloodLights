package de.keridos.floodlights.client.render.block;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import de.keridos.floodlights.item.ItemLightDebugTool;
import de.keridos.floodlights.reference.RenderIDs;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.IBlockAccess;

/**
 * Created by Keridos on 17/09/2015.
 * This Class is the renderer for the phantom light (only when debug item is held).
 */
@SideOnly(Side.CLIENT)
public class PhantomLightRenderer implements ISimpleBlockRenderingHandler {

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        if (player.getHeldItem() != null) {
            if (player.getHeldItem().getItem() instanceof ItemLightDebugTool) {
                renderer.renderStandardBlock(block, x, y, z);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int i) {
        return false;
    }

    @Override
    public int getRenderId() {
        return RenderIDs.PHANTOM_LIGHT;
    }
}