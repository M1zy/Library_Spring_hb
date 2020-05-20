package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.Set;

@Getter @Setter
public class LibraryDto {
    private Long id;

    @NotEmpty(message = "name must not be empty")
    private String name;

    @NotEmpty(message = "address must not be empty")
    private String address;

}
