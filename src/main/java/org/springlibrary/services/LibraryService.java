package org.springlibrary.services;

import org.springframework.stereotype.Service;
import org.springlibrary.exceptions.BookNotFoundException;
import org.springlibrary.exceptions.InvalidBookException;
import org.springlibrary.models.Book;
import org.springlibrary.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    private final BooksRepository booksRepository;

    public LibraryService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    public List<Book> getAllBooks() {
        return booksRepository.findAll();
    }

    public void addBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new InvalidBookException("error.book.title.empty");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new InvalidBookException("error.book.author.empty");
        }

        booksRepository.create(book);
    }

    public Book findByIdOrTitle(String input) {
        Optional<Book> result;

        if (input.matches("\\d+")) {
            int id = Integer.parseInt(input);
            result = booksRepository.findById(id);
        } else {
            result = booksRepository.findByTitle(input);
        }

        return result.orElseThrow(() -> new BookNotFoundException("error.book.notfound"));
    }

    public void updateBook(Book book) {
        int updated = booksRepository.update(book);

        if (updated == 0) {
            throw new BookNotFoundException("error.book.notfound");
        }
    }

    public void deleteByIdOrTitle(String input) {
        int deleted;

        if (input.matches("\\d+")) {
            int id = Integer.parseInt(input);
            deleted = booksRepository.deleteById(id);
        } else {
            deleted = booksRepository.deleteByTitle(input);
        }

        if (deleted == 0) {
            throw new BookNotFoundException("error.book.notfound");
        }
    }
}
