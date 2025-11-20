package com.example.prueba_sprint.controller;

import com.example.prueba_sprint.entity.User;
import com.example.prueba_sprint.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * Mostrar página de login
     */
    @GetMapping("/login")
    public String showLoginPage(HttpSession session, Model model) {
        // Si ya hay sesión activa, redirigir al inicio
        if (session.getAttribute("loggedUser") != null) {
            return "redirect:/";
        }
        model.addAttribute("user", new User());
        return "login/login";
    }

    /**
     * Procesar login
     */
    @PostMapping("/login")
    public String login(@RequestParam String email, 
                       @RequestParam String password,
                       HttpSession session,
                       RedirectAttributes redirectAttributes) {
        
        Optional<User> userOpt = userService.authenticateUser(email, password);
        
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            // Guardar usuario en sesión
            session.setAttribute("loggedUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("username", user.getUsername());
            
            redirectAttributes.addFlashAttribute("successMessage", "¡Bienvenido " + user.getName() + "!");
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Email o contraseña incorrectos");
            return "redirect:/login";
        }
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
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * Mostrar perfil de usuario
     */
    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        
        if (loggedUser == null) {
            return "redirect:/login";
        }
        
        // Recargar datos actualizados del usuario
        Optional<User> userOpt = userService.getUserById(loggedUser.getId());
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "login/profile";
        }
        
        return "redirect:/login";
    }

    /**
     * Actualizar perfil de usuario
     */
    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User loggedUser = (User) session.getAttribute("loggedUser");
        
        if (loggedUser == null) {
            return "redirect:/login";
        }
        
        try {
            // Actualizar solo los campos permitidos
            user.setId(loggedUser.getId());
            Optional<User> updatedUser = userService.updateUser(loggedUser.getId(), user);
            
            if (updatedUser.isPresent()) {
                // Actualizar sesión con datos nuevos
                session.setAttribute("loggedUser", updatedUser.get());
                session.setAttribute("username", updatedUser.get().getUsername());
                
                redirectAttributes.addFlashAttribute("successMessage", "Perfil actualizado correctamente");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar perfil: " + e.getMessage());
        }
        
        return "redirect:/profile";
    }

    /**
     * Cerrar sesión
     */
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("successMessage", "Sesión cerrada correctamente");
        return "redirect:/";
    }
}
