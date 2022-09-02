package dev.martinl.bsbrewritten.commands;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.util.BSBPermission;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
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

        if (args.length == 0 || args[0].equalsIgnoreCase("help") || !sender.hasPermission(BSBPermission.ADMIN.toString())) {
            sendPluginInfo(sender);
        } else if (args[0].equalsIgnoreCase("reload")) {
            instance.reloadConfig();
            instance.loadAndParseConfig();
            sender.sendMessage(instance.getBSBConfig().getPrefix() + ChatColor.AQUA + "Configuration reloaded!");
        } else if (args[0].equalsIgnoreCase("check")) {
            Bukkit.getScheduler().runTaskAsynchronously(instance, () -> {
                instance.getUpdateChecker().checkForUpdates();
                if (instance.getUpdateChecker().isNewerVersionAvailable()) {
                    for (String msg : instance.getUpdateChecker().getUpdateMessages()) {
                        sender.sendMessage(msg);
                    }
                } else {
                    sender.sendMessage(ChatColor.YELLOW + "[BSB] " + ChatColor.GRAY + "You are running the latest version of BetterShulkerBoxes.");
                }
                if (instance.getUpdateChecker().isRunningVulnerableVersion()) {
                    sender.sendMessage(ChatColor.RED +
                            "WARNING! You a re currently using a vulnerable version of Better Shulker Boxes!\n" +
                            "The plugin " + (instance.isLockFeatures() ? "disabled the features to prevent exploitation"
                            : "did NOT disable anything because of the configuration"));
                    sender.sendMessage("" + ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "Please update the plugin as soon as possible!");
                }
            });
        }
        return false;
    }

    private void sendPluginInfo(CommandSender sender) {
        String prefix = instance.getConfigurationParser().getPrefix();
        sender.sendMessage(prefix + ChatColor.AQUA + "This server is running " + ChatColor.YELLOW + "Better Shulker Boxes v" + instance.getDescription().getVersion() + ChatColor.AQUA + ".");
        if (sender.hasPermission(BSBPermission.ADMIN.toString())) {
            sender.sendMessage(prefix + ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/bsb reload" + ChatColor.GRAY + " to reload the configuration.");
            sender.sendMessage(prefix + ChatColor.GRAY + "Use " + ChatColor.YELLOW + "/bsb check" + ChatColor.GRAY + " to check for updates.");
            BaseComponent[] discordLink =
                    new ComponentBuilder("-> Better Shulker Boxes support Discord server").color(net.md_5.bungee.api.ChatColor.BLUE)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to join the support Discord server")))
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/DnAHWMG"))
                            .create();
            BaseComponent[] spigotLink =
                    new ComponentBuilder("-> Contact author via Spigot MC forums").color(net.md_5.bungee.api.ChatColor.GOLD)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open the author's page on Spigot")))
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://www.spigotmc.org/members/rektb.384908/"))
                            .create();
            BaseComponent[] githubLink =
                    new ComponentBuilder("-> GitHub issues page").color(net.md_5.bungee.api.ChatColor.GRAY)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to open the GitHub issues page")))
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/lMartin3"))
                            .create();
            sender.sendMessage(prefix + ChatColor.YELLOW + "If you have any issues, do not hesitate to reach out to me!");
            sender.spigot().sendMessage(discordLink);
            sender.spigot().sendMessage(spigotLink);
            sender.spigot().sendMessage(githubLink);

        }
    }
}
