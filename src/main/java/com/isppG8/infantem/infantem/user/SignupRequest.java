package com.isppG8.infantem.infantem.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @NotBlank
    private String nameUser;

    @NotBlank
    private String password;

    @Email
    @NotBlank
    private String email;
}
