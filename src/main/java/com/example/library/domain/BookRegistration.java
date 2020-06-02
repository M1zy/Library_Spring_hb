package com.example.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="book_registration")
@Getter
@Setter
@NoArgsConstructor
public class BookRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name="library_id",nullable = false)
    private Library library;

    @ManyToOne
    @JoinColumn(name="book_id",nullable = false)
    private Book book;

    @Column(columnDefinition = "integer default 0")
    private Integer count;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "registration_id")
    private Set<CartRegistration> cartRegistrations = new HashSet<>();

    public BookRegistration(Library library, Book book, Integer count){
        this.library = library;
        this.book = book;
        this.count = count;
    }
    public BookRegistration(Long id, Library library, Book book, Integer count){
        this.id = id;
        this.library = library;
        this.book = book;
        this.count = count;
    }

    public void addCartRegistration(CartRegistration cartRegistration){
        cartRegistration.setBookRegistration(this);
        this.cartRegistrations.add(cartRegistration);
    }
}
