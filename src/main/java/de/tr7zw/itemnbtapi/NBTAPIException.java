package de.tr7zw.itemnbtapi;

/**
 *
 */
public class NBTAPIException extends RuntimeException {
    public NBTAPIException(String message) {
        super(message);
    }

    public NBTAPIException(String message, Throwable cause) {
        super(message, cause);
    }
}
