package com.darkyen.nbtapi.nbt;

/**
 *
 */
public final class NBTInt extends NBTBase {
    public final int value;

    public NBTInt(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && this.value == ((com.darkyen.nbtapi.nbt.NBTInt) obj).value;
    }

}
