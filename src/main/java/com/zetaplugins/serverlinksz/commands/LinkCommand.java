package com.zetaplugins.serverlinksz.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import com.zetaplugins.serverlinksz.ServerLinksZ;
import com.zetaplugins.serverlinksz.util.LinkManager;
import com.zetaplugins.serverlinksz.util.MessageUtils;

import java.util.List;

public class LinkCommand implements CommandExecutor, TabCompleter {
    private final ServerLinksZ plugin;

    public LinkCommand(ServerLinksZ plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        List<String> linkCommands = List.of(
                "discord", "website", "store", "teamspeak", "twitter", "youtube", "instagram", "facebook", "tiktok", "vote"
        );

        String linkID = linkCommands.contains(command.getName()) | plugin.getLinkManager().getLinkKeys().contains(command.getName())
                ? command.getName()
                : args.length > 0
                ? args[0]
                : null;

        if (linkID == null) {
            CommandUtils.throwUsageError(sender, "/" + command.getName() + " <id>");
            return false;
        }

        if (!plugin.getLinkManager().getLinkKeys().contains(linkID)) {
            sender.sendMessage(MessageUtils.getAndFormatMsg(
                    false,
                    "linkNotFound",
                    "&cLink with id <#00BFFF>%id%&c not found!",
                    new MessageUtils.Replaceable("%id%", linkID)
            ));
            return false;
        }

        LinkManager.Link link = plugin.getLinkManager().getLink(linkID);

        if (link == null) return false;

        if (!link.allowCommand()) {
            sender.sendMessage(MessageUtils.getAndFormatMsg(
                    false,
                    "linkCcommandNotAllowed",
                    "&cYou are not allowed to access this command via a link!"
            ));
            return false;
        }

        sender.sendMessage(MessageUtils.getAndFormatMsg(
                false,
                "linkCommand",
                "&7-> <click:OPEN_URL:%url%><hover:show_text:'&7%url%'><u>%name%</u></hover></click> &r&7<-",
                new MessageUtils.Replaceable("%url%", link.url()),
                new MessageUtils.Replaceable("%name%", link.name())
        ));
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            return plugin.getLinkManager().getLinkKeys(LinkManager.Link::allowCommand).stream().toList();
        }
        return null;
    }
}
