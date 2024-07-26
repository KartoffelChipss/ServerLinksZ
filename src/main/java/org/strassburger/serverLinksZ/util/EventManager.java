package org.strassburger.serverLinksZ.util;

import org.bukkit.event.Listener;
import org.strassburger.serverLinksZ.ServerLinksZ;

public class EventManager {
    private static final ServerLinksZ plugin = ServerLinksZ.getInstance();

    private EventManager() {}

    /**
     * Registers all listeners
     */
    public static void registerListeners() {
    }

    /**
     * Registers a listener
     *
     * @param listener The listener to register
     */
    private static void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }
}
