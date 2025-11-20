package com.example.prueba_sprint.service;

import com.example.prueba_sprint.entity.Game;
import java.util.List;
import java.util.Optional;

public interface GameSevice {
    
    void saveGame(Game game);
    
    List<Game> findAll();
    
    Optional<Game> findById(Long id);
    
    void deleteGame(Long id);
    
    Game updateGame(Game game);
}
