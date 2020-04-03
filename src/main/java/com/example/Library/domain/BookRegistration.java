package com.example.Library.domain;

import javax.persistence.*;
import java.time.LocalDateTime;

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

    String registeredAt="a";

    public BookRegistration(){}

    public BookRegistration(Book book,Library library){
        this.book=book;this.library=library; registeredAt=book.getName()+"; "+
                library.getName();
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

    public String getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(String registeredAt) {
        this.registeredAt = registeredAt;
    }
}
