package com.darkyen.nbtapi.nbt;

/**
 *
 */
public final class NBTFloat extends NBTBase {
    public final float value;

    public NBTFloat(float value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && this.value == ((com.darkyen.nbtapi.nbt.NBTFloat) obj).value;
    }

}
