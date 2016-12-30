package com.darkyen.nbtapi.json;

import com.darkyen.nbtapi.nbt.NBTBase;
import com.darkyen.nbtapi.NBTException;
import com.darkyen.nbtapi.nbt.NBTTagType;
import com.darkyen.nbtapi.nbt.NBTCompound;
import com.darkyen.nbtapi.nbt.*;
import com.esotericsoftware.jsonbeans.JsonValue;

import java.util.List;
import java.util.Map;

/**
 * API for conversion between NBT and Json
 */
@SuppressWarnings("unused")
public final class NBTJson {

    private NBTJson() {}

    private static final byte BYTE = 1;
    private static final byte SHORT = 2;
    private static final byte INT = 3;
    private static final byte LONG = 4;

    private static byte getWidth(long number) {
        if (((byte)number) == number) {
            return BYTE;
        } else if (((short)number) == number) {
            return SHORT;
        } else if (((int)number) == number) {
            return INT;
        } else {
            return LONG;
        }
    }

    public static NBTBase nbtFromJson(JsonValue value) throws NBTException {
        return nbtFromJson(value, null, null, null);
    }

    public static NBTBase nbtFromJson(JsonValue value, NBTTagType preferredInteger, NBTTagType preferredFloat, NBTTagType preferredListOrArray) throws NBTException {
        switch (value.type()) {
            case object: {
                final NBTCompound result = new NBTCompound();
                for (JsonValue element : value) {
                    result.value.put(element.name(), nbtFromJson(element, preferredInteger, preferredFloat, preferredListOrArray));
                }
                return result;
            }
            case array: {
                if (value.size == 0) {
                    return new NBTList<>();
                } else {
                    // All values must be same type. Which type is this?
                    final byte NON_INTEGER = Byte.MAX_VALUE;
                    byte widestType = 0;

                    for (JsonValue element : value) {
                        if (element.type() != JsonValue.ValueType.longValue) {
                            widestType = NON_INTEGER;
                            break;
                        } else {
                            final byte width = getWidth(element.asLong());
                            if (width > widestType) {
                                widestType = width;
                                if (width == LONG) break;
                            }
                        }
                    }

                    if (widestType == BYTE && preferredListOrArray != NBTTagType.Int_Array) {
                        //It can be byte array (and called did not prefer int array, which is also possible)!
                        return new NBTByteArray(value.asByteArray());
                    } else if (widestType <= INT) {
                        //It can be int array, and we will do just that
                        return new NBTIntArray(value.asIntArray());
                    } else {
                        //It is something different, do something sane

                        final NBTList<NBTBase> result = new NBTList<>();
                        for (JsonValue element : value) {
                            result.value.add(nbtFromJson(element, NBTTagType.Long, NBTTagType.Double, preferredListOrArray));
                        }
                        return result;
                    }
                }
            }
            case stringValue:
                return new NBTString(value.asString());
            case doubleValue: {
                final double d = value.asDouble();
                if (((float)d) == d || preferredFloat != NBTTagType.Double) {
                    return new NBTFloat((float)d);
                } else {
                    return new NBTDouble(d);
                }
            }
            case longValue:{
                final long l = value.asLong();
                final byte width = getWidth(l);

                if (preferredInteger != null) {
                    switch (preferredInteger) {//byte is automatic, if possible
                        case Short:
                            if (width <= SHORT) {
                                return new NBTShort((short)l);
                            }
                            break;
                        case Int:
                            if (width <= INT) {
                                return new NBTInt((int)l);
                            }
                            break;
                        case Long:
                            return new NBTLong(l);
                    }
                }

                switch (width) {
                    case BYTE: return new NBTByte((byte)l);
                    case SHORT: return new NBTShort((short)l);
                    case INT: return new NBTInt((int)l);
                    case LONG: return new NBTLong(l);
                }
            }
            case booleanValue:
                return new NBTByte((byte) (value.asBoolean() ? 1 : 0));
            case nullValue:
                return new NBTString("");
            default:
                throw new NBTException("Unknown type of JsonValue: "+value);
        }
    }

    public static JsonValue nbtToJson(NBTBase nbt) {
        if (nbt instanceof NBTByte) {
            return new JsonValue(((NBTByte) nbt).value);
        } else if (nbt instanceof NBTShort) {
            return new JsonValue(((NBTShort) nbt).value);
        } else if (nbt instanceof NBTInt) {
            return new JsonValue(((NBTInt) nbt).value);
        } else if (nbt instanceof NBTLong) {
            return new JsonValue(((NBTLong) nbt).value);
        } else if (nbt instanceof NBTFloat) {
            return new JsonValue(((NBTFloat) nbt).value);
        } else if (nbt instanceof NBTDouble) {
            return new JsonValue(((NBTDouble) nbt).value);
        } else if (nbt instanceof NBTByteArray) {
            final byte[] array = ((NBTByteArray) nbt).value;
            final JsonValue result = new JsonValue(JsonValue.ValueType.array);
            result.size = array.length;
            if (array.length != 0) {
                JsonValue element = new JsonValue(array[0]);
                result.child = element;
                for (int i = 1; i < array.length; i++) {
                    JsonValue next = new JsonValue(array[i]);
                    element.next = next;
                    next.prev = element;
                    element = next;
                }
            }
            return result;
        } else if (nbt instanceof NBTString) {
            return new JsonValue(((NBTString) nbt).value);
        } else if (nbt instanceof NBTList) {
            @SuppressWarnings("unchecked") final List<NBTBase> array = ((NBTList) nbt).value;
            final JsonValue result = new JsonValue(JsonValue.ValueType.array);
            result.size = array.size();
            if (array.size() != 0) {
                JsonValue element = nbtToJson(array.get(0));
                result.child = element;
                for (int i = 1; i < array.size(); i++) {
                    JsonValue next = nbtToJson(array.get(i));
                    element.next = next;
                    next.prev = element;
                    element = next;
                }
            }
            return result;
        } else if (nbt instanceof NBTCompound) {
            final JsonValue result = new JsonValue(JsonValue.ValueType.object);

            JsonValue previous = null;
            for (Map.Entry<String, NBTBase> entry : ((NBTCompound) nbt).value.entrySet()) {
                final JsonValue next = nbtToJson(entry.getValue());
                next.name = entry.getKey();
                if (previous == null) {
                    result.child = next;
                } else {
                    previous.next = next;
                    next.prev = previous;
                }
                previous = next;
            }

            return result;
        } else if (nbt instanceof NBTIntArray) {
            final int[] array = ((NBTIntArray) nbt).value;
            final JsonValue result = new JsonValue(JsonValue.ValueType.array);
            result.size = array.length;
            if (array.length != 0) {
                JsonValue element = new JsonValue(array[0]);
                result.child = element;
                for (int i = 1; i < array.length; i++) {
                    JsonValue next = new JsonValue(array[i]);
                    element.next = next;
                    next.prev = element;
                    element = next;
                }
            }
            return result;
        } else {
            throw new IllegalArgumentException("Invalid NBT tag: " + nbt);
        }
    }

}
