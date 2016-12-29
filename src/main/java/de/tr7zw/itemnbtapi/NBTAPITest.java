package de.tr7zw.itemnbtapi;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

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
}
