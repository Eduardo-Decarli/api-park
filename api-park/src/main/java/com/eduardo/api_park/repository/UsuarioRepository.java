package com.eduardo.api_park.repository;

import com.eduardo.api_park.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}