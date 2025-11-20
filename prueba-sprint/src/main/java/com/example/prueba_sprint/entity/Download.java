package com.example.prueba_sprint.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "downloads")
public class Download {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;
    
    @Column(name = "download_url", nullable = false)
    private String downloadUrl;
    
    @Column(name = "download_type")
    private String downloadType; // Installer, Patch, DLC, Mod
    
    private String platform; // PC, PS5, Xbox, etc.
    
    @Column(name = "file_size")
    private String fileSize; // "15 GB", "2.5 GB"
    
    private String version;
    
    @Column(name = "is_official")
    private Boolean isOfficial = true;
    
    @Column(name = "download_count")
    private Integer downloadCount = 0;
    
    @Column(name = "requires_account")
    private Boolean requiresAccount = false;
    
    private String notes;
    
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
    public Download() {}
    
    public Download(Game game, String downloadUrl, String downloadType, String platform) {
        this.game = game;
        this.downloadUrl = downloadUrl;
        this.downloadType = downloadType;
        this.platform = platform;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Game getGame() {
        return game;
    }
    
    public void setGame(Game game) {
        this.game = game;
    }
    
    public String getDownloadUrl() {
        return downloadUrl;
    }
    
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    
    public String getDownloadType() {
        return downloadType;
    }
    
    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }
    
    public String getPlatform() {
        return platform;
    }
    
    public void setPlatform(String platform) {
        this.platform = platform;
    }
    
    public String getFileSize() {
        return fileSize;
    }
    
    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }
    
    public String getVersion() {
        return version;
    }
    
    public void setVersion(String version) {
        this.version = version;
    }
    
    public Boolean getIsOfficial() {
        return isOfficial;
    }
    
    public void setIsOfficial(Boolean isOfficial) {
        this.isOfficial = isOfficial;
    }
    
    public Integer getDownloadCount() {
        return downloadCount;
    }
    
    public void setDownloadCount(Integer downloadCount) {
        this.downloadCount = downloadCount;
    }
    
    public Boolean getRequiresAccount() {
        return requiresAccount;
    }
    
    public void setRequiresAccount(Boolean requiresAccount) {
        this.requiresAccount = requiresAccount;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
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
    public void incrementDownloadCount() {
        this.downloadCount++;
    }
}
