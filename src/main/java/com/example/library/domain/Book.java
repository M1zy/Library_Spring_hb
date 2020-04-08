package com.example.library.domain;


import javax.persistence.*;
import java.util.Set;

@Entity
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String author;
    private Integer year;
    private String description;


    @OneToMany(mappedBy = "book")
    Set<BookRegistration> libraries;


    public Book(String name, String author, Integer year,
                String description) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.description = description;

    }
    public Book(){

    }

    public Set<BookRegistration> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<BookRegistration> libraries) {
        this.libraries = libraries;
    }

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
}
