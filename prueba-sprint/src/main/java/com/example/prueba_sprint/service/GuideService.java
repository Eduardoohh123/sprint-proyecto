package com.example.prueba_sprint.service;

import com.example.prueba_sprint.entity.Guide;
import com.example.prueba_sprint.repository.GuideRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GuideService {
    
    @Autowired
    private GuideRepository guideRepository;
    
    public List<Guide> getAllGuides() {
        return guideRepository.findAll();
    }
    
    public Optional<Guide> getGuideById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return guideRepository.findById(id);
    }
    
    public List<Guide> getPopularGuides() {
        return guideRepository.findTop4ByOrderByViewCountDesc();
    }
    
    public List<Guide> getGuidesByCategory(String category) {
        return guideRepository.findByCategory(category);
    }
    
    public Guide createGuide(Guide guide) {
        if (guide == null) {
            throw new IllegalArgumentException("Guide cannot be null");
        }
        return guideRepository.save(guide);
    }
    
    public Optional<Guide> updateGuide(Long id, Guide guide) {
        if (id == null || guide == null) {
            return Optional.empty();
        }
        
        return guideRepository.findById(id).map(existingGuide -> {
            existingGuide.setTitle(guide.getTitle());
            existingGuide.setContent(guide.getContent());
            existingGuide.setDescription(guide.getDescription());
            existingGuide.setCategory(guide.getCategory());
            existingGuide.setIconClass(guide.getIconClass());
            existingGuide.setIconGradient(guide.getIconGradient());
            existingGuide.setDifficultyLevel(guide.getDifficultyLevel());
            existingGuide.setEstimatedTime(guide.getEstimatedTime());
            existingGuide.setGuideUrl(guide.getGuideUrl());
            return guideRepository.save(existingGuide);
        });
    }
    
    public void deleteGuide(Long id) {
        if (id != null && guideRepository.existsById(id)) {
            guideRepository.deleteById(id);
        }
    }
    
    public void incrementViewCount(Long id) {
        guideRepository.findById(id).ifPresent(guide -> {
            guide.incrementViewCount();
            guideRepository.save(guide);
        });
    }
}
