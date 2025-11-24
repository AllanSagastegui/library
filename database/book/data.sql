CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS author (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    pseudonym VARCHAR(100),
    nationality VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS publisher (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    address TEXT,
    country VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS category (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
);

CREATE TABLE IF NOT EXISTS book (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    gender VARCHAR(100),
    summary TEXT,
    number_of_pages INTEGER,
    language VARCHAR(50),
    format VARCHAR(50),
    stock INTEGER NOT NULL DEFAULT 0,
    
    publisher_id UUID NOT NULL,
    category_id UUID NOT NULL,
    author_id UUID NOT NULL,

    CONSTRAINT fk_book_publisher 
        FOREIGN KEY (publisher_id) 
        REFERENCES publisher (id)
        ON DELETE CASCADE,

    CONSTRAINT fk_book_category 
        FOREIGN KEY (category_id) 
        REFERENCES category (id)
        ON DELETE RESTRICT,

    CONSTRAINT fk_book_author 
        FOREIGN KEY (author_id) 
        REFERENCES author (id)
        ON DELETE RESTRICT
);

CREATE INDEX IF NOT EXISTS idx_book_publisher ON book(publisher_id);
CREATE INDEX IF NOT EXISTS idx_book_category ON book(category_id);
CREATE INDEX IF NOT EXISTS idx_book_author ON book(author_id);


INSERT INTO category (name, description) VALUES 
('Tecnología', 'Libros sobre desarrollo de software, arquitectura y programación.'),
('Realismo Mágico', 'Corriente literaria que funde la realidad con elementos fantásticos.'),
('Ciencia Ficción', 'Narrativa basada en especulaciones científicas y futuristas.'),
('Fantasía Épica', 'Historias de mundos imaginarios, magia y aventuras heroicas.'),
('Terror', 'Relatos diseñados para provocar miedo y suspense.');

INSERT INTO publisher (name, address, country) VALUES 
('O''Reilly Media', '1005 Gravenstein Highway North, Sebastopol, CA', 'Estados Unidos'),
('Editorial Planeta', 'Av. Diagonal 662, Barcelona', 'España'),
('Penguin Random House', '1745 Broadway, New York', 'Estados Unidos'),
('Addison-Wesley', '75 Arlington Street, Boston', 'Estados Unidos'),
('Debolsillo', 'Travessera de Gracia, 47, Barcelona', 'España');

INSERT INTO author (first_name, last_name, pseudonym, nationality) VALUES 
('Robert C.', 'Martin', 'Uncle Bob', 'Estadounidense'),
('Gabriel', 'García Márquez', 'Gabo', 'Colombiana'),
('Isaac', 'Asimov', NULL, 'Rusa-Estadounidense'),
('J.R.R.', 'Tolkien', NULL, 'Británica'),
('Stephen', 'King', NULL, 'Estadounidense'),
('Joshua', 'Bloch', NULL, 'Estadounidense'),
('Eric', 'Evans', NULL, 'Estadounidense'),
('Mario', 'Vargas Llosa', NULL, 'Peruana'),
('Frank', 'Herbert', NULL, 'Estadounidense'),
('George R.R.', 'Martin', NULL, 'Estadounidense');


INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'Clean Code: A Handbook of Agile Software Craftsmanship', 
    'Educational', 
    'Incluso un código malo puede funcionar. Pero si el código no es limpio, puede acabar con una empresa de desarrollo.',
    464, 'Inglés', 'PHYSICAL', 15,
    (SELECT id FROM publisher WHERE name = 'O''Reilly Media'),
    (SELECT id FROM category WHERE name = 'Tecnología'),
    (SELECT id FROM author WHERE pseudonym = 'Uncle Bob')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'Clean Architecture: A Craftsman''s Guide to Software Structure', 
    'Educational', 
    'Las reglas universales de la arquitectura de software aplicadas por el legendario Uncle Bob.',
    432, 'Inglés', 'DIGITAL', 50,
    (SELECT id FROM publisher WHERE name = 'O''Reilly Media'),
    (SELECT id FROM category WHERE name = 'Tecnología'),
    (SELECT id FROM author WHERE pseudonym = 'Uncle Bob')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'Effective Java', 
    'Educational', 
    'La guía definitiva de las mejores prácticas para la plataforma Java.',
    416, 'Inglés', 'PHYSICAL', 10,
    (SELECT id FROM publisher WHERE name = 'Addison-Wesley'),
    (SELECT id FROM category WHERE name = 'Tecnología'),
    (SELECT id FROM author WHERE last_name = 'Bloch')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'Domain-Driven Design: Tackling Complexity in the Heart of Software', 
    'Educational', 
    'El libro seminal sobre DDD que cambió la forma de diseñar software complejo.',
    560, 'Inglés', 'HARDCOVER', 8,
    (SELECT id FROM publisher WHERE name = 'Addison-Wesley'),
    (SELECT id FROM category WHERE name = 'Tecnología'),
    (SELECT id FROM author WHERE last_name = 'Evans')
);


INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'Cien años de soledad', 
    'Novela', 
    'La historia de la familia Buendía en el pueblo ficticio de Macondo a lo largo de siete generaciones.',
    471, 'Español', 'PHYSICAL', 25,
    (SELECT id FROM publisher WHERE name = 'Editorial Planeta'),
    (SELECT id FROM category WHERE name = 'Realismo Mágico'),
    (SELECT id FROM author WHERE last_name = 'García Márquez')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'El amor en los tiempos del cólera', 
    'Novela', 
    'La historia de amor entre Fermina Daza y Florentino Ariza que dura más de 50 años.',
    368, 'Español', 'DIGITAL', 60,
    (SELECT id FROM publisher WHERE name = 'Debolsillo'),
    (SELECT id FROM category WHERE name = 'Realismo Mágico'),
    (SELECT id FROM author WHERE last_name = 'García Márquez')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'La ciudad y los perros', 
    'Novela', 
    'Narra la brutalidad de la vida militar en el colegio Leoncio Prado de Lima.',
    448, 'Español', 'PHYSICAL', 18,
    (SELECT id FROM publisher WHERE name = 'Editorial Planeta'),
    (SELECT id FROM category WHERE name = 'Realismo Mágico'),
    (SELECT id FROM author WHERE last_name = 'Vargas Llosa')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'Conversación en La Catedral', 
    'Novela Política', 
    'Un retrato crudo de la corrupción y la dictadura en el Perú de Odría.',
    704, 'Español', 'PHYSICAL', 5,
    (SELECT id FROM publisher WHERE name = 'Editorial Planeta'),
    (SELECT id FROM category WHERE name = 'Realismo Mágico'),
    (SELECT id FROM author WHERE last_name = 'Vargas Llosa')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'Dune', 
    'Sci-Fi', 
    'En el planeta desértico Arrakis, Paul Atreides debe liderar a los Fremen para recuperar su destino.',
    896, 'Inglés', 'AUDIOBOOK', 35,
    (SELECT id FROM publisher WHERE name = 'Penguin Random House'),
    (SELECT id FROM category WHERE name = 'Ciencia Ficción'),
    (SELECT id FROM author WHERE last_name = 'Herbert')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'Foundation', 
    'Sci-Fi', 
    'Hari Seldon utiliza la psicohistoria para predecir la caída del Imperio Galáctico y acortar la era oscura.',
    255, 'Inglés', 'PHYSICAL', 22, 
    (SELECT id FROM publisher WHERE name = 'O''Reilly Media'),
    (SELECT id FROM category WHERE name = 'Ciencia Ficción'),
    (SELECT id FROM author WHERE last_name = 'Asimov')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'Yo, Robot', 
    'Sci-Fi', 
    'Colección de historias que establecen las Tres Leyes de la Robótica.',
    320, 'Español', 'DIGITAL', 45, 
    (SELECT id FROM publisher WHERE name = 'Debolsillo'),
    (SELECT id FROM category WHERE name = 'Ciencia Ficción'),
    (SELECT id FROM author WHERE last_name = 'Asimov')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'The Fellowship of the Ring', 
    'Fantasy', 
    'Frodo Bolsón hereda un anillo mágico que debe ser destruido para salvar la Tierra Media.',
    423, 'Inglés', 'HARDCOVER', 12, 
    (SELECT id FROM publisher WHERE name = 'Penguin Random House'),
    (SELECT id FROM category WHERE name = 'Fantasía Épica'),
    (SELECT id FROM author WHERE last_name = 'Tolkien')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'A Game of Thrones', 
    'Fantasy', 
    'En una tierra donde los veranos duran décadas y los inviernos toda una vida, los problemas acechan.',
    694, 'Inglés', 'PHYSICAL', 17,
    (SELECT id FROM publisher WHERE name = 'Penguin Random House'),
    (SELECT id FROM category WHERE name = 'Fantasía Épica'),
    (SELECT id FROM author WHERE last_name = 'Martin' AND first_name = 'George R.R.')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'It', 
    'Horror', 
    'Siete adolescentes luchan contra una entidad maligna que explota los miedos de sus víctimas.',
    1138, 'Inglés', 'PHYSICAL', 9,
    (SELECT id FROM publisher WHERE name = 'Penguin Random House'),
    (SELECT id FROM category WHERE name = 'Terror'),
    (SELECT id FROM author WHERE last_name = 'King')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
VALUES (
    'The Shining', 
    'Horror', 
    'Jack Torrance acepta un trabajo como cuidador de invierno en el aislado Hotel Overlook.',
    447, 'Español', 'DIGITAL', 30,
    (SELECT id FROM publisher WHERE name = 'Debolsillo'),
    (SELECT id FROM category WHERE name = 'Terror'),
    (SELECT id FROM author WHERE last_name = 'King')
);

INSERT INTO book (title, gender, summary, number_of_pages, language, format, stock, publisher_id, category_id, author_id)
SELECT 
    title || ' - Edición ' || generate_series,
    gender,
    summary,
    number_of_pages,
    language,
    CASE WHEN generate_series % 2 = 0 THEN 'DIGITAL' ELSE 'PHYSICAL' END,
    10,
    publisher_id,
    category_id,
    author_id
FROM book
CROSS JOIN generate_series(1, 15)
WHERE title IN ('Clean Code: A Handbook of Agile Software Craftsmanship', 'Dune', 'Cien años de soledad');