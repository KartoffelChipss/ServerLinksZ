package com.zetaplugins.serverlinksz.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import com.zetaplugins.serverlinksz.util.MessageUtils;

public class CommandUtils {
    /**
     * Throws a usage error message to the sender.
     * @param sender Command sender
     * @param usage Usage string
     */
    public static void throwUsageError(CommandSender sender, String usage) {
        Component msg = MessageUtils.getAndFormatMsg(false, "usageError", "&cUsage: %usage%", new MessageUtils.Replaceable("%usage%", usage));
        sender.sendMessage(msg);
    }

    /**
     * Throws a permission error message to the sender.
     * @param sender Command sender
     */
    public static void throwPermissionError(CommandSender sender) {
        Component msg = MessageUtils.getAndFormatMsg(false, "noPermissionError", "&cYou don't have permission to use this!");
        sender.sendMessage(msg);
    }
}
