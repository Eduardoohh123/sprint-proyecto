-- ============================================
-- SCRIPT DE CREACIÓN DE BASE DE DATOS
-- Sistema de Gestión de Animes y Juegos
-- Base de datos: prueba_sprint_db
-- ============================================

-- Eliminar tablas existentes (en orden inverso por las FK)
DROP TABLE IF EXISTS downloads CASCADE;
DROP TABLE IF EXISTS guides CASCADE;
DROP TABLE IF EXISTS news CASCADE;
DROP TABLE IF EXISTS user_anime_lists CASCADE;
DROP TABLE IF EXISTS episodes CASCADE;
DROP TABLE IF EXISTS animes CASCADE;
DROP TABLE IF EXISTS games CASCADE;
DROP TABLE IF EXISTS categories CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- ============================================
-- TABLA: users
-- Descripción: Almacena los usuarios del sistema
-- ============================================
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    name VARCHAR(255),
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    avatar_url VARCHAR(500),
    role VARCHAR(50) DEFAULT 'USER' NOT NULL, -- USER, ADMIN
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_users_username UNIQUE (username),
    CONSTRAINT uk_users_email UNIQUE (email)
);

-- Índices para búsquedas frecuentes
CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- ============================================
-- TABLA: categories
-- Descripción: Categorías para juegos y otros contenidos
-- ============================================
CREATE TABLE categories (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    icon_class VARCHAR(100), -- Clase Font Awesome
    color_code VARCHAR(20), -- Color hex
    slug VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    display_order INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_categories_name UNIQUE (name)
);

-- Índices
CREATE INDEX idx_categories_slug ON categories(slug);
CREATE INDEX idx_categories_active ON categories(is_active);
CREATE INDEX idx_categories_order ON categories(display_order);

-- ============================================
-- TABLA: animes
-- Descripción: Catálogo de animes
-- ============================================
CREATE TABLE animes (
    id BIGSERIAL PRIMARY KEY,
    title_japanese VARCHAR(255) NOT NULL,
    title_romaji VARCHAR(255) NOT NULL,
    title_english VARCHAR(255),
    synopsis TEXT,
    poster_url VARCHAR(500),
    banner_url VARCHAR(500),
    genres VARCHAR(500), -- Separados por comas
    type VARCHAR(50), -- TV, MOVIE, OVA, ONA, SPECIAL
    total_episodes INTEGER,
    status VARCHAR(50), -- ONGOING, COMPLETED, UPCOMING, CANCELLED
    release_date DATE,
    end_date DATE,
    studio VARCHAR(255),
    rating DECIMAL(3,2), -- 0.00 a 10.00
    view_count INTEGER DEFAULT 0,
    favorite_count INTEGER DEFAULT 0,
    trailer_url VARCHAR(500),
    tags VARCHAR(500),
    is_featured BOOLEAN DEFAULT false,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_animes_title_romaji ON animes(title_romaji);
CREATE INDEX idx_animes_type ON animes(type);
CREATE INDEX idx_animes_status ON animes(status);
CREATE INDEX idx_animes_rating ON animes(rating DESC);
CREATE INDEX idx_animes_featured ON animes(is_featured);
CREATE INDEX idx_animes_release_date ON animes(release_date DESC);

-- ============================================
-- TABLA: episodes
-- Descripción: Episodios de cada anime
-- Relación: N:1 con animes
-- ============================================
CREATE TABLE episodes (
    id BIGSERIAL PRIMARY KEY,
    anime_id BIGINT NOT NULL,
    episode_number INTEGER NOT NULL,
    title VARCHAR(255),
    description TEXT,
    thumbnail_url VARCHAR(500),
    duration INTEGER, -- Duración en minutos
    release_date DATE,
    video_url VARCHAR(500),
    server_urls TEXT, -- URLs de servidores separados por coma
    view_count INTEGER DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_episodes_anime FOREIGN KEY (anime_id) 
        REFERENCES animes(id) ON DELETE CASCADE,
    CONSTRAINT uk_anime_episode UNIQUE (anime_id, episode_number)
);

-- Índices
CREATE INDEX idx_episodes_anime_id ON episodes(anime_id);
CREATE INDEX idx_episodes_number ON episodes(episode_number);
CREATE INDEX idx_episodes_release_date ON episodes(release_date DESC);

-- ============================================
-- TABLA: user_anime_lists
-- Descripción: Lista de animes por usuario
-- Relación: N:1 con users y animes
-- ============================================
CREATE TABLE user_anime_lists (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    anime_id BIGINT NOT NULL,
    watch_status VARCHAR(50) NOT NULL, -- WATCHING, COMPLETED, PLAN_TO_WATCH, ON_HOLD, DROPPED
    episodes_watched INTEGER DEFAULT 0,
    user_rating DECIMAL(3,2), -- 0.00 a 10.00
    is_favorite BOOLEAN DEFAULT false,
    notes TEXT,
    added_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_anime_list_user FOREIGN KEY (user_id) 
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_user_anime_list_anime FOREIGN KEY (anime_id) 
        REFERENCES animes(id) ON DELETE CASCADE,
    CONSTRAINT uk_user_anime UNIQUE (user_id, anime_id)
);

-- Índices
CREATE INDEX idx_user_anime_lists_user_id ON user_anime_lists(user_id);
CREATE INDEX idx_user_anime_lists_anime_id ON user_anime_lists(anime_id);
CREATE INDEX idx_user_anime_lists_status ON user_anime_lists(watch_status);
CREATE INDEX idx_user_anime_lists_favorite ON user_anime_lists(is_favorite);

-- ============================================
-- TABLA: games
-- Descripción: Catálogo de juegos
-- Relación: N:1 con categories
-- ============================================
CREATE TABLE games (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    short_description VARCHAR(500),
    developer VARCHAR(255),
    publisher VARCHAR(255),
    release_date DATE,
    thumbnail_url VARCHAR(500),
    cover_image_url VARCHAR(500),
    trailer_url VARCHAR(500),
    rating DECIMAL(3,2), -- 0.00 a 10.00
    genre VARCHAR(255), -- Acción, RPG, Aventura, etc.
    platforms VARCHAR(500), -- PC, PS5, Xbox, etc.
    badge VARCHAR(100), -- "¡Recién salido!", "Más jugado"
    badge_color VARCHAR(50),
    is_featured BOOLEAN DEFAULT false,
    view_count INTEGER DEFAULT 0,
    download_count INTEGER DEFAULT 0,
    category_id BIGINT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_games_category FOREIGN KEY (category_id) 
        REFERENCES categories(id) ON DELETE SET NULL
);

-- Índices
CREATE INDEX idx_games_title ON games(title);
CREATE INDEX idx_games_genre ON games(genre);
CREATE INDEX idx_games_rating ON games(rating DESC);
CREATE INDEX idx_games_featured ON games(is_featured);
CREATE INDEX idx_games_category_id ON games(category_id);
CREATE INDEX idx_games_release_date ON games(release_date DESC);

-- ============================================
-- TABLA: downloads
-- Descripción: Enlaces de descarga para juegos
-- Relación: N:1 con games
-- ============================================
CREATE TABLE downloads (
    id BIGSERIAL PRIMARY KEY,
    game_id BIGINT NOT NULL,
    download_url VARCHAR(500) NOT NULL,
    download_type VARCHAR(100), -- Installer, Patch, DLC, Mod
    platform VARCHAR(100), -- PC, PS5, Xbox, etc.
    file_size VARCHAR(50), -- "15 GB", "2.5 GB"
    version VARCHAR(50),
    is_official BOOLEAN DEFAULT true,
    download_count INTEGER DEFAULT 0,
    requires_account BOOLEAN DEFAULT false,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_downloads_game FOREIGN KEY (game_id) 
        REFERENCES games(id) ON DELETE CASCADE
);

-- Índices
CREATE INDEX idx_downloads_game_id ON downloads(game_id);
CREATE INDEX idx_downloads_platform ON downloads(platform);
CREATE INDEX idx_downloads_type ON downloads(download_type);

-- ============================================
-- TABLA: news
-- Descripción: Noticias sobre gaming y animes
-- ============================================
CREATE TABLE news (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    summary VARCHAR(500),
    author VARCHAR(255),
    image_url VARCHAR(500),
    category VARCHAR(100), -- Esports, VR, Hardware, etc.
    icon_color VARCHAR(50),
    icon_class VARCHAR(100),
    news_url VARCHAR(500),
    published_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_news_category ON news(category);
CREATE INDEX idx_news_published_at ON news(published_at DESC);
CREATE INDEX idx_news_created_at ON news(created_at DESC);

-- ============================================
-- TABLA: guides
-- Descripción: Guías y tutoriales
-- ============================================
CREATE TABLE guides (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    description VARCHAR(500),
    category VARCHAR(100), -- FPS, RTS, Optimización, etc.
    icon_class VARCHAR(100),
    icon_gradient VARCHAR(100),
    difficulty_level VARCHAR(50), -- Principiante, Intermedio, Avanzado
    estimated_time VARCHAR(50), -- "10 min", "30 min"
    view_count INTEGER DEFAULT 0,
    guide_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_guides_category ON guides(category);
CREATE INDEX idx_guides_difficulty ON guides(difficulty_level);
CREATE INDEX idx_guides_view_count ON guides(view_count DESC);

-- ============================================
-- DATOS DE PRUEBA (OPCIONAL)
-- ============================================

-- Insertar usuario administrador por defecto
INSERT INTO users (username, name, email, password, role) 
VALUES ('admin', 'Administrador', 'admin@prueba.com', '$2a$10$XYZ...', 'ADMIN');

-- Insertar usuario de prueba
INSERT INTO users (username, name, email, password, role) 
VALUES ('user1', 'Usuario Prueba', 'user@prueba.com', '$2a$10$XYZ...', 'USER');

-- Insertar categorías de ejemplo
INSERT INTO categories (name, description, icon_class, color_code, slug, display_order) VALUES
('Acción', 'Juegos de acción y aventura', 'fa-fire', '#FF5733', 'accion', 1),
('RPG', 'Juegos de rol', 'fa-dragon', '#C70039', 'rpg', 2),
('Deportes', 'Juegos deportivos', 'fa-football-ball', '#900C3F', 'deportes', 3),
('Estrategia', 'Juegos de estrategia', 'fa-chess', '#581845', 'estrategia', 4);

-- ============================================
-- COMENTARIOS Y DOCUMENTACIÓN
-- ============================================

COMMENT ON TABLE users IS 'Tabla de usuarios del sistema';
COMMENT ON TABLE animes IS 'Catálogo de animes disponibles';
COMMENT ON TABLE episodes IS 'Episodios de cada anime';
COMMENT ON TABLE user_anime_lists IS 'Listas personales de animes por usuario';
COMMENT ON TABLE games IS 'Catálogo de juegos';
COMMENT ON TABLE downloads IS 'Enlaces de descarga de juegos';
COMMENT ON TABLE categories IS 'Categorías para clasificar contenido';
COMMENT ON TABLE news IS 'Noticias del mundo gaming y anime';
COMMENT ON TABLE guides IS 'Guías y tutoriales';

-- ============================================
-- FIN DEL SCRIPT
-- ============================================
