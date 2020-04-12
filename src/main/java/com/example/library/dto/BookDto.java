package com.example.library.dto;

import java.util.Set;

public class BookDto {
    private Long id;

    private String name;

    private String author;

    private Integer year;

    private String description;

    private Set<Long> libraryIds;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Long> getLibraryIds() {
        return libraryIds;
    }

    public void setLibraryIds(Set<Long> libraryIds) {
        this.libraryIds = libraryIds;
    }

    public void addLibraryId(Long id){
        libraryIds.add(id);
    }

    public void removeLibraryId(Long id){
        libraryIds.remove(id);
    }
}
