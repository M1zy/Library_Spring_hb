package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class RentDto {
    private Long id;

    @NotNull(message = "libraryID must not be empty")
    private Long libraryId;

    @NotNull(message = "userID must not be empty")
    private Long userId;

    private Set<Long> bookIds;
}
