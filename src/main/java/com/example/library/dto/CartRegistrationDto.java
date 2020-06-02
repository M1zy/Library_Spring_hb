package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartRegistrationDto {
    private Long id;

    private Long registration_id;

    private Integer count = 1;

    private Long cart_id;
}
