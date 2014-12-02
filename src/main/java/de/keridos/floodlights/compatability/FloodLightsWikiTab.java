package de.keridos.floodlights.compatability;

import de.keridos.floodlights.init.ModBlocks;
import igwmod.gui.GuiWiki;
import igwmod.gui.tabs.BaseWikiTab;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

/**
 * Created by Keridos on 02/12/2014.
 * This Class
 */
public class FloodLightsWikiTab extends BaseWikiTab {
    public FloodLightsWikiTab() {
        pageEntries.add("block/floodlights:electricFloodlight");
        pageEntries.add("block/floodlights:carbonFloodlight");
    }

    @Override
    public String getName() {
        return "FloodLights";
    }

    @Override
    public ItemStack renderTabIcon(GuiWiki gui) {
        return new ItemStack(ModBlocks.blockElectricLight);
    }

    @Override
    protected String getPageName(String pageEntry) {
        if (pageEntry.startsWith("item") || pageEntry.startsWith("block")) {
            return I18n.format(pageEntry.replace("/", ".").replace("block", "tile") + ".name");
        } else {
            return I18n.format("igwtab.entry." + pageEntry);
        }
    }

    @Override
    protected String getPageLocation(String pageEntry) {
        if (pageEntry.startsWith("item") || pageEntry.startsWith("block"))
            return I18n.format(pageEntry.replace("/floodlights:", "/"));
        return "menu/" + pageEntry;
    }
}
