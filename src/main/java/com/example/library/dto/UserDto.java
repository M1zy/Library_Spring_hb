package com.example.library.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class UserDto {
    private Long id;

    @NotEmpty(message = "name must not be empty")
    private String name;

    @NotEmpty(message = "login must not be empty")
    private String login;

    @NotEmpty(message = "password must not be empty")
    private String password;

    @NotEmpty(message = "email must not be empty")
    @Email(message = "email should be a valid email")
    private String email;

}
