package org.springlibrary.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateBookRequestDTO {
    @NotNull
    private Long id;

    private String title;
    private String author;
    private String description;
}
