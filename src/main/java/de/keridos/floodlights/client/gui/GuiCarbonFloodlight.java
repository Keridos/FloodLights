package de.keridos.floodlights.client.gui;

import de.keridos.floodlights.client.gui.container.ContainerCarbonFloodlight;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntityCarbonFloodlight;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 09/10/2014.
 * This Class implements the Gui for the Carbon floodlight.
 */
public class GuiCarbonFloodlight extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation(Textures.Gui.CARBON_FLOODLIGHT);
    private TileEntityCarbonFloodlight tileEntityCarbonFloodlight;

    public GuiCarbonFloodlight(InventoryPlayer invPlayer, TileEntityCarbonFloodlight entity) {
        super(new ContainerCarbonFloodlight(invPlayer, entity));
        this.tileEntityCarbonFloodlight = entity;
        xSize = 176;
        ySize = 146;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String guiText = safeLocalize(Names.Localizations.NONELECTRIC_GUI_TEXT) + ": "
                + tileEntityCarbonFloodlight.timeRemaining / 1200
                + safeLocalize(Names.Localizations.GUI_MINUTES_SHORT)
                + String.format("%02d", tileEntityCarbonFloodlight.timeRemaining / 20 % 60)
                + safeLocalize(Names.Localizations.GUI_SECONDS_SHORT);
        fontRenderer.drawString(guiText, 50, 26, 0x000000);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int j, int i) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}

