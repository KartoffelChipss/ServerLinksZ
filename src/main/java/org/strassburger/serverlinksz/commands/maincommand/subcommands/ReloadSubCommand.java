package org.strassburger.serverlinksz.commands.maincommand.subcommands;

import org.bukkit.command.CommandSender;
import org.strassburger.serverlinksz.ServerLinksZ;
import org.strassburger.serverlinksz.commands.CommandUtils;
import org.strassburger.serverlinksz.commands.SubCommand;
import org.strassburger.serverlinksz.util.LinkManager;
import org.strassburger.serverlinksz.util.MessageUtils;

public class ReloadSubCommand implements SubCommand {
    private final ServerLinksZ plugin;

    public ReloadSubCommand(ServerLinksZ plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            CommandUtils.throwPermissionError(sender);
            return false;
        }

        boolean showHints = plugin.getConfig().getBoolean("hints");

        plugin.reloadConfig();
        plugin.getLanguageManager().reload();
        LinkManager.updateLinks();
        sender.sendMessage(MessageUtils.getAndFormatMsg(
                true,
                "reloadMsg",
                "&7Successfully reloaded the plugin!"
        ));
        if (showHints) sender.sendMessage(MessageUtils.getAndFormatMsg(
                false,
                "restartServerToRegisterCustomCommands",
                "<#E9D502>⚠ Please restart the server to register the custom commands!"
        ));
        return false;
    }

    @Override
    public String getUsage() {
        return "/serverlinksz reload";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("serverlinksz.admin");
    }
}
