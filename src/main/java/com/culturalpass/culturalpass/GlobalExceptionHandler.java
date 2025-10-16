package com.culturalpass.culturalpass;

import com.culturalpass.culturalpass.Event.exceptions.*;
import com.culturalpass.culturalpass.Security.exceptions.*;
import com.culturalpass.culturalpass.Statistic.exceptions.InvalidDateRangeException;
import com.culturalpass.culturalpass.User.exceptions.UserAlreadyRegisteredException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUserNotFound(UserNotFoundException ex) {
        logger.info("User not found: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.BAD_REQUEST, "Usuario no encontrado", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(AuthenticationFailedException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationFailed(AuthenticationFailedException ex) {
        logger.warn("Authentication failed for request: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.UNAUTHORIZED, "Error de autenticación", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        logger.info("Attempt to create existing user: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.CONFLICT, "Usuario ya existe", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleEventNotFound(EventNotFoundException ex) {
        logger.info("Event not found: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.NOT_FOUND, "Evento no encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(EventAlreadyExistsException.class)
    public ResponseEntity<Map<String, Object>> handleEventAlreadyExists(EventAlreadyExistsException ex) {
        logger.info("Attempt to create existing event: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.CONFLICT, "Evento ya existe", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<Map<String, Object>> handleUserAlreadyRegistered(UserAlreadyRegisteredException ex) {
        logger.info("User already registered in event: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.CONFLICT, "Usuario ya registrado en evento", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<Map<String, Object>> handleRegistrationException(RegistrationException ex) {
        logger.warn("Registration system error: {}", ex.getMessage(), ex);
        Map<String, Object> error = createErrorResponse(HttpStatus.BAD_REQUEST, "Error en el registro", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(AdminRegistrationNotAllowedException.class)
    public ResponseEntity<Map<String, Object>> handleAdminRegistrationNotAllowed(AdminRegistrationNotAllowedException ex) {
        logger.warn("Unauthorized admin registration attempt: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.FORBIDDEN, "Registro de administrador no permitido", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(MethodArgumentNotValidException ex) {
        if (logger.isDebugEnabled()) {
            logger.debug("Validation errors occurred: {}", ex.getMessage(), ex);
        }
        Map<String, String> validationErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((errorObj) -> {
            String fieldName = ((FieldError) errorObj).getField();
            String errorMessage = errorObj.getDefaultMessage();
            validationErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.BAD_REQUEST.value());
        error.put("error", "Errores de validación");
        error.put("validationErrors", validationErrors);

        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        logger.error("Unhandled exception occurred: {}", ex.getMessage(), ex);
        Map<String, Object> error = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Error interno del servidor",
                "Ha ocurrido un error inesperado"
        );
        return ResponseEntity.internalServerError().body(error);
    }

    @ExceptionHandler(MissingEventFieldException.class)
    public ResponseEntity<Map<String, Object>> handleMissingEventField(MissingEventFieldException ex) {
        logger.info("Missing event fields: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.BAD_REQUEST, "Campos obligatorios faltantes para evento", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTokenNotFound(TokenNotFoundException ex) {
        logger.info("Token not found: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.NOT_FOUND, "Token no encontrado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(TokenAlreadyValidatedException.class)
    public ResponseEntity<Map<String, Object>> handleTokenAlreadyValidated(TokenAlreadyValidatedException ex) {
        logger.info("Token already validated: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.CONFLICT, "Token ya validado", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(UserValidationException.class)
    public ResponseEntity<Map<String, Object>> handleUserValidation(UserValidationException ex) {
        logger.info("User validation error: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.BAD_REQUEST, "Error de validación de usuario", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(BadImageUploadException.class)
    public ResponseEntity<Map<String, Object>> handleBadImageUpload(BadImageUploadException ex) {
        logger.warn("Bad image upload: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.BAD_REQUEST, "Error al subir la imagen", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    @ExceptionHandler(InvalidDateRangeException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidDateRange(InvalidDateRangeException ex) {
        logger.info("Invalid date range for statistics: {}", ex.getMessage());
        Map<String, Object> error = createErrorResponse(HttpStatus.BAD_REQUEST, "Rango de fecha inválido", ex.getMessage());
        return ResponseEntity.badRequest().body(error);
    }

    private Map<String, Object> createErrorResponse(HttpStatus status, String error, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", status.value());
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        return errorResponse;
    }
}