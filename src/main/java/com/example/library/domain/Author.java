package com.example.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Author extends Essence {

    private String country;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Book> books = new HashSet<>();

    public void addBook(Book book){
        book.setAuthor(this);
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
