package com.example.prueba_sprint.controller;

import com.example.prueba_sprint.dto.AuthRequest;
import com.example.prueba_sprint.dto.UserDTO;
import com.example.prueba_sprint.entity.User;
import com.example.prueba_sprint.service.UserService;
import com.example.prueba_sprint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class ApiUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    // Map entity -> DTO
    private UserDTO toDto(User u) {
        return new UserDTO(u.getId(), u.getName(), u.getUsername(), u.getEmail(), u.getAvatarUrl(), u.getSupabaseId());
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            User created = userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(toDto(created));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("error: " + ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest req) {
        Optional<User> userOpt = userService.authenticateUser(req.getEmail(), req.getPassword());
        if (userOpt.isPresent()) {
            // TODO: Empezar con JWT o similar; por ahora devolvemos el DTO
            return ResponseEntity.ok(toDto(userOpt.get()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> userOpt = userService.getUserById(id);
        if (userOpt.isPresent()) {
            return ResponseEntity.ok(toDto(userOpt.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user) {
        try {
            Optional<User> updated = userService.updateUser(id, user);
            if (updated.isPresent()) {
                User updatedUser = updated.get();
                updatedUser.setPassword(null); // No devolver contraseña
                return ResponseEntity.ok(updatedUser);
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al actualizar usuario: " + e.getMessage());
        }
    }

    /**
     * Obtener todos los usuarios (para panel de administración)
     * GET /api/users
     */
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        // Ocultar contraseñas
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
    }

    /**
     * Eliminar un usuario
     * DELETE /api/users/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            Optional<User> userOpt = userRepository.findById(id);
            
            if (!userOpt.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Usuario no encontrado");
                response.put("status", "error");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
            userRepository.deleteById(id);
            
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario eliminado exitosamente");
            response.put("status", "success");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al eliminar usuario: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Buscar usuarios por nombre o email
     * GET /api/users/search?q=texto
     */
    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String q) {
        List<User> users = userRepository.findAll();
        String query = q.toLowerCase();
        
        List<User> filteredUsers = users.stream()
            .filter(user -> 
                user.getName().toLowerCase().contains(query) ||
                user.getEmail().toLowerCase().contains(query) ||
                user.getUsername().toLowerCase().contains(query)
            )
            .collect(Collectors.toList());
        
        // Ocultar contraseñas
        filteredUsers.forEach(user -> user.setPassword(null));
        
        return ResponseEntity.ok(filteredUsers);
    }

}
