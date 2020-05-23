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
@Getter
@Setter
@NoArgsConstructor
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String deliveryAddress;

    @ManyToOne
    @JoinColumn(name= "users_id",nullable = false)
    private User user;

    @OneToMany
    private Set<BookRent> bookRentSet = new HashSet<>();

    @Min(value = 0)
    @NotNull
    private Double totalPrice;

    @NotNull
    private String status = Status.PROCESSING.toString();

    public void setStatus(Status status){
        this.status = status.toString();
    }

    public void setTotalPrice(){
        totalPrice = 0d;
        for (BookRent bookrent:
             bookRentSet) {
            totalPrice += bookrent.getTotalPrice();
        }
    }
}
