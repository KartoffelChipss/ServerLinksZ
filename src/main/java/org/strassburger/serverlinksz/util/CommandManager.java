package org.strassburger.serverlinksz.util;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.strassburger.serverlinksz.ServerLinksZ;
import org.strassburger.serverlinksz.commands.LinkCommand;
import org.strassburger.serverlinksz.commands.MainCommand;

import java.util.List;

public class CommandManager {
    private static final ServerLinksZ plugin = ServerLinksZ.getInstance();

    private CommandManager() {}

    /**
     * Registers all commands
     */
    public static void registerCommands() {
        registerCommand("serverlinksz", new MainCommand(), new MainCommand());
        registerCommand("link", new LinkCommand(), new LinkCommand());

        for (String linkCommand : List.of("discord", "website", "store", "teamspeak", "twitter", "youtube", "instagram", "facebook", "tiktok", "vote")) {
            registerCommand(linkCommand, new LinkCommand(), new LinkCommand());
        }
    }

    /**
     * Registers a command
     *
     * @param name The name of the command
     * @param executor The executor of the command
     * @param tabCompleter The tab completer of the command
     */
    private static void registerCommand(String name, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand command = plugin.getCommand(name);

        if (command != null) {
            command.setExecutor(executor);
            command.setTabCompleter(tabCompleter);
        }
    }
}
