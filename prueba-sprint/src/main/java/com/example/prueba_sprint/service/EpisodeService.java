package com.example.prueba_sprint.service;

import com.example.prueba_sprint.entity.Episode;
import com.example.prueba_sprint.repository.EpisodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EpisodeService {
    
    @Autowired
    private EpisodeRepository episodeRepository;
    
    // CRUD Operations
    public List<Episode> findAll() {
        return episodeRepository.findAll();
    }
    
    public Optional<Episode> findById(Long id) {
        return episodeRepository.findById(id);
    }
    
    public Episode save(Episode episode) {
        return episodeRepository.save(episode);
    }
    
    public void deleteById(Long id) {
        episodeRepository.deleteById(id);
    }
    
    // Query Methods
    public List<Episode> findByAnimeId(Long animeId) {
        return episodeRepository.findByAnimeIdOrderByEpisodeNumberAsc(animeId);
    }
    
    public Optional<Episode> findByAnimeIdAndEpisodeNumber(Long animeId, Integer episodeNumber) {
        return episodeRepository.findByAnimeIdAndEpisodeNumber(animeId, episodeNumber);
    }
    
    public List<Episode> findLatestEpisodes() {
        return episodeRepository.findLatestEpisodes();
    }
    
    public List<Episode> findMostViewedEpisodes() {
        return episodeRepository.findMostViewedEpisodes();
    }
    
    // Business Logic
    public void incrementViewCount(Long episodeId) {
        Optional<Episode> episodeOpt = episodeRepository.findById(episodeId);
        if (episodeOpt.isPresent()) {
            Episode episode = episodeOpt.get();
            episode.incrementViewCount();
            episodeRepository.save(episode);
        }
    }
}
