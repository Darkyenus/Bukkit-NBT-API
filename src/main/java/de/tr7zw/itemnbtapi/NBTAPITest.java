package de.tr7zw.itemnbtapi;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

class NBTAPITest {

    static void doItemTest() {
        try {
            ItemStack item = new ItemStack(Material.STONE, 1);
            NBTItem nbtItem = new NBTItem(item);

            nbtItem.setString("stringTest", "TestString");
            nbtItem.setInteger("intTest", 42);
            nbtItem.setDouble("doubleTest", 1.5d);
            nbtItem.setBoolean("booleanTest", true);

            if (!nbtItem.hasKey("stringTest")) {
                throw new NBTAPIException("Test failed, item does not have stringTest key");
            }
            if (!nbtItem.getString("stringTest").equals("TestString")
                    || nbtItem.getInteger("intTest") != 42
                    || nbtItem.getDouble("doubleTest") != 1.5d
                    || !nbtItem.getBoolean("booleanTest")) {
                throw new NBTAPIException("Test failed, saved values do not match expectation");
            }
        } catch (Exception ex) {
            throw new NBTAPIException("Test failed", ex);
        }
    }

    static void doAdvancedItemTest() {
        try {
            final NBT.NBTCompound compound = new NBT.NBTCompound();

            final byte BYTE = (byte) (Byte.MAX_VALUE - 5);
            final short SHORT = (short) (Short.MAX_VALUE - 5);
            final int INT = Integer.MAX_VALUE - 5;
            final long LONG = Long.MAX_VALUE - 5;
            final float FLOAT = Float.MAX_VALUE / 5;
            final double DOUBLE = Double.MAX_VALUE / 5;
            final byte[] BYTE_ARRAY = {1,2,3,4,5,6,7,8,9};
            final String STRING = "TestString";
            final List<NBT.NBTByte> LIST = new ArrayList<>();
            for (int i = 0; i < 5; i++) {
                LIST.add(new NBT.NBTByte((byte)(i*2)));
            }
            final Map<String, NBT> COMPOUND = new HashMap<>();
            COMPOUND.put("key1", new NBT.NBTByte((byte) (BYTE-1)));
            COMPOUND.put("key2", new NBT.NBTShort((short) (SHORT-1)));
            COMPOUND.put("key3", new NBT.NBTInt(INT-1));
            COMPOUND.put("key4", new NBT.NBTLong(LONG-1));
            COMPOUND.put("key5", new NBT.NBTFloat(FLOAT-1));
            COMPOUND.put("key6", new NBT.NBTDouble(DOUBLE-1));
            COMPOUND.put("key8", new NBT.NBTString("Hello!"));
            final int[] INT_ARRAY = {-1, -2, -3, -4, -5, -6, -7, -8, -9};

            compound.value.put("1-byte", new NBT.NBTByte(BYTE));
            compound.value.put("2-short", new NBT.NBTShort(SHORT));
            compound.value.put("3-int", new NBT.NBTInt(INT));
            compound.value.put("4-long", new NBT.NBTLong(LONG));
            compound.value.put("5-float", new NBT.NBTFloat(FLOAT));
            compound.value.put("6-double", new NBT.NBTDouble(DOUBLE));
            compound.value.put("7-byte-array", new NBT.NBTByteArray(BYTE_ARRAY));
            compound.value.put("8-string", new NBT.NBTString(STRING));
            compound.value.put("9-list", new NBT.NBTList<>(LIST));
            compound.value.put("10-compound", new NBT.NBTCompound(COMPOUND));
            compound.value.put("11-int-array", new NBT.NBTIntArray(INT_ARRAY));

            ItemStack item = new ItemStack(Material.STONE, 1);
            final ItemStack itemWithTag = NBTReflectionUtil.setTag(item, compound);

            final NBT.NBTCompound tagAfterTest = NBTReflectionUtil.getTag(itemWithTag);

            if (compound.equals(tagAfterTest)) {
                Bukkit.getLogger().info("NBT API Item Test Successful");
            } else {
                throw new NBTAPIException("Test failed, tags don't match!\n\tOriginal: "+compound+"\n\tTransformed: "+tagAfterTest);
            }
        } catch (Exception ex) {
            throw new NBTAPIException("Test failed", ex);
        }
    }
}
