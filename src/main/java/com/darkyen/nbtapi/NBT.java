package com.darkyen.nbtapi;

import java.util.*;

/**
 *
 */
public abstract class NBT {

    static final ReflectionClass NBTBase = new ReflectionClass("net.minecraft.server."+NBTReflectionUtil.BukkitVersion+".NBTBase");
    static final ReflectionMethod<Byte> NBTBase_getTypeId = NBTBase.method("getTypeId");
    
    static final ReflectionClass NBTTagByte = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagByte");
    static final ReflectionField<Byte> NBTTagByte_data = NBTTagByte.field("data");

    static final ReflectionClass NBTTagShort = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagShort");
    static final ReflectionField<Short> NBTTagShort_data = NBTTagShort.field("data");

    static final ReflectionClass NBTTagInt = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagInt");
    static final ReflectionField<Integer> NBTTagInt_data = NBTTagInt.field("data");

    static final ReflectionClass NBTTagLong = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagLong");
    static final ReflectionField<Long> NBTTagLong_data = NBTTagLong.field("data");

    static final ReflectionClass NBTTagFloat = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagFloat");
    static final ReflectionField<Float> NBTTagFloat_data = NBTTagFloat.field("data");

    static final ReflectionClass NBTTagDouble = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagDouble");
    static final ReflectionField<Double> NBTTagDouble_data = NBTTagDouble.field("data");

    static final ReflectionClass NBTTagByteArray = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagByteArray");
    static final ReflectionMethod<byte[]> NBTTagByteArray_asByteArray = NBTTagByteArray.method("c");

    static final ReflectionClass NBTTagString = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagString");
    static final ReflectionMethod<String> NBTTagString_asString = NBTTagString.method("c_");

    static final ReflectionClass NBTTagList = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagList");
    static final ReflectionField<List<Object>> NBTTagList_list = NBTTagList.field("list");
    static final ReflectionMethod NBTTagList_add = NBTTagList.method("add", NBTBase.Class());

    static final ReflectionClass NBTTagCompound = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagCompound");
    static final ReflectionField<Map<String, Object>> NBTTagCompound_map = NBTTagCompound.field("map");
    static final ReflectionMethod NBTTagCompound_set = NBTTagCompound.method("set", String.class, NBTBase.Class());

    static final ReflectionClass NBTTagIntArray = new ReflectionClass("net.minecraft.server." + NBTReflectionUtil.BukkitVersion + ".NBTTagIntArray");
    static final ReflectionMethod<int[]> NBTTagIntArray_asIntArray = NBTTagIntArray.method("d");

    public static NBT fromNMSTag(Object nmsTag) {
        final byte type = NBTBase_getTypeId.invoke(nmsTag);
		switch (type) {
		case TYPE_End: {
		    return null;
        }
		case TYPE_Byte: {
		    return new NBTByte(NBTTagByte_data.get(nmsTag));
        }
		case TYPE_Short: {
            return new NBTShort(NBTTagShort_data.get(nmsTag));
        }
		case TYPE_Int: {
            return new NBTInt(NBTTagInt_data.get(nmsTag));
        }
		case TYPE_Long: {
            return new NBTLong(NBTTagLong_data.get(nmsTag));
        }
		case TYPE_Float: {
            return new NBTFloat(NBTTagFloat_data.get(nmsTag));
        }
		case TYPE_Double: {
            return new NBTDouble(NBTTagDouble_data.get(nmsTag));
        }
		case TYPE_Byte_Array: {
		    return new NBTByteArray(NBTTagByteArray_asByteArray.invoke(nmsTag));
        }
		case TYPE_String: {
		    return new NBTString(NBTTagString_asString.invoke(nmsTag));
        }
		case TYPE_List: {
            final NBTList list = new NBTList();
            final List<Object> nmsList = NBTTagList_list.get(nmsTag);
            for (Object innerNmsTag : nmsList) {
                final NBT innerNbt = fromNMSTag(innerNmsTag);
                if (innerNbt != null) {
                    //noinspection unchecked
                    list.value.add(innerNbt);
                }
            }
            return list;
        }
		case TYPE_Compound: {
            final NBTCompound compound = new NBTCompound();
            final Map<String, Object> compoundMembers = NBTTagCompound_map.get(nmsTag);
            for (Map.Entry<String, Object> entry : compoundMembers.entrySet()) {
                final NBT nbt = fromNMSTag(entry.getValue());
                if (nbt != null) compound.value.put(entry.getKey(), nbt);
            }
            return compound;
        }
		case TYPE_Int_Array: {
		    return new NBTIntArray(NBTTagIntArray_asIntArray.invoke(nmsTag));
        }
		}
		throw new NBTAPIException("Unknown type "+type);
    }
    
    public static Object toNMSTag(NBT nbt) {
        if (nbt instanceof NBTByte) {
            return NBTTagByte.newInstance(((NBTByte) nbt).value);
        } else if (nbt instanceof NBTShort) {
            return NBTTagShort.newInstance(((NBTShort) nbt).value);
        } else if (nbt instanceof NBTInt) {
            return NBTTagInt.newInstance(((NBTInt) nbt).value);
        } else if (nbt instanceof NBTLong) {
            return NBTTagLong.newInstance(((NBTLong) nbt).value);
        } else if (nbt instanceof NBTFloat) {
            return NBTTagFloat.newInstance(((NBTFloat) nbt).value);
        } else if (nbt instanceof NBTDouble) {
            return NBTTagDouble.newInstance(((NBTDouble) nbt).value);
        } else if (nbt instanceof NBTByteArray) {
            return NBTTagByteArray.newInstance((Object) ((NBTByteArray) nbt).value);
        } else if (nbt instanceof NBTString) {
            return NBTTagString.newInstance(((NBTString) nbt).value);
        } else if (nbt instanceof NBTList) {
            final Object nbtList = NBTTagList.newInstance();
            //noinspection unchecked
            for (NBT element : (List<NBT>)((NBTList) nbt).value) {
                NBTTagList_add.invoke(nbtList, toNMSTag(element));
            }
            return nbtList;
        } else if (nbt instanceof NBTCompound) {
            final Object nbtCompound = NBTTagCompound.newInstance();
            for (Map.Entry<String, NBT> entry : ((NBTCompound) nbt).value.entrySet()) {
                NBTTagCompound_set.invoke(nbtCompound, entry.getKey(), toNMSTag(entry.getValue()));
            }
            return nbtCompound;
        } else if (nbt instanceof NBTIntArray) {
            return NBTTagIntArray.newInstance((Object) ((NBTIntArray) nbt).value);
        } else {
            throw new IllegalArgumentException("Invalid NBT tag: "+nbt);
        }
    }

    private static final byte TYPE_End          = 0;
    private static final byte TYPE_Byte         = 1;
    private static final byte TYPE_Short        = 2;
    private static final byte TYPE_Int          = 3;
    private static final byte TYPE_Long         = 4;
    private static final byte TYPE_Float        = 5;
    private static final byte TYPE_Double       = 6;
    private static final byte TYPE_Byte_Array   = 7;
    private static final byte TYPE_String       = 8;
    private static final byte TYPE_List         = 9;
    private static final byte TYPE_Compound     = 10;
    private static final byte TYPE_Int_Array    = 11;

    public static final class NBTByte extends NBT {
        public final byte value;

        public NBTByte(byte value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+"["+value+"]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.value == ((NBTByte) obj).value;
        }
    }

    public static final class NBTShort extends NBT {
        public final short value;

        public NBTShort(short value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+"["+value+"]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.value == ((NBTShort) obj).value;
        }

    }

    public static final class NBTInt extends NBT {
        public final int value;

        public NBTInt(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+"["+value+"]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.value == ((NBTInt) obj).value;
        }

    }

    public static final class NBTLong extends NBT {
        public final long value;

        public NBTLong(long value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+"["+value+"]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.value == ((NBTLong) obj).value;
        }

    }

    public static final class NBTFloat extends NBT {
        public final float value;

        public NBTFloat(float value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+"["+value+"]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.value == ((NBTFloat) obj).value;
        }

    }

    public static final class NBTDouble extends NBT {
        public final double value;

        public NBTDouble(double value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+"["+value+"]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.value == ((NBTDouble) obj).value;
        }
    }

    public static final class NBTByteArray extends NBT {
        public final byte[] value;

        public NBTByteArray(byte[] value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+ Arrays.toString(value);
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && Arrays.equals(this.value, ((NBTByteArray) obj).value);
        }
    }

    public static final class NBTString extends NBT {
        public final String value;

        public NBTString(String value) {
            if (value == null) throw new NullPointerException("value can't be null");
            this.value = value;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+"["+value+"]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.value.equals(((NBTString) obj).value);
        }
    }

    public static final class NBTList <Element extends NBT> extends NBT {
        public final List<Element> value = new ArrayList<>();

        public NBTList() {
        }

        public NBTList(Collection<Element> values) {
            this.value.addAll(values);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+"["+value+"]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.value.equals( ((NBTList) obj).value);
        }
    }

    public static final class NBTCompound extends NBT {
        public final Map<String, NBT> value = new HashMap<>();

        public NBTCompound() {
        }

        public NBTCompound(Map<String, NBT> initialValues) {
            this.value.putAll(initialValues);
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+"["+value+"]";
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && this.value.equals(((NBTCompound)obj).value);
        }

    }

    public static final class NBTIntArray extends NBT {
        public final int[] value;

        public NBTIntArray(int[] value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName()+ Arrays.toString(value);
        }

        @Override
        public boolean equals(Object obj) {
            return obj.getClass() == getClass() && Arrays.equals(this.value, ((NBTIntArray) obj).value);
        }
    }
}
