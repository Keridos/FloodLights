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

    FloodLightsWikiTab() {
        pageEntries.add("block/electric_floodlight");
        pageEntries.add("block/carbon_floodlight");
        pageEntries.add("block/small_electric_floodlight");
    }

    @Override
    public String getName() {
        return "FloodLights";
    }

    @Override
    public ItemStack renderTabIcon(GuiWiki gui) {
        return new ItemStack(ModBlocks.blockElectricFloodlight);
    }

    @Override
    protected String getPageName(String pageEntry) {
        // Super class converts all page entries to lowercase.
        // This temporary workaround fixes that by replaceing underscore with capital letter.
        // TODO: should unlocalized names be underscore-based?
        String subject = pageEntry.substring(pageEntry.indexOf('/') + 1);
        StringBuilder convertedSubject = new StringBuilder();
        int lastIndex = 0;
        boolean capitalize = false;
        boolean exec = true;
        while (exec) {
            String sub;
            int currentIndex = subject.indexOf('_', lastIndex);
            if (currentIndex == -1) {
                sub = subject.substring(lastIndex);
                exec = false;
            } else
                sub = subject.substring(lastIndex, currentIndex);

            lastIndex = currentIndex + 1;
            if (capitalize)
                sub = sub.substring(0, 1).toUpperCase() + sub.substring(1);
            else
                capitalize = true;

            convertedSubject.append(sub);
        }

        if (pageEntry.startsWith("item") || pageEntry.startsWith("block")) {
            return I18n.format("tile.floodlights:" + convertedSubject.toString() + ".name");
        } else {
            return I18n.format("igwtab.entry." + pageEntry);
        }
    }

    @Override
    protected String getPageLocation(String pageEntry) {
        if (pageEntry.startsWith("item") || pageEntry.startsWith("block"))
            return pageEntry;
        return "floodlights:menu/" + pageEntry;
    }
}
