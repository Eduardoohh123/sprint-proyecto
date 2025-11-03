package com.example.prueba_sprint.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "home";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/gamer")
    public String gamer() {
        return "gamer";
    }
    
    @GetMapping("/anime")
    public String anime() {
        return "anime";
    }
}
