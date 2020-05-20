package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter @Setter
public class BookDto {
    private Long id;

    @NotEmpty(message = "name must not be empty")
    private String name;

    private Set<Long> authorIds = new HashSet<>();

    private Integer year;

    private String description;
}
