package com.pablo.autenticacion;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    public  Optional<Usuario> getUsuarioByNombreUsuario(String nombreUsuario);
}
