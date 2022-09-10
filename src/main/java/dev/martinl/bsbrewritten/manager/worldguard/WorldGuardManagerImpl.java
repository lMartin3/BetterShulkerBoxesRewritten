package dev.martinl.bsbrewritten.manager.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.util.Location;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import org.bukkit.entity.Player;

public class WorldGuardManagerImpl implements IWorldGuardManager {

    private final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
    private final RegionQuery query = container.createQuery();

    @Override
    public boolean isInRegion(Player player, String regionID) {
        Location loc = BukkitAdapter.adapt(player.getLocation());
        ApplicableRegionSet set = query.getApplicableRegions(loc);
        for (ProtectedRegion region : set) {
            if (region.getId().equals(regionID)) {
                return true;
            }
        }
        return false;
    }
}
