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

    private Long author_id;

    private Integer year;

    private String description;

    private Set<Long> libraryIds=new HashSet<>();

    public void addLibraryId(Long id){
        libraryIds.add(id);
    }

    public void removeLibraryId(Long id){
        libraryIds.remove(id);
    }
}
