package com.zetaplugins.serverlinksz.commands.maincommand.subcommands;

import org.bukkit.command.CommandSender;
import com.zetaplugins.serverlinksz.commands.SubCommand;
import com.zetaplugins.serverlinksz.util.MessageUtils;

public class HelpSubCommand implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        sender.sendMessage(MessageUtils.getAndFormatMsg(false, "help", "&7HELP"));
        return false;
    }

    @Override
    public String getUsage() {
        return "/serverlinksz help";
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return true;
    }
}
