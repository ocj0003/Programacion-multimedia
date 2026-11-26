-- Base de datos de la Tarea 8: mis videojuegos
SET NAMES utf8mb4;
CREATE DATABASE IF NOT EXISTS pmdm CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pmdm;

DROP TABLE IF EXISTS videojuegos;
CREATE TABLE videojuegos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    plataforma VARCHAR(50) NOT NULL,
    imagen VARCHAR(255) NOT NULL
);

INSERT INTO videojuegos (titulo, plataforma, imagen) VALUES
('The Legend of Zelda', 'Nintendo Switch', 'https://picsum.photos/id/1011/300/300'),
('FIFA 23', 'PlayStation 5', 'https://picsum.photos/id/1015/300/300'),
('Counter-Strike 2', 'PC', 'https://picsum.photos/id/1025/300/300'),
('Clash Royale', 'Móvil', 'https://picsum.photos/id/1035/300/300'),
('Mario Kart 8', 'Nintendo Switch', 'https://picsum.photos/id/1043/300/300'),
('Gran Turismo 7', 'PlayStation 5', 'https://picsum.photos/id/1050/300/300'),
('League of Legends', 'PC', 'https://picsum.photos/id/1062/300/300'),
('Brawl Stars', 'Móvil', 'https://picsum.photos/id/1074/300/300'),
('Minecraft', 'PC', 'https://picsum.photos/id/1080/300/300');