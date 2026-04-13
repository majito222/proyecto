package co.edu.uniquindio.proyecto.infrastructure.exception;

import co.edu.uniquindio.proyecto.application.dto.response.ErrorDetailResponse;
import co.edu.uniquindio.proyecto.application.dto.response.ErrorResponse;
import co.edu.uniquindio.proyecto.domain.exception.ReglaDominioException;
import co.edu.uniquindio.proyecto.domain.exception.RolNoAutorizadoException;
import co.edu.uniquindio.proyecto.domain.exception.SolicitudCerradaException;
import co.edu.uniquindio.proyecto.domain.exception.TransicionEstadoInvalidaException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ReglaDominioException.class)
    public ResponseEntity<ErrorResponse> handleReglaDominio(ReglaDominioException ex,
                                                            HttpServletRequest request) {
        log.warn("Error de dominio: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError("DOMINIO_ERROR", ex.getMessage(), HttpStatus.BAD_REQUEST, request));
    }

    @ExceptionHandler(RolNoAutorizadoException.class)
    public ResponseEntity<ErrorResponse> handleRolNoAutorizado(RolNoAutorizadoException ex,
                                                               HttpServletRequest request) {
        log.warn("Error de autorizacion: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(buildError("NO_AUTORIZADO", ex.getMessage(), HttpStatus.FORBIDDEN, request));
    }

    @ExceptionHandler(SolicitudCerradaException.class)
    public ResponseEntity<ErrorResponse> handleSolicitudCerrada(SolicitudCerradaException ex,
                                                                HttpServletRequest request) {
        log.warn("Solicitud cerrada: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError("SOLICITUD_CERRADA", ex.getMessage(), HttpStatus.CONFLICT, request));
    }

    @ExceptionHandler(TransicionEstadoInvalidaException.class)
    public ResponseEntity<ErrorResponse> handleTransicionInvalida(TransicionEstadoInvalidaException ex,
                                                                  HttpServletRequest request) {
        log.warn("Transicion invalida: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(buildError("TRANSICION_INVALIDA", ex.getMessage(), HttpStatus.CONFLICT, request));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NoSuchElementException ex,
                                                        HttpServletRequest request) {
        log.warn("Elemento no encontrado: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildError("NO_ENCONTRADO", ex.getMessage(), HttpStatus.NOT_FOUND, request));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex,
                                                          HttpServletRequest request) {
        List<ErrorDetailResponse> detalles = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorDetailResponse(error.getField(), error.getDefaultMessage()))
                .toList();

        String mensaje = detalles.stream()
                .map(error -> error.campo() + ": " + error.mensaje())
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Error de validacion");

        log.warn("Error de validacion: {}", mensaje);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildError("VALIDATION_ERROR", mensaje, HttpStatus.BAD_REQUEST, request, detalles));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex,
                                                       HttpServletRequest request) {
        log.error("Error inesperado", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError("INTERNAL_ERROR", "Error interno del servidor",
                        HttpStatus.INTERNAL_SERVER_ERROR, request));
    }

    private ErrorResponse buildError(String codigo,
                                     String mensaje,
                                     HttpStatus status,
                                     HttpServletRequest request) {
        return new ErrorResponse(
                codigo,
                mensaje,
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI()
        );
    }

    private ErrorResponse buildError(String codigo,
                                     String mensaje,
                                     HttpStatus status,
                                     HttpServletRequest request,
                                     List<ErrorDetailResponse> detalles) {
        return new ErrorResponse(
                codigo,
                mensaje,
                status.value(),
                status.getReasonPhrase(),
                request.getRequestURI(),
                detalles
        );
    }
}
