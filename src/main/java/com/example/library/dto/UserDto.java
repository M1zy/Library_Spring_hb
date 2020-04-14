package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter @Setter
public class UserDto {
    private Long id;

    private String name;

    private String login;

    private String password;

    private String email;

}
