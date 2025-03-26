package org.springlibrary.exceptions;

public class BookPersistenceException extends RuntimeException {
    public BookPersistenceException(String message) {
        super(message);
    }

    public BookPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
