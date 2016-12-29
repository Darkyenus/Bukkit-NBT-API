package com.darkyen.nbtapi.nbt;

/**
 *
 */
public final class NBTLong extends NBTBase {
    public final long value;

    public NBTLong(long value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && this.value == ((com.darkyen.nbtapi.nbt.NBTLong) obj).value;
    }

}
