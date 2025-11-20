package com.example.prueba_sprint.dao;

import com.example.prueba_sprint.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IGameDao extends JpaRepository<Game, Long> {
    
    // Buscar juegos destacados
    List<Game> findByIsFeaturedTrue();
    
    // Buscar por género
    List<Game> findByGenre(String genre);
    
    // Buscar por plataforma
    List<Game> findByPlatformsContaining(String platform);
    
    // Top 3 lanzamientos más recientes
    List<Game> findTop3ByOrderByReleaseDateDesc();
    
    // Top 6 más descargados
    List<Game> findTop6ByOrderByDownloadCountDesc();
    
    // Buscar por rating mínimo
    @Query("SELECT g FROM Game g WHERE g.rating >= ?1 ORDER BY g.rating DESC")
    List<Game> findByMinRating(Double minRating);
    
    // Buscar por título (búsqueda parcial)
    List<Game> findByTitleContainingIgnoreCase(String title);
}
