package dev.martinl.bsbrewritten.manager.worldguard;

import org.bukkit.entity.Player;

public interface IWorldGuardManager {

    boolean isInRegion(Player player, String regionID);
}
