package com.example.library.domain;

import javax.persistence.*;

@Entity
public class BookRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    Book book;

    @ManyToOne
    @JoinColumn(name = "library_id")
    Library library;



    public BookRegistration(){}

    public BookRegistration(Book book,Library library){
        this.book=book;
        this.library=library;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Library getLibrary() {
        return library;
    }

    public void setLibrary(Library library) {
        this.library = library;
    }


}
