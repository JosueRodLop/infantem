package com.isppG8.infantem.infantem.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String surname;

    @Column(unique = true)
    @NotBlank
    private String nameUser;

    @NotBlank
    private String password;
    
    @Email
    @Column(unique = true)
    @NotBlank
    private String email;
}
