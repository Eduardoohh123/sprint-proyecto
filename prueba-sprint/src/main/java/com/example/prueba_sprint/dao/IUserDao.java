package com.example.prueba_sprint.dao;

import com.example.prueba_sprint.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface IUserDao extends JpaRepository<User, Long> {
    
    // Buscar usuario por email
    Optional<User> findByEmail(String email);
    
    // Buscar usuario por nombre de usuario
    Optional<User> findByUsername(String username);
    
    // Verificar si existe un email
    boolean existsByEmail(String email);
    
    // Verificar si existe un username
    boolean existsByUsername(String username);
    
    // Buscar usuarios activos
    @Query("SELECT u FROM User u WHERE u.enabled = true")
    List<User> findActiveUsers();
    
    // Buscar por nombre o apellido
    List<User> findByNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String name, String lastName);
}
