package de.tr7zw.itemnbtapi;

import java.util.Set;

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

    public static ItemStack setTag(ItemStack item, NBT.NBTCompound nbt) {
        final Object nmsItem = asNMSCopy(item);
        final Object nmsTag = NBT.toNMSTag(nbt);
        NMSItemStack_setTag.invoke(nmsItem, nmsTag);
        return getBukkitItemStack(nmsItem);
    }

    @Deprecated
    public static ItemStack setString(ItemStack item, String key, String text) {
        if(text == null)return remove(item, key);
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            nbttag = NBT.NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("setString", String.class, String.class);
            method.invoke(nbttag, key, text);
            NMSItemStack_setTag.invoke(nmsitem, nbttag);
            nmsitem = nmsitem;
            return getBukkitItemStack(nmsitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    @Deprecated
    public static String getString(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            return null;
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("getString", String.class);
            return (String) method.invoke(nbttag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public static ItemStack setInt(ItemStack item, String key, Integer i) {
        if(i == null)return remove(item, key);
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            nbttag = NBT.NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("setInt", String.class, int.class);
            method.invoke(nbttag, key, i);
            NMSItemStack_setTag.invoke(nmsitem, nbttag);
            nmsitem = nmsitem;
            return getBukkitItemStack(nmsitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    @Deprecated
    public static Integer getInt(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            return null;
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("getInt", String.class);
            return (Integer) method.invoke(nbttag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public static ItemStack setDouble(ItemStack item, String key, Double d) {
        if(d == null)return remove(item, key);
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            nbttag = NBT.NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("setDouble", String.class, double.class);
            method.invoke(nbttag, key, d);
            NMSItemStack_setTag.invoke(nmsitem, nbttag);
            nmsitem = nmsitem;
            return getBukkitItemStack(nmsitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    @Deprecated
    public static Double getDouble(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            return null;
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("getDouble", String.class);
            return (Double) method.invoke(nbttag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public static ItemStack setBoolean(ItemStack item, String key, Boolean d) {
        if(d == null)return remove(item, key);
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            nbttag = NBT.NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("setBoolean", String.class, boolean.class);
            method.invoke(nbttag, key, d);
            NMSItemStack_setTag.invoke(nmsitem, nbttag);
            nmsitem = nmsitem;
            return getBukkitItemStack(nmsitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    @Deprecated
    public static Boolean getBoolean(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            return false;
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("getBoolean", String.class);
            return (Boolean) method.invoke(nbttag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Deprecated
    public static ItemStack remove(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            nbttag = NBT.NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("remove", String.class);
            method.invoke(nbttag, key);
            NMSItemStack_setTag.invoke(nmsitem, nbttag);
            nmsitem = nmsitem;
            return getBukkitItemStack(nmsitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    @Deprecated
    public static Boolean hasKey(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            return false;
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("hasKey", String.class);
            return (Boolean) method.invoke(nbttag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Deprecated
    @SuppressWarnings("unchecked")
    public static Set<String> getKeys(ItemStack item) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = NMSItemStack_getTag.invoke(nmsitem);
        if (nbttag == null) {
            nbttag = NBT.NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("c");
            return (Set<String>) method.invoke(nbttag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //All initialized, do tests
    static {
        NBTAPITest.doItemTest();
        NBTAPITest.doAdvancedItemTest();
    }
}
