package de.tr7zw.itemnbtapi;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemNBTAPI {
    private static Boolean compatible = null;

    public static boolean isCompatible() {
        if (compatible == null) {
            try {
                ItemStack item = new ItemStack(Material.STONE, 1);
                NBTItem nbtItem = new NBTItem(item);

                nbtItem.setString("stringTest", "TestString");
                nbtItem.setInteger("intTest", 42);
                nbtItem.setDouble("doubleTest", 1.5d);
                nbtItem.setBoolean("booleanTest", true);

                item = nbtItem.getItem();

                if (!nbtItem.hasKey("stringTest")) {
                    compatible = false;
                }
                if (!nbtItem.getString("stringTest").equals("TestString")
                        || nbtItem.getInteger("intTest") != 42
                        || nbtItem.getDouble("doubleTest") != 1.5d
                        || !nbtItem.getBoolean("booleanTest")) {
                    compatible = false;
                }
            } catch (Exception ignore) {
                compatible = false;
            }
            compatible = true;
        }

        return compatible;
    }
}
