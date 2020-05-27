package com.example.library.dto;

import com.example.library.domain.TypeOperation;
import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class CartDto {
    private Long id;

    @NotNull(message = "delivery address must not be empty")
    private String deliveryAddress;

    @NotNull(message = "userID must not be empty")
    private Long userId;

    private Set<Long> registrationsIds;
}
