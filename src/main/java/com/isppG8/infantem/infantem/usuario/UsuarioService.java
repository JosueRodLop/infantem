package com.isppG8.infantem.infantem.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario createUsuario(Usuario usuario) {
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> updateUsuario(Long id, Usuario usuarioDetails) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(usuarioDetails.getNombre());
            usuario.setApellidos(usuarioDetails.getApellidos());
            usuario.setNombreUsuario(usuarioDetails.getNombreUsuario());
            usuario.setContraseña(passwordEncoder.encode(usuarioDetails.getContraseña()));
            usuario.setEmail(usuarioDetails.getEmail());
            return usuarioRepository.save(usuario);
        });
    }
    
    public boolean deleteUsuario(Long id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    public Optional<Usuario> login(String nombreUsuario, String contraseña) {
        Optional<Usuario> user = usuarioRepository.findbyUsername(nombreUsuario);
        if(user.isPresent() && passwordEncoder.matches(contraseña, user.get().getContraseña())) {
            return user;
        }
        return Optional.empty();
    }
}
