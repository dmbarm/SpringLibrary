package org.springlibrary.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CreateBookRequestDTO {
    @NotBlank
    @NotNull
    private String title;

    @NotBlank
    @NotNull
    private String author;

    @NotNull
    private String description;
}
