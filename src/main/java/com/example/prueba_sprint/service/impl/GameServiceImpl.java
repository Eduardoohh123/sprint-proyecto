package com.example.prueba_sprint.service.impl;

import com.example.prueba_sprint.entity.Game;
import com.example.prueba_sprint.service.GameSevice;
import com.example.prueba_sprint.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class GameServiceImpl implements GameSevice {
    
    @Autowired
    private GameRepository gameRepository;
    
    @Override
    @Transactional
    public void saveGame(Game game) {
        if (game == null) {
            throw new IllegalArgumentException("Game cannot be null");
        }
        gameRepository.save(game);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Game> findAll() {
        return gameRepository.findAll();
    }
    
    @Override
    @Transactional(readOnly = true)
    public Optional<Game> findById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return gameRepository.findById(id);
    }
    
    @Override
    @Transactional
    public void deleteGame(Long id) {
        if (id != null && gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
        }
    }
    
    @Override
    @Transactional
    public Game updateGame(Game game) {
        if (game == null || game.getId() == null) {
            throw new IllegalArgumentException("Game and ID cannot be null");
        }
        return gameRepository.save(game);
    }
}
