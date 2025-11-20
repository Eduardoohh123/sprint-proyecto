package com.example.prueba_sprint.controller;

import com.example.prueba_sprint.service.impl.GameServiceImpl;
import com.example.prueba_sprint.service.NewsService;
import com.example.prueba_sprint.service.GuideService;
import com.example.prueba_sprint.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private GameServiceImpl gameServiceImpl;
    
    @Autowired
    private NewsService newsService;
    
    @Autowired
    private GuideService guideService;
    
    @Autowired
    private AnimeService animeService;

    @GetMapping("/")
    public String home() {
        return "home/home";
    }
    
    @GetMapping("/gamer")
    public String gamer(Model model) {
        // Mostrar solo los primeros 4 juegos destacados en la página principal
        model.addAttribute("games", gameServiceImpl.findAll().stream()
                .filter(game -> game.getIsFeatured() != null && game.getIsFeatured())
                .limit(4)
                .toList());
        model.addAttribute("news", newsService.getLatestNews());
        model.addAttribute("guides", guideService.getPopularGuides());
        return "games/gamer";
    }
    
    @GetMapping("/anime-home")
    public String animeHome(Model model) {
        // Mostrar solo los primeros 3 animes destacados en la página principal
        model.addAttribute("featuredAnimes", animeService.findFeatured().stream()
                .limit(3)
                .toList());
        return "anime/anime";
    }
}

