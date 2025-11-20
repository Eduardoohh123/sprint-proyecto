package com.example.prueba_sprint.repository;

import com.example.prueba_sprint.entity.Download;
import com.example.prueba_sprint.entity.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DownloadRepository extends JpaRepository<Download, Long> {
    
    List<Download> findByGame(Game game);
    
    List<Download> findByPlatform(String platform);
    
    List<Download> findByGameAndPlatform(Game game, String platform);
}
