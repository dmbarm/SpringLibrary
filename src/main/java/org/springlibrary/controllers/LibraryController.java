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
        }
    }

    private void displayBookList() {
        List<Book> booksList = libraryService.getAllBooks();
        for (Book book : booksList) {
            System.out.println(book);
        }
    }

    private void createNewBook() {
        String title = input.prompt("Enter title of the book:");
        String author = input.prompt("Enter author of the book:");
        String description = input.prompt("Enter short description for the book:");;

        try {
            libraryService.addBook(new Book(title, author, description));
            System.out.println("Book added successfully.");
        } catch (InvalidBookException | DuplicateBookException e) {
            System.err.println("Could not add book: " + e.getMessage());
        } catch (BookPersistenceException e) {
            System.err.println("System error: failed to save the book.");
        }
    }

    private void editBook() {
        String input = this.input.prompt("Enter id or title of the book:");

        Book book;
        try {
            book = libraryService.findByIdOrTitle(input);
        } catch (BookNotFoundException e) {
            System.err.println(e.getMessage());
            return;
        }

        String title = this.input.prompt("Enter new title (leave blank to keep current): ");
        if (!title.isBlank()) book.setTitle(title);

        String author = this.input.prompt("Enter new author (leave blank to keep current):");
        if (!author.isBlank()) book.setAuthor(author);

        String description = this.input.prompt("Enter new description (leave blank to keep current):");
        if (!description.isBlank()) book.setDescription(description);

        try {
            libraryService.updateBook(book);
            System.out.println("Book updated successfully.");
        } catch (BookNotFoundException | InvalidBookException e) {
            System.err.println(e.getMessage());
        } catch (BookPersistenceException e) {
            System.err.println("System error: could not save the book.");
        }
    }

    private void deleteBook() {
        String input = this.input.prompt("Enter id or title of the book to delete:");

        try {
            libraryService.deleteByIdOrTitle(input);
            System.out.println("Book deleted successfully.");
        } catch (BookNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
