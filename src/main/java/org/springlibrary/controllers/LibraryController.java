package org.springlibrary.controllers;

import org.springframework.stereotype.Controller;
import org.springlibrary.exceptions.BookNotFoundException;
import org.springlibrary.exceptions.BookPersistenceException;
import org.springlibrary.exceptions.DuplicateBookException;
import org.springlibrary.exceptions.InvalidBookException;
import org.springlibrary.models.Book;
import org.springlibrary.services.InputService;
import org.springlibrary.services.LibraryService;
import org.springlibrary.services.MessagesService;

import java.text.MessageFormat;
import java.util.List;

@Controller
public class LibraryController {
    private final InputService input;
    private final MessagesService message;
    private final LibraryService libraryService;

    public LibraryController(InputService inputService, MessagesService messagesService, LibraryService libraryService) {
        this.input = inputService;
        this.message = messagesService;
        this.libraryService = libraryService;
    }

    public void startBookManagement() {
        libraryService.startSession();
        boolean exit = false;

        System.out.println(message.getMessage("welcome"));
        while (!exit) {
            System.out.println(message.getMessage("choose.option"));
            System.out.println("1) " + message.getMessage("option.1"));
            System.out.println("2) " + message.getMessage("option.2"));
            System.out.println("3) " + message.getMessage("option.3"));
            System.out.println("4) " + message.getMessage("option.4"));
            System.out.println("e) " + message.getMessage("option.e"));

            String input = this.input.prompt("\n" + message.getMessage("prompt.choose"));

            switch (input) {
                case "1" -> displayBookList();
                case "2" -> createNewBook();
                case "3" -> editBook();
                case "4" -> deleteBook();
                case "e" -> exit = true;
                default -> System.out.println(message.getMessage("invalid.option"));
            }
            System.out.println();
        }
    }

    private void displayBookList() {
        List<Book> booksList = libraryService.getAllBooks();
        for (Book book : booksList) {
            System.out.println(getFormattedBookView(book));
        }
    }

    private void createNewBook() {
        String title = input.prompt(message.getMessage("prompt.title"));
        String author = input.prompt(message.getMessage("prompt.author"));
        String description = input.prompt(message.getMessage("prompt.description"));

        try {
            libraryService.addBook(new Book(title, author, description));
            System.out.println(message.getMessage("book.add.success"));
        } catch (InvalidBookException | DuplicateBookException e) {
            System.err.println(message.getMessage("book.add.failed.user") + ": " + message.getMessage(e.getMessage()));
        } catch (BookPersistenceException e) {
            System.err.println(message.getMessage("book.add.failed.system"));
        }
    }

    private void editBook() {
        String userInput = input.prompt(message.getMessage("prompt.find.idOrTitle"));

        Book book;
        try {
            book = libraryService.findByIdOrTitle(userInput);
        } catch (BookNotFoundException e) {
            System.err.println(message.getMessage("error.book.notfound"));
            return;
        }

        String title = input.prompt(message.getMessage("prompt.new.title"));
        if (!title.isBlank()) book.setTitle(title);

        String author = input.prompt(message.getMessage("prompt.new.author"));
        if (!author.isBlank()) book.setAuthor(author);

        String description = input.prompt(message.getMessage("prompt.new.description"));
        if (!description.isBlank()) book.setDescription(description);

        try {
            libraryService.updateBook(book);
            System.out.println(message.getMessage("book.update.success"));
        } catch (BookNotFoundException | InvalidBookException e) {
            System.err.println(message.getMessage("book.update.failed.user") + ": " + message.getMessage(e.getMessage()));
        } catch (BookPersistenceException e) {
            System.err.println(message.getMessage("book.update.failed.system"));
        }
    }

    private void deleteBook() {
        String userInput = input.prompt(message.getMessage("prompt.find.idOrTitle"));

        try {
            libraryService.deleteByIdOrTitle(userInput);
            System.out.println(message.getMessage("book.delete.success"));
        } catch (BookNotFoundException e) {
            System.err.println(message.getMessage("book.delete.failed.user") + ": " + message.getMessage(e.getMessage()));
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
