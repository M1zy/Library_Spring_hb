package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class LibraryDto {
    private Long id;

    private String name;

    private String address;

    private Set<Long> bookIds;

}
