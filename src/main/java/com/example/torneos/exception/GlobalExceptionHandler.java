package com.example.torneos.exception;

import com.example.torneos.dto.GeneralResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GeneralResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(GeneralResponse.builder()
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<GeneralResponse> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(GeneralResponse.builder()
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GeneralResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GeneralResponse.builder()
                .message(ex.getMessage())
                .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<GeneralResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(GeneralResponse.builder()
                .data(errors)
                .message("Error de validación en los campos")
                .build());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<GeneralResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(GeneralResponse.builder()
                .message("Credenciales inválidas")
                .build());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<GeneralResponse> handleAccessDenied(AccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(GeneralResponse.builder()
                .message("No tienes permisos para acceder a este recurso")
                .build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GeneralResponse> handleGlobalException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(GeneralResponse.builder()
                .message("Ocurrió un error inesperado en el servidor: " + ex.getMessage())
                .build());
    }
}
