package com.example.library.domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Book extends Essence {

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
    private Set<Library> libraries;

    public Book(String name,String author, Integer year,
                String description,Set<Library> libraries) {
        super(name);
        this.author = author;
        this.year = year;
        this.description = description;
        this.libraries=libraries;
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
}
