<?php
// GET ver.php?id=3 → una película en JSON
require_once __DIR__ . '/datos/ConexionBD.php';

header('Content-Type: application/json; charset=utf-8');

// TODO 1: lee el parámetro id de la URL con filter_var(..., FILTER_VALIDATE_INT).
//         Si no es un número: código 400, el error ERROR_FALTA_ID en JSON y exit.

try {
    $bd = new ConexionBD();

    // TODO 2: consulta preparada con prepare('SELECT … WHERE id = ?'),
    //         execute([$id]) y fetch() para leer una sola fila.

    // TODO 3: si fetch() devuelve false: código 404 y el error ERROR_NO_EXISTE en JSON.
    //         Si no, la fila con json_encode, igual que en listar.php.

} catch (PDOException $e) {
    http_response_code(500);
    echo json_encode(['error' => ERROR_CONEXION], JSON_UNESCAPED_UNICODE);
}
