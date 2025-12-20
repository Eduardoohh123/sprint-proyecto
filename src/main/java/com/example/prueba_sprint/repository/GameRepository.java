package com.example.prueba_sprint.repository;

import com.example.prueba_sprint.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
    
    List<Game> findByIsFeaturedTrue();
    
    List<Game> findByGenre(String genre);
    
    List<Game> findByPlatformsContaining(String platform);
    
    List<Game> findTop3ByOrderByReleaseDateDesc();
    
    List<Game> findTop6ByOrderByDownloadCountDesc();
}
