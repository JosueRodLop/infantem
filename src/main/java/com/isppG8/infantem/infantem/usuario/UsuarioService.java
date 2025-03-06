package com.isppG8.infantem.infantem.usuario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Usuario createUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public Optional<Usuario> updateUsuario(Long id, Usuario usuarioDetails) {
        return usuarioRepository.findById(id).map(usuario -> {
            usuario.setNombre(usuarioDetails.getNombre());
            usuario.setApellidos(usuarioDetails.getApellidos());
            usuario.setNombreUsuario(usuarioDetails.getNombreUsuario());
            usuario.setContraseña(usuarioDetails.getContraseña());
            usuario.setEmail(usuarioDetails.getEmail());
            usuario.setRutaFotoPerfil(usuarioDetails.getRutaFotoPerfil());
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
    
}
