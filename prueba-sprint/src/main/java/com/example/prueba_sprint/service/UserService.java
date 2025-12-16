package com.example.prueba_sprint.service;

import com.example.prueba_sprint.entity.User;
import com.example.prueba_sprint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

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
        
        // Encriptar contraseña con BCrypt
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        // Asegurar que tenga rol por defecto
        if (user.getRole() == null || user.getRole().trim().isEmpty()) {
            user.setRole("USER");
        }
        
        return userRepository.save(user);
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
            
            // Actualizar solo los campos no nulos
            if (user.getName() != null) {
                existingUser.setName(user.getName());
            }
            if (user.getUsername() != null) {
                existingUser.setUsername(user.getUsername());
            }
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                // Encriptar nueva contraseña
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }
            if (user.getAvatarUrl() != null) {
                existingUser.setAvatarUrl(user.getAvatarUrl());
            }
            
            return Optional.of(userRepository.save(existingUser));
        }
        return Optional.empty();
    }

    public boolean deleteUser(Long id) {
        if (id == null) {
            return false;
        }
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
