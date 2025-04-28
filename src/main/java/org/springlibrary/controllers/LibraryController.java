package org.springlibrary.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springlibrary.exceptions.BookNotFoundException;
import org.springlibrary.exceptions.BookPersistenceException;
import org.springlibrary.exceptions.InvalidBookException;
import org.springlibrary.models.Book;
import org.springlibrary.services.LibraryService;
import org.springlibrary.services.MessagesService;
import org.springlibrary.services.io.ConsoleService;
import org.springlibrary.services.io.IOService;

import java.text.MessageFormat;
import java.util.List;

@Controller
public class LibraryController {
    private static final Logger logger = LoggerFactory.getLogger(LibraryController.class);
    private static final String FORMAT_TWO_PARTS = "{}: {}";

    private final IOService ioService;
    private final MessagesService message;
    private final LibraryService libraryService;

    public LibraryController(ConsoleService consoleService, MessagesService messagesService, LibraryService libraryService) {
        this.ioService = consoleService;
        this.message = messagesService;
        this.libraryService = libraryService;
    }

    public void startBookManagement() {
        libraryService.startSession();
        boolean exit = false;

        ioService.print(message.getMessage("welcome"));
        while (!exit) {
            ioService.print(message.getMessage("choose.option"));
            ioService.print("1) " + message.getMessage("option.1"));
            ioService.print("2) " + message.getMessage("option.2"));
            ioService.print("3) " + message.getMessage("option.3"));
            ioService.print("4) " + message.getMessage("option.4"));
            ioService.print("e) " + message.getMessage("option.e"));

            String userInput = this.ioService.prompt("\n" + message.getMessage("prompt.choose"));

            switch (userInput) {
                case "1" -> displayBookList();
                case "2" -> createNewBook();
                case "3" -> editBook();
                case "4" -> deleteBook();
                case "e" -> exit = true;
                default -> {
                    if (logger.isWarnEnabled()) {
                        logger.warn(message.getMessage("invalid.option"));
                    }
                }
            }
            ioService.print("");
        }
    }

    private void displayBookList() {
        List<Book> booksList = libraryService.getAllBooks();
        for (Book book : booksList) {
            ioService.print(getFormattedBookView(book));
        }
    }

    private void createNewBook() {
        String title = ioService.prompt(message.getMessage("prompt.title"));
        String author = ioService.prompt(message.getMessage("prompt.author"));
        String description = ioService.prompt(message.getMessage("prompt.description"));

        try {
            libraryService.addBook(new Book(title, author, description));
            if (logger.isInfoEnabled()) {
                logger.info("{}", message.getMessage("book.add.success"));
            }
        } catch (InvalidBookException e) {
            logger.error(FORMAT_TWO_PARTS, message.getMessage("book.add.failed.user"), e.getMessage());
        } catch (BookPersistenceException e) {
            logger.error(FORMAT_TWO_PARTS, message.getMessage("book.add.failed.system"), e.getMessage());
        }
    }

    private void editBook() {
        String userInput = ioService.prompt(message.getMessage("prompt.find.idOrTitle"));

        Book book;
        try {
            book = libraryService.findByIdOrTitle(userInput);
        } catch (BookNotFoundException e) {
            logger.error(FORMAT_TWO_PARTS, message.getMessage("error.book.notfound"), e.getMessage());
            return;
        }

        String title = ioService.prompt(message.getMessage("prompt.new.title"));
        if (!title.isBlank()) book.setTitle(title);

        String author = ioService.prompt(message.getMessage("prompt.new.author"));
        if (!author.isBlank()) book.setAuthor(author);

        String description = ioService.prompt(message.getMessage("prompt.new.description"));
        if (!description.isBlank()) book.setDescription(description);

        try {
            libraryService.updateBook(book);
            if (logger.isInfoEnabled()) {
                logger.info("{}", message.getMessage("book.update.success"));
            }
        } catch (BookNotFoundException | InvalidBookException e) {
            logger.error(FORMAT_TWO_PARTS, message.getMessage("book.update.failed.user"), e.getMessage());
        } catch (BookPersistenceException e) {
            logger.error(FORMAT_TWO_PARTS, message.getMessage("book.update.failed.system"), e.getMessage());
        }
    }

    private void deleteBook() {
        String userInput = ioService.prompt(message.getMessage("prompt.find.idOrTitle"));

        try {
            libraryService.deleteByIdOrTitle(userInput);
            if (logger.isInfoEnabled()) {
                logger.info("{}", message.getMessage("book.delete.success"));
            }
        } catch (BookNotFoundException e) {
            logger.error(FORMAT_TWO_PARTS, message.getMessage("book.delete.failed.user"), e.getMessage());
        }
    }

    // Helper
    private String getFormattedBookView(Book book) {
        return MessageFormat.format(message.getMessage("book.view.format"),
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription());
    }
}
