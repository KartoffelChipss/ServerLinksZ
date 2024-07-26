package org.strassburger.serverLinksZ.util;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.strassburger.serverLinksZ.ServerLinksZ;
import org.strassburger.serverLinksZ.commands.LinkCommand;
import org.strassburger.serverLinksZ.commands.MainCommand;

public class CommandManager {
    private static final ServerLinksZ plugin = ServerLinksZ.getInstance();

    private CommandManager() {}

    /**
     * Registers all commands
     */
    public static void registerCommands() {
        registerCommand("serverlinksz", new MainCommand(), new MainCommand());
        registerCommand("link", new LinkCommand(), new LinkCommand());
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
