package com.example.prueba_sprint.util;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DatabaseMigrationRunner implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // Añadir columna supabase_id si no existe (idempotente)
        try {
            jdbcTemplate.execute("ALTER TABLE users ADD COLUMN IF NOT EXISTS supabase_id VARCHAR(255);");
            System.out.println("DB migration: ensured column supabase_id exists");
        } catch (Exception e) {
            System.err.println("DB migration failed: " + e.getMessage());
        }
    }
}
