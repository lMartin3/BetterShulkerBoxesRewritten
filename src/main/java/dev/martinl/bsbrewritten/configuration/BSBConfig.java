package dev.martinl.bsbrewritten.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Sound;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BSBConfig implements IDeepCloneable {
    public String configVersion = "unspecified";
    public Sound openSound = Sound.BLOCK_SHULKER_BOX_OPEN;
    public Sound closeSound = Sound.BLOCK_SHULKER_BOX_CLOSE;

    public int cooldown = 5000;
    public boolean requiresPermission = true;
    public boolean enableReadOnly = false;
    public boolean enableRightClickOpen = true;
    public boolean enableInventoryClickOpen = true;
    public boolean disableMovementCheck = false;


    public boolean disableVulnerableVersionProtection = false;

    public String prefix = "&b[&eBSB&b]";
    public String inventoryName = "< %shulker_name% >";
    public String openMessage = "open";
    public String closeMessage = "close";
    public String noPermissionMessage = "noperm";
    public String cooldownMessage = "cooldown";

    public boolean enableStatistics = true;

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
