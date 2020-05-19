package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter
@Setter
public class AuthorDto {
    private Long id;

    @NotEmpty(message = "name must not be empty")
    private String name;

    @NotEmpty(message = "country must not be empty")
    private String country;

    private Set<Long> bookIds;
}
