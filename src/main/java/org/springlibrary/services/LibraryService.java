package org.springlibrary.services;

import org.springframework.stereotype.Service;
import org.springlibrary.exceptions.BookNotFoundException;
import org.springlibrary.exceptions.DuplicateBookException;
import org.springlibrary.exceptions.InvalidBookException;
import org.springlibrary.models.Book;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    private final UnitOfWork uow;

    public LibraryService(UnitOfWork uow) {
        this.uow = uow;
    }

    public void startSession() {
        uow.reload();
    }

    public List<Book> getAllBooks() {
        return uow.getBooksList();
    }

    public void addBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new InvalidBookException("error.book.title.empty");
        }

        book.setId(generateNextId());

        if (uow.getBooksList().stream().anyMatch(b -> b.getId() == book.getId())) {
            throw new DuplicateBookException("error.book.duplicate");
        }

        uow.add(book);

        uow.commit();
    }

    public Book findByIdOrTitle(String input) {
        Optional<Book> result;

        if (input.matches("\\d+")) {
            int id = Integer.parseInt(input);
            result = uow.findById(id);
        } else {
            result = uow.findByTitle(input);
        }

        return result.orElseThrow(() -> new BookNotFoundException("error.book.notfound"));
    }

    public void updateBook(Book book) {
        boolean updated = uow.update(book);

        if (!updated) {
            throw new BookNotFoundException("error.book.notfound");
        }

        uow.commit();
    }

    public void deleteByIdOrTitle(String input) {
        boolean deleted;

        if (input.matches("\\d+")) {
            int id = Integer.parseInt(input);
            deleted = uow.deleteById(id);
        } else {
            deleted = uow.deleteByTitle(input);
        }

        if (!deleted) {
            throw new BookNotFoundException("error.book.notfound");
        }

        uow.commit();
    }

    // Helper
    public long generateNextId() {
        return getAllBooks().stream()
                .mapToLong(Book::getId)
                .max()
                .orElse(0) + 1;
    }
}
