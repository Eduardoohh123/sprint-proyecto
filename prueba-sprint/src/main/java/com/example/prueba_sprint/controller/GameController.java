package com.example.prueba_sprint.controller;

import com.example.prueba_sprint.entity.Game;
import com.example.prueba_sprint.service.impl.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/games")
public class GameController {

    @Autowired
    private GameServiceImpl gameService;

    /**
     * Listar todos los juegos
     */
    @GetMapping
    public String listGames(Model model) {
        List<Game> games = gameService.findAll();
        model.addAttribute("games", games);
        model.addAttribute("totalGames", games.size());
        return "games/list";
    }

    /**
     * Buscar juegos por título
     */
    @GetMapping("/search")
    public String searchGames(@RequestParam(required = false) String query, 
                             @RequestParam(required = false) String genre,
                             @RequestParam(required = false) String platform,
                             Model model) {
        List<Game> games = gameService.findAll();
        
        // Filtrar por título si hay búsqueda
        if (query != null && !query.trim().isEmpty()) {
            String searchTerm = query.toLowerCase();
            games = games.stream()
                    .filter(game -> game.getTitle().toLowerCase().contains(searchTerm) ||
                                  (game.getDescription() != null && game.getDescription().toLowerCase().contains(searchTerm)))
                    .collect(Collectors.toList());
        }
        
        // Filtrar por género
        if (genre != null && !genre.trim().isEmpty() && !genre.equals("all")) {
            games = games.stream()
                    .filter(game -> game.getGenre() != null && game.getGenre().toLowerCase().contains(genre.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        // Filtrar por plataforma
        if (platform != null && !platform.trim().isEmpty() && !platform.equals("all")) {
            games = games.stream()
                    .filter(game -> game.getPlatforms() != null && game.getPlatforms().toLowerCase().contains(platform.toLowerCase()))
                    .collect(Collectors.toList());
        }
        
        model.addAttribute("games", games);
        model.addAttribute("query", query);
        model.addAttribute("selectedGenre", genre);
        model.addAttribute("selectedPlatform", platform);
        model.addAttribute("totalResults", games.size());
        
        return "games/list";
    }

    /**
     * Mostrar formulario para crear nuevo juego
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("game", new Game());
        model.addAttribute("action", "create");
        return "games/form";
    }

    /**
     * Guardar nuevo juego
     */
    @PostMapping("/save")
    public String saveGame(@ModelAttribute Game game, RedirectAttributes redirectAttributes) {
        try {
            gameService.saveGame(game);
            redirectAttributes.addFlashAttribute("successMessage", "Juego creado exitosamente!");
            return "redirect:/games";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al crear el juego: " + e.getMessage());
            return "redirect:/games/new";
        }
    }

    /**
     * Mostrar detalles de un juego
     */
    @GetMapping("/{id}")
    public String viewGame(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Game> gameOpt = gameService.findById(id);
        
        if (gameOpt.isPresent()) {
            Game game = gameOpt.get();
            game.incrementViewCount();
            gameService.updateGame(game);
            
            model.addAttribute("game", game);
            return "games/detail";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Juego no encontrado");
            return "redirect:/games";
        }
    }

    /**
     * Mostrar formulario para editar juego
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Game> gameOpt = gameService.findById(id);
        
        if (gameOpt.isPresent()) {
            model.addAttribute("game", gameOpt.get());
            model.addAttribute("action", "edit");
            return "games/form";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Juego no encontrado");
            return "redirect:/games";
        }
    }

    /**
     * Actualizar juego existente
     */
    @PostMapping("/{id}/update")
    public String updateGame(@PathVariable Long id, @ModelAttribute Game game, RedirectAttributes redirectAttributes) {
        try {
            Optional<Game> existingGameOpt = gameService.findById(id);
            
            if (existingGameOpt.isPresent()) {
                game.setId(id);
                gameService.updateGame(game);
                redirectAttributes.addFlashAttribute("successMessage", "Juego actualizado exitosamente!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Juego no encontrado");
            }
            
            return "redirect:/games";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al actualizar el juego: " + e.getMessage());
            return "redirect:/games/" + id + "/edit";
        }
    }

    /**
     * Eliminar juego
     */
    @PostMapping("/{id}/delete")
    public String deleteGame(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Game> gameOpt = gameService.findById(id);
            
            if (gameOpt.isPresent()) {
                gameService.deleteGame(id);
                redirectAttributes.addFlashAttribute("successMessage", "Juego eliminado exitosamente!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Juego no encontrado");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error al eliminar el juego: " + e.getMessage());
        }
        
        return "redirect:/games";
    }

    /**
     * Obtener juegos destacados
     */
    @GetMapping("/featured")
    public String featuredGames(Model model) {
        List<Game> games = gameService.findAll().stream()
                .filter(game -> game.getIsFeatured() != null && game.getIsFeatured())
                .collect(Collectors.toList());
        
        model.addAttribute("games", games);
        model.addAttribute("featuredOnly", true);
        return "games/list";
    }

    /**
     * Incrementar contador de descargas
     */
    @PostMapping("/{id}/download")
    @ResponseBody
    public String incrementDownload(@PathVariable Long id) {
        try {
            Optional<Game> gameOpt = gameService.findById(id);
            if (gameOpt.isPresent()) {
                Game game = gameOpt.get();
                game.incrementDownloadCount();
                gameService.updateGame(game);
                return "{\"success\": true, \"downloads\": " + game.getDownloadCount() + "}";
            }
            return "{\"success\": false, \"message\": \"Game not found\"}";
        } catch (Exception e) {
            return "{\"success\": false, \"message\": \"" + e.getMessage() + "\"}";
        }
    }
}
