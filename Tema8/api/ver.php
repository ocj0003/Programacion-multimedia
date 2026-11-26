<?php
// GET ver.php?id=3 → un videojuego en JSON
require_once __DIR__ . '/datos/ConexionBD.php';

header('Content-Type: application/json; charset=utf-8');

// Si el id no es un número, filter_var devuelve false
$id = filter_var($_GET['id'] ?? '', FILTER_VALIDATE_INT);
if ($id === false) {
    http_response_code(400);
    echo json_encode(['error' => ERROR_FALTA_ID], JSON_UNESCAPED_UNICODE);
    exit;
}

try {
    $bd = new ConexionBD();

    // Consulta preparada: el id va en el ?, no pegado dentro del SQL
    $consulta = $bd->getConexion()->prepare('SELECT id, titulo, plataforma, imagen FROM videojuegos WHERE id = ?');
    $consulta->execute([$id]);
    $fila = $consulta->fetch();

    // Si no hay ninguna fila con ese id, fetch devuelve false
    if ($fila === false) {
        http_response_code(404);
        echo json_encode(['error' => ERROR_NO_EXISTE], JSON_UNESCAPED_UNICODE);
    } else {
        echo json_encode($fila, JSON_UNESCAPED_UNICODE | JSON_UNESCAPED_SLASHES | JSON_PRETTY_PRINT);
    }
} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => ERROR_CONEXION], JSON_UNESCAPED_UNICODE);
}