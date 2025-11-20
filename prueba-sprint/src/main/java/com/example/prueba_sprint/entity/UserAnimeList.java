package com.example.prueba_sprint.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_anime_lists", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "anime_id"})
})
public class UserAnimeList {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    
    @ManyToOne
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WatchStatus watchStatus;
    
    private Integer episodesWatched;
    
    private Double userRating; // 0-10
    
    private Boolean isFavorite;
    
    @Column(length = 1000)
    private String notes;
    
    private LocalDateTime addedDate;
    private LocalDateTime updatedDate;
    
    // Constructors
    public UserAnimeList() {}
    
    public UserAnimeList(User user, Anime anime, WatchStatus watchStatus) {
        this.user = user;
        this.anime = anime;
        this.watchStatus = watchStatus;
        this.episodesWatched = 0;
        this.isFavorite = false;
        this.addedDate = LocalDateTime.now();
        this.updatedDate = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public User getUser() {
        return user;
    }
    
    public void setUser(User user) {
        this.user = user;
    }
    
    public Anime getAnime() {
        return anime;
    }
    
    public void setAnime(Anime anime) {
        this.anime = anime;
    }
    
    public WatchStatus getWatchStatus() {
        return watchStatus;
    }
    
    public void setWatchStatus(WatchStatus watchStatus) {
        this.watchStatus = watchStatus;
    }
    
    public Integer getEpisodesWatched() {
        return episodesWatched;
    }
    
    public void setEpisodesWatched(Integer episodesWatched) {
        this.episodesWatched = episodesWatched;
    }
    
    public Double getUserRating() {
        return userRating;
    }
    
    public void setUserRating(Double userRating) {
        this.userRating = userRating;
    }
    
    public Boolean getIsFavorite() {
        return isFavorite;
    }
    
    public void setIsFavorite(Boolean isFavorite) {
        this.isFavorite = isFavorite;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public LocalDateTime getAddedDate() {
        return addedDate;
    }
    
    public void setAddedDate(LocalDateTime addedDate) {
        this.addedDate = addedDate;
    }
    
    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
    
    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
    
    // Helper methods
    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }
    
    public void incrementEpisodesWatched() {
        this.episodesWatched++;
        this.updatedDate = LocalDateTime.now();
    }
    
    // Enum
    public enum WatchStatus {
        WATCHING,
        COMPLETED,
        ON_HOLD,
        DROPPED,
        PLAN_TO_WATCH
    }
}
