package com.example.prueba_sprint.service;

import com.example.prueba_sprint.entity.User;
import com.example.prueba_sprint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar usuario por email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Construir el rol con prefijo ROLE_
        String role = user.getRole();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        // Retornar UserDetails de Spring Security
        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail()) // Usamos email como username
                .password(user.getPassword()) // Debe estar encriptada
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

    /**
     * Método adicional para cargar usuario por username (opcional)
     */
    public UserDetails loadUserByUsernameOrEmail(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(usernameOrEmail)
                .or(() -> userRepository.findByUsername(usernameOrEmail))
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + usernameOrEmail));

        String role = user.getRole();
        if (!role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        return org.springframework.security.core.userdetails.User
                .builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singletonList(new SimpleGrantedAuthority(role)))
                .build();
    }
}
