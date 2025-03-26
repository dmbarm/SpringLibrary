package org.springlibrary.controllers;

import org.springframework.stereotype.Controller;
import org.springlibrary.exceptions.BookNotFoundException;
import org.springlibrary.exceptions.BookPersistenceException;
import org.springlibrary.exceptions.DuplicateBookException;
import org.springlibrary.exceptions.InvalidBookException;
import org.springlibrary.models.Book;
import org.springlibrary.services.LibraryService;

import java.util.List;
import java.util.Scanner;

@Controller
public class LibraryController {
    //todo add some field
    private final Scanner scanner = new Scanner(System.in);
    private final LibraryService libraryService;

    public LibraryController(LibraryService libraryService) {
        this.libraryService = libraryService;
    }

    public void startUserInput() {
        boolean exit = false;

        while (!exit) {
            System.out.println("Welcome to the library!");
            System.out.println("Choose an option:");
            System.out.println("1) Display book list");
            System.out.println("2) Create new book");
            System.out.println("3) Edit book");
            System.out.println("4) Delete book");
            System.out.print("\nChoose an option: ");

            String input = scanner.nextLine();
            switch (input) {
                case "1" -> displayBookList();
                case "2" -> createNewBook();
                case "3" -> editBook();
                case "4" -> deleteBook();
                case "e" -> exit = true;
                default -> System.out.println("Invalid option, please try again.");
            }
        }

        //todo make closing of everything(if needed)
    }

    private void displayBookList() {
        List<Book> booksList = libraryService.getAllBooks();
        for (Book book : booksList) {
            System.out.println(book);
        }
    }

    private void createNewBook() {
        System.out.println("Enter title of the book:");
        String title = scanner.nextLine();
        System.out.println("Enter author of the book:");
        String author = scanner.nextLine();
        System.out.println("Enter short description for the book:");
        String description = scanner.nextLine();

        try {
            libraryService.addBook(new Book(title, author, description));
            System.out.println("Book added successfully.");
        } catch (InvalidBookException | DuplicateBookException e) {
            System.out.println("Could not add book: " + e.getMessage());
        } catch (BookPersistenceException e) {
            System.out.println("System error: failed to save the book.");
        }
    }

    private void editBook() {
        System.out.println("Enter id or title of the book:");
        String input = scanner.nextLine();

        Book book;
        try {
            book = libraryService.findByIdOrTitle(input);
        } catch (BookNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }

        System.out.println("Enter new title (leave blank to keep current):");
        String title = scanner.nextLine();
        if (!title.isBlank()) book.setTitle(title);

        System.out.println("Enter new author (leave blank to keep current):");
        String author = scanner.nextLine();
        if (!author.isBlank()) book.setAuthor(author);

        System.out.println("Enter new description (leave blank to keep current):");
        String description = scanner.nextLine();
        if (!description.isBlank()) book.setDescription(description);

        try {
            libraryService.updateBook(book);
            System.out.println("Book updated successfully.");
        } catch (BookNotFoundException | InvalidBookException e) {
            System.out.println(e.getMessage());
        } catch (BookPersistenceException e) {
            System.out.println("System error: could not save the book.");
        }
    }

    private void deleteBook() {
        System.out.println("Enter id or title of the book to delete:");
        String input = scanner.nextLine();

        try {
            libraryService.deleteByIdOrTitle(input);
            System.out.println("Book deleted successfully.");
        } catch (BookNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
