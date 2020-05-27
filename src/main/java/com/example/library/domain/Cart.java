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
@Table(name="cart")
@Getter @Setter @NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @OneToMany
    private Set<BookRegistration> registrations = new HashSet<>();

    @Min(value = 0)
    @NotNull
    private Double totalPrice;

    @NotNull
    private String deliveryAddress;

    @NotNull
    private String typeOperation = TypeOperation.BUY.toString();

    @NotNull
    private String status = Status.PROCESSING.toString();

    public void setStatus(Status status){
        this.status = status.toString();
    }

    public Cart(Set<BookRegistration> registrations, User user){
        this.registrations = registrations;
        this.user = user;
    }

    public void setTotalPrice(){
        totalPrice = 0d;
        for (BookRegistration registration:
             registrations) {
            totalPrice += registration.getBook().getPrice();
        }
        if(typeOperation==TypeOperation.RENT.toString()){
            totalPrice*=0.7;
        }
    }

    public void addRegistration(BookRegistration registration){
        this.registrations.add(registration);
    }

    public void removeRegistration(BookRegistration registration){this.registrations.remove(registration);}
}