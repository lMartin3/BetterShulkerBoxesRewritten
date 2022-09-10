package dev.martinl.bsbrewritten.configuration;

import dev.martinl.bsbrewritten.BSBRewritten;
import dev.martinl.bsbrewritten.configuration.types.ConfigMessage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Sound;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BSBConfig implements IDeepCloneable {
    private String configVersion = BSBRewritten.getInstance().getDescription().getVersion();
    private Sound openSound = Sound.BLOCK_SHULKER_BOX_OPEN;
    private Sound closeSound = Sound.BLOCK_SHULKER_BOX_CLOSE;

    private int cooldown = 5000;
    private boolean requiresPermission = true;
    private boolean enableReadOnly = false;
    private boolean enableRightClickOpen = true;
    private boolean enableInventoryClickOpen = true;
    private boolean disableMovementCheck = false;

    private boolean enableChestSortHook = true;
    private boolean enableWorldGuardHook = true;
    private boolean blacklistRegions = true;
    private List<String> regionList = List.of("region1");

    private boolean disableVulnerableVersionProtection = false;

    private ConfigMessage prefix = new ConfigMessage("&b[&eBSB&b] &r");
    private String inventoryName = "< %shulker_name% >";
    private ConfigMessage openMessage = new ConfigMessage("&7Opening shulkerbox (%shulker_name%)...");
    private ConfigMessage closeMessage = new ConfigMessage("&7Closing shulkerbox (%shulker_name%)...");
    private ConfigMessage noPermissionMessage = new ConfigMessage("&cNo permission");
    private ConfigMessage cooldownMessage = new ConfigMessage("&cYou have to wait %minutes% minutes and %seconds% before using this again");

    private boolean enableStatistics = true;

    @Override
    public IDeepCloneable clone() {
        return new BSBConfig(
                configVersion,
                openSound,
                closeSound,
                cooldown,
                requiresPermission,
                enableReadOnly,
                enableRightClickOpen,
                enableInventoryClickOpen,
                disableMovementCheck,
                enableChestSortHook,
                enableWorldGuardHook,
                blacklistRegions,
                regionList,
                disableVulnerableVersionProtection,
                prefix,
                inventoryName,
                openMessage,
                closeMessage,
                noPermissionMessage,
                cooldownMessage,
                enableStatistics
        );
    }
}
