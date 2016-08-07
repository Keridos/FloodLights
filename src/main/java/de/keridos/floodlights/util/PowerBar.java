package de.keridos.floodlights.util;

import de.keridos.floodlights.tileentity.TileEntityFLElectric;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


/**
 * Created by Keridos on 06/08/16.
 * This Class
 */


@SideOnly(Side.CLIENT)
public class PowerBar {

    /**
     * The width of the power bar.
     */
    public static final int WIDTH = 14;

    /**
     * The height of the power bar.
     */
    public static final int HEIGHT = 50;

    /**
     * Constant reference to the included gui element sheet.
     */
    private static final ResourceLocation TEXTURE_SHEET = new ResourceLocation("floodlights", "textures/gui/powerbar.png");

    /**
     * The x position of the power bar on the screen.
     */
    private final int x;

    /**
     * The y position of the power bar on the screen.
     */
    private final int y;

    /**
     * The screen to draw the power bar on.
     */
    private final GuiScreen screen;

    /**
     * The type of background the use when drawing the power bar.
     */

    /**
     * Object that can be used to draw a power bar on the screen.
     *
     * @param screen The screen to draw the power bar on.
     * @param x      The x position of the power bar on the screen.
     * @param y      The y position of the power bar on the screen.
     */
    public PowerBar(GuiScreen screen, int x, int y) {
        this.screen = screen;
        this.x = x;
        this.y = y;
    }

    /**
     * Draws a power bar that represents the power held within an ITeslaHolder.
     *
     * @param holder The holder to represent.
     */
    public void draw(TileEntityFLElectric holder) {

        draw(holder.getEnergyStored(EnumFacing.UP), holder.getMaxEnergyStored(EnumFacing.UP));
    }

    /**
     * Draws a power bard that represents the based power information.
     *
     * @param power    The amount of power to represent.
     * @param capacity The capacity to represent.
     */
    public void draw(long power, long capacity) {

        screen.mc.getTextureManager().bindTexture(TEXTURE_SHEET);
        screen.drawTexturedModalRect(x, y, 3, 1, WIDTH, HEIGHT);
        long powerOffset = (power * (HEIGHT + 1)) / capacity;
        screen.drawTexturedModalRect(x + 1, (int) (y + HEIGHT - powerOffset), 18, (int) ((HEIGHT + 1) - powerOffset), WIDTH, (int) (powerOffset + 2));
    }

    /**
     * @return The x position of the power bar on the screen.
     */
    public int getX() {

        return x;
    }

    /**
     * @return The y position of the power bar on the screen.
     */
    public int getY() {

        return y;
    }

}