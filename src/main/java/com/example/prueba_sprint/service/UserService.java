package com.example.prueba_sprint.service;

import com.example.prueba_sprint.entity.User;
import com.example.prueba_sprint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.beans.factory.annotation.Autowired;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SupabaseAdminService supabaseAdminService;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return userRepository.findById(id);
    }

    public User createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        return userRepository.save(user);
    }

    /**
     * Registrar nuevo usuario con contraseña encriptada
     */
    public User registerUser(User user) {
        // Validar que el email no exista
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        // Validar que el username no exista
        if (user.getUsername() != null && !user.getUsername().trim().isEmpty() 
            && userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("El username ya está en uso");
        }
        
        // Si no tiene username, usar el email
        if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            user.setUsername(user.getEmail().split("@")[0]);
        }

        // Crear usuario en Supabase Auth primero (Admin API)
        try {
            log.info("Creando usuario en Supabase: {}", user.getEmail());
            String supabaseUid = supabaseAdminService.createUser(user.getEmail(), user.getPassword());
            log.info("Supabase create returned uid={} for {}", supabaseUid, user.getEmail());
            if (supabaseUid == null || supabaseUid.isBlank()) {
                // Si Supabase no devolvió un id, considerar esto un fallo
                log.error("Supabase did not return a UID when creating user {}", user.getEmail());
                throw new IllegalArgumentException("No se recibió id de Supabase al crear el usuario");
            }
            user.setSupabaseId(supabaseUid);
        } catch (Exception e) {
            // Loguear detalle para que el deploy en Render lo muestre y sea más fácil el diagnóstico
            log.error("Error registrando en Supabase: {}", e.getMessage(), e);
            throw new IllegalArgumentException("No se pudo crear usuario en Supabase: " + e.getMessage());
        }
        
        // Encriptar contraseña con BCrypt (aún guardamos hash local)
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Asegurar que tenga rol por defecto
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("USER");
        }
        
        // Intentar guardar localmente; si falla, intentar limpiar el usuario remoto en Supabase para evitar inconsistencias
        try {
            User saved = userRepository.save(user);
            log.info("Usuario registrado localmente: {} (id={})", user.getEmail(), saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error guardando usuario localmente para {}: {}", user.getEmail(), e.getMessage(), e);
            // Intentar eliminar el usuario recién creado en Supabase para evitar usuarios huérfanos
            try {
                if (user.getSupabaseId() != null && !user.getSupabaseId().isBlank()) {
                    supabaseAdminService.deleteUser(user.getSupabaseId());
                    log.info("Usuario Supabase {} eliminado tras fallo local", user.getSupabaseId());
                }
            } catch (Exception ex) {
                log.error("No se pudo eliminar el usuario Supabase tras fallo local: {}", ex.getMessage(), ex);
            }
            throw new RuntimeException("Error guardando usuario local: " + e.getMessage(), e);
        }
    }

    /**
     * Autenticar usuario (ya no se usa - Spring Security maneja la autenticación)
     * @deprecated Usar Spring Security Authentication en su lugar
     */
    @Deprecated
    public Optional<User> authenticateUser(String email, String password) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Verificar contraseña encriptada
            if (passwordEncoder.matches(password, user.getPassword())) {
                return Optional.of(user);
            }
        }
        
        return Optional.empty();
    }

    public Optional<User> updateUser(Long id, User user) {
        if (id == null || user == null) {
            return Optional.empty();
        }
        
        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isPresent()) {
            User existingUser = existingUserOpt.get();
            
            // Validar username si se está cambiando
            if (user.getUsername() != null && !user.getUsername().equals(existingUser.getUsername())) {
                Optional<User> usernameCheck = userRepository.findByUsername(user.getUsername());
                if (usernameCheck.isPresent() && !usernameCheck.get().getId().equals(id)) {
                    throw new IllegalArgumentException("El username ya está en uso");
                }
            }
            
            // Validar email si se está cambiando
            if (user.getEmail() != null && !user.getEmail().equals(existingUser.getEmail())) {
                Optional<User> emailCheck = userRepository.findByEmail(user.getEmail());
                if (emailCheck.isPresent() && !emailCheck.get().getId().equals(id)) {
                    throw new IllegalArgumentException("El email ya está en uso");
                }
            }
            
            // Actualizar solo los campos no nulos
            if (user.getName() != null && !user.getName().trim().isEmpty()) {
                existingUser.setName(user.getName());
            }
            if (user.getUsername() != null && !user.getUsername().trim().isEmpty()) {
                existingUser.setUsername(user.getUsername());
            }
            if (user.getEmail() != null && !user.getEmail().trim().isEmpty()) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                // Encriptar nueva contraseña
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getAvatarUrl() != null) {
                existingUser.setAvatarUrl(user.getAvatarUrl());
            }
            if (user.getRole() != null && !user.getRole().trim().isEmpty()) {
                existingUser.setRole(user.getRole());
            }
            
            return Optional.of(userRepository.save(existingUser));
        }
        return Optional.empty();
    }

    public boolean deleteUser(Long id) {
        if (id == null) {
            return false;
        }
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Primero intentar eliminar en Supabase si existe supabaseId
            try {
                if (user.getSupabaseId() != null) {
                    supabaseAdminService.deleteUser(user.getSupabaseId());
                }
            } catch (Exception e) {
                // Loguear y continuar con eliminación local para evitar inconsistencias completas
                System.err.println("Advertencia: no se pudo eliminar usuario en Supabase: " + e.getMessage());
            }

            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
