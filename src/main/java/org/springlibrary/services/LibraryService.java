package org.springlibrary.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return booksRepository.findAll();
    }

    @Transactional
    public void addBook(Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank()) {
            throw new InvalidBookException("error.book.title.empty");
        }

        if (book.getAuthor() == null || book.getAuthor().isBlank()) {
            throw new InvalidBookException("error.book.author.empty");
        }

        booksRepository.save(book);
    }

    @Transactional
    public Book findByIdOrTitle(String input) {
        Optional<Book> result;

        if (input.matches("\\d+")) {
            long id = Long.parseLong(input);
            result = booksRepository.findById(id);
        } else {
            result = booksRepository.findByTitle(input);
        }

        return result.orElseThrow(() -> new BookNotFoundException("error.book.notfound"));
    }

    @Transactional
    public void updateBook(Book book) {
        if (!booksRepository.existsById(book.getId())) {
            throw new BookNotFoundException("error.book.notfound");
        }

        booksRepository.save(book);
    }

    @Transactional
    public void deleteByIdOrTitle(String input) {
        int deleted;

        if (input.matches("\\d+")) {
            long id = Long.parseLong(input);

            if (!booksRepository.existsById(id)) {
                throw new BookNotFoundException("error.book.notfound");
            }

            booksRepository.deleteById(id);
        } else {
            deleted = booksRepository.deleteByTitle(input);
            if (deleted == 0) {
                throw new BookNotFoundException("error.book.notfound");
            }
        }
    }
}
