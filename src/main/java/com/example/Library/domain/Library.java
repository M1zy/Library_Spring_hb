package com.example.Library.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "library")
    Set<BookRegistration> books;

    public Library() {
    }

    public Library(String name) {
        this.name=name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<BookRegistration> getBooks() {
        return books;
    }

    public void setBooks(Set<BookRegistration> books) {
        this.books = books;
    }
}
