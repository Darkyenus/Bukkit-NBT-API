package com.darkyen.nbtapi;

/**
 * Thrown when something goes wrong in this library.
 *
 * When this gets thrown, it most likely means that this library is used with incompatible minecraft version.
 */
public class NBTException extends RuntimeException {

    public NBTException(String message) {
        super(message);
    }

    public NBTException(String message, Throwable cause) {
        super(message, cause);
    }
}
