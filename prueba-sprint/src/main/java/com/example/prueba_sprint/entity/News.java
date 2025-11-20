package com.example.prueba_sprint.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "news")
public class News {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 2000)
    private String content;
    
    @Column(length = 500)
    private String summary;
    
    private String author;
    
    @Column(name = "image_url")
    private String imageUrl;
    
    private String category; // Esports, VR, Hardware, etc.
    
    @Column(name = "icon_color")
    private String iconColor; // Para el gradiente del ícono
    
    @Column(name = "icon_class")
    private String iconClass; // Clase Font Awesome
    
    @Column(name = "news_url")
    private String newsUrl;
    
    @Column(name = "published_at")
    private LocalDateTime publishedAt;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (publishedAt == null) {
            publishedAt = LocalDateTime.now();
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Constructors
    public News() {}
    
    public News(String title, String content, String summary, String category) {
        this.title = title;
        this.content = content;
        this.summary = summary;
        this.category = category;
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
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getIconColor() {
        return iconColor;
    }
    
    public void setIconColor(String iconColor) {
        this.iconColor = iconColor;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
    
    public String getNewsUrl() {
        return newsUrl;
    }
    
    public void setNewsUrl(String newsUrl) {
        this.newsUrl = newsUrl;
    }
    
    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }
    
    public void setPublishedAt(LocalDateTime publishedAt) {
        this.publishedAt = publishedAt;
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
    
    // Helper method para mostrar tiempo relativo
    public String getTimeAgo() {
        if (publishedAt == null) return "";
        
        LocalDateTime now = LocalDateTime.now();
        long hours = java.time.Duration.between(publishedAt, now).toHours();
        
        if (hours < 1) {
            return "Hace " + java.time.Duration.between(publishedAt, now).toMinutes() + " minutos";
        } else if (hours < 24) {
            return "Hace " + hours + " horas";
        } else {
            long days = hours / 24;
            return "Hace " + days + (days == 1 ? " día" : " días");
        }
    }
}
