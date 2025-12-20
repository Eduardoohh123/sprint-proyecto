package com.example.prueba_sprint.service;

import com.example.prueba_sprint.entity.Anime;
import com.example.prueba_sprint.entity.User;
import com.example.prueba_sprint.entity.UserAnimeList;
import com.example.prueba_sprint.repository.UserAnimeListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserAnimeListService {
    
    @Autowired
    private UserAnimeListRepository userAnimeListRepository;
    
    @Autowired
    private AnimeService animeService;
    
    // CRUD Operations
    public List<UserAnimeList> findAll() {
        return userAnimeListRepository.findAll();
    }
    
    public Optional<UserAnimeList> findById(Long id) {
        return userAnimeListRepository.findById(id);
    }
    
    public UserAnimeList save(UserAnimeList userAnimeList) {
        return userAnimeListRepository.save(userAnimeList);
    }
    
    public void deleteById(Long id) {
        userAnimeListRepository.deleteById(id);
    }
    
    // Query Methods
    public List<UserAnimeList> findByUserId(Long userId) {
        return userAnimeListRepository.findByUserId(userId);
    }
    
    public List<UserAnimeList> findByUserIdAndStatus(Long userId, UserAnimeList.WatchStatus status) {
        return userAnimeListRepository.findByUserIdAndWatchStatus(userId, status);
    }
    
    public List<UserAnimeList> findUserFavorites(Long userId) {
        return userAnimeListRepository.findByUserIdAndIsFavoriteTrue(userId);
    }
    
    public Optional<UserAnimeList> findByUserIdAndAnimeId(Long userId, Long animeId) {
        return userAnimeListRepository.findByUserIdAndAnimeId(userId, animeId);
    }
    
    public boolean isAnimeInUserList(Long userId, Long animeId) {
        return userAnimeListRepository.existsByUserIdAndAnimeId(userId, animeId);
    }
    
    // Business Logic
    public UserAnimeList addAnimeToList(User user, Anime anime, UserAnimeList.WatchStatus status) {
        // Check if already exists
        Optional<UserAnimeList> existing = userAnimeListRepository.findByUserIdAndAnimeId(user.getId(), anime.getId());
        if (existing.isPresent()) {
            UserAnimeList userAnimeList = existing.get();
            userAnimeList.setWatchStatus(status);
            userAnimeList.setUpdatedDate(LocalDateTime.now());
            return userAnimeListRepository.save(userAnimeList);
        }
        
        UserAnimeList userAnimeList = new UserAnimeList(user, anime, status);
        return userAnimeListRepository.save(userAnimeList);
    }
    
    public void removeAnimeFromList(Long userId, Long animeId) {
        Optional<UserAnimeList> userAnimeList = userAnimeListRepository.findByUserIdAndAnimeId(userId, animeId);
        userAnimeList.ifPresent(list -> {
            // If it was favorite, decrement anime's favorite count
            if (list.getIsFavorite()) {
                animeService.decrementFavoriteCount(animeId);
            }
            userAnimeListRepository.delete(list);
        });
    }
    
    public void updateWatchStatus(Long userId, Long animeId, UserAnimeList.WatchStatus newStatus) {
        Optional<UserAnimeList> userAnimeList = userAnimeListRepository.findByUserIdAndAnimeId(userId, animeId);
        userAnimeList.ifPresent(list -> {
            list.setWatchStatus(newStatus);
            list.setUpdatedDate(LocalDateTime.now());
            userAnimeListRepository.save(list);
        });
    }
    
    public void incrementEpisodesWatched(Long userId, Long animeId) {
        Optional<UserAnimeList> userAnimeList = userAnimeListRepository.findByUserIdAndAnimeId(userId, animeId);
        userAnimeList.ifPresent(list -> {
            list.incrementEpisodesWatched();
            userAnimeListRepository.save(list);
        });
    }
    
    public void toggleFavorite(Long userId, Long animeId) {
        Optional<UserAnimeList> userAnimeList = userAnimeListRepository.findByUserIdAndAnimeId(userId, animeId);
        userAnimeList.ifPresent(list -> {
            boolean newFavoriteStatus = !list.getIsFavorite();
            list.setIsFavorite(newFavoriteStatus);
            list.setUpdatedDate(LocalDateTime.now());
            userAnimeListRepository.save(list);
            
            // Update anime's favorite count
            if (newFavoriteStatus) {
                animeService.incrementFavoriteCount(animeId);
            } else {
                animeService.decrementFavoriteCount(animeId);
            }
        });
    }
    
    public void rateAnime(Long userId, Long animeId, Double rating) {
        Optional<UserAnimeList> userAnimeList = userAnimeListRepository.findByUserIdAndAnimeId(userId, animeId);
        userAnimeList.ifPresent(list -> {
            list.setUserRating(rating);
            list.setUpdatedDate(LocalDateTime.now());
            userAnimeListRepository.save(list);
        });
    }
}
