package org.springlibrary.exceptions;

public class BookCoverNotFoundException extends RuntimeException {
  public BookCoverNotFoundException(String message) {
    super(message);
  }
}
