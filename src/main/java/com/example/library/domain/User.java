package com.example.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="USER", schema = "public")
@Getter @Setter @NoArgsConstructor
public class User extends Essence {

    private String login;
    private String password;
    private String email;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "user_id")
    private Set<BookRent> bookRentSet =new HashSet<BookRent>();

    public User(String name, String login, String password, String email){
        super.setName(name);
        this.login=login;
        this.email=email;
        this.password=password;
    }

    public void addBookRent(BookRent bookRent){
        this.bookRentSet.add(bookRent);
    }

    public void removeBookRent(BookRent bookRent){
        this.bookRentSet.remove(bookRent);
    }
}
