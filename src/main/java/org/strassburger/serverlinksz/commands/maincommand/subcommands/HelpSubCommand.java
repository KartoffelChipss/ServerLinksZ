package org.strassburger.serverlinksz.commands.maincommand.subcommands;

import org.bukkit.command.CommandSender;
import org.strassburger.serverlinksz.commands.SubCommand;
import org.strassburger.serverlinksz.util.MessageUtils;

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
