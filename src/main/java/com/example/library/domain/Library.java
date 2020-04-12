package com.example.library.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private String address;

    @ManyToMany(mappedBy = "libraries")
    private Set<Book> books=new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "library_id")
    private Set<BookRent> bookRentSet =new HashSet<BookRent>();

    public Library(String name,String address,Set<Book> books) {
        this.name=name;
        this.address=address;
        this.books=books;
    }

    public Library(){

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

    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> books) {
        this.books = books;
    }

    public void addBook(Book book){
        Set<Library> libraries = book.getLibraries();
        libraries.add(this);
        book.setLibraries(libraries);
        books.add(book);
    }

    public void removeBook(Book book){
        books.remove(book);
    }

    public Set<BookRent> getBookRentSet() {
        return bookRentSet;
    }

    public void setBookRentSet(Set<BookRent> bookRentSet) {
        this.bookRentSet = bookRentSet;
    }
}
