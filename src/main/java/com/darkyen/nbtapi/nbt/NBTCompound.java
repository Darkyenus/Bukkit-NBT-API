package com.darkyen.nbtapi.nbt;

import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public final class NBTCompound extends NBTBase {
    public final Map<String, NBTBase> value = new HashMap<>();

    public NBTCompound() {
    }

    public NBTCompound(Map<String, NBTBase> initialValues) {
        this.value.putAll(initialValues);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && this.value.equals(((NBTCompound) obj).value);
    }

}
