package de.tr7zw.itemnbtapi;

/**
 *
 */
public class ReflectionClass {

    public static final Class[] NO_CLASSES = new Class[0];
    public static final Object[] NO_OBJECTS = new Object[0];

    private final Class cache;

    public ReflectionClass(String className) {
        try {
            cache = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new NBTAPIException("Failed to get class \""+className+"\"", e);
        }
    }

    public Object newInstance() {
        try {
            return cache.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new NBTAPIException("Failed to create new instance of "+this, e);
        }
    }

    public ReflectionMethod method(String methodName, Class...parameters) {
        return new ReflectionMethod(this, methodName, parameters);
    }

    public ReflectionMethod method(String methodName) {
        return new ReflectionMethod(this, methodName, NO_CLASSES);
    }

    public Class Class() {
        return cache;
    }

    @Override
    public String toString() {
        return cache.getName();
    }
}
