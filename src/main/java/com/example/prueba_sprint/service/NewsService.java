package com.example.prueba_sprint.service;

import com.example.prueba_sprint.entity.News;
import com.example.prueba_sprint.repository.NewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NewsService {
    
    @Autowired
    private NewsRepository newsRepository;
    
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }
    
    public Optional<News> getNewsById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return newsRepository.findById(id);
    }
    
    public List<News> getLatestNews() {
        return newsRepository.findTop4ByOrderByPublishedAtDesc();
    }
    
    public List<News> getNewsByCategory(String category) {
        return newsRepository.findByCategory(category);
    }
    
    public News createNews(News news) {
        if (news == null) {
            throw new IllegalArgumentException("News cannot be null");
        }
        return newsRepository.save(news);
    }
    
    public Optional<News> updateNews(Long id, News news) {
        if (id == null || news == null) {
            return Optional.empty();
        }
        
        return newsRepository.findById(id).map(existingNews -> {
            existingNews.setTitle(news.getTitle());
            existingNews.setContent(news.getContent());
            existingNews.setSummary(news.getSummary());
            existingNews.setAuthor(news.getAuthor());
            existingNews.setImageUrl(news.getImageUrl());
            existingNews.setCategory(news.getCategory());
            existingNews.setIconColor(news.getIconColor());
            existingNews.setIconClass(news.getIconClass());
            existingNews.setNewsUrl(news.getNewsUrl());
            existingNews.setPublishedAt(news.getPublishedAt());
            return newsRepository.save(existingNews);
        });
    }
    
    public void deleteNews(Long id) {
        if (id != null && newsRepository.existsById(id)) {
            newsRepository.deleteById(id);
        }
    }
}
