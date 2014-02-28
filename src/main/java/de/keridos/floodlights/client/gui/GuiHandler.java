package de.keridos.floodlights.client.gui;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import de.keridos.floodlights.FloodLights;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by Nico on 28.02.14.
 */
public class GuiHandler implements IGuiHandler {
    private static GuiHandler instance = null;

    private GuiHandler() {
        NetworkRegistry.instance().registerGuiHandler(FloodLights.instance, this);
    }

    public static GuiHandler getInstance() {
        if (instance == null) {
            instance = new GuiHandler();
        }
        return instance;
    }

    @Override
    public Object getServerGuiElement(int i, EntityPlayer entityPlayer, World world, int i2, int i3, int i4) {
        return null;
    }

    @Override
    public Object getClientGuiElement(int i, EntityPlayer entityPlayer, World world, int i2, int i3, int i4) {
        return null;
    }
}
