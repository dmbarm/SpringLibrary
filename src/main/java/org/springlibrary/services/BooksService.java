package org.springlibrary.services;

import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springlibrary.dtos.BookResponseDTO;
import org.springlibrary.dtos.CreateBookRequestDTO;
import org.springlibrary.dtos.UpdateBookRequestDTO;
import org.springlibrary.exceptions.BookCoverNotFoundException;
import org.springlibrary.exceptions.BookNotFoundException;
import org.springlibrary.exceptions.InvalidBookException;
import org.springlibrary.entities.Book;
import org.springlibrary.exceptions.MongoDbStoringException;
import org.springlibrary.mappers.BookMapper;
import org.springlibrary.repositories.BooksRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class BooksService {
    private static final String BOOK_NOT_FOUND_MSG = "error.book.notfound";

    private final BooksRepository booksRepository;
    private final GridFsTemplate gridFsTemplate;

    public BooksService(BooksRepository booksRepository,  GridFsTemplate gridFsTemplate) {
        this.booksRepository = booksRepository;
        this.gridFsTemplate = gridFsTemplate;
    }

    @Transactional(readOnly = true)
    public List<BookResponseDTO> getAllBooks() {
        return booksRepository.findAll().stream().map(BookMapper::toDto).toList();
    }

    @Transactional(readOnly = true)
    public BookResponseDTO getById(long id) {
        return booksRepository.findById(id).map(BookMapper::toDto).orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND_MSG));
    }

    @Transactional(readOnly = true)
    public BookResponseDTO getByTitle(String title) {
        return booksRepository.findByTitle(title).map(BookMapper::toDto).orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND_MSG));
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
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND_MSG));

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
            throw new BookNotFoundException(BOOK_NOT_FOUND_MSG);
        booksRepository.deleteById(id);
    }


    @Transactional
    public void deleteByTitle(String title) {
        int deleted = booksRepository.deleteByTitle(title);
        if (deleted == 0) {
            throw new BookNotFoundException(BOOK_NOT_FOUND_MSG);
        }
    }

    @Transactional
    public GridFsResource getImage(long bookId) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND_MSG));

        if (book.getBookCoverFileId() == null)
            throw new BookCoverNotFoundException("error.book.cover.notfound");

        GridFSFile file = gridFsTemplate.findOne(
                Query.query(Criteria.where("_id").is(new ObjectId(book.getBookCoverFileId())))
        );

        if (file == null)
            throw new BookCoverNotFoundException("error.book.cover.notfound");

        return gridFsTemplate.getResource(file);
    }

    @Transactional
    public void addImage(long bookId, MultipartFile image) {
        Book book = booksRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException(BOOK_NOT_FOUND_MSG));

        ObjectId imageId;
        try (InputStream is = image.getInputStream()) {
            imageId = gridFsTemplate.store(is, image.getOriginalFilename(), image.getContentType());
        } catch (IOException _) {
            throw new MongoDbStoringException("error.mongodb.storing");
        }

        if (book.getBookCoverFileId() != null) {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is(new ObjectId(book.getBookCoverFileId()))));
        }
        book.setBookCoverFileId(imageId.toHexString());
    }
}
