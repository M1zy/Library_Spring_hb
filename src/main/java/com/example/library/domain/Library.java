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
public class Library extends Essence{
    private String address;

    @ManyToMany(mappedBy = "libraries")
    private Set<Book> books=new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "library_id")
    private Set<BookRent> bookRentSet =new HashSet<BookRent>();

    public Library(String name,String address,Set<Book> books) {
        super(name);
        this.address=address;
        this.books=books;
    }

    public void addBook(Book book){
        Set<Library> libraries = book.getLibraries();
        libraries.add(this);
        book.setLibraries(libraries);
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

    public void toConsole(){
        System.out.println("Id:"+ getId()+"; Name:"+getName()+"; Address:"+ getAddress()+";");
    }
}
