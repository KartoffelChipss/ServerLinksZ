package org.strassburger.serverlinksz.commands.maincommand.subcommands;

import org.bukkit.command.CommandSender;
import org.strassburger.serverlinksz.ServerLinksZ;
import org.strassburger.serverlinksz.commands.CommandUtils;
import org.strassburger.serverlinksz.commands.SubCommand;
import org.strassburger.serverlinksz.util.LinkManager;
import org.strassburger.serverlinksz.util.MessageUtils;

public class RemoveSubCommand implements SubCommand {
    private final ServerLinksZ plugin;

    public RemoveSubCommand(ServerLinksZ plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (!hasPermission(sender)) {
            CommandUtils.throwPermissionError(sender);
            return false;
        }

        if (args.length < 2) {
            CommandUtils.throwUsageError(sender, getUsage());
            return false;
        }

        boolean showHints = plugin.getConfig().getBoolean("hints");

        String id = args[1];

        boolean linkExists = LinkManager.getLinkKeys().contains(id);

        if (!linkExists) {
            sender.sendMessage(MessageUtils.getAndFormatMsg(
                    false,
                    "linkNotFound",
                    "&cLink with id %id%&c not found!",
                    new MessageUtils.Replaceable("%id%", id)
            ));
            return false;
        }

        LinkManager.removeLink(id);
        sender.sendMessage(MessageUtils.getAndFormatMsg(
                true,
                "removeLinkMsg",
                "&7Successfully removed link with id %id%!",
                new MessageUtils.Replaceable("%id%", id)
        ));
        if (showHints) sender.sendMessage(MessageUtils.getAndFormatMsg(
                false,
                "rejoinHint",
                "<#E9D502>⚠ To update the Serverlinks, please rejoin the server!"
        ));
        return true;
    }

    @Override
    public String getUsage() {
        return "/serverlinksz remove <id>";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("serverlinksz.admin");
    }
}
