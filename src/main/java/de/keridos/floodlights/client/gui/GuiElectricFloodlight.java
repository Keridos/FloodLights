package de.keridos.floodlights.client.gui;

import com.google.common.collect.Lists;
import de.keridos.floodlights.client.gui.container.ContainerElectricFloodlight;
import de.keridos.floodlights.reference.Names;
import de.keridos.floodlights.reference.Textures;
import de.keridos.floodlights.tileentity.TileEntityFLElectric;
import de.keridos.floodlights.util.PowerBar;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by Keridos on 09/10/2014.
 * This Class implements the Gui for the Carbon floodlight.
 */
public class GuiElectricFloodlight extends GuiContainer {
    private static final ResourceLocation texture = new ResourceLocation(Textures.Gui.ELECTRIC_FLOODLIGHT);
    private TileEntityFLElectric tileEntityFLElectric;
    PowerBar powerBar;

    public GuiElectricFloodlight(InventoryPlayer invPlayer, TileEntityFLElectric entity) {
        super(new ContainerElectricFloodlight(invPlayer, entity));
        this.tileEntityFLElectric = entity;
        xSize = 176;
        ySize = 146;
        powerBar = new PowerBar(this,guiLeft+ 8,guiTop+4);
    }

    @Override
    public void drawGuiContainerForegroundLayer(int i, int j) {
        drawBarTooltip(i, j, 8, 4);

    }

    @Override
    public void drawGuiContainerBackgroundLayer(float f, int j, int i) {
        GL11.glColor4f(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(texture);
        drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
        powerBar.draw(this.tileEntityFLElectric,guiLeft+ 8,guiTop+4);

    }

    private void drawBarTooltip(int mx, int my, int ox, int oy)
    {
        int rx = mx - ox - guiLeft;
        int ry = my - oy - guiTop;

        if (rx < 0 || ry < 0 || rx > 14 || ry > 50)
            return;

        List<String> tooltip = Lists.newArrayList();
        tooltip.add(I18n.format(Names.Localizations.RF_STORAGE));
        tooltip.add(String.format("%d / %d RF", tileEntityFLElectric.getEnergyStored(EnumFacing.EAST), tileEntityFLElectric.getMaxEnergyStored(EnumFacing.EAST)));

        drawHoveringText(tooltip, mx - guiLeft, my - guiTop);
    }
}

