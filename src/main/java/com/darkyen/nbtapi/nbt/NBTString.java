package com.darkyen.nbtapi.nbt;

/**
 *
 */
public final class
NBTString extends NBTBase {
    public final String value;

    public NBTString(String value) {
        if (value == null) throw new NullPointerException("value can't be null");
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && this.value.equals(((com.darkyen.nbtapi.nbt.NBTString) obj).value);
    }
}
