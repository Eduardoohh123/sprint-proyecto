package com.example.prueba_sprint.repository;

import com.example.prueba_sprint.entity.Guide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuideRepository extends JpaRepository<Guide, Long> {
    
    List<Guide> findByCategory(String category);
    
    List<Guide> findByDifficultyLevel(String difficultyLevel);
    
    List<Guide> findTop4ByOrderByViewCountDesc();
}
