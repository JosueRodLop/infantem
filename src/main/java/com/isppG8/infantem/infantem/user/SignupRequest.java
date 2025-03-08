package com.isppG8.infantem.infantem.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String name;
    private String surname;
    private String nameUser;
    private String password;
    private String email;
}
