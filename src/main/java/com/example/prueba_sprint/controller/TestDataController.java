package com.example.prueba_sprint.controller;

import com.example.prueba_sprint.entity.*;
import com.example.prueba_sprint.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestDataController {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private AnimeRepository animeRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private GameRepository gameRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private com.example.prueba_sprint.service.UserService userService;

    @Autowired
    private com.example.prueba_sprint.service.SupabaseAdminService supabaseAdminService;

    @Autowired
    private javax.sql.DataSource dataSource;

    private static final Logger log = LoggerFactory.getLogger(TestDataController.class);

    /**
     * Obtener estadísticas de la base de datos
     */
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("users", userRepository.count());
        stats.put("animes", animeRepository.count());
        stats.put("categories", categoryRepository.count());
        stats.put("games", gameRepository.count());
        stats.put("timestamp", System.currentTimeMillis());
        return ResponseEntity.ok(stats);
    }

    /**
     * Endpoint de diagnóstico/health para Supabase Admin API (temporal)
     */
    @GetMapping("/supabase")
    public ResponseEntity<Map<String, Object>> supabaseStatus() {
        try {
            Map<String, Object> status = supabaseAdminService.healthCheck();
            boolean ok = Boolean.TRUE.equals(status.get("ok"));
            if (ok) {
                return ResponseEntity.ok(status);
            } else {
                return ResponseEntity.status(502).body(status);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("ok", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    /**
     * Endpoint temporal para ejecutar el script schema.sql en la BD de producción.
     * Uso: POST /api/test/run-schema
     * ADVERTENCIA: solo para uso de mantenimiento en staging/provisión — eliminar tras uso.
     */
    @PostMapping("/run-schema")
    public ResponseEntity<Map<String, Object>> runSchema() {
        Map<String, Object> response = new HashMap<>();
        try {
            // Ejecutar script SQL incluida en classpath: schema.sql
            org.springframework.core.io.ClassPathResource resource = new org.springframework.core.io.ClassPathResource("schema.sql");
            org.springframework.jdbc.datasource.init.ResourceDatabasePopulator pop = new org.springframework.jdbc.datasource.init.ResourceDatabasePopulator(resource);
            pop.execute(this.dataSource);

            response.put("ok", true);
            response.put("message", "Schema ejecutado correctamente");
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error ejecutando schema.sql: {}", ex.getMessage(), ex);
            response.put("ok", false);
            response.put("message", "Error ejecutando schema.sql: " + ex.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Endpoint temporal para verificar existencia de la tabla `users` y su recuento.
     */
    @GetMapping("/db-check")
    public ResponseEntity<Map<String, Object>> dbCheck() {
        Map<String, Object> response = new HashMap<>();
        try {
            org.springframework.jdbc.core.JdbcTemplate jt = new org.springframework.jdbc.core.JdbcTemplate(this.dataSource);
            Integer exists = jt.queryForObject("select count(*) from information_schema.tables where table_schema='public' and table_name='users'", Integer.class);
            response.put("table_exists", exists != null && exists > 0);
            if (exists != null && exists > 0) {
                Integer count = jt.queryForObject("select count(*) from users", Integer.class);
                response.put("users_count", count != null ? count : 0);
            } else {
                response.put("users_count", 0);
            }
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error en db-check: {}", ex.getMessage(), ex);
            response.put("ok", false);
            response.put("message", ex.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Endpoint temporal de depuración: devuelve información pública sobre un usuario por email.
     * Uso: GET /api/test/users/debug?email=...  (dev-only)
     */
    @GetMapping("/users/debug")
    public ResponseEntity<Map<String, Object>> debugUser(@RequestParam String email) {
        Map<String, Object> response = new HashMap<>();
        try {
            var userOpt = userRepository.findByEmail(email);
            if (!userOpt.isPresent()) {
                response.put("found", false);
                response.put("message", "No existe usuario con ese email");
                return ResponseEntity.status(404).body(response);
            }
            User u = userOpt.get();
            response.put("found", true);
            response.put("id", u.getId());
            response.put("username", u.getUsername());
            response.put("name", u.getName());
            response.put("email", u.getEmail());
            response.put("supabaseId", u.getSupabaseId());
            response.put("role", u.getRole());
            response.put("createdAt", u.getCreatedAt());
            // Indicador de si la contraseña parece hasheada con bcrypt
            String pw = u.getPassword();
            boolean looksHashed = (pw != null && pw.startsWith("$2"));
            response.put("passwordLooksHashed", looksHashed);
            // No devolver la contraseña ni hashes
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            log.error("Error en users/debug: {}", ex.getMessage(), ex);
            response.put("ok", false);
            response.put("message", ex.getMessage());
            return ResponseEntity.status(500).body(response);
        }
    }
    /**
     * Insertar datos de prueba
     */
    @PostMapping("/seed")
    public ResponseEntity<Map<String, String>> seedTestData() {
        try {
            // Crear un usuario de prueba
            if (userRepository.count() == 0) {
                User user = new User();
                user.setUsername("testuser");
                user.setName("Usuario de Prueba");
                user.setEmail("test@example.com");
                user.setPassword("password123");
                user.setRole("USER");
                userRepository.save(user);
            }

            // Crear una categoría de prueba
            if (categoryRepository.count() == 0) {
                Category category = new Category();
                category.setName("Acción");
                category.setDescription("Juegos de acción y aventura");
                category.setIconClass("fa-fire");
                category.setColorCode("#FF5733");
                categoryRepository.save(category);
            }

            // Crear un anime de prueba
            if (animeRepository.count() == 0) {
                Anime anime = new Anime();
                anime.setTitleJapanese("ワンピース");
                anime.setTitleRomaji("One Piece");
                anime.setTitleEnglish("One Piece");
                anime.setSynopsis("Aventuras de Monkey D. Luffy y su tripulación");
                anime.setType(Anime.AnimeType.TV);
                anime.setStatus(Anime.AnimeStatus.AIRING);
                anime.setRating(9.5);
                anime.setViewCount(0);
                anime.setFavoriteCount(0);
                animeRepository.save(anime);
            }

            Map<String, String> response = new HashMap<>();
            response.put("message", "Datos de prueba insertados correctamente");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Error al insertar datos: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Registrar nuevo usuario (desde Ionic)
     */
    @PostMapping("/users/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody Map<String, String> userData) {
        try {
            String username = userData.get("username");
            String name = userData.get("name");
            String email = userData.get("email");
            String password = userData.get("password");

            // Construir entidad User y delegar en el servicio centralizado
            User user = new User();
            user.setUsername(username);
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setRole("USER");

            log.info("Registrando usuario desde API: {}", email);

            User savedUser = userService.registerUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Usuario registrado exitosamente");
            response.put("status", "success");

            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", savedUser.getId());
            userInfo.put("username", savedUser.getUsername());
            userInfo.put("name", savedUser.getName());
            userInfo.put("email", savedUser.getEmail());
            userInfo.put("supabaseId", savedUser.getSupabaseId());

            response.put("user", userInfo);
            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            log.warn("Registro rechazado: {}", e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            response.put("status", "error");
            // Si el error viene del upstream Supabase, devolver 502 Bad Gateway
            if (e.getMessage().toLowerCase().contains("supabase") || e.getMessage().toLowerCase().contains("service_role_key")) {
                return ResponseEntity.status(502).body(response);
            }
            return ResponseEntity.status(400).body(response);
        } catch (Exception e) {
            log.error("Error al registrar usuario: {}", e.getMessage(), e);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al registrar usuario: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Login de usuario (desde Ionic)
     */
    @PostMapping("/users/login")
    public ResponseEntity<Map<String, Object>> loginUser(@RequestBody Map<String, String> credentials) {
        try {
            String email = credentials.get("email");
            String password = credentials.get("password");

            // Buscar usuario por email
            var userOptional = userRepository.findByEmail(email);
            
            if (!userOptional.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No existe una cuenta con este correo");
                response.put("status", "error");
                return ResponseEntity.status(401).body(response);
            }

            User user = userOptional.get();

            // Verificar contraseña con BCrypt (o migrar si estaba en texto plano)
            if (!passwordEncoder.matches(password, user.getPassword())) {
                // Si la contraseña en la BD no parece hasheada y coincide exactamente, migrar a BCrypt
                if (user.getPassword() != null && !user.getPassword().startsWith("$2") && password.equals(user.getPassword())) {
                    String hashed = passwordEncoder.encode(password);
                    user.setPassword(hashed);
                    userRepository.save(user);
                } else {
                    Map<String, Object> response = new HashMap<>();
                    response.put("message", "Contraseña incorrecta");
                    response.put("status", "error");
                    return ResponseEntity.status(401).body(response);
                }
            }

            // Login exitoso
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Login exitoso");
            response.put("status", "success");
            
            // Datos del usuario (sin la contraseña)
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("username", user.getUsername());
            userInfo.put("name", user.getName());
            userInfo.put("email", user.getEmail());
            userInfo.put("role", user.getRole());
            userInfo.put("createdAt", user.getCreatedAt());
            
            response.put("user", userInfo);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al iniciar sesión: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }

    /**
     * Endpoint temporal para actualizar la contraseña de un usuario (hash con BCrypt).
     * Úsalo solo en desarrollo: POST /api/test/users/set-password { "email": "..", "password": ".." }
     */
    @PostMapping("/users/set-password")
    public ResponseEntity<Map<String, Object>> setPassword(@RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");

            var userOptional = userRepository.findByEmail(email);
            if (!userOptional.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "No existe una cuenta con este correo");
                response.put("status", "error");
                return ResponseEntity.status(404).body(response);
            }

            User user = userOptional.get();
            user.setPassword(passwordEncoder.encode(password));
            userRepository.save(user);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Contraseña actualizada y hasheada");
            response.put("status", "success");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error al actualizar contraseña: " + e.getMessage());
            response.put("status", "error");
            return ResponseEntity.status(500).body(response);
        }
    }
}
