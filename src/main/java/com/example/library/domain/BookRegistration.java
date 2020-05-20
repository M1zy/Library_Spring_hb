package com.example.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;

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

    public BookRegistration(Library library, Book book, Integer count){
        this.library = library;
        this.book = book;
        this.count = count;
    }
}
