package com.example.prueba_sprint.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "animes")
public class Anime {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String titleJapanese;
    
    @Column(nullable = false)
    private String titleRomaji;
    
    private String titleEnglish;
    
    @Column(length = 2000)
    private String synopsis;
    
    private String posterUrl;
    private String bannerUrl;
    
    @Column(length = 500)
    private String genres; // Almacenado como String separado por comas
    
    @Enumerated(EnumType.STRING)
    private AnimeType type;
    
    private Integer totalEpisodes;
    
    @Enumerated(EnumType.STRING)
    private AnimeStatus status;
    
    private LocalDate releaseDate;
    private LocalDate endDate;
    
    private String studio;
    private Double rating; // 0-10
    private Integer viewCount;
    private Integer favoriteCount;
    
    private String trailerUrl;
    
    @Column(length = 500)
    private String tags; // Tags adicionales
    
    private Boolean isFeatured;
    
    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL)
    private List<Episode> episodes;
    
    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL)
    private List<UserAnimeList> userAnimeLists;
    
    // Constructors
    public Anime() {}
    
    public Anime(String titleJapanese, String titleRomaji, String titleEnglish, String synopsis,
                 String posterUrl, String genres, AnimeType type, Integer totalEpisodes,
                 AnimeStatus status, String studio, Double rating) {
        this.titleJapanese = titleJapanese;
        this.titleRomaji = titleRomaji;
        this.titleEnglish = titleEnglish;
        this.synopsis = synopsis;
        this.posterUrl = posterUrl;
        this.genres = genres;
        this.type = type;
        this.totalEpisodes = totalEpisodes;
        this.status = status;
        this.studio = studio;
        this.rating = rating;
        this.viewCount = 0;
        this.favoriteCount = 0;
        this.isFeatured = false;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitleJapanese() {
        return titleJapanese;
    }
    
    public void setTitleJapanese(String titleJapanese) {
        this.titleJapanese = titleJapanese;
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
    
    public String getBannerUrl() {
        return bannerUrl;
    }
    
    public void setBannerUrl(String bannerUrl) {
        this.bannerUrl = bannerUrl;
    }
    
    public String getGenres() {
        return genres;
    }
    
    public void setGenres(String genres) {
        this.genres = genres;
    }
    
    public AnimeType getType() {
        return type;
    }
    
    public void setType(AnimeType type) {
        this.type = type;
    }
    
    public Integer getTotalEpisodes() {
        return totalEpisodes;
    }
    
    public void setTotalEpisodes(Integer totalEpisodes) {
        this.totalEpisodes = totalEpisodes;
    }
    
    public AnimeStatus getStatus() {
        return status;
    }
    
    public void setStatus(AnimeStatus status) {
        this.status = status;
    }
    
    public LocalDate getReleaseDate() {
        return releaseDate;
    }
    
    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public Integer getFavoriteCount() {
        return favoriteCount;
    }
    
    public void setFavoriteCount(Integer favoriteCount) {
        this.favoriteCount = favoriteCount;
    }
    
    public String getTrailerUrl() {
        return trailerUrl;
    }
    
    public void setTrailerUrl(String trailerUrl) {
        this.trailerUrl = trailerUrl;
    }
    
    public String getTags() {
        return tags;
    }
    
    public void setTags(String tags) {
        this.tags = tags;
    }
    
    public Boolean getIsFeatured() {
        return isFeatured;
    }
    
    public void setIsFeatured(Boolean isFeatured) {
        this.isFeatured = isFeatured;
    }
    
    public List<Episode> getEpisodes() {
        return episodes;
    }
    
    public void setEpisodes(List<Episode> episodes) {
        this.episodes = episodes;
    }
    
    public List<UserAnimeList> getUserAnimeLists() {
        return userAnimeLists;
    }
    
    public void setUserAnimeLists(List<UserAnimeList> userAnimeLists) {
        this.userAnimeLists = userAnimeLists;
    }
    
    // Helper methods
    public void incrementViewCount() {
        this.viewCount++;
    }
    
    public void incrementFavoriteCount() {
        this.favoriteCount++;
    }
    
    public void decrementFavoriteCount() {
        if (this.favoriteCount > 0) {
            this.favoriteCount--;
        }
    }
    
    // Enums
    public enum AnimeType {
        TV, MOVIE, OVA, ONA, SPECIAL
    }
    
    public enum AnimeStatus {
        AIRING, FINISHED, UPCOMING, CANCELLED
    }
}
