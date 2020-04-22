package com.example.library.domain;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
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
    private Set<Book> books=new HashSet<>();

    public BookRent(Set<Book> books,Library library,User user){
        this.books=books;
        this.user=user;
        this.library=library;
    }

    public void addBook(Book book){
        this.books.add(book);
    }

    public void removeBook(Book book){
        this.books.remove(book);
    }
}
