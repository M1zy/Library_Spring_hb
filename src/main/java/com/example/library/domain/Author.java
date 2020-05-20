package com.example.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Author extends Essence {

    private String country;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "book_authors",
            joinColumns = @JoinColumn(name = "authors_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id")
    )
    private Set<Book> books = new HashSet<>();

    public void addBook(Book book){
        Set<Author> authors = book.getAuthors();
        authors.add(this);
        book.setAuthors(authors);
        books.add(book);
    }

    public void addBooks(Set<Book> books){
        for (Book book:
                books) {
            addBook(book);
        }
    }

    public void removeBook(Book book){
        books.remove(book);
    }
}
