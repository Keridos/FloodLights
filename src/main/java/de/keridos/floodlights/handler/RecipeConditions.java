package de.keridos.floodlights.handler;

import com.google.gson.JsonObject;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class RecipeConditions {

    public static class ElectricIngredientsCondition implements IConditionFactory {
        @Override
        public BooleanSupplier parse(JsonContext context, JsonObject json) {
            return () -> ConfigHandler.electricFloodlight || ConfigHandler.smallElectricFloodlight || ConfigHandler.uvFloodlight;
        }
    }

    public static class ElectricFloodlightCondition implements IConditionFactory {
        @Override
        public BooleanSupplier parse(JsonContext context, JsonObject json) {
            return () -> ConfigHandler.electricFloodlight;
        }
    }

    public static class SmallElectricFloodlightCondition implements IConditionFactory {
        @Override
        public BooleanSupplier parse(JsonContext context, JsonObject json) {
            return () -> ConfigHandler.smallElectricFloodlight;
        }
    }

    public static class CarbonFloodlightCondition implements IConditionFactory {
        @Override
        public BooleanSupplier parse(JsonContext context, JsonObject json) {
            return () -> ConfigHandler.carbonFloodlight;
        }
    }

    public static class UVFloodlightCondition implements IConditionFactory {
        @Override
        public BooleanSupplier parse(JsonContext context, JsonObject json) {
            return () -> ConfigHandler.uvFloodlight;
        }
    }

    public static class GrowLightCondition implements IConditionFactory {
        @Override
        public BooleanSupplier parse(JsonContext context, JsonObject json) {
            return () -> ConfigHandler.growLight;
        }
    }
}
