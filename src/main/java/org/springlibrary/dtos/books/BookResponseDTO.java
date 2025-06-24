package org.springlibrary.dtos.books;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BookResponseDTO {
    private long id;
    private String title;
    private String author;
    private String description;
}
