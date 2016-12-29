package com.darkyen.nbtapi.nbt;

/**
 *
 */
public final class NBTDouble extends NBTBase {
    public final double value;

    public NBTDouble(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && this.value == ((com.darkyen.nbtapi.nbt.NBTDouble) obj).value;
    }
}
