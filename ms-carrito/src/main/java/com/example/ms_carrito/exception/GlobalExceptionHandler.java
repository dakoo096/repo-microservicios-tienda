package com.example.ms_carrito.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ProductoServiceUnavailableException.class)
    public ResponseEntity<String> manejarProductoCaido(ProductoServiceUnavailableException e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> manejarRuntimeException(RuntimeException e) {
        String message = e.getMessage();
        if (message != null && message.contains("no encontrado")) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(message);
        }
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error de ejecución: " + (message != null ? message : e.toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> manejarCualquierError(Exception e) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error inesperado: " + (e.getMessage() != null ? e.getMessage() : e.toString()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<String> manejarThrowable(Throwable t) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error fatal (Throwable): " + (t.getMessage() != null ? t.getMessage() : t.toString()));
    }
}
