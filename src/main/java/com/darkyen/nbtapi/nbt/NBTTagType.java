package com.darkyen.nbtapi.nbt;

import com.darkyen.nbtapi.NBTException;

/**
 *
 */
public enum NBTTagType {
    End, Byte, Short, Int, Long, Float, Double, Byte_Array, String, List, Compound, Int_Array;

    private final static NBTTagType[] VALUES = values();

    public static NBTTagType typeOf(byte b) {
        if (b < 0 || b >= VALUES.length) throw new NBTException("Illegal NBT tag type number: " + b);
        return VALUES[b];
    }
}
