package org.strassburger.serverlinksz;

import org.bukkit.plugin.java.JavaPlugin;
import org.strassburger.serverlinksz.util.*;
import org.strassburger.serverlinksz.util.bStats.CustomCharts;
import org.strassburger.serverlinksz.util.bStats.Metrics;

public final class ServerLinksZ extends JavaPlugin {
    private CommandManager commandManager;
    private LanguageManager languageManager;
    private EventManager eventManager;
    private LinkManager linkManager;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        languageManager = new LanguageManager(this);

        linkManager = new LinkManager(this);
        linkManager.updateLinks();

        eventManager = new EventManager(this);
        eventManager.registerListeners();
        commandManager = new CommandManager(this);
        commandManager.registerCommands();

        initializeBStats();

        getLogger().info("ServerLinksZ has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("ServerLinksZ has been disabled!");
    }

    public static ServerLinksZ getInstance() {
        return JavaPlugin.getPlugin(ServerLinksZ.class);
    }

    public LanguageManager getLanguageManager() {
        return languageManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public EventManager getEventManager() {
        return eventManager;
    }

    public LinkManager getLinkManager() {
        return linkManager;
    }

    private void initializeBStats() {
        final int pluginId = 22795;
        Metrics metrics = new Metrics(this, pluginId);

        metrics.addCustomChart(CustomCharts.getLanguageChart(this));
        metrics.addCustomChart(CustomCharts.getLinksChart(this));
    }
}
