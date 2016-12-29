package com.darkyen.nbtapi;

import com.darkyen.nbtapi.nbt.*;
import com.darkyen.nbtapi.reflection.ReflectionClass;
import com.darkyen.nbtapi.reflection.ReflectionField;
import com.darkyen.nbtapi.reflection.ReflectionMethod;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

final class NBTReflection {

    //region Cache

    static final String BukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    private static final ReflectionClass NMSItemStack = new ReflectionClass("net.minecraft.server." + BukkitVersion + ".ItemStack");
    private static final ReflectionClass CraftItemStack = new ReflectionClass("org.bukkit.craftbukkit." + BukkitVersion + ".inventory.CraftItemStack");

    private static final ReflectionMethod<Object> CraftItemStack_asNMSCopy = CraftItemStack.method("asNMSCopy", ItemStack.class);
    private static final ReflectionMethod<ItemStack> CraftItemStack_asCraftMirror = CraftItemStack.method("asCraftMirror", NMSItemStack.Class());

    //region NBT Tags
    static final ReflectionClass NBTBase = new ReflectionClass("net.minecraft.server."+ NBTReflection.BukkitVersion+".NBTBase");
    static final ReflectionMethod<Byte> NBTBase_getTypeId = NBTBase.method("getTypeId");

    static final ReflectionClass NBTTagByte = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagByte");
    static final ReflectionField<Byte> NBTTagByte_data = NBTTagByte.field("data");

    static final ReflectionClass NBTTagShort = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagShort");
    static final ReflectionField<Short> NBTTagShort_data = NBTTagShort.field("data");

    static final ReflectionClass NBTTagInt = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagInt");
    static final ReflectionField<Integer> NBTTagInt_data = NBTTagInt.field("data");

    static final ReflectionClass NBTTagLong = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagLong");
    static final ReflectionField<Long> NBTTagLong_data = NBTTagLong.field("data");

    static final ReflectionClass NBTTagFloat = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagFloat");
    static final ReflectionField<Float> NBTTagFloat_data = NBTTagFloat.field("data");

    static final ReflectionClass NBTTagDouble = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagDouble");
    static final ReflectionField<Double> NBTTagDouble_data = NBTTagDouble.field("data");

    static final ReflectionClass NBTTagByteArray = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagByteArray");
    static final ReflectionMethod<byte[]> NBTTagByteArray_asByteArray = NBTTagByteArray.method("c");

    static final ReflectionClass NBTTagString = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagString");
    static final ReflectionMethod<String> NBTTagString_asString = NBTTagString.method("c_");

    static final ReflectionClass NBTTagList = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagList");
    static final ReflectionField<List<Object>> NBTTagList_list = NBTTagList.field("list");
    static final ReflectionMethod NBTTagList_add = NBTTagList.method("add", NBTBase.Class());

    static final ReflectionClass NBTTagCompound = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagCompound");
    static final ReflectionField<Map<String, Object>> NBTTagCompound_map = NBTTagCompound.field("map");
    static final ReflectionMethod NBTTagCompound_set = NBTTagCompound.method("set", String.class, NBTBase.Class());

    static final ReflectionClass NBTTagIntArray = new ReflectionClass("net.minecraft.server." + NBTReflection.BukkitVersion + ".NBTTagIntArray");
    static final ReflectionMethod<int[]> NBTTagIntArray_asIntArray = NBTTagIntArray.method("d");
    //endregion

    private static final ReflectionMethod NMSItemStack_getTag = NMSItemStack.method("getTag");
    private static final ReflectionMethod NMSItemStack_setTag = NMSItemStack.method("setTag", NBTTagCompound.Class());
    //endregion

    private static Object asNMSCopy(ItemStack item) {
        return CraftItemStack_asNMSCopy.invokeStatic(item);
    }

    private static ItemStack getBukkitItemStack(Object item) {
        return CraftItemStack_asCraftMirror.invokeStatic(item);
    }

    static com.darkyen.nbtapi.nbt.NBTBase fromNMSTag (Object nmsTag) {
        final byte type = NBTBase_getTypeId.invoke(nmsTag);
        switch (NBTTagType.typeOf(type)) {
            case End: {
                return null;
            }
            case Byte: {
                return new NBTByte(NBTTagByte_data.get(nmsTag));
            }
            case Short: {
                return new NBTShort(NBTTagShort_data.get(nmsTag));
            }
            case Int: {
                return new NBTInt(NBTTagInt_data.get(nmsTag));
            }
            case Long: {
                return new NBTLong(NBTTagLong_data.get(nmsTag));
            }
            case Float: {
                return new NBTFloat(NBTTagFloat_data.get(nmsTag));
            }
            case Double: {
                return new NBTDouble(NBTTagDouble_data.get(nmsTag));
            }
            case Byte_Array: {
                return new NBTByteArray(NBTTagByteArray_asByteArray.invoke(nmsTag));
            }
            case String: {
                return new NBTString(NBTTagString_asString.invoke(nmsTag));
            }
            case List: {
                final NBTList list = new NBTList();
                final List<Object> nmsList = NBTTagList_list.get(nmsTag);
                for (Object innerNmsTag : nmsList) {
                    final com.darkyen.nbtapi.nbt.NBTBase innerNbt = fromNMSTag(innerNmsTag);
                    if (innerNbt != null) {
                        // noinspection unchecked
                        list.value.add(innerNbt);
                    }
                }
                return list;
            }
            case Compound: {
                final NBTCompound compound = new NBTCompound();
                final Map<String, Object> compoundMembers = NBTTagCompound_map.get(nmsTag);
                for (Map.Entry<String, Object> entry : compoundMembers.entrySet()) {
                    final com.darkyen.nbtapi.nbt.NBTBase nbt = fromNMSTag(entry.getValue());
                    if (nbt != null) compound.value.put(entry.getKey(), nbt);
                }
                return compound;
            }
            case Int_Array: {
                return new NBTIntArray(NBTTagIntArray_asIntArray.invoke(nmsTag));
            }
            default:
                throw new AssertionError("Unknown type " + type);
        }
    }

    static Object toNMSTag (com.darkyen.nbtapi.nbt.NBTBase nbt) {
        if (nbt instanceof NBTByte) {
            return NBTTagByte.newInstance(((NBTByte)nbt).value);
        } else if (nbt instanceof NBTShort) {
            return NBTTagShort.newInstance(((NBTShort)nbt).value);
        } else if (nbt instanceof NBTInt) {
            return NBTTagInt.newInstance(((NBTInt)nbt).value);
        } else if (nbt instanceof NBTLong) {
            return NBTTagLong.newInstance(((NBTLong)nbt).value);
        } else if (nbt instanceof NBTFloat) {
            return NBTTagFloat.newInstance(((NBTFloat)nbt).value);
        } else if (nbt instanceof NBTDouble) {
            return NBTTagDouble.newInstance(((NBTDouble)nbt).value);
        } else if (nbt instanceof NBTByteArray) {
            return NBTTagByteArray.newInstance((Object)((NBTByteArray)nbt).value);
        } else if (nbt instanceof NBTString) {
            return NBTTagString.newInstance(((NBTString)nbt).value);
        } else if (nbt instanceof NBTList) {
            final Object nbtList = NBTTagList.newInstance();
            // noinspection unchecked
            for (com.darkyen.nbtapi.nbt.NBTBase element : (List<com.darkyen.nbtapi.nbt.NBTBase>)((NBTList)nbt).value) {
                NBTTagList_add.invoke(nbtList, toNMSTag(element));
            }
            return nbtList;
        } else if (nbt instanceof NBTCompound) {
            final Object nbtCompound = NBTTagCompound.newInstance();
            for (Map.Entry<String, com.darkyen.nbtapi.nbt.NBTBase> entry : ((NBTCompound)nbt).value.entrySet()) {
                NBTTagCompound_set.invoke(nbtCompound, entry.getKey(), toNMSTag(entry.getValue()));
            }
            return nbtCompound;
        } else if (nbt instanceof NBTIntArray) {
            return NBTTagIntArray.newInstance((Object)((NBTIntArray)nbt).value);
        } else {
            throw new IllegalArgumentException("Invalid NBT tag: " + nbt);
        }
    }

    static NBTCompound getTag(ItemStack item) {
        final Object nmsItem = asNMSCopy(item);
        final Object nmsNBTTagCompound = NMSItemStack_getTag.invoke(nmsItem);
        if (nmsNBTTagCompound == null) {
            return new NBTCompound();
        }

        final com.darkyen.nbtapi.nbt.NBTBase nbt = fromNMSTag(nmsNBTTagCompound);
        if (nbt instanceof NBTCompound) {
            return (NBTCompound) nbt;
        } else {
            throw new NBTException("Item's tag is not compound, but: "+nbt);
        }
    }

    static ItemStack createItemWithTag(ItemStack item, NBTCompound nbt) {
        final Object nmsItem = asNMSCopy(item);
        final Object nmsTag = toNMSTag(nbt);
        NMSItemStack_setTag.invoke(nmsItem, nmsTag);
        return getBukkitItemStack(nmsItem);
    }

    //All initialized, do tests
    static {
        NBTAPITest.doItemTest();
    }
}
