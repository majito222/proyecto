package co.edu.uniquindio.proyecto.infrastructure.controller;

import co.edu.uniquindio.proyecto.domain.exception.*;
import co.edu.uniquindio.proyecto.infrastructure.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;

/**
 * Manejador global de excepciones para la API REST.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Maneja excepciones de dominio.
     */
    @ExceptionHandler(ReglaDominioException.class)
    public ResponseEntity<ErrorResponse> handleReglaDominio(ReglaDominioException ex) {
        log.warn("Error de dominio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(ex.getMessage(), "DOMINIO_ERROR"));
    }

    /**
     * Maneja excepciones de rol no autorizado.
     */
    @ExceptionHandler(RolNoAutorizadoException.class)
    public ResponseEntity<ErrorResponse> handleRolNoAutorizado(RolNoAutorizadoException ex) {
        log.warn("Error de autorización: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
            .body(new ErrorResponse(ex.getMessage(), "NO_AUTORIZADO"));
    }

    /**
     * Maneja excepciones de solicitud cerrada.
     */
    @ExceptionHandler(SolicitudCerradaException.class)
    public ResponseEntity<ErrorResponse> handleSolicitudCerrada(SolicitudCerradaException ex) {
        log.warn("Solicitud cerrada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(ex.getMessage(), "SOLICITUD_CERRADA"));
    }

    /**
     * Maneja excepciones de transición de estado inválida.
     */
    @ExceptionHandler(TransicionEstadoInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleTransicionInvalida(TransicionEstadoInvalidaException ex) {
        log.warn("Transición inválida: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse(ex.getMessage(), "TRANSICION_INVALIDA"));
    }

    /**
     * Maneja excepciones de elemento no encontrado.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex) {
        log.warn("Elemento no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(ex.getMessage(), "NO_ENCONTRADO"));
    }

    /**
     * Maneja excepciones de validación de argumentos.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String mensaje = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .reduce((msg1, msg2) -> msg1 + "; " + msg2)
            .orElse("Error de validación");
        
        log.warn("Error de validación: {}", mensaje);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse(mensaje, "VALIDATION_ERROR"));
    }

    /**
     * Maneja excepciones generales.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("Error interno del servidor", "INTERNAL_ERROR"));
    }
}
