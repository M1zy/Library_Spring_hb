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
@Getter @Setter
@NoArgsConstructor
public class Book extends Essence {

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    private Set<Author> authors = new HashSet<>();

    private Integer year;
    private String description;
    private String path;

    @Min(value = 0)
    @NotNull
    private Double price;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "book_id")
    private Set<BookRegistration> bookRegistrations = new HashSet<>();

    public Book(String name,Set<Author> authors, Integer year,
                String description,Set<BookRegistration> bookRegistrations) {
        super(name);
        this.authors = authors;
        this.year = year;
        this.description = description;
        this.bookRegistrations = bookRegistrations;
    }

    public void addRegistration(BookRegistration registration){
        bookRegistrations.add(registration);
    }

    public boolean takeBook(Library library){
        for(BookRegistration bookRegistration :
                bookRegistrations){
            if(bookRegistration.getLibrary() == library){
                if(bookRegistration.getCount()>=1){
                    bookRegistrations.remove(bookRegistration);
                    bookRegistrations.add(new BookRegistration(bookRegistration.getLibrary(),
                            bookRegistration.getBook(),bookRegistration.getCount()-1));
                    return true;
                }
            }
        }
        return false;
    }

    public void addAuthor(Author author){
        Set<Book> books = author.getBooks();
        books.add(this);
        author.setBooks(books);
        authors.add(author);
    }

    public void toConsole(){
        System.out.println("Id:"+ getId()+"; Name:"+getName()+"; Year:"+ getYear()+";");
    }

    public Set<Library> getLibraries(){
        Set<Library> libraries = new HashSet<>();
        for (BookRegistration bookRegistration:
                bookRegistrations) {
            libraries.add(bookRegistration.getLibrary());
        }
        return libraries;
    }
}