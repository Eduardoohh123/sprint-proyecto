package com.example.prueba_sprint.repository;

import com.example.prueba_sprint.entity.Anime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {
    
    // Find by status
    List<Anime> findByStatus(Anime.AnimeStatus status);
    
    // Find by type
    List<Anime> findByType(Anime.AnimeType type);
    
    // Find featured animes
    List<Anime> findByIsFeaturedTrue();
    
    // Search by title (any of the three titles)
    @Query("SELECT a FROM Anime a WHERE " +
           "LOWER(a.titleRomaji) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.titleEnglish) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.titleJapanese) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Anime> searchByTitle(@Param("searchTerm") String searchTerm);
    
    // Find by genre (partial match)
    @Query("SELECT a FROM Anime a WHERE LOWER(a.genres) LIKE LOWER(CONCAT('%', :genre, '%'))")
    List<Anime> findByGenreContaining(@Param("genre") String genre);
    
    // Find by studio
    List<Anime> findByStudioContainingIgnoreCase(String studio);
    
    // Get top rated animes
    @Query("SELECT a FROM Anime a WHERE a.rating IS NOT NULL ORDER BY a.rating DESC")
    List<Anime> findTopRated();
    
    // Get most popular (by view count)
    @Query("SELECT a FROM Anime a ORDER BY a.viewCount DESC")
    List<Anime> findMostPopular();
    
    // Get most favorited
    @Query("SELECT a FROM Anime a ORDER BY a.favoriteCount DESC")
    List<Anime> findMostFavorited();
    
    // Advanced search with multiple filters
    @Query("SELECT DISTINCT a FROM Anime a WHERE " +
           "(:searchTerm IS NULL OR " +
           "LOWER(a.titleRomaji) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.titleEnglish) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(a.titleJapanese) LIKE LOWER(CONCAT('%', :searchTerm, '%'))) AND " +
           "(:genre IS NULL OR LOWER(a.genres) LIKE LOWER(CONCAT('%', :genre, '%'))) AND " +
           "(:type IS NULL OR a.type = :type) AND " +
           "(:status IS NULL OR a.status = :status) AND " +
           "(:studio IS NULL OR LOWER(a.studio) LIKE LOWER(CONCAT('%', :studio, '%')))")
    List<Anime> advancedSearch(
        @Param("searchTerm") String searchTerm,
        @Param("genre") String genre,
        @Param("type") Anime.AnimeType type,
        @Param("status") Anime.AnimeStatus status,
        @Param("studio") String studio
    );
}
