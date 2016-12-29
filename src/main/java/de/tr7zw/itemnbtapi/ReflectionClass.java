package de.tr7zw.itemnbtapi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 *
 */
public class ReflectionClass {

    public static final Class[] NO_CLASSES = new Class[0];
    public static final Object[] NO_OBJECTS = new Object[0];

    private final Class clazz;
    private final Constructor[] constructors;

    public ReflectionClass(String className) {
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new NBTAPIException("Failed to get class \""+className+"\"", e);
        }
        constructors = clazz.getConstructors();
    }

    public Object newInstance() {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new NBTAPIException("Failed to create new instance of "+this, e);
        }
    }

    public Object newInstance(Object...parameters) {
        Constructor bestFit = null;
        for (Constructor constructor : constructors) {
            if (constructor.getParameterCount() == parameters.length) {
                if (bestFit != null) throw new NBTAPIException("Can't determine best constructor from parameter count alone");
                bestFit = constructor;
            }
        }
        if (bestFit == null) throw new NBTAPIException("No constructor with "+parameters.length+" parameters");

        try {
            return bestFit.newInstance(parameters);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new NBTAPIException("Failed to create new instance of "+this+", using "+bestFit+" with parameters "+ Arrays.toString(parameters));
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
