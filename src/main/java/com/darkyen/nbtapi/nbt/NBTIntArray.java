package com.darkyen.nbtapi.nbt;

import java.util.Arrays;

/**
 *
 */
public final class NBTIntArray extends NBTBase {
    public final int[] value;

    public NBTIntArray(int[] value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + Arrays.toString(value);
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && Arrays.equals(this.value, ((com.darkyen.nbtapi.nbt.NBTIntArray) obj).value);
    }
}
