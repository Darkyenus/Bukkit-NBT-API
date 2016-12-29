package de.tr7zw.itemnbtapi;

import java.lang.reflect.Field;

/**
 *
 */
public class ReflectionField<Type> {

    private final Field field;

    ReflectionField(ReflectionClass ofClass, String fieldName) {
        Field field;
        try {
            field = ofClass.Class().getField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                field = ofClass.Class().getDeclaredField(fieldName);
            } catch (NoSuchFieldException e1) {
                throw new NBTAPIException("Field "+ofClass+"#"+fieldName+" does not exist", e1);
            }
        }
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        this.field = field;
    }

    public void set(Object instance, Type value) {
        try {
            field.set(instance, value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new NBTAPIException("Can't set field "+field+" of "+instance+" to "+value, e);
        }
    }

    public Type get(Object instance) {
        try {
            //noinspection unchecked
            return (Type)field.get(instance);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            throw new NBTAPIException("Can't get field "+field+" of "+instance, e);
        }
    }
}
