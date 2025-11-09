-- Flyway Migration: V2
-- Title: Insert Reference Data
-- Author: devzelix
-- Date: 2025-11-09
-- Description: Populates the lookup tables (municipalities, parishes, 
--              art_categories, art_disciplines) with their default values.
--              Uses subqueries to be ID-agnostic and resilient.

-- ---------------------------------------------------------------------
-- Step 1: Municipalities
-- ---------------------------------------------------------------------
INSERT INTO municipalities (name) VALUES
('Bejuma'), ('Carlos Arvelo'), ('Diego Ibarra'), ('Guacara'), ('Juan José Mora'),
('Libertador'), ('Los Guayos'), ('Miranda'), ('Montalbán'), ('Naguanagua'),
('Puerto Cabello'), ('San Diego'), ('San Joaquín'), ('Valencia');

-- ---------------------------------------------------------------------
-- Step 2: Parishes
-- (Uses subqueries to link to the correct municipality ID)
-- ---------------------------------------------------------------------
INSERT INTO parishes (name, municipality_id) VALUES
('Bejuma', (SELECT id FROM municipalities WHERE name = 'Bejuma')),
('Canoabo', (SELECT id FROM municipalities WHERE name = 'Bejuma')),
('Simón Bolívar', (SELECT id FROM municipalities WHERE name = 'Bejuma')),
('Güigüe', (SELECT id FROM municipalities WHERE name = 'Carlos Arvelo')),
('Belén', (SELECT id FROM municipalities WHERE name = 'Carlos Arvelo')),
('Tacarigua', (SELECT id FROM municipalities WHERE name = 'Carlos Arvelo')),
('Mariara', (SELECT id FROM municipalities WHERE name = 'Diego Ibarra')),
('Aguas Calientes', (SELECT id FROM municipalities WHERE name = 'Diego Ibarra')),
('Guacara', (SELECT id FROM municipalities WHERE name = 'Guacara')),
('Yagua', (SELECT id FROM municipalities WHERE name = 'Guacara')),
('Ciudad Alianza', (SELECT id FROM municipalities WHERE name = 'Guacara')),
('Morón', (SELECT id FROM municipalities WHERE name = 'Juan José Mora')),
('Urama', (SELECT id FROM municipalities WHERE name = 'Juan José Mora')),
('Tocuyito', (SELECT id FROM municipalities WHERE name = 'Libertador')),
('Independencia', (SELECT id FROM municipalities WHERE name = 'Libertador')),
('Los Guayos', (SELECT id FROM municipalities WHERE name = 'Los Guayos')),
('Miranda', (SELECT id FROM municipalities WHERE name = 'Miranda')),
('Montalbán', (SELECT id FROM municipalities WHERE name = 'Montalbán')),
('Naguanagua', (SELECT id FROM municipalities WHERE name = 'Naguanagua')),
('Bartolomé Salom', (SELECT id FROM municipalities WHERE name = 'Puerto Cabello')),
('Borburata', (SELECT id FROM municipalities WHERE name = 'Puerto Cabello')),
('Democracia', (SELECT id FROM municipalities WHERE name = 'Puerto Cabello')),
('Fraternidad', (SELECT id FROM municipalities WHERE name = 'Puerto Cabello')),
('Goaigoaza', (SELECT id FROM municipalities WHERE name = 'Puerto Cabello')),
('Juan José Flores', (SELECT id FROM municipalities WHERE name = 'Puerto Cabello')),
('Patanemo', (SELECT id FROM municipalities WHERE name = 'Puerto Cabello')),
('Unión', (SELECT id FROM municipalities WHERE name = 'Puerto Cabello')),
('San Diego', (SELECT id FROM municipalities WHERE name = 'San Diego')),
('San Joaquín', (SELECT id FROM municipalities WHERE name = 'San Joaquín')),
('Candelaria', (SELECT id FROM municipalities WHERE name = 'Valencia')),
('Catedral', (SELECT id FROM municipalities WHERE name = 'Valencia')),
('El Socorro', (SELECT id FROM municipalities WHERE name = 'Valencia')),
('Miguel Peña', (SELECT id FROM municipalities WHERE name = 'Valencia')),
('Rafael Urdaneta', (SELECT id FROM municipalities WHERE name = 'Valencia')),
('San Blas', (SELECT id FROM municipalities WHERE name = 'Valencia')),
('San José', (SELECT id FROM municipalities WHERE name = 'Valencia')),
('Santa Rosa', (SELECT id FROM municipalities WHERE name = 'Valencia')),
('Negro Primero', (SELECT id FROM municipalities WHERE name = 'Valencia'));

-- ---------------------------------------------------------------------
-- Step 3: Art Categories
-- ---------------------------------------------------------------------
INSERT INTO art_categories (name) VALUES
('Artes Plásticas'), ('Artesanía'), ('Audiovisuales'), ('Danza'),
('Literatura'), ('Música'), ('Teatro');

-- ---------------------------------------------------------------------
-- Step 4: Art Disciplines
-- ---------------------------------------------------------------------
INSERT INTO art_disciplines (name, art_category_id) VALUES
-- Artes Plásticas
('Escultura', (SELECT id FROM art_categories WHERE name = 'Artes Plásticas')),
('Instalación', (SELECT id FROM art_categories WHERE name = 'Artes Plásticas')),
('Pintura', (SELECT id FROM art_categories WHERE name = 'Artes Plásticas')),
('Otra...', (SELECT id FROM art_categories WHERE name = 'Artes Plásticas')),
-- Artesanía
('Cestería', (SELECT id FROM art_categories WHERE name = 'Artesanía')),
('Cerámica', (SELECT id FROM art_categories WHERE name = 'Artesanía')),
('Dulcería criolla', (SELECT id FROM art_categories WHERE name = 'Artesanía')),
('Luthería', (SELECT id FROM art_categories WHERE name = 'Artesanía')),
('Muñequería', (SELECT id FROM art_categories WHERE name = 'Artesanía')),
('Orfebrería', (SELECT id FROM art_categories WHERE name = 'Artesanía')),
('Prendas', (SELECT id FROM art_categories WHERE name = 'Artesanía')),
('Tejido', (SELECT id FROM art_categories WHERE name = 'Artesanía')),
('Otra...', (SELECT id FROM art_categories WHERE name = 'Artesanía')),
-- Audiovisuales
('Cine', (SELECT id FROM art_categories WHERE name = 'Audiovisuales')),
('Experimental', (SELECT id FROM art_categories WHERE name = 'Audiovisuales')),
('Fotografía', (SELECT id FROM art_categories WHERE name = 'Audiovisuales')),
('Videoarte', (SELECT id FROM art_categories WHERE name = 'Audiovisuales')),
('Otra...', (SELECT id FROM art_categories WHERE name = 'Audiovisuales')),
-- Danza
('Ballet', (SELECT id FROM art_categories WHERE name = 'Danza')),
('Contemporánea', (SELECT id FROM art_categories WHERE name = 'Danza')),
('Moderna', (SELECT id FROM art_categories WHERE name = 'Danza')),
('Urbana', (SELECT id FROM art_categories WHERE name = 'Danza')),
('Tradicional', (SELECT id FROM art_categories WHERE name = 'Danza')),
('Otra...', (SELECT id FROM art_categories WHERE name = 'Danza')),
-- Literatura
('Crónica', (SELECT id FROM art_categories WHERE name = 'Literatura')),
('Cuento', (SELECT id FROM art_categories WHERE name = 'Literatura')),
('Ensayo', (SELECT id FROM art_categories WHERE name = 'Literatura')),
('Novela', (SELECT id FROM art_categories WHERE name = 'Literatura')),
('Poesía', (SELECT id FROM art_categories WHERE name = 'Literatura')),
('Otra...', (SELECT id FROM art_categories WHERE name = 'Literatura')),
-- Música
('Clásica o académica', (SELECT id FROM art_categories WHERE name = 'Música')),
('Experimental', (SELECT id FROM art_categories WHERE name = 'Música')),
('Fusión', (SELECT id FROM art_categories WHERE name = 'Música')),
('Llanera', (SELECT id FROM art_categories WHERE name = 'Música')),
('Popular', (SELECT id FROM art_categories WHERE name = 'Música')),
('Rock', (SELECT id FROM art_categories WHERE name = 'Música')),
('Tradicional', (SELECT id FROM art_categories WHERE name = 'Música')),
('Urbana', (SELECT id FROM art_categories WHERE name = 'Música')),
('Otra...', (SELECT id FROM art_categories WHERE name = 'Música')),
-- Teatro
('Circo', (SELECT id FROM art_categories WHERE name = 'Teatro')),
('Clown', (SELECT id FROM art_categories WHERE name = 'Teatro')),
('Mimo', (SELECT id FROM art_categories WHERE name = 'Teatro')),
('Teatro', (SELECT id FROM art_categories WHERE name = 'Teatro')),
('Títeres', (SELECT id FROM art_categories WHERE name = 'Teatro')),
('Otra...', (SELECT id FROM art_categories WHERE name = 'Teatro'));