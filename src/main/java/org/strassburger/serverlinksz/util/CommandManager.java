package org.strassburger.serverlinksz.util;

import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.strassburger.serverlinksz.ServerLinksZ;
import org.strassburger.serverlinksz.commands.LinkCommand;
import org.strassburger.serverlinksz.commands.maincommand.MainCommandHandler;
import org.strassburger.serverlinksz.commands.maincommand.MainTabCompleter;

import java.lang.reflect.Field;
import java.util.List;

public class CommandManager {
    private final ServerLinksZ plugin;
    private final List<String> defaultLinkCommands = List.of(
            "discord", "website", "store", "teamspeak", "twitter", "youtube", "instagram", "facebook", "tiktok", "vote"
    );

    public CommandManager(ServerLinksZ plugin) {
        this.plugin = plugin;
    }

    /**
     * Gets the command map
     * @return The command map
     */
    private CommandMap getCommandMap() {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            return (CommandMap) field.get(Bukkit.getServer());
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to get command map: " + e.getMessage());
            return null;
        }
    }

    /**
     * Registers all commands
     */
    public void registerCommands() {
        registerCommand("serverlinksz", new MainCommandHandler(plugin), new MainTabCompleter());

        if (ServerLinksZ.getInstance().getConfig().getBoolean("linkCommand")) {
            registerCommand("link", new LinkCommand(), new LinkCommand());
        }

        for (String linkCommand : defaultLinkCommands) {
            registerCommand(linkCommand, new LinkCommand(), new LinkCommand());
        }

        CommandMap commandMap = getCommandMap();

        if (commandMap == null || !plugin.getConfig().getBoolean("dynamicCommands")) return;

        for (String linkKey : LinkManager.getLinkKeys()) {
            if (defaultLinkCommands.contains(linkKey)) continue;
            LinkManager.Link link = LinkManager.getLink(linkKey);
            if (link == null || !link.allowCommand()) continue;
            commandMap.register(linkKey, link.getCommand());
        }
    }

    /**
     * Registers a command
     *
     * @param name The name of the command
     * @param executor The executor of the command
     * @param tabCompleter The tab completer of the command
     */
    private void registerCommand(String name, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand command = plugin.getCommand(name);

        if (command != null) {
            command.setExecutor(executor);
            command.setTabCompleter(tabCompleter);
            command.permissionMessage(MessageUtils.getAndFormatMsg(
                    false,
                    "noPermissionError",
                    "&cYou don't have permission to use this!"
            ));
        }
    }
}
