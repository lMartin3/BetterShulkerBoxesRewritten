package dev.martinl.bsbrewritten.util;

public enum BSBPermission {
    OPEN_SHULKER("bsb.use");
    final String value;
    BSBPermission(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
