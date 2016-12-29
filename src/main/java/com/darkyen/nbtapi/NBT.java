
package com.darkyen.nbtapi;

import com.darkyen.nbtapi.nbt.*;
import org.bukkit.inventory.ItemStack;

/**
 * Main entry point of the library
 */
@SuppressWarnings("unused")
public final class NBT {

    public static NBTCompound getItemTag(ItemStack item) {
        return NBTReflection.getTag(item);
    }

    public static ItemStack createItemWithTag(ItemStack item, NBTCompound nbt) {
        return NBTReflection.createItemWithTag(item, nbt);
    }
}
