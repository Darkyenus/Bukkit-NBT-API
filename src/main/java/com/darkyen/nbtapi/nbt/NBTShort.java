package com.darkyen.nbtapi.nbt;

/**
 *
 */
public final class NBTShort extends NBTBase {
    public final short value;

    public NBTShort(short value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && this.value == ((com.darkyen.nbtapi.nbt.NBTShort) obj).value;
    }

}
