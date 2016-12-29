package com.darkyen.nbtapi;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class NBTReflectionUtil {

    //region Cache

    static final String BukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    private static final ReflectionClass NMSItemStack = new ReflectionClass("net.minecraft.server." + BukkitVersion + ".ItemStack");
    private static final ReflectionClass CraftItemStack = new ReflectionClass("org.bukkit.craftbukkit." + BukkitVersion + ".inventory.CraftItemStack");

    private static final ReflectionMethod<Object> CraftItemStack_asNMSCopy = CraftItemStack.method("asNMSCopy", ItemStack.class);
    private static final ReflectionMethod<ItemStack> CraftItemStack_asCraftMirror = CraftItemStack.method("asCraftMirror", NMSItemStack.Class());

    private static final ReflectionMethod NMSItemStack_getTag = NMSItemStack.method("getTag");
    private static final ReflectionMethod NMSItemStack_setTag = NMSItemStack.method("setTag", NBT.NBTTagCompound.Class());
    //endregion

    private static Object asNMSCopy(ItemStack item) {
        return CraftItemStack_asNMSCopy.invokeStatic(item);
    }

    private static ItemStack getBukkitItemStack(Object item) {
        return CraftItemStack_asCraftMirror.invokeStatic(item);
    }

    public static NBT.NBTCompound getTag(ItemStack item) {
        final Object nmsItem = asNMSCopy(item);
        final Object nmsNBTTagCompound = NMSItemStack_getTag.invoke(nmsItem);
        if (nmsNBTTagCompound == null) {
            return new NBT.NBTCompound();
        }

        final NBT nbt = NBT.fromNMSTag(nmsNBTTagCompound);
        if (nbt instanceof NBT.NBTCompound) {
            return (NBT.NBTCompound) nbt;
        } else {
            throw new NBTAPIException("Item's tag is not compound, but: "+nbt);
        }
    }

    public static ItemStack createItemWithTag(ItemStack item, NBT.NBTCompound nbt) {
        final Object nmsItem = asNMSCopy(item);
        final Object nmsTag = NBT.toNMSTag(nbt);
        NMSItemStack_setTag.invoke(nmsItem, nmsTag);
        return getBukkitItemStack(nmsItem);
    }

    //All initialized, do tests
    static {
        NBTAPITest.doItemTest();
    }
}
