-- Flyway Migration: V1
-- Title: Create Complete Application Schema
-- Author: devzelix
-- Date: 2025-11-09
-- Description: Establishes the core tables for the SICUC application,
--              including lookup tables (municipalities, categories),
--              dependent tables (parishes, disciplines), the user table,
--              and the main 'cultors' table.

-- ---------------------------------------------------------------------
-- Step 1: Lookup Tables (Entities with no dependencies)
-- These tables are created first as other tables will reference them.
-- ---------------------------------------------------------------------

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
    role VARCHAR(50) NOT NULL -- Stores the EnumType.STRING (e.g., "ROLE_ADMIN")
);

-- ---------------------------------------------------------------------
-- Step 2: Dependent Tables (Entities with foreign keys)
-- These tables depend on the lookup tables above.
-- ---------------------------------------------------------------------

CREATE TABLE parishes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    municipality_id INT NOT NULL,
    
    FOREIGN KEY (municipality_id) REFERENCES municipalities(id),
    
    -- Ensures a parish name is unique *within* its municipality
    UNIQUE KEY uk_parish_municipality (name, municipality_id)
);

CREATE TABLE art_disciplines (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    art_category_id INT NOT NULL,
    
    FOREIGN KEY (art_category_id) REFERENCES art_categories(id),
    
    -- Ensures a discipline name is unique *within* its category
    UNIQUE KEY uk_discipline_category (name, art_category_id)
);

-- ---------------------------------------------------------------------
-- Step 3: Core Table (cultors)
-- This table depends on all four lookup/dependent tables above.
-- ---------------------------------------------------------------------

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
    
    -- This field is managed by @CreatedDate (JPA Auditing)
    created_at DATE NOT NULL,
    
    -- Foreign Keys
    municipality_id INT NOT NULL,
    parish_id INT NOT NULL,
    art_category_id INT NOT NULL,
    art_discipline_id INT NOT NULL,
    
    FOREIGN KEY (municipality_id) REFERENCES municipalities(id),
    FOREIGN KEY (parish_id) REFERENCES parishes(id),
    FOREIGN KEY (art_category_id) REFERENCES art_categories(id),
    FOREIGN KEY (art_discipline_id) REFERENCES art_disciplines(id)
);