package com.example.ms_carrito.service;

import com.example.ms_carrito.dto.CarritoDTO;
import com.example.ms_carrito.dto.ProductoDTO;

import java.util.List;

public interface ICarritoService {

    // Crear carrito genérico
    CarritoDTO crearCarritoDTO();

    // Crear carrito para un usuario específico
    CarritoDTO crearCarritoDTOParaPersona(Long personaId);

    // Traer carrito por ID
    CarritoDTO traerCarritoDTO(Long id);

    // Traer carrito de un usuario por personaId
    CarritoDTO traerCarritoPorPersona(Long personaId);

    // Traer todos los carritos
    List<CarritoDTO> traerCarritosDTO();

    // Traer todos los carritos (alias)
    List<CarritoDTO> traerTodosDTO();

    // Agregar producto a un carrito
    void agregarProducto(Long carritoId, Long productoId);

    // Quitar producto de un carrito
    void quitarProducto(Long carritoId, Long productoId);

    // Obtener los productos completos de un carrito
    List<ProductoDTO> obtenerProductosDelCarrito(Long carritoId);
}