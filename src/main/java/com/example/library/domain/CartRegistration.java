package com.example.library.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="cart_registrations")
@Getter
@Setter
@NoArgsConstructor
public class CartRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "registration_id", nullable = false)
    private BookRegistration bookRegistration;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @Min(value = 1)
    @Column(columnDefinition = "integer default 1")
    private Integer count = 1;

    @Min(value = 0)
    @NotNull
    private Double totalPrice;

    public void calculateTotalPrice(){
        totalPrice = bookRegistration.getBook().getPrice() * count;
    }
}
