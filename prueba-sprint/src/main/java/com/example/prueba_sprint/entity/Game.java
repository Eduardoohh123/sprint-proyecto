package com.example.prueba_sprint.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "games")
public class Game {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 3000)
    private String description;
    
    @Column(name = "short_description", length = 500)
    private String shortDescription;
    
    private String developer;
    
    private String publisher;
    
    @Column(name = "release_date")
    private LocalDate releaseDate;
    
    @Column(name = "thumbnail_url")
    private String thumbnailUrl;
    
    @Column(name = "cover_image_url")
    private String coverImageUrl;
    
    @Column(name = "trailer_url")
    private String trailerUrl;
    
    private Double rating; // 0.0 - 10.0
    
    private String genre; // Acción, RPG, Aventura, etc.
    
    private String platforms; // "PC, PS5, Xbox Series X"
    
    private String badge; // "¡Recién salido!", "Más jugado", "Mejor valorado"
    
    @Column(name = "badge_color")
    private String badgeColor; // Color del badge
    
    @Column(name = "is_featured")
    private Boolean isFeatured = false;
    
    @Column(name = "view_count")
    private Integer viewCount = 0;
    
    @Column(name = "download_count")
    private Integer downloadCount = 0;
    
    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Download> downloads = new HashSet<>();
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public Game() {}
    
    public Game(String title, String description, LocalDate releaseDate) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
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
    
    public String getShortDescription() {
        return shortDescription;
    }
    
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }
    
    public String getDeveloper() {
        return developer;
    }
    
    public void setDeveloper(String developer) {
        this.developer = developer;
    }
    
    public String getPublisher() {
        return publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
    
    public String getCoverImageUrl() {
        return coverImageUrl;
    }
    
    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }
    
    public String getTrailerUrl() {
        return trailerUrl;
    }
    
    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
    
    public Double getRating() {
        return rating;
    }
    
    public void setRating(Double rating) {
        this.rating = rating;
    }
    
    public String getGenre() {
        return genre;
    }
    
    public void setGenre(String genre) {
        this.genre = genre;
    }
    
    public String getPlatforms() {
        return platforms;
    }
    
    public void setPlatforms(String platforms) {
        this.platforms = platforms;
    }
    
    public String getBadge() {
        return badge;
    }
    
    public void setBadge(String badge) {
        this.badge = badge;
    }
    
    public String getBadgeColor() {
        return badgeColor;
    }
    
    public void setBadgeColor(String badgeColor) {
        this.badgeColor = badgeColor;
    }
    
    public Boolean getIsFeatured() {
        return isFeatured;
    }
    
    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public Integer getDownloadCount() {
        return downloadCount;
    }
    
    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }
    
    public Set<Download> getDownloads() {
        return downloads;
    }
    
    public void setDownloads(Set<Download> downloads) {
        this.downloads = downloads;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public void setCategory(Category category) {
        this.category = category;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    // Helper methods
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    public void incrementDownloadCount() {
        this.downloadCount++;
    }
    
    public void addDownload(Download download) {
        downloads.add(download);
        download.setGame(this);
    }
    
    public void removeDownload(Download download) {
        downloads.remove(download);
        download.setGame(null);
    }
}
