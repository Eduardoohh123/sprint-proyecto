package com.example.prueba_sprint.dto;

import java.time.LocalDate;

public class GameDTO {
    private Long id;
    private String title;
    private String shortDescription;
    private String thumbnailUrl;
    private String coverImageUrl;
    private String genre;
    private String platforms;
    private Double rating;
    private Boolean isFeatured;
    private Integer downloadCount;
    private LocalDate releaseDate;

    public GameDTO() {}

    public GameDTO(Long id, String title, String shortDescription, String thumbnailUrl, String coverImageUrl,
                   String genre, String platforms, Double rating, Boolean isFeatured, Integer downloadCount, LocalDate releaseDate) {
        this.id = id;
        this.title = title;
        this.shortDescription = shortDescription;
        this.thumbnailUrl = thumbnailUrl;
        this.coverImageUrl = coverImageUrl;
        this.genre = genre;
        this.platforms = platforms;
        this.rating = rating;
        this.isFeatured = isFeatured;
        this.downloadCount = downloadCount;
        this.releaseDate = releaseDate;
    }

    // Getters and setters

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

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
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

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public Integer getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}