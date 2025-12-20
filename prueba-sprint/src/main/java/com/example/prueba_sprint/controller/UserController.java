package com.example.prueba_sprint.controller;

import com.example.prueba_sprint.entity.User;
import com.example.prueba_sprint.service.UserService;
import com.example.prueba_sprint.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

@Controller
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    /**
     * Mostrar página de login - Spring Security maneja el login automáticamente
     */
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Email o contraseña incorrectos");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "Sesión cerrada correctamente");
        }
        return "login/login";
    }

    /**
     * Mostrar página de registro
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new User());
        return "login/register";
    }

    /**
     * Procesar registro
     */
    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                          RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("successMessage", "¡Registro exitoso! Ahora puedes iniciar sesión");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // Log para facilitar debugging en entornos como Render
            log.error("Error durante registro de usuario: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * Mostrar perfil de usuario - Usa Spring Security Authentication
     */
    @GetMapping("/profile")
    public String showProfile(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        // Obtener usuario por email (username en Spring Security)
        String email = userDetails.getUsername();
        Optional<User> userOpt = userRepository.findByEmail(email);
        
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "login/profile";
        }
        
        return "redirect:/login";
    }

    /**
     * Actualizar perfil de usuario - Usa Spring Security Authentication
     */
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user,
                               @AuthenticationPrincipal UserDetails userDetails,
                               RedirectAttributes redirectAttributes) {
        if (userDetails == null) {
            return "redirect:/login";
        }
        
        try {
            // Obtener usuario actual
            String email = userDetails.getUsername();
            Optional<User> currentUserOpt = userRepository.findByEmail(email);
            
            if (currentUserOpt.isPresent()) {
                User currentUser = currentUserOpt.get();
                user.setId(currentUser.getId());
                
                Optional<User> updatedUser = userService.updateUser(currentUser.getId(), user);
                
                if (updatedUser.isPresent()) {
                    redirectAttributes.addFlashAttribute("successMessage", "Perfil actualizado correctamente");
                }
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar perfil: " + e.getMessage());
        }
        
        return "redirect:/profile";
    }
}
