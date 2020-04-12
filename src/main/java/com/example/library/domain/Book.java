package com.example.library.domain;


import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
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

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "book_registration",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "library_id")
    )
    Set<Library> libraries;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "book_id")
    private Set<BookRent> bookRentSet =new HashSet<BookRent>();

    public Book(String name, String author, Integer year,
                String description,Set<Library> libraries) {
        this.name = name;
        this.author = author;
        this.year = year;
        this.description = description;
        this.libraries=libraries;
    }
    public Book(){

    }

    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<Library> libraries) {
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

    public void addLibrary(Library library){
        Set<Book> books=library.getBooks();
        books.add(this);
        library.setBooks(books);
        libraries.add(library);
    }

    public void removeLibrary(Library library){
        libraries.remove(library);
    }

    public Set<BookRent> getBookRentSet() {
        return bookRentSet;
    }

    public void setBookRentSet(Set<BookRent> bookRentSet) {
        this.bookRentSet = bookRentSet;
    }
}
