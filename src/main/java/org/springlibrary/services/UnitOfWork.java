package org.springlibrary.services;

import org.springframework.stereotype.Component;
import org.springlibrary.exceptions.BookPersistenceException;
import org.springlibrary.models.Book;
import org.springlibrary.repositories.BooksRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UnitOfWork {
    private final BooksRepository booksRepository;
    private List<Book> booksCopy;

    public UnitOfWork(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
        booksCopy = new ArrayList<>(booksRepository.findAll());
    }

    public List<Book> getBooksList() {
        return booksCopy;
    }

    public void add(Book book) {
        booksCopy.add(book);
    }

    public boolean update(Book book) {
        for (int i = 0; i < booksCopy.size(); i++) {
            if (booksCopy.get(i).getId() == book.getId()) {
                booksCopy.set(i, book);
                return true;
            }
        }
        return false;
    }

    public Optional<Book> findById(int id) {
        return booksCopy.stream().filter(book -> book.getId() == id).findFirst();
    }

    public Optional<Book> findByTitle(String input) {
        return booksCopy.stream().filter(book -> book.getTitle().equals(input)).findFirst();
    }

    public boolean deleteById(int id) {
        return booksCopy.removeIf(book -> book.getId() == id);
    }

    public boolean deleteByTitle(String input) {
        return booksCopy.removeIf(book -> book.getTitle().equals(input));
    }

    public void commit() {
        try {
            booksRepository.saveAll(booksCopy);
        } catch (IOException e) {
            throw new BookPersistenceException("Failed to commit changes.", e);
        }
    }

    public void reload() {
        this.booksCopy = new ArrayList<>(booksRepository.findAll());
    }
}
