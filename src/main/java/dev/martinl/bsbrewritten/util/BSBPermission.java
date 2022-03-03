package dev.martinl.bsbrewritten.util;

public enum BSBPermission {
    OPEN_SHULKER("bettershulkerboxes.use"),
    ADMIN("bettershulkerboxes.admin"),
    BYPASS_COOLDOWN("bettershulkerboxes.bypasscooldown");
    final String value;
    BSBPermission(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
