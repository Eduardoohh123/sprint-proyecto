package com.example.prueba_sprint.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/health")
public class HealthCheckController {

    @Autowired
    private DataSource dataSource;

    /**
     * Endpoint para verificar el estado de la aplicación
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("application", "Spring Boot API");
        status.put("status", "running");
        status.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(status);
    }

    /**
     * Endpoint para verificar la conexión con PostgreSQL
     */
    @GetMapping("/database")
    public ResponseEntity<Map<String, Object>> checkDatabaseConnection() {
        Map<String, Object> response = new HashMap<>();
        
        try (Connection connection = dataSource.getConnection()) {
            response.put("database", "PostgreSQL");
            response.put("status", "connected");
            response.put("url", connection.getMetaData().getURL());
            response.put("username", connection.getMetaData().getUserName());
            response.put("databaseProductName", connection.getMetaData().getDatabaseProductName());
            response.put("databaseProductVersion", connection.getMetaData().getDatabaseProductVersion());
            response.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Endpoint de prueba simple
     */
    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
