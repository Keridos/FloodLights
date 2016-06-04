package de.keridos.floodlights.block.properties;

import net.minecraft.util.IStringSerializable;

/**
 * Created by Keridos on 04/06/2016.
 * This Class
 */
public class BlockProperties {
    public enum EnumModel implements IStringSerializable {
        SMALLELECTRICFLOODLIGHTSTRIP(0, "smallElectricFloodlightStrip"),
        SMALLELECTRICFLOODLIGHTSQUARE(1, "smallElectricFloodlightSquare");

        private int ID;
        private String name;

        private EnumModel(int ID, String name) {
            this.ID = ID;
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public int getID() {
            return ID;
        }

        @Override
        public String toString() {
            return getName();
        }
    }
}
