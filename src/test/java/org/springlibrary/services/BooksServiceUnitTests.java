package org.springlibrary.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springlibrary.dtos.books.BookResponseDTO;
import org.springlibrary.dtos.books.CreateBookRequestDTO;
import org.springlibrary.dtos.books.UpdateBookRequestDTO;
import org.springlibrary.entities.Book;
import org.springlibrary.exceptions.BookNotFoundException;
import org.springlibrary.repositories.BooksRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BooksServiceUnitTests {
    @Mock
    private BooksRepository booksRepository;

    @InjectMocks
    private BooksService booksService;

    @Test
    void shouldReturnAllBooks() {
        List<Book> books = Arrays.asList(
                new Book(1L, "Test1", "Author1", "Description1"),
                new Book(2L, "Test2", "Author2", "Description2"),
                new Book(3L, "Test3", "Author3", "Description3"));
        when(booksRepository.findAll()).thenReturn(books);

        List<BookResponseDTO> booksResponse = booksService.getAllBooks();

        for (int i = 0; i < booksResponse.size(); i++) {
            assertThat(booksResponse.get(i).getTitle()).isEqualTo(books.get(i).getTitle());
        }
        verify(booksRepository).findAll();
    }

    @Test
    void shouldReturnBookById() {
        long bookId = 1L;
        Book book = new Book(bookId, "TestTitle", "TestAuthor", "TestDescription");
        when(booksRepository.findById(book.getId())).thenReturn(Optional.of(book));

        BookResponseDTO dtoResult = booksService.getById(bookId);

        assertThat(dtoResult.getTitle()).isEqualTo(book.getTitle());
        verify(booksRepository).findById(bookId);
    }

    @Test
    void shouldReturnBookByTitle() {
        long bookId = 1L;
        Book book = new Book(bookId, "TestTitle", "TestAuthor", "TestDescription");
        when(booksRepository.findByTitle(book.getTitle())).thenReturn(Optional.of(book));

        BookResponseDTO dtoResult = booksService.getByTitle("TestTitle");

        assertThat(dtoResult.getTitle()).isEqualTo(book.getTitle());
        verify(booksRepository).findByTitle("TestTitle");
    }

    @Test
    void shouldAddBookSuccessfully() {
        long bookId = 1L;
        CreateBookRequestDTO createRequestDto = new CreateBookRequestDTO("TestTitle", "TestAuthor",
                "TestDescription");
        Book saved = new Book(bookId, "TestTitle", "TestAuthor", "TestDescription");

        when(booksRepository.save(any(Book.class))).thenReturn(saved);

        long returnedBookId = booksService.addBook(createRequestDto);

        assertThat(returnedBookId).isEqualTo(bookId);
        verify(booksRepository).save(any(Book.class));
    }

    @Test
    void shouldUpdateBookSuccessfully() {
        long bookId = 1L;
        UpdateBookRequestDTO updateRequestDto = new UpdateBookRequestDTO(bookId, "UpdatedTestTitle", "UpdatedTestAuthor",
                "UpdatedTestDescription");
        Book saved = new Book(bookId, "TestTitle", "TestAuthor", "TestDescription");
        when(booksRepository.findById(updateRequestDto.getId())).thenReturn(Optional.of(saved));

        booksService.updateBook(updateRequestDto);

        assertThat(saved.getTitle()).isEqualTo(updateRequestDto.getTitle());
        verify(booksRepository).findById(updateRequestDto.getId());
    }

    @Test
    void shouldDeleteById() {
        long bookId = 1L;

        when(booksRepository.existsById(bookId)).thenReturn(true);

        booksService.deleteById(bookId);

        verify(booksRepository).existsById(bookId);
        verify(booksRepository).deleteById(bookId);
    }

    @Test
    void shouldDeleteByTitle() {
        String bookTitle = "TestTitle";

        when(booksRepository.deleteByTitle(bookTitle)).thenReturn(1);

        booksService.deleteByTitle(bookTitle);

        verify(booksRepository).deleteByTitle(bookTitle);
    }

    @Test
    void shouldThrowExceptionWhenBookNotFound() {
        long bookId = 1L;
        when(booksRepository.existsById(bookId)).thenReturn(false);

        assertThatThrownBy(() -> booksService.deleteById(bookId))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("error.book.notfound");

        verify(booksRepository).existsById(bookId);
        verify(booksRepository, never()).deleteById(any());
    }

}
