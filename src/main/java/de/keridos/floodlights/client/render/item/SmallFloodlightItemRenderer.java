package de.keridos.floodlights.client.render.item;

import de.keridos.floodlights.client.render.block.TileEntitySmallFoodlightRenderer;
import de.keridos.floodlights.tileentity.TileEntitySmallFloodlight;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import org.lwjgl.opengl.GL11;

/**
 * Created by Keridos on 30.06.2015.
 * This Class
 */
public class SmallFloodlightItemRenderer implements IItemRenderer {

    private final TileEntitySmallFoodlightRenderer tileEntitySmallFoodlightRenderer;
    private final TileEntitySmallFloodlight tileEntitySmallFoodlight;

    public SmallFloodlightItemRenderer(TileEntitySmallFoodlightRenderer tileEntitySmallFoodlightRenderer, TileEntitySmallFloodlight tileEntitySmallFoodlight) {
        this.tileEntitySmallFoodlightRenderer = tileEntitySmallFoodlightRenderer;
        this.tileEntitySmallFoodlight = tileEntitySmallFoodlight;

    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        if (type == ItemRenderType.EQUIPPED) {
            GL11.glRotatef(-90, 0, 1, 0);
            GL11.glTranslated(0.5, 0, -0.5);
        } else if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glRotatef(-90, 0, 1, 0);
            GL11.glRotatef(-20, 0, 1, 0);
            GL11.glTranslated(-0.5, 0.2, -0.5);
        } else if (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY) {
            GL11.glRotatef(-90, 1, 0, 0);
            GL11.glTranslated(-0.5, -0.5, -0.5);
        }

        this.tileEntitySmallFoodlightRenderer.renderTileEntityAt(this.tileEntitySmallFoodlight, 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glPopMatrix();
    }


}