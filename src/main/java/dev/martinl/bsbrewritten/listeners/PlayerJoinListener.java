package dev.martinl.bsbrewritten.listeners;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.util.BSBPermission;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final BSBRewritten instance;

    public PlayerJoinListener(BSBRewritten instance) {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPermission(BSBPermission.ADMIN.toString())) return;
        if (instance.getUpdateChecker().isNewerVersionAvailable()) {
            for (String msg : instance.getUpdateChecker().getUpdateMessages()) {
                e.getPlayer().sendMessage(msg);
            }
        }
        if(instance.getUpdateChecker().isRunningVulnerableVersion()) {
            e.getPlayer().sendMessage(ChatColor.RED +
                    "WARNING! You a re currently using a vulnerable version of Better Shulker Boxes!\n" +
                    "The plugin " + (instance.isLockFeatures() ? "disabled the features to prevent exploitation"
                    : "did NOT disable anything because of the configuration\n"+
                    ChatColor.GOLD + ChatColor.BOLD + ChatColor.UNDERLINE + "Please update the plugin as soon as possible!"));
        }
    }
}
