package de.keridos.floodlights.core;

/**
 * Created by Keridos on 28.02.14.
 * This Class Listens for normal forge events.
 */
public class EventListener {
    private static EventListener instance;

    private EventListener() {
    }

    public static EventListener getInstance() {
        if (instance == null) {
            instance = new EventListener();
        }
        return instance;
    }

}
