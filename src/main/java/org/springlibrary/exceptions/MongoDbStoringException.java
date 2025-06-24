package org.springlibrary.exceptions;

public class MongoDbStoringException extends RuntimeException {
    public MongoDbStoringException(String message) {
        super(message);
    }
}
