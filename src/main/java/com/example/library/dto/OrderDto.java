package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class OrderDto {
    @NotNull
    private String deliveryAddress;

    @NotNull
    private Long userId;

    @NotNull
    private Set<Long> bookRentIds;
}
