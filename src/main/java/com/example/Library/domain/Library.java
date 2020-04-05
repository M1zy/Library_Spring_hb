package com.example.Library.domain;

import javax.persistence.*;
import java.util.Set;

@Entity
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String address;

    @OneToMany(mappedBy = "library")
    Set<BookRegistration> books;

    public Library() {
    }

    public Library(String name,String address) {
        this.name=name;
        this.address=address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
