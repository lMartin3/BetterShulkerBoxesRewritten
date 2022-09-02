package dev.martinl.bsbrewritten.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Sound;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BSBConfig implements IDeepCloneable {
    protected String configVersion = "unspecified";
    protected Sound openSound = Sound.BLOCK_SHULKER_BOX_OPEN;
    protected Sound closeSound = Sound.BLOCK_SHULKER_BOX_CLOSE;

    protected int cooldown = 5000;
    protected boolean requiresPermission = true;
    protected boolean enableReadOnly = false;
    protected boolean enableRightClickOpen = true;
    protected boolean enableInventoryClickOpen = true;
    protected boolean disableMovementCheck = false;


    protected boolean disableVulnerableVersionProtection = false;

    protected String prefix = "&b[&eBSB&b]";
    protected String inventoryName = "< %shulker_name% >";
    protected String openMessage = "open";
    protected String closeMessage = "close";
    protected String noPermissionMessage = "noperm";
    protected String cooldownMessage = "cooldown";

    protected boolean enableStatistics = true;

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
