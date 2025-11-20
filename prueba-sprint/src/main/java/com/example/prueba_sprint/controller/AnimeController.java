package com.example.prueba_sprint.controller;

import com.example.prueba_sprint.entity.Anime;
import com.example.prueba_sprint.entity.Episode;
import com.example.prueba_sprint.entity.UserAnimeList;
import com.example.prueba_sprint.service.AnimeService;
import com.example.prueba_sprint.service.EpisodeService;
import com.example.prueba_sprint.service.UserAnimeListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/anime")
public class AnimeController {
    
    @Autowired
    private AnimeService animeService;
    
    @Autowired
    private EpisodeService episodeService;
    
    @Autowired
    private UserAnimeListService userAnimeListService;
    
    // Catalog page with filters
    @GetMapping
    public String catalog(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String studio,
            @RequestParam(required = false) String sort,
            Model model) {
        
        List<Anime> animes;
        
        // Apply filters
        if (search != null || genre != null || type != null || status != null || studio != null) {
            Anime.AnimeType animeType = null;
            if (type != null && !type.isEmpty()) {
                try {
                    animeType = Anime.AnimeType.valueOf(type.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // Invalid type, ignore
                }
            }
            
            Anime.AnimeStatus animeStatus = null;
            if (status != null && !status.isEmpty()) {
                try {
                    animeStatus = Anime.AnimeStatus.valueOf(status.toUpperCase());
                } catch (IllegalArgumentException e) {
                    // Invalid status, ignore
                }
            }
            
            animes = animeService.advancedSearch(search, genre, animeType, animeStatus, studio);
        } else {
            // Default: show all animes
            animes = animeService.findAll();
        }
        
        // Apply sorting
        if ("rating".equals(sort)) {
            animes = animeService.findTopRated();
        } else if ("popular".equals(sort)) {
            animes = animeService.findMostPopular();
        } else if ("favorites".equals(sort)) {
            animes = animeService.findMostFavorited();
        }
        
        model.addAttribute("animes", animes);
        model.addAttribute("search", search);
        model.addAttribute("genre", genre);
        model.addAttribute("type", type);
        model.addAttribute("status", status);
        model.addAttribute("studio", studio);
        model.addAttribute("sort", sort);
        
        return "anime/catalog";
    }
    
    // Anime detail page
    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Anime> animeOpt = animeService.findById(id);
        
        if (animeOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Anime no encontrado");
            return "redirect:/anime";
        }
        
        Anime anime = animeOpt.get();
        List<Episode> episodes = episodeService.findByAnimeId(id);
        
        // Increment view count
        animeService.incrementViewCount(id);
        
        model.addAttribute("anime", anime);
        model.addAttribute("episodes", episodes);
        
        return "anime/detail";
    }
    
    // Episode player page
    @GetMapping("/{animeId}/episode/{episodeNumber}")
    public String watchEpisode(
            @PathVariable Long animeId,
            @PathVariable Integer episodeNumber,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        Optional<Anime> animeOpt = animeService.findById(animeId);
        if (animeOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Anime no encontrado");
            return "redirect:/anime";
        }
        
        Optional<Episode> episodeOpt = episodeService.findByAnimeIdAndEpisodeNumber(animeId, episodeNumber);
        if (episodeOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Episodio no encontrado");
            return "redirect:/anime/" + animeId;
        }
        
        Anime anime = animeOpt.get();
        Episode episode = episodeOpt.get();
        List<Episode> allEpisodes = episodeService.findByAnimeId(animeId);
        
        // Increment episode view count
        episodeService.incrementViewCount(episode.getId());
        
        model.addAttribute("anime", anime);
        model.addAttribute("episode", episode);
        model.addAttribute("allEpisodes", allEpisodes);
        
        return "anime/watch";
    }
    
    // CRUD operations for admin (similar to games)
    
    @GetMapping("/manage")
    public String manage(Model model) {
        model.addAttribute("animes", animeService.findAll());
        return "anime/manage";
    }
    
    @GetMapping("/new")
    public String newAnimeForm(Model model) {
        model.addAttribute("anime", new Anime());
        return "anime/form";
    }
    
    @GetMapping("/{id}/edit")
    public String editAnimeForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Anime> animeOpt = animeService.findById(id);
        
        if (animeOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Anime no encontrado");
            return "redirect:/anime/manage";
        }
        
        model.addAttribute("anime", animeOpt.get());
        return "anime/form";
    }
    
    @PostMapping("/save")
    public String saveAnime(@ModelAttribute Anime anime, RedirectAttributes redirectAttributes) {
        try {
            animeService.save(anime);
            redirectAttributes.addFlashAttribute("successMessage", 
                anime.getId() == null ? "Anime creado exitosamente" : "Anime actualizado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al guardar el anime: " + e.getMessage());
        }
        return "redirect:/anime/manage";
    }
    
    @PostMapping("/{id}/delete")
    public String deleteAnime(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            animeService.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Anime eliminado exitosamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el anime: " + e.getMessage());
        }
        return "redirect:/anime/manage";
    }
    
    // User list operations (requires authentication - placeholder for now)
    
    @PostMapping("/{animeId}/addToList")
    @ResponseBody
    public String addToList(
            @PathVariable Long animeId,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "1") Long userId) {
        
        try {
            Optional<Anime> animeOpt = animeService.findById(animeId);
            if (animeOpt.isEmpty()) {
                return "{\"success\": false, \"message\": \"Anime no encontrado\"}";
            }
            
            // TODO: Get actual user from session
            // For now, we'll use a placeholder
            
            UserAnimeList.WatchStatus watchStatus = UserAnimeList.WatchStatus.valueOf(status.toUpperCase());
            // userAnimeListService.addAnimeToList(user, animeOpt.get(), watchStatus);
            
            return "{\"success\": true, \"message\": \"Agregado a tu lista\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"" + e.getMessage() + "\"}";
        }
    }
    
    @PostMapping("/{animeId}/toggleFavorite")
    @ResponseBody
    public String toggleFavorite(
            @PathVariable Long animeId,
            @RequestParam(required = false, defaultValue = "1") Long userId) {
        
        try {
            // TODO: Get actual user from session
            userAnimeListService.toggleFavorite(userId, animeId);
            return "{\"success\": true}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"" + e.getMessage() + "\"}";
        }
    }
}
