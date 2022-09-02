package dev.martinl.bsbrewritten.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationData implements IDeepCloneable {
    public String configVersion = "unspecified";
    public String openSound = "BLOCK_SHULKER_BOX_OPEN";
    public String closeSound = "BLOCK_SHULKER_BOX_CLOSE";

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
        return new ConfigurationData(

        )
    }
}
