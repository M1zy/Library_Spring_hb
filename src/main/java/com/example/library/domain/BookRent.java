package com.example.library.domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="book_rent")
@Getter @Setter @NoArgsConstructor
public class BookRent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="library_id",nullable = false)
    private Library library;

    @ManyToOne
    @JoinColumn(name="user_id",nullable = false)
    private User user;

    @OneToMany
    private Set<Book> books = new HashSet<>();

    @ManyToOne
    private Orders order;

    @Min(value = 0)
    @NotNull
    private Double totalPrice;

    public BookRent(Set<Book> books,Library library,User user){
        this.books=books;
        this.user=user;
        this.library=library;
    }

    public void setTotalPrice(){
        totalPrice = 0d;
        for (Book book:
             books) {
            totalPrice += book.getPrice();
        }
    }

    public void addBook(Book book){
        this.books.add(book);
    }

    public void removeBook(Book book){
        this.books.remove(book);
    }

    public void removeBooks(Set<Book> books){this.books.removeAll(books);}
}