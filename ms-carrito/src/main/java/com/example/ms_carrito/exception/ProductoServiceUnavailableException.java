package com.example.ms_carrito.exception;

public class ProductoServiceUnavailableException extends RuntimeException {

    public ProductoServiceUnavailableException(String mensaje) {
        super(mensaje);
    }
}
