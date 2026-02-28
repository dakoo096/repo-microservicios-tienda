package com.example.ms_carrito.service;

import com.example.ms_carrito.dto.ProductoDTO;
import com.example.ms_carrito.feign.ProductoFeignClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoClientService {

    @Autowired
    private ProductoFeignClient productoFeign;

    @CircuitBreaker(name = "productosCB", fallbackMethod = "fallbackProducto")
    public ProductoDTO obtenerProducto(Long productoId){
        return productoFeign.obtenerProducto(productoId);
    }

    // ❌ SIN @Autowired
    public ProductoDTO fallbackProducto(Long productoId, Throwable e){
        return new ProductoDTO(
                productoId,
                "Producto no disponible",
                "Servicio caído",
                0.0
        );
    }

    public List<ProductoDTO> obtenerProductos(){
        return productoFeign.obtenerProductos();
    }

}
