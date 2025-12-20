package com.example.prueba_sprint.controller;

import com.example.prueba_sprint.dto.GameDTO;
import com.example.prueba_sprint.entity.Game;
import com.example.prueba_sprint.service.impl.GameServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/games")
public class ApiGameController {

    @Autowired
    private GameServiceImpl gameService;

    @GetMapping
    public List<GameDTO> listGames() {
        return gameService.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<GameDTO> getGame(@PathVariable Long id) {
        return gameService.findById(id)
                .map(game -> ResponseEntity.ok(toDto(game)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/featured")
    public List<GameDTO> featuredGames() {
        return gameService.findAll().stream()
                .filter(g -> g.getIsFeatured() != null && g.getIsFeatured())
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/search")
    public List<GameDTO> searchGames(@RequestParam(required = false) String query) {
        if (query == null || query.isBlank()) {
            return listGames();
        }
        String q = query.trim().toLowerCase();
        return gameService.findAll().stream()
                .filter(g -> g.getTitle() != null && g.getTitle().toLowerCase().contains(q))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // Simple DTO mapper
    private GameDTO toDto(Game g) {
        return new GameDTO(
                g.getId(),
                g.getTitle(),
                g.getShortDescription(),
                g.getThumbnailUrl(),
                g.getCoverImageUrl(),
                g.getGenre(),
                g.getPlatforms(),
                g.getRating(),
                g.getIsFeatured(),
                g.getDownloadCount(),
                g.getReleaseDate()
        );
    }
}