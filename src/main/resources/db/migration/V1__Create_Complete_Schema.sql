-- V1: Creación del esquema completo de la aplicación
-- El orden es importante para las llaves foráneas (FOREIGN KEY)

-- 1. Tablas sin dependencias
CREATE TABLE municipalities (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE art_categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL -- Para "ROLE_ADMIN", "ROLE_EDITOR", etc.
);

-- 2. Tablas con dependencias
CREATE TABLE parishes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    municipality_id INT NOT NULL,
    FOREIGN KEY (municipality_id) REFERENCES municipalities(id),
    UNIQUE KEY uk_parish_municipality (name, municipality_id) -- Constraint de tu entidad
);

CREATE TABLE art_disciplines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    art_category_id INT NOT NULL,
    FOREIGN KEY (art_category_id) REFERENCES art_categories(id),
    UNIQUE KEY uk_discipline_category (name, art_category_id) -- Constraint de tu entidad
);

-- 3. Tabla 'cultors' (depende de las 4 tablas anteriores)
CREATE TABLE cultors (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    gender VARCHAR(1) NOT NULL,
    id_number VARCHAR(10) NOT NULL UNIQUE,
    birth_date DATE NOT NULL,
    phone_number VARCHAR(12) NOT NULL UNIQUE,
    email VARCHAR(150) UNIQUE,
    instagram_user VARCHAR(30) UNIQUE,
    home_address VARCHAR(100) NOT NULL,
    other_discipline VARCHAR(100),
    years_of_experience INT NOT NULL,
    group_name VARCHAR(100),
    disability VARCHAR(100),
    illness VARCHAR(100),
    created_at DATE NOT NULL,
    
    -- Llaves Foráneas
    municipality_id INT NOT NULL,
    parish_id INT NOT NULL,
    art_category_id INT NOT NULL,
    art_discipline_id INT NOT NULL,
    
    FOREIGN KEY (municipality_id) REFERENCES municipalities(id),
    FOREIGN KEY (parish_id) REFERENCES parishes(id),
    FOREIGN KEY (art_category_id) REFERENCES art_categories(id),
    FOREIGN KEY (art_discipline_id) REFERENCES art_disciplines(id)
);