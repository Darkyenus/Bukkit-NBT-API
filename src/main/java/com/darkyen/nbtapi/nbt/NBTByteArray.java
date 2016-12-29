package com.darkyen.nbtapi.nbt;

import java.util.Arrays;

/**
 *
 */
public final class NBTByteArray extends NBTBase {
    public final byte[] value;

    public NBTByteArray(byte[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && Arrays.equals(this.value, ((com.darkyen.nbtapi.nbt.NBTByteArray) obj).value);
    }
}
