package com.example.prueba_sprint.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "guides")
public class Guide {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String title;
    
    @Column(length = 3000)
    private String content;
    
    @Column(length = 500)
    private String description;
    
    private String category; // FPS, RTS, Optimización, etc.
    
    @Column(name = "icon_class")
    private String iconClass; // Clase Font Awesome
    
    @Column(name = "icon_gradient")
    private String iconGradient; // Color del gradiente
    
    @Column(name = "difficulty_level")
    private String difficultyLevel; // Principiante, Intermedio, Avanzado
    
    @Column(name = "estimated_time")
    private String estimatedTime; // Tiempo estimado de lectura
    
    @Column(name = "view_count")
    private Integer viewCount = 0;
    
    @Column(name = "guide_url")
    private String guideUrl;
    
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
    public Guide() {}
    
    public Guide(String title, String description, String category) {
        this.title = title;
        this.description = description;
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
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getIconClass() {
        return iconClass;
    }
    
    public void setIconClass(String iconClass) {
        this.iconClass = iconClass;
    }
    
    public String getIconGradient() {
        return iconGradient;
    }
    
    public void setIconGradient(String iconGradient) {
        this.iconGradient = iconGradient;
    }
    
    public String getDifficultyLevel() {
        return difficultyLevel;
    }
    
    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    
    public String getEstimatedTime() {
        return estimatedTime;
    }
    
    public void setEstimatedTime(String estimatedTime) {
        this.estimatedTime = estimatedTime;
    }
    
    public Integer getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(Integer viewCount) {
        this.viewCount = viewCount;
    }
    
    public String getGuideUrl() {
        return guideUrl;
    }
    
    public void setGuideUrl(String guideUrl) {
        this.guideUrl = guideUrl;
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
    
    // Helper method
    public void incrementViewCount() {
        this.viewCount++;
    }
}
