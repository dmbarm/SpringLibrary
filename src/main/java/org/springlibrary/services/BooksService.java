package org.springlibrary.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springlibrary.dtos.BookResponseDTO;
import org.springlibrary.dtos.CreateBookRequestDTO;
import org.springlibrary.dtos.UpdateBookRequestDTO;
import org.springlibrary.exceptions.BookNotFoundException;
import org.springlibrary.exceptions.InvalidBookException;
import org.springlibrary.entities.Book;
import org.springlibrary.mappers.BookMapper;
import org.springlibrary.repositories.BooksRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BooksService {
    private final BooksRepository booksRepository;

    public BooksService(BooksRepository booksRepository) {
        this.booksRepository = booksRepository;
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAllBooks() {
        return booksRepository.findAll().stream().map(BookMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public BookResponseDTO getById(long id) {
        return booksRepository.findById(id).map(BookMapper::toDto).orElseThrow(() -> new BookNotFoundException("error.book.notfound"));
    }

    @Transactional(readOnly = true)
    public BookResponseDTO getByTitle(String title) {
        return booksRepository.findByTitle(title).map(BookMapper::toDto).orElseThrow(() -> new BookNotFoundException("error.book.notfound"));
    }

    @Transactional
    public long addBook(CreateBookRequestDTO dto) {
        if (dto.getTitle() == null || dto.getTitle().isBlank()) {
            throw new InvalidBookException("error.book.title.empty");
        }

        if (dto.getAuthor() == null || dto.getAuthor().isBlank()) {
            throw new InvalidBookException("error.book.author.empty");
        }

        Book book = BookMapper.toEntity(dto);
        booksRepository.save(book);
        return book.getId();
    }

    @Transactional
    public void updateBook(UpdateBookRequestDTO dto) {
        Book book = booksRepository.findById(dto.getId())
                .orElseThrow(() -> new BookNotFoundException("error.book.notfound"));

        if (dto.getTitle() != null && !dto.getTitle().isBlank()) {
            book.setTitle(dto.getTitle());
        }
        if (dto.getAuthor() != null && !dto.getAuthor().isBlank()) {
            book.setAuthor(dto.getAuthor());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            book.setDescription(dto.getDescription());
        }

        booksRepository.save(book);
    }

    @Transactional
    public void deleteById(long id) {
        if (!booksRepository.existsById(id))
            throw new BookNotFoundException("error.book.notfound");
        booksRepository.deleteById(id);
    }


    @Transactional
    public void deleteByTitle(String title) {
        int deleted = booksRepository.deleteByTitle(title);
        if (deleted == 0) {
            throw new BookNotFoundException("error.book.notfound");
        }
    }
}
