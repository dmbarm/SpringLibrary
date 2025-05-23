package org.springlibrary.services;

import org.springframework.stereotype.Service;
import org.springlibrary.exceptions.BookNotFoundException;
import org.springlibrary.exceptions.InvalidBookException;
import org.springlibrary.models.Book;
import org.springlibrary.dao.BooksDao;

import java.util.List;
import java.util.Optional;

@Service
public class LibraryService {
    private final BooksDao booksDao;

    public LibraryService(BooksDao booksDao) {
        this.booksDao = booksDao;
    }

    public List<Book> getAllBooks() {
        return booksDao.findAll();
    }

    public void addBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new InvalidBookException("error.book.title.empty");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new InvalidBookException("error.book.author.empty");
        }

        booksDao.create(book);
    }

    public Book findByIdOrTitle(String input) {
        Optional<Book> result;

        if (input.matches("\\d+")) {
            int id = Integer.parseInt(input);
            result = booksDao.findById(id);
        } else {
            result = booksDao.findByTitle(input);
        }

        return result.orElseThrow(() -> new BookNotFoundException("error.book.notfound"));
    }

    public void updateBook(Book book) {
        int updated = booksDao.update(book);

        if (updated == 0) {
            throw new BookNotFoundException("error.book.notfound");
        }
    }

    public void deleteByIdOrTitle(String input) {
        int deleted;

        if (input.matches("\\d+")) {
            int id = Integer.parseInt(input);
            deleted = booksDao.deleteById(id);
        } else {
            deleted = booksDao.deleteByTitle(input);
        }

        if (deleted == 0) {
            throw new BookNotFoundException("error.book.notfound");
        }
    }
}
