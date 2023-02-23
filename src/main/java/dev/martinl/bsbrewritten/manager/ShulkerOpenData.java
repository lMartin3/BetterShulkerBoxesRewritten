package dev.martinl.bsbrewritten.manager;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@Data
@RequiredArgsConstructor
public class ShulkerOpenData {
    private final ItemStack itemStack;
    private final Location openLocation;
    private final SlotType slotType;
    private final int rawSlot;
}
