package com.example.prueba_sprint.repository;

import com.example.prueba_sprint.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    
    List<News> findTop4ByOrderByPublishedAtDesc();
    
    List<News> findByCategory(String category);
    
    List<News> findByAuthor(String author);
}
