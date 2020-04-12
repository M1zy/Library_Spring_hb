package com.example.library.domain;

import javax.persistence.*;

@Entity
@Table(name="book_rent")
public class BookRent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="book_id",nullable = false)
    private Book book;

    @ManyToOne
    @JoinColumn(name="library_id",nullable = false)
    private Library library;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    public BookRent(){
    }

    public BookRent(Book book,Library library,User user){
        this.book=book;
        this.user=user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
