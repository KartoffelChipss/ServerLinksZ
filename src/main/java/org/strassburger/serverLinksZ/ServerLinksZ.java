package org.strassburger.serverLinksZ;

import org.bukkit.plugin.java.JavaPlugin;
import org.strassburger.serverLinksZ.util.*;

public final class ServerLinksZ extends JavaPlugin {
    private static ServerLinksZ instance;

    private LanguageManager languageManager;

    @Override
    public void onEnable() {
        instance = this;

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        languageManager = new LanguageManager();

        EventManager.registerListeners();
        CommandManager.registerCommands();

        LinkManager.updateLinks();

        new Metrics(this, 22795);

        getLogger().info("ServerLinksZ has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ServerLinksZ has been disabled!");
    }

    public static ServerLinksZ getInstance() {
        return instance;
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }
}
