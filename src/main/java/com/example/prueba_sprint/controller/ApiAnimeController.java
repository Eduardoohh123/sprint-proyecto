package com.example.prueba_sprint.controller;

import com.example.prueba_sprint.dto.AnimeDTO;
import com.example.prueba_sprint.entity.Anime;
import com.example.prueba_sprint.service.AnimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/anime")
public class ApiAnimeController {

    @Autowired
    private AnimeService animeService;

    @GetMapping
    public List<AnimeDTO> listAnime() {
        return animeService.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnimeDTO> getAnime(@PathVariable Long id) {
        return animeService.findById(id)
                .map(a -> ResponseEntity.ok(toDto(a)))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/search")
    public List<AnimeDTO> search(@RequestParam(required = false) String q) {
        if (q == null || q.isBlank()) return listAnime();
        String query = q.trim().toLowerCase();
        return animeService.findAll().stream()
                .filter(a -> (a.getTitleRomaji() != null && a.getTitleRomaji().toLowerCase().contains(query))
                        || (a.getTitleEnglish() != null && a.getTitleEnglish().toLowerCase().contains(query)))
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private AnimeDTO toDto(Anime a) {
        return new AnimeDTO(
                a.getId(),
                a.getTitleRomaji(),
                a.getTitleEnglish(),
                a.getSynopsis(),
                a.getPosterUrl(),
                a.getGenres(),
                a.getStudio(),
                a.getRating(),
                a.getTotalEpisodes(),
                a.getIsFeatured(),
                a.getReleaseDate()
        );
    }
}