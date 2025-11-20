package com.example.prueba_sprint.service;

import com.example.prueba_sprint.entity.Anime;
import com.example.prueba_sprint.repository.AnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AnimeService {
    
    @Autowired
    private AnimeRepository animeRepository;
    
    // CRUD Operations
    public List<Anime> findAll() {
        return animeRepository.findAll();
    }
    
    public Optional<Anime> findById(Long id) {
        return animeRepository.findById(id);
    }
    
    public Anime save(Anime anime) {
        return animeRepository.save(anime);
    }
    
    public void deleteById(Long id) {
        animeRepository.deleteById(id);
    }
    
    // Search and Filter Methods
    public List<Anime> searchByTitle(String searchTerm) {
        return animeRepository.searchByTitle(searchTerm);
    }
    
    public List<Anime> findByGenre(String genre) {
        return animeRepository.findByGenreContaining(genre);
    }
    
    public List<Anime> findByType(Anime.AnimeType type) {
        return animeRepository.findByType(type);
    }
    
    public List<Anime> findByStatus(Anime.AnimeStatus status) {
        return animeRepository.findByStatus(status);
    }
    
    public List<Anime> findByStudio(String studio) {
        return animeRepository.findByStudioContainingIgnoreCase(studio);
    }
    
    public List<Anime> findFeatured() {
        return animeRepository.findByIsFeaturedTrue();
    }
    
    public List<Anime> findTopRated() {
        return animeRepository.findTopRated();
    }
    
    public List<Anime> findMostPopular() {
        return animeRepository.findMostPopular();
    }
    
    public List<Anime> findMostFavorited() {
        return animeRepository.findMostFavorited();
    }
    
    // Advanced search
    public List<Anime> advancedSearch(String searchTerm, String genre, 
                                      Anime.AnimeType type, Anime.AnimeStatus status, 
                                      String studio) {
        return animeRepository.advancedSearch(searchTerm, genre, type, status, studio);
    }
    
    // Business Logic
    public void incrementViewCount(Long animeId) {
        Optional<Anime> animeOpt = animeRepository.findById(animeId);
        if (animeOpt.isPresent()) {
            Anime anime = animeOpt.get();
            anime.incrementViewCount();
            animeRepository.save(anime);
        }
    }
    
    public void incrementFavoriteCount(Long animeId) {
        Optional<Anime> animeOpt = animeRepository.findById(animeId);
        if (animeOpt.isPresent()) {
            Anime anime = animeOpt.get();
            anime.incrementFavoriteCount();
            animeRepository.save(anime);
        }
    }
    
    public void decrementFavoriteCount(Long animeId) {
        Optional<Anime> animeOpt = animeRepository.findById(animeId);
        if (animeOpt.isPresent()) {
            Anime anime = animeOpt.get();
            anime.decrementFavoriteCount();
            animeRepository.save(anime);
        }
    }
}
