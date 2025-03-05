package com.isppG8.infantem.infantem.usuario;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;
    
    @PostMapping("/signup")
    public ResponseEntity<Usuario> signup(@RequestBody SignupRequest signupRequest) {
        Usuario usuario = new Usuario();
        usuario.setNombre(signupRequest.getNombre());
        usuario.setApellidos(signupRequest.getApellidos());
        usuario.setNombreUsuario(signupRequest.getNombreUsuario());
        usuario.setContraseña(signupRequest.getContraseña());
        usuario.setEmail(signupRequest.getEmail());
        return ResponseEntity.ok(usuarioService.createUsuario(usuario));
    }

    @PostMapping("/login")
    public ResponseEntity<Usuario> login(@RequestBody LoginRequest loginRequest) {
        Optional<Usuario> user = usuarioService.login(loginRequest.getUsername(), loginRequest.getPassword());
        return user.map(ResponseEntity::ok).orElseGet(()->ResponseEntity.status(401).build());
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }
}
