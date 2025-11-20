package com.example.prueba_sprint.repository;

import com.example.prueba_sprint.entity.UserAnimeList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAnimeListRepository extends JpaRepository<UserAnimeList, Long> {
    
    // Find all anime lists for a user
    List<UserAnimeList> findByUserId(Long userId);
    
    // Find anime lists by user and status
    List<UserAnimeList> findByUserIdAndWatchStatus(Long userId, UserAnimeList.WatchStatus status);
    
    // Find user's favorite animes
    List<UserAnimeList> findByUserIdAndIsFavoriteTrue(Long userId);
    
    // Check if user has anime in their list
    Optional<UserAnimeList> findByUserIdAndAnimeId(Long userId, Long animeId);
    
    // Check if anime is in user's list
    boolean existsByUserIdAndAnimeId(Long userId, Long animeId);
}
