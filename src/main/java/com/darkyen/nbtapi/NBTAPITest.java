package com.darkyen.nbtapi;

import com.darkyen.nbtapi.nbt.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

class NBTAPITest {

    static void doItemTest() {
        try {
            final NBTCompound compound = new NBTCompound();

            final byte BYTE = (byte) (Byte.MAX_VALUE - 5);
            final short SHORT = (short) (Short.MAX_VALUE - 5);
            final int INT = Integer.MAX_VALUE - 5;
            final long LONG = Long.MAX_VALUE - 5;
            final float FLOAT = Float.MAX_VALUE / 5;
            final double DOUBLE = Double.MAX_VALUE / 5;
            final byte[] BYTE_ARRAY = {1,2,3,4,5,6,7,8,9};
            final String STRING = "TestString";
            final List<NBTByte> LIST = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                LIST.add(new NBTByte((byte)(i*2)));
            }
            final Map<String, NBTBase> COMPOUND = new HashMap<>();
            COMPOUND.put("key1", new NBTByte((byte) (BYTE-1)));
            COMPOUND.put("key2", new NBTShort((short) (SHORT-1)));
            COMPOUND.put("key3", new NBTInt(INT-1));
            COMPOUND.put("key4", new NBTLong(LONG-1));
            COMPOUND.put("key5", new NBTFloat(FLOAT-1));
            COMPOUND.put("key6", new NBTDouble(DOUBLE-1));
            COMPOUND.put("key8", new NBTString("Hello!"));
            final int[] INT_ARRAY = {-1, -2, -3, -4, -5, -6, -7, -8, -9};

            compound.value.put("1-byte", new NBTByte(BYTE));
            compound.value.put("2-short", new NBTShort(SHORT));
            compound.value.put("3-int", new NBTInt(INT));
            compound.value.put("4-long", new NBTLong(LONG));
            compound.value.put("5-float", new NBTFloat(FLOAT));
            compound.value.put("6-double", new NBTDouble(DOUBLE));
            compound.value.put("7-byte-array", new NBTByteArray(BYTE_ARRAY));
            compound.value.put("8-string", new NBTString(STRING));
            compound.value.put("9-list", new NBTList<>(LIST));
            compound.value.put("10-compound", new NBTCompound(COMPOUND));
            compound.value.put("11-int-array", new NBTIntArray(INT_ARRAY));

            ItemStack item = new ItemStack(Material.STONE, 1);
            final ItemStack itemWithTag = NBTReflection.createItemWithTag(item, compound);

            final NBTCompound tagAfterTest = NBTReflection.getTag(itemWithTag);

            if (compound.equals(tagAfterTest)) {
                Bukkit.getLogger().info("NBT API Item Test Successful");
            } else {
                throw new NBTException("Test failed, tags don't match!\n\tOriginal: "+compound+"\n\tTransformed: "+tagAfterTest);
            }
        } catch (Exception ex) {
            throw new NBTException("Test failed", ex);
        }
    }
}
