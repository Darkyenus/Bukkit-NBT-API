package com.darkyen.nbtapi.nbt;

/**
 *
 */
public final class NBTByte extends NBTBase {
    public final byte value;

    public NBTByte(byte value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && this.value == ((com.darkyen.nbtapi.nbt.NBTByte) obj).value;
    }
}
