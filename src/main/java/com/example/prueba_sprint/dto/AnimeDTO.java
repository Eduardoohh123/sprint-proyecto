package com.example.prueba_sprint.dto;

import java.time.LocalDate;

public class AnimeDTO {
    private Long id;
    private String titleRomaji;
    private String titleEnglish;
    private String synopsis;
    private String posterUrl;
    private String genres;
    private String studio;
    private Double rating;
    private Integer totalEpisodes;
    private Boolean isFeatured;
    private LocalDate releaseDate;

    public AnimeDTO() {}

    public AnimeDTO(Long id, String titleRomaji, String titleEnglish, String synopsis, String posterUrl,
                    String genres, String studio, Double rating, Integer totalEpisodes, Boolean isFeatured, LocalDate releaseDate) {
        this.id = id;
        this.titleRomaji = titleRomaji;
        this.titleEnglish = titleEnglish;
        this.synopsis = synopsis;
        this.posterUrl = posterUrl;
        this.genres = genres;
        this.studio = studio;
        this.rating = rating;
        this.totalEpisodes = totalEpisodes;
        this.isFeatured = isFeatured;
        this.releaseDate = releaseDate;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleRomaji() {
        return titleRomaji;
    }

    public void setTitleRomaji(String titleRomaji) {
        this.titleRomaji = titleRomaji;
    }

    public String getTitleEnglish() {
        return titleEnglish;
    }

    public void setTitleEnglish(String titleEnglish) {
        this.titleEnglish = titleEnglish;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Integer getTotalEpisodes() {
        return totalEpisodes;
    }

    public void setTotalEpisodes(Integer totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }

    public Boolean getIsFeatured() {
        return isFeatured;
    }

    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
}