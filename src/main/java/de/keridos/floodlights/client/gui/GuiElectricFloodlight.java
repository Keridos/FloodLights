package de.keridos.floodlights.client.gui;

import de.keridos.floodlights.client.gui.container.ElectricFloodlightContainer;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntityFLElectric;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

/**
 * Created by Keridos on 09/10/2014.
 * This Class implements the Gui for the Carbon floodlight.
 */
public class GuiElectricFloodlight extends GuiContainer {
    private static final ResourceLocation NORMAL_TEXTURE = new ResourceLocation(Textures.Gui.ELECTRIC_FLOODLIGHT);
    private static final ResourceLocation CLOAK_TEXTURE = new ResourceLocation(Textures.Gui.ELECTRIC_FLOODLIGHT_CLOAK);

    private TileEntityFLElectric tileEntityFLElectric;
    private boolean cloak;

    public GuiElectricFloodlight(InventoryPlayer invPlayer, TileEntityFLElectric entity, boolean cloak) {
        super(ElectricFloodlightContainer.create(invPlayer, entity, cloak));
        this.tileEntityFLElectric = entity;
        this.cloak = cloak;
        xSize = 176;
        ySize = cloak ? 156 : 146;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        String format = I18n.format(
                Names.Localizations.RF_STORAGE,
                String.format("%.1fk", tileEntityFLElectric.energy.getEnergyStored() / 1000f),
                tileEntityFLElectric.energy.getMaxEnergyStored() / 1000 + "k"
        );
        fontRenderer.drawString(format, 50, cloak ? 20 : 26, 0x000000);
    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int j, int i) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(cloak ? CLOAK_TEXTURE : NORMAL_TEXTURE);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
    }
}

