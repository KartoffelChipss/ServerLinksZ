package org.strassburger.serverlinksz.commands.maincommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.strassburger.serverlinksz.ServerLinksZ;
import org.strassburger.serverlinksz.commands.SubCommand;
import org.strassburger.serverlinksz.commands.maincommand.subcommands.AddSubCommand;
import org.strassburger.serverlinksz.commands.maincommand.subcommands.HelpSubCommand;
import org.strassburger.serverlinksz.commands.maincommand.subcommands.ReloadSubCommand;
import org.strassburger.serverlinksz.commands.maincommand.subcommands.RemoveSubCommand;
import org.strassburger.serverlinksz.util.MessageUtils;

import java.util.HashMap;
import java.util.Map;

public class MainCommandHandler implements CommandExecutor {
    private final ServerLinksZ plugin;
    private final Map<String, SubCommand> commands = new HashMap<>();

    public MainCommandHandler(ServerLinksZ plugin) {
        this.plugin = plugin;

        commands.put("help", new HelpSubCommand());
        commands.put("reload", new ReloadSubCommand(plugin));
        commands.put("add", new AddSubCommand(plugin));
        commands.put("remove", new RemoveSubCommand(plugin));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sendVersionMessage(sender);
            return true;
        }

        SubCommand subCommand = commands.get(args[0]);

        if (subCommand == null) {
            sendVersionMessage(sender);
            return true;
        }

        return subCommand.execute(sender, args);
    }

    private void sendVersionMessage(CommandSender sender) {
        sender.sendMessage(MessageUtils.getAndFormatMsg(
                true,
                "versionMsg",
                "FALLBACK&7You are using version %version%",
                new MessageUtils.Replaceable(
                        "%version%",
                        plugin.getDescription().getVersion()
                )
        ));
    }
}
