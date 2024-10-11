package ies.jandula.incidencia.utils;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public final class Constants {
    // Constantes de estado de incidencias
    public static final String EN_PROGRESO = "EN PROGRESO"; 
    public static final String CANCELADA = "CANCELADA"; 
    public static final String RESUELTA = "RESUELTA";
    public static final String PENDIENTE = "PENDIENTE";

    // Respuestas predefinidas para situaciones comunes
    public static final ResponseEntity<String> ID_NO_ENCONTRADO = ResponseEntity.status(HttpStatus.NOT_FOUND).body("ERROR: No existen incidencias con ese ID.");
    public static final ResponseEntity<String> ERROR_INESPERADO = ResponseEntity.internalServerError().body("Se ha producido un error inesperado.");

    // Constructor privado para evitar instanciación
    private Constants() {
        throw new UnsupportedOperationException("Esta clase es de constantes, no puede ser instanciada.");
    }
}
