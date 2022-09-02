package dev.martinl.bsbrewritten.configuration;

import dev.martinl.bsbrewritten.configuration.annotations.ColorString;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Sound;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BSBConfig implements IDeepCloneable {
    private String configVersion = "unspecified";
    private Sound openSound = Sound.BLOCK_SHULKER_BOX_OPEN;
    private Sound closeSound = Sound.BLOCK_SHULKER_BOX_CLOSE;

    private int cooldown = 5000;
    private boolean requiresPermission = true;
    private boolean enableReadOnly = false;
    private boolean enableRightClickOpen = true;
    private boolean enableInventoryClickOpen = true;
    private boolean disableMovementCheck = false;


    private boolean disableVulnerableVersionProtection = false;

    @ColorString
    private String prefix = "&b[&eBSB&b] &r";
    private String inventoryName = "< %shulker_name% >";
    private String openMessage = "open";
    private String closeMessage = "close";
    private String noPermissionMessage = "noperm";
    private String cooldownMessage = "cooldown";

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
