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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class MainCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        FileConfiguration config = ServerLinksZ.getInstance().getConfig();
        boolean showHints = config.getBoolean("hints");

        if (args.length == 0) {
            sender.sendMessage(MessageUtils.getAndFormatMsg(true, "messages.versionMsg", "FALLBACK&7You are using version %version%", new MessageUtils.Replaceable("%version%", ServerLinksZ.getInstance().getDescription().getVersion())));
            return false;
        }

        String optionOne = args[0];

        if (optionOne.equals("help")) {
            sender.sendMessage(MessageUtils.getAndFormatMsg(false, "messages.help", "&7HELP"));
            return false;
        }

        if (optionOne.equals("reload")) {
            if (!sender.hasPermission("serverlinksz.admin")) {
                throwPermissionError(sender);
                return false;
            }

            ServerLinksZ.getInstance().reloadConfig();
            config = ServerLinksZ.getInstance().getConfig();
            ServerLinksZ.getInstance().getLanguageManager().reload();
            LinkManager.updateLinks();
            sender.sendMessage(MessageUtils.getAndFormatMsg(true, "messages.reloadMsg", "&7Successfully reloaded the plugin!"));
            return false;
        }

        if (optionOne.equals("add")) {
            if (!sender.hasPermission("serverlinksz.manage")) {
                throwPermissionError(sender);
                return false;
            }

            if (args.length < 4) {
                throwUsageError(sender, "/serverlinksz add <id> <name> <url> <allowCommand>");
                return false;
            }

            String id = args[1];
            String name = args[2];
            String url = args[3];
            boolean allowCommand = args.length > 4 && args[4].equals("true");

            if (!isValidURL(url)) {
                sender.sendMessage(MessageUtils.getAndFormatMsg(false, "messages.invalidUrlError", "&cThe URL is invalid!"));
                return false;
            }

            if (!url.startsWith("http://") && !url.startsWith("https://")) {
                sender.sendMessage(MessageUtils.getAndFormatMsg(false, "messages.urlProtocolError", "&cThe URL must start with 'http://' or 'https://'!"));
                return false;
            }

            LinkManager.addLink(id, name, url, allowCommand);
            sender.sendMessage(MessageUtils.getAndFormatMsg(true, "messages.addLinkMsg", "&7Successfully added link with id %id%!", new MessageUtils.Replaceable("%id%", id)));
            if (showHints) {
                sender.sendMessage(MessageUtils.getAndFormatMsg(false, "messages.rejoinHint", "<#E9D502>⚠ To update the Serverlinks, please rejoin the server!"));
                sender.sendMessage(MessageUtils.getAndFormatMsg(false, "messages.moreConfigOptionsHint", "<#E9D502>⚠ For more configuration options, please refer to the config.yml file!"));
            }
        }

        if (optionOne.equals("remove")) {
            if (!sender.hasPermission("serverlinksz.manage")) {
                throwPermissionError(sender);
                return false;
            }

            if (args.length < 2) {
                throwUsageError(sender, "/serverlinksz remove <id>");
                return false;
            }

            String id = args[1];

            LinkManager.removeLink(id);
            sender.sendMessage(MessageUtils.getAndFormatMsg(true, "messages.removeLinkMsg", "&7Successfully removed link with id %id%!", new MessageUtils.Replaceable("%id%", id)));
            if (showHints) sender.sendMessage(MessageUtils.getAndFormatMsg(false, "messages.rejoinHint", "&cTo update the Serverlinks, please rejoin the server!"));
        }

        return false;
    }

    private void throwUsageError(CommandSender sender, String usage) {
        Component msg = MessageUtils.getAndFormatMsg(false, "messages.usageError", "&cUsage: %usage%", new MessageUtils.Replaceable("%usage%", usage));
        sender.sendMessage(msg);
    }

    private void throwPermissionError(CommandSender sender) {
        Component msg = MessageUtils.getAndFormatMsg(false, "messages.noPermissionError", "&cYou don't have permission to use this!");
        sender.sendMessage(msg);
    }

    private boolean isValidURL(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            List<String> availableOptions = new ArrayList<>(List.of("help"));
            if (sender.hasPermission("serverlinksz.manage")) {
                availableOptions.add("add");
                availableOptions.add("remove");
            }
            if (sender.hasPermission("serverlinksz.admin")) {
                availableOptions.add("reload");
            }
            return availableOptions;
        }

        if (args.length == 2) {
            if (args[0].equals("add")) {
                return List.of("<id>");
            }

            if (args[0].equals("remove")) {
                return LinkManager.getLinkKeys().stream().toList();
            }
        }

        if (args.length == 3) {
            if (args[0].equals("add")) {
                return List.of("<name>");
            }
        }

        if (args.length == 4) {
            if (args[0].equals("add")) {
                return List.of("<url>");
            }
        }

        if (args.length == 5) {
            if (args[0].equals("add")) {
                return List.of("true", "false");
            }
        }

        return null;
    }
}
