package org.springlibrary.exceptions;

public class DatabaseInitializationException extends RuntimeException {
    public DatabaseInitializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
