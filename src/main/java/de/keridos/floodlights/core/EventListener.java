package de.keridos.floodlights.core;

/**
 * Created by Nico on 28.02.14.
 */
public class EventListener {
    private static EventListener instance = null;

    private EventListener() {
    }

    public static EventListener getInstance() {
        if (instance == null) {
            instance = new EventListener();
        }
        return instance;
    }
}
