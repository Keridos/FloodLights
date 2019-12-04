package de.keridos.floodlights.handler;

import de.keridos.floodlights.FloodLights;
import de.keridos.floodlights.client.gui.GuiCarbonFloodlight;
import de.keridos.floodlights.client.gui.GuiElectricFloodlight;
import de.keridos.floodlights.client.gui.container.CarbonFloodlightContainer;
import de.keridos.floodlights.client.gui.container.ElectricFloodlightContainer;
import de.keridos.floodlights.tileentity.TileEntityCarbonFloodlight;
import de.keridos.floodlights.tileentity.TileEntityElectricFloodlight;
import de.keridos.floodlights.tileentity.TileEntityFLElectric;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;

/**
 * Created by Keridos on 28.02.14.
 * This Class handles the GUIs that this mod uses (will use soon).
 */
public class GuiHandler implements IGuiHandler {

    public static final int GUI_CARBON_FLOODLIGHT = 0;
    public static final int GUI_ELECTIC_FLOODLIGHT = 1;
    public static final int GUI_ELECTIC_FLOODLIGHT_CLOAK = 2;

    private static GuiHandler instance = null;

    private GuiHandler() {
        NetworkRegistry.INSTANCE.registerGuiHandler(FloodLights.instance, this);
    }

    public static GuiHandler getInstance() {
        if (instance == null) {
            instance = new GuiHandler();
        }
        return instance;
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));

        switch (id) {
            case GUI_CARBON_FLOODLIGHT:
                if (entity instanceof TileEntityCarbonFloodlight) {
                    return CarbonFloodlightContainer.create(player.inventory, (TileEntityCarbonFloodlight) entity);
                }
            case GUI_ELECTIC_FLOODLIGHT:
                if (entity instanceof TileEntityFLElectric) {
                    return ElectricFloodlightContainer.create(player.inventory, (TileEntityFLElectric) entity, false);
                }
            case GUI_ELECTIC_FLOODLIGHT_CLOAK:
                if (entity instanceof TileEntityFLElectric) {
                    return ElectricFloodlightContainer.create(player.inventory, (TileEntityFLElectric) entity, true);
                }
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));

        switch (id) {
            case GUI_CARBON_FLOODLIGHT:
                if (entity instanceof TileEntityCarbonFloodlight) {
                    return new GuiCarbonFloodlight(player.inventory, (TileEntityCarbonFloodlight) entity);
                } else {
                    return null;
                }
            case GUI_ELECTIC_FLOODLIGHT:
                if (entity instanceof TileEntityFLElectric) {
                    return new GuiElectricFloodlight(player.inventory, (TileEntityFLElectric) entity, false);
                } else {
                    return null;
                }
            case GUI_ELECTIC_FLOODLIGHT_CLOAK:
                if (entity instanceof TileEntityElectricFloodlight) {
                    return new GuiElectricFloodlight(player.inventory, (TileEntityFLElectric) entity, true);
                } else {
                    return null;
                }
        }

        return null;
    }
}
