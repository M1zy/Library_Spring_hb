package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RegistrationDto {
    private Long id;

    @NotNull(message = "libraryID must not be empty")
    private Long libraryId;

    @NotNull(message = "bookID must not be empty")
    private Long bookId;

    private Integer count;
}
