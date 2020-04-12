package com.example.library.domain;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="USER", schema = "public")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String login;
    private String password;
    private String email;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<BookRent> bookRentSet =new HashSet<BookRent>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<BookRent> getBookRentSet() {
        return bookRentSet;
    }

    public void setBookRentSet(Set<BookRent> bookRentSet) {
        this.bookRentSet = bookRentSet;
    }

    public void addBookRent(BookRent bookRent){
        this.bookRentSet.add(bookRent);
    }

    public void removeBookRent(BookRent bookRent){
        this.bookRentSet.remove(bookRent);
    }
}
