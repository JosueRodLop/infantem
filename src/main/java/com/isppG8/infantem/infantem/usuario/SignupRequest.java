package com.isppG8.infantem.infantem.usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    private String nombre;
    private String apellidos;
    private String nombreUsuario;
    private String contraseña;
    private String email;
}
