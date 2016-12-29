package com.darkyen.nbtapi.nbt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 *
 */
public final class NBTList<Element extends NBTBase> extends NBTBase {
    public final List<Element> value = new ArrayList<>();

    public NBTList() {
    }

    public NBTList(Collection<Element> values) {
        this.value.addAll(values);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "[" + value + "]";
    }

    @Override
    public boolean equals(Object obj) {
        return obj.getClass() == getClass() && this.value.equals(((com.darkyen.nbtapi.nbt.NBTList) obj).value);
    }
}
