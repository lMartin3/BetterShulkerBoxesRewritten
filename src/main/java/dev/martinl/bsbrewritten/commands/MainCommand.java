package dev.martinl.bsbrewritten.commands;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.util.BSBPermission;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    public static final String COMMAND_NAME = "bsb";
    private final BSBRewritten instance;
    public MainCommand(BSBRewritten instance) {
        this.instance = instance;
        instance.getCommand(COMMAND_NAME).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(args.length==0||args[0].equalsIgnoreCase("help")||!sender.hasPermission(BSBPermission.ADMIN.toString())) {
            sendPluginInfo(sender);
        } else if(args[0].equalsIgnoreCase("reload")) {
            instance.reloadConfig();
            instance.loadAndParseConfig();
            sender.sendMessage(instance.getConfigurationParser().getPrefix() + ChatColor.AQUA + "Configuration reloaded!");
        }
        return false;
    }

    private void sendPluginInfo(CommandSender sender) {
        String prefix = instance.getConfigurationParser().getPrefix();
        sender.sendMessage(prefix + ChatColor.AQUA + "This server is running " + ChatColor.YELLOW + "Better Shulker Boxes v" + instance.getDescription().getVersion() + ChatColor.AQUA + ".");
        if(sender.hasPermission(BSBPermission.ADMIN.toString())) {
            sender.sendMessage(prefix + ChatColor.AQUA + "Use " + ChatColor.YELLOW + "/bsb reload" + ChatColor.AQUA + " to reload the configuration.");
            sender.sendMessage(prefix + ChatColor.AQUA + "Use " + ChatColor.YELLOW + "/bsb check" + ChatColor.AQUA + " to check for updates.");
        }
    }
}
