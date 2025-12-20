package com.example.prueba_sprint.repository;

import com.example.prueba_sprint.entity.Episode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EpisodeRepository extends JpaRepository<Episode, Long> {
    
    // Find all episodes of an anime ordered by episode number
    List<Episode> findByAnimeIdOrderByEpisodeNumberAsc(Long animeId);
    
    // Find specific episode by anime and episode number
    Optional<Episode> findByAnimeIdAndEpisodeNumber(Long animeId, Integer episodeNumber);
    
    // Get latest episodes across all animes
    @Query("SELECT e FROM Episode e ORDER BY e.releaseDate DESC")
    List<Episode> findLatestEpisodes();
    
    // Get most viewed episodes
    @Query("SELECT e FROM Episode e ORDER BY e.viewCount DESC")
    List<Episode> findMostViewedEpisodes();
}
