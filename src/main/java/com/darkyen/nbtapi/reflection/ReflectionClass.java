package com.darkyen.nbtapi.reflection;

import com.darkyen.nbtapi.NBTException;
import org.bukkit.Bukkit;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 *
 */
public class ReflectionClass {

    public static final String BukkitVersion = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];

    public static final Class[] NO_CLASSES = new Class[0];
    public static final Object[] NO_OBJECTS = new Object[0];

    private final Class clazz;
    private final Constructor[] constructors;

    public ReflectionClass(String className) {
        final String replacedClassName = className.replace("{BukkitVersion}", BukkitVersion);
        try {
            clazz = Class.forName(replacedClassName);
        } catch (ClassNotFoundException e) {
            throw new NBTException("Failed to get class \""+replacedClassName+"\" "+(replacedClassName.equals(className) ? "" : "(Original: \""+className+"\")"), e);
        }
        constructors = clazz.getConstructors();
    }

    public Object newInstance() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new NBTException("Failed to create new instance of "+this, e);
        }
    }

    public Object newInstance(Object...parameters) {
        Constructor bestFit = null;
        for (Constructor constructor : constructors) {
            if (constructor.getParameterCount() == parameters.length) {
                if (bestFit != null) throw new NBTException("Can't determine best constructor from parameter count alone");
                bestFit = constructor;
            }
        }
        if (bestFit == null) throw new NBTException("No constructor with "+parameters.length+" parameters");

        try {
            return bestFit.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new NBTException("Failed to create new instance of "+this+", using "+bestFit+" with parameters "+ Arrays.toString(parameters));
        }
    }

    public <Result> ReflectionMethod<Result> method(String methodName, Class...parameters) {
        return new ReflectionMethod<>(this, methodName, parameters);
    }

    public <Result> ReflectionMethod<Result> method(String methodName) {
        return new ReflectionMethod<>(this, methodName, NO_CLASSES);
    }

    public <Type> ReflectionField<Type> field(String fieldName) {
        return new ReflectionField<>(this, fieldName);
    }

    public Class Class() {
        return clazz;
    }

    @Override
    public String toString() {
        return clazz.getName();
    }
}
