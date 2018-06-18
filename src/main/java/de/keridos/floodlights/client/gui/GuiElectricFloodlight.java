package de.keridos.floodlights.client.gui;

import de.keridos.floodlights.client.gui.container.ContainerElectricFloodlight;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntityFLElectric;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import static de.keridos.floodlights.util.GeneralUtil.safeLocalize;

/**
 * Created by Keridos on 09/10/2014.
 * This Class implements the Gui for the Carbon floodlight.
 */
public class GuiElectricFloodlight extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation(Textures.Gui.ELECTRIC_FLOODLIGHT);
    private TileEntityFLElectric tileEntityFLElectric;

    public GuiElectricFloodlight(InventoryPlayer invPlayer, TileEntityFLElectric entity) {
        super(new ContainerElectricFloodlight(invPlayer, entity));
        this.tileEntityFLElectric = entity;
        xSize = 176;
        ySize = 146;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String guiText = safeLocalize(Names.Localizations.RF_STORAGE) + ": "
                + tileEntityFLElectric.getEnergyStored(EnumFacing.DOWN) / 1000 + "."
                + tileEntityFLElectric.getEnergyStored(EnumFacing.DOWN) % 1000 / 100 + "k/"
                + tileEntityFLElectric.getMaxEnergyStored(EnumFacing.DOWN) / 1000 + "k";
        fontRenderer.drawString(guiText, 50, 26, 0x000000);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int j, int i) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}

