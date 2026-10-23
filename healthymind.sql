DROP DATABASE IF EXISTS healthymind;
CREATE DATABASE healthymind;
USE healthymind;

CREATE TABLE rango (
    id_rango INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL,
    nivel INT NOT NULL
);

CREATE TABLE categoria (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    descripcion VARCHAR(255)
);

CREATE TABLE logro (
    id_logro INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    descripcion VARCHAR(255),
    criterio VARCHAR(255)
);

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    edad INT NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    nacionalidad VARCHAR(50),
    telefono VARCHAR(20),
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo_usuario VARCHAR(30) NOT NULL,
    id_rango INT,
    FOREIGN KEY (id_rango) REFERENCES rango (id_rango) ON DELETE SET NULL
);

CREATE TABLE pqr (
    id_pqr INT AUTO_INCREMENT PRIMARY KEY,
    tipo VARCHAR(30) NOT NULL,
    descripcion TEXT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'PENDIENTE',
    id_usuario INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE CASCADE
);

CREATE TABLE notificacion (
    id_notificacion INT AUTO_INCREMENT PRIMARY KEY,
    mensaje VARCHAR(255) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    leida BOOLEAN NOT NULL DEFAULT FALSE,
    id_usuario INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE CASCADE
);

CREATE TABLE foro (
    id_foro INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) NOT NULL DEFAULT 'ABIERTO',
    id_usuario INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE CASCADE
);

CREATE TABLE comentario (
    id_comentario INT AUTO_INCREMENT PRIMARY KEY,
    contenido TEXT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    id_usuario INT NOT NULL,
    id_foro INT NOT NULL,
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_foro) REFERENCES foro (id_foro) ON DELETE CASCADE
);

CREATE TABLE actividad (
    id_actividad INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    descripcion TEXT,
    fecha_publicacion DATE,
    tiempo FLOAT,
    estado VARCHAR(30) NOT NULL DEFAULT 'ACTIVA',
    id_categoria INT NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria (id_categoria)
);

CREATE TABLE usuario_actividad (
    id_usuario_actividad INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_actividad INT NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'EN_PROGRESO',
    resultado VARCHAR(100),
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_actividad) REFERENCES actividad (id_actividad) ON DELETE CASCADE
);

CREATE TABLE usuario_logro (
    id_usuario_logro INT AUTO_INCREMENT PRIMARY KEY,
    id_usuario INT NOT NULL,
    id_logro INT NOT NULL,
    fecha_desbloqueo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_usuario) REFERENCES usuario (id_usuario) ON DELETE CASCADE,
    FOREIGN KEY (id_logro) REFERENCES logro (id_logro) ON DELETE CASCADE
);

INSERT INTO rango (nombre, nivel) VALUES
    ('Principiante', 1),
    ('Intermedio', 2),
    ('Avanzado', 3);

INSERT INTO categoria (nombre, descripcion) VALUES
    ('Memoria', 'Juegos para ejercitar la memoria'),
    ('Relajacion', 'Actividades para reducir el estres'),
    ('Concentracion', 'Ejercicios para mejorar el enfoque');

INSERT INTO logro (nombre, descripcion, criterio) VALUES
    ('Primer paso', 'Completaste tu primera actividad', 'Finalizar 1 actividad'),
    ('Constante', 'Completaste 10 actividades', 'Finalizar 10 actividades');

INSERT INTO actividad (titulo, descripcion, fecha_publicacion, tiempo, id_categoria) VALUES
    ('Memoria de parejas', 'Encuentra las parejas de cartas', CURDATE(), 5, 1),
    ('Respiracion guiada', 'Ejercicio de respiracion de 3 minutos', CURDATE(), 3, 2);

USE healthymind;
SHOW TABLES;