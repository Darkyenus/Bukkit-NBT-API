package de.tr7zw.itemnbtapi;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 *
 */
@SuppressWarnings("unused")
public final class ReflectionMethod {

    private final Method method;

    public ReflectionMethod(ReflectionClass ofClass, String methodName, Class...parameters) {
        try {
            //noinspection unchecked
            method = ofClass.Class().getMethod(methodName, parameters);
        } catch (NoSuchMethodException e) {
            throw new NBTAPIException("Method \""+methodName+"\" of class "+ofClass+" not found", e);
        }
    }

    public <Result> Result invoke(Object instance) {
        return invoke(instance, ReflectionClass.NO_OBJECTS);
    }

    public <Result> Result invoke(Object instance, Object...parameters) {
        try {
            //noinspection unchecked
            return (Result)method.invoke(instance, parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new NBTAPIException("Failed to invoke "+method.getDeclaringClass().getName()+"#"+method.getName(), e);
        }
    }

    public <Result> Result invokeStatic() {
        return invokeStatic(ReflectionClass.NO_OBJECTS);
    }

    public <Result> Result invokeStatic(Object...parameters) {
        return invoke(null, parameters);
    }

}
