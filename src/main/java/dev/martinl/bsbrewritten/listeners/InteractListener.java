package dev.martinl.bsbrewritten.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.util.MaterialUtil;
import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

public class InteractListener implements Listener {
    private final BSBRewritten instance;
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public InteractListener(BSBRewritten instance) {
        this.instance = instance;
        instance.getServer().getPluginManager().registerEvents(this, instance);
    }

    @EventHandler(ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent e) {
        Bukkit.broadcastMessage(e.getAction().toString());
        if(e.getAction()!=Action.RIGHT_CLICK_AIR) return;
        ItemStack is = e.getPlayer().getInventory().getItemInMainHand();
        if(!MaterialUtil.isShulkerBox(is.getType())) return;
        //todo permissions, cooldown, worldguard area perms, etc
        BlockStateMeta bsm = (BlockStateMeta) is.getItemMeta();
        assert bsm != null;
        instance.getShulkerManager().openShulkerBoxInventory(e.getPlayer(), is);
    }
}
