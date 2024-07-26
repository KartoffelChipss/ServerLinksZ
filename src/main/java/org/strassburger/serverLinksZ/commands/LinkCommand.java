package org.strassburger.serverLinksZ.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.strassburger.serverLinksZ.ServerLinksZ;
import org.strassburger.serverLinksZ.util.LinkManager;
import org.strassburger.serverLinksZ.util.MessageUtils;

import java.util.List;

public class LinkCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final FileConfiguration config = ServerLinksZ.getInstance().getConfig();

        if (!ServerLinksZ.getInstance().getConfig().getBoolean("linkCommand")) {
            sender.sendMessage(MessageUtils.getAndFormatMsg(false, "messages.linkCommandDisabled", "&cThe link command is disabled!"));
            return false;
        }

        if (args.length == 0) {
            throwUsageError(sender, "/link <name>");
            return false;
        }

        String linkID = args[0];

        if (!LinkManager.getLinkKeys().contains(linkID)) {
            throwUsageError(sender, "/link <name>");
            return false;
        }

        String url = config.getString("links." + linkID + ".url");
        String name = config.getString("links." + linkID + ".name");
        boolean allowCommand = config.getBoolean("links." + linkID + ".allowCommand");

        if (url == null || name == null || !allowCommand) return false;

        sender.sendMessage(MessageUtils.getAndFormatMsg(false, "linkCommand", "&7-> <click:OPEN_URL:%url%><hover:show_text:'&7%url%'><u>%name%</u></hover></click> &r&7<-",
                new MessageUtils.Replaceable("%url%", url),
                new MessageUtils.Replaceable("%name%", name)
        ));
        return false;
    }

    private void throwUsageError(CommandSender sender, String usage) {
        Component msg = MessageUtils.getAndFormatMsg(false, "messages.usageError", "&cUsage: %usage%", new MessageUtils.Replaceable("%usage%", usage));
        sender.sendMessage(msg);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            return LinkManager.getLinkKeys((key) -> ServerLinksZ.getInstance().getConfig().getBoolean("links." + key + ".allowCommand")).stream().toList();
        }
        return null;
    }
}
