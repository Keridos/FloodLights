package de.keridos.floodlights.client.gui;

import de.keridos.floodlights.client.gui.container.ContainerCarbonFloodlight;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntityCarbonFloodlight;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Keridos on 09/10/2014.
 * This Class
 */
public class GuiCarbonFloodlight extends GuiContainer {
    public static final ResourceLocation texture = new ResourceLocation(Textures.Gui.CARBON_FLOODLIGHT);
    private TileEntityCarbonFloodlight tileEntityCarbonFloodlight = null;

    public GuiCarbonFloodlight(InventoryPlayer invPlayer, TileEntityCarbonFloodlight entity) {
        super(new ContainerCarbonFloodlight(invPlayer, entity));
        this.tileEntityCarbonFloodlight = entity;
        xSize = 176;
        ySize = 146;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString("Time Remaining: " + tileEntityCarbonFloodlight.timeRemaining / 20 + " s", 60, (ySize - 120) + 2, 0x000000);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int j, int i) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}

