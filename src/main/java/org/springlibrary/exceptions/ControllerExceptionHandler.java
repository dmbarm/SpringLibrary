package org.springlibrary.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springlibrary.services.MessagesService;

@ControllerAdvice
public class ControllerExceptionHandler {
    private final MessagesService messagesService;

    public ControllerExceptionHandler(MessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<String> handleNotFound(final BookNotFoundException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(messagesService.getMessage(e.getMessage()));
    }

    @ExceptionHandler(InvalidBookException.class)
    public ResponseEntity<String> handleInvalid(final InvalidBookException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(messagesService.getMessage(e.getMessage()));
    }

    @ExceptionHandler(MongoDbStoringException.class)
    public ResponseEntity<String> handleMongoDbStoring(final MongoDbStoringException e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(messagesService.getMessage(e.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneral(final Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(messagesService.getMessage("error.internal-server"));
    }
}
