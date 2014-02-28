package de.keridos.floodlights.recipe;

/**
 * Created by Nico on 28.02.14.
 */
public class RecipeHandler {
    private static RecipeHandler instance = null;

    private RecipeHandler() {
    }

    public static RecipeHandler getInstance() {
        if (instance == null) {
            instance = new RecipeHandler();
        }
        return instance;
    }
}
