package com.example.prueba_sprint.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "episodes")
public class Episode {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;
    
    @Column(nullable = false)
    private Integer episodeNumber;
    
    private String title;
    
    @Column(length = 1000)
    private String description;
    
    private String thumbnailUrl;
    
    private Integer duration; // in minutes
    
    private LocalDate releaseDate;
    
    private String videoUrl;
    
    private String serverUrls; // Multiple servers separated by comma
    
    private Integer viewCount;
    
    // Constructors
    public Episode() {}
    
    public Episode(Anime anime, Integer episodeNumber, String title, String description,
                   String thumbnailUrl, Integer duration, String videoUrl) {
        this.anime = anime;
        this.episodeNumber = episodeNumber;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.duration = duration;
        this.videoUrl = videoUrl;
        this.viewCount = 0;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Anime getAnime() {
        return anime;
    }
    
    public void setAnime(Anime anime) {
        this.anime = anime;
    }
    
    public Integer getEpisodeNumber() {
        return episodeNumber;
    }
    
    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    
    public Integer getDuration() {
        return duration;
    }
    
    public void setDuration(Integer duration) {
        this.duration = duration;
    }
    
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public String getVideoUrl() {
        return videoUrl;
    }
    
    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
    
    public String getServerUrls() {
        return serverUrls;
    }
    
    public void setServerUrls(String serverUrls) {
        this.serverUrls = serverUrls;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public void incrementViewCount() {
        this.viewCount++;
    }
}
