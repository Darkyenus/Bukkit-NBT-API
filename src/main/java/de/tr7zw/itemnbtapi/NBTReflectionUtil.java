package de.tr7zw.itemnbtapi;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

public class NBTReflectionUtil {

    //region Cache

    private static final String bukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    private static final ReflectionClass NMSItemStack = new ReflectionClass("net.minecraft.server." + bukkitVersion + ".ItemStack");
    private static final ReflectionClass CraftItemStack = new ReflectionClass("org.bukkit.craftbukkit." + bukkitVersion + ".inventory.CraftItemStack");
    private static final ReflectionClass NBTTagCompound = new ReflectionClass("net.minecraft.server." + bukkitVersion + ".NBTTagCompound");

    private static final ReflectionMethod CraftItemStack_asNMSCopy = CraftItemStack.method("asNMSCopy", ItemStack.class);
    private static final ReflectionMethod CraftItemStack_asCraftMirror = CraftItemStack.method("asCraftMirror", NMSItemStack.Class());

    private static final ReflectionMethod NMSItemStack_getTag = NMSItemStack.method("getTag");
    //endregion

    private static Object setNBTTag(Object nmsItem, Object nbtTag) {
        try {
            java.lang.reflect.Method method;
            method = nmsItem.getClass().getMethod("setTag", nbtTag.getClass());
            method.invoke(nmsItem, nbtTag);
            return nmsItem;
        } catch (Exception ex) {
            throw new NBTAPIException("setNBTTag failed", ex);
        }
    }

    private static Object asNMSCopy(ItemStack item) {
        return CraftItemStack_asNMSCopy.invokeStatic(item);
    }

    private static ItemStack getBukkitItemStack(Object item) {
        return CraftItemStack_asCraftMirror.invokeStatic(item);
    }

    private static Object getNBTTagCompound(Object nmsItem) {
        return NMSItemStack_getTag.invoke(nmsItem);
    }

    public static ItemStack setString(ItemStack item, String key, String text) {
        if(text == null)return remove(item, key);
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("setString", String.class, String.class);
            method.invoke(nbttag, key, text);
            nmsitem = setNBTTag(nmsitem, nbttag);
            return getBukkitItemStack(nmsitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    public static String getString(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
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

    public static ItemStack setInt(ItemStack item, String key, Integer i) {
        if(i == null)return remove(item, key);
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("setInt", String.class, int.class);
            method.invoke(nbttag, key, i);
            nmsitem = setNBTTag(nmsitem, nbttag);
            return getBukkitItemStack(nmsitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    public static Integer getInt(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
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

    public static ItemStack setDouble(ItemStack item, String key, Double d) {
        if(d == null)return remove(item, key);
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("setDouble", String.class, double.class);
            method.invoke(nbttag, key, d);
            nmsitem = setNBTTag(nmsitem, nbttag);
            return getBukkitItemStack(nmsitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    public static Double getDouble(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
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

    public static ItemStack setBoolean(ItemStack item, String key, Boolean d) {
        if(d == null)return remove(item, key);
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("setBoolean", String.class, boolean.class);
            method.invoke(nbttag, key, d);
            nmsitem = setNBTTag(nmsitem, nbttag);
            return getBukkitItemStack(nmsitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    public static Boolean getBoolean(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
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
    
    public static ItemStack remove(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = NBTTagCompound.newInstance();
        }
        java.lang.reflect.Method method;
        try {
            method = nbttag.getClass().getMethod("remove", String.class);
            method.invoke(nbttag, key);
            nmsitem = setNBTTag(nmsitem, nbttag);
            return getBukkitItemStack(nmsitem);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return item;
    }

    public static Boolean hasKey(ItemStack item, String key) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
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
    
    @SuppressWarnings("unchecked")
    public static Set<String> getKeys(ItemStack item) {
        Object nmsitem = asNMSCopy(item);
        if (nmsitem == null) {
            System.out.println("Got null! (Outdated Plugin?)");
            return null;
        }
        Object nbttag = getNBTTagCompound(nmsitem);
        if (nbttag == null) {
            nbttag = NBTTagCompound.newInstance();
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
    }
}
