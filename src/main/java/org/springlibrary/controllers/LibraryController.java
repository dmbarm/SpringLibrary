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

        System.out.println(message.getString("welcome"));
        while (!exit) {
            System.out.println(message.getString("choose.option"));
            System.out.println("1) " + message.getString("option.1"));
            System.out.println("2) " + message.getString("option.2"));
            System.out.println("3) " + message.getString("option.3"));
            System.out.println("4) " + message.getString("option.4"));
            System.out.println("e) " + message.getString("option.e"));

            String input = this.input.prompt("\n" + message.getString("prompt.choose"));

            switch (input) {
                case "1" -> displayBookList();
                case "2" -> createNewBook();
                case "3" -> editBook();
                case "4" -> deleteBook();
                case "e" -> exit = true;
                default -> System.out.println(message.getString("invalid.option"));
            }
            System.out.println();
        }
    }

    private void displayBookList() {
        List<Book> booksList = libraryService.getAllBooks();
        for (Book book : booksList) {
            System.out.println(book);
        }
    }

    private void createNewBook() {
        String title = input.prompt(message.getString("prompt.title"));
        String author = input.prompt(message.getString("prompt.author"));
        String description = input.prompt(message.getString("prompt.description"));

        try {
            libraryService.addBook(new Book(title, author, description));
            System.out.println(message.getString("book.add.success"));
        } catch (InvalidBookException | DuplicateBookException e) {
            System.err.println(message.getString("book.add.failed.user") + ": " + e.getMessage());
        } catch (BookPersistenceException e) {
            System.err.println(message.getString("book.add.failed.system"));
        }
    }

    private void editBook() {
        String userInput = input.prompt(message.getString("prompt.find.idOrTitle"));

        Book book;
        try {
            book = libraryService.findByIdOrTitle(userInput);
        } catch (BookNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }

        String title = input.prompt(message.getString("prompt.new.title"));
        if (!title.isBlank()) book.setTitle(title);

        String author = input.prompt(message.getString("prompt.new.author"));
        if (!author.isBlank()) book.setAuthor(author);

        String description = input.prompt(message.getString("prompt.new.description"));
        if (!description.isBlank()) book.setDescription(description);

        try {
            libraryService.updateBook(book);
            System.out.println(message.getString("book.update.success"));
        } catch (BookNotFoundException | InvalidBookException e) {
            System.err.println(message.getString("book.update.failed.user") + ": " + e.getMessage());
        } catch (BookPersistenceException e) {
            System.err.println(message.getString("book.update.failed.system"));
        }
    }

    private void deleteBook() {
        String userInput = input.prompt(message.getString("prompt.find.idOrTitle"));

        try {
            libraryService.deleteByIdOrTitle(userInput);
            System.out.println(message.getString("book.delete.success"));
        } catch (BookNotFoundException e) {
            System.err.println(message.getString("book.delete.failed.user") + ": " + e.getMessage());
        }
    }
}
