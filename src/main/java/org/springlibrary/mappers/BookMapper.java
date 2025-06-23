package org.springlibrary.mappers;

import org.springlibrary.dtos.BookResponseDTO;
import org.springlibrary.dtos.CreateBookRequestDTO;
import org.springlibrary.entities.Book;

public class BookMapper {
    private BookMapper() {}

    public static Book toEntity(CreateBookRequestDTO dto) {
        return new Book(
                dto.getTitle(),
                dto.getAuthor(),
                dto.getDescription()
        );
    }

    public static BookResponseDTO toDto(Book book) {
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getDescription()
        );
    }
}
