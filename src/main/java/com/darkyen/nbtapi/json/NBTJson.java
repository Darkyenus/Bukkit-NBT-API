package com.darkyen.nbtapi.json;

import com.darkyen.nbtapi.NBT;
import com.darkyen.nbtapi.NBTAPIException;
import com.esotericsoftware.jsonbeans.JsonValue;

/**
 *
 */
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

    public static NBT nbtFromJson(JsonValue value) throws NBTAPIException {
        return nbtFromJson(value, null, null, null);
    }

    public static NBT nbtFromJson(JsonValue value, NBT.Type preferredInteger, NBT.Type preferredFloat, NBT.Type preferredListOrArray) throws NBTAPIException {
        switch (value.type()) {
            case object: {
                final NBT.NBTCompound result = new NBT.NBTCompound();
                for (JsonValue element : value) {
                    result.value.put(element.name(), nbtFromJson(element, preferredInteger, preferredFloat, preferredListOrArray));
                }
                return result;
            }
            case array: {
                if (value.size == 0) {
                    return new NBT.NBTList<>();
                } else {
                    // All values must be same type. Which type is this?
                    final byte NON_INTEGER = -1;
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

                    if (widestType == BYTE && preferredListOrArray != NBT.Type.Int_Array) {
                        //It can be byte array (and called did not prefer int array, which is also possible)!
                        return new NBT.NBTByteArray(value.asByteArray());
                    } else if (widestType <= INT) {
                        //It can be int array, and we will do just that
                        return new NBT.NBTIntArray(value.asIntArray());
                    } else {
                        //It is something different, do something sane

                        final NBT.NBTList<NBT> result = new NBT.NBTList<>();
                        for (JsonValue element : value) {
                            result.value.add(nbtFromJson(element, NBT.Type.Long, NBT.Type.Double, preferredListOrArray));
                        }
                        return result;
                    }
                }
            }
            case stringValue:
                return new NBT.NBTString(value.asString());
            case doubleValue: {
                final double d = value.asDouble();
                if (((float)d) == d || preferredFloat != NBT.Type.Double) {
                    return new NBT.NBTFloat((float)d);
                } else {
                    return new NBT.NBTDouble(d);
                }
            }
            case longValue:{
                final long l = value.asLong();
                final byte width = getWidth(l);

                if (preferredInteger != null) {
                    switch (preferredInteger) {//byte is automatic, if possible
                        case Short:
                            if (width <= SHORT) {
                                return new NBT.NBTShort((short)l);
                            }
                            break;
                        case Int:
                            if (width <= INT) {
                                return new NBT.NBTInt((int)l);
                            }
                            break;
                        case Long:
                            return new NBT.NBTLong(l);
                    }
                }

                switch (width) {
                    case BYTE: return new NBT.NBTByte((byte)l);
                    case SHORT: return new NBT.NBTShort((short)l);
                    case INT: return new NBT.NBTInt((int)l);
                    case LONG: return new NBT.NBTLong(l);
                }
            }
            case booleanValue:
                return new NBT.NBTByte((byte) (value.asBoolean() ? 1 : 0));
            case nullValue:
                return new NBT.NBTString("");
            default:
                throw new NBTAPIException("Unknown type of JsonValue: "+value);
        }
    }

}
