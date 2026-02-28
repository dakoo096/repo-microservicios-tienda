package com.example.ms_carrito.controller;

import com.example.ms_carrito.dto.CarritoDTO;
import com.example.ms_carrito.service.ICarritoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carritos")
public class CarritoController {

    @Autowired
    private ICarritoService carritoServ;

    /**
     * Crear carrito genérico (opcional)
     */
    @PostMapping
    public CarritoDTO crearCarrito(){
        return carritoServ.crearCarritoDTO();
    }

    /**
     * Crear carrito para un usuario específico
     */
    @PostMapping("/usuario")
    public CarritoDTO crearCarritoParaUsuario(@RequestHeader("X-Persona-Id") Long personaId){
        return carritoServ.crearCarritoDTOParaPersona(personaId);
    }

    /**
     * Traer carrito por ID
     */
    @GetMapping("/{id}")
    public CarritoDTO traerCarrito(@PathVariable Long id) {
        return carritoServ.traerCarritoDTO(id);
    }

    /**
     * Traer carrito del usuario actual por personaId
     * Si no tiene carrito, se crea uno automáticamente
     */
    @GetMapping("/usuario")
    public CarritoDTO traerCarritoDelUsuario(@RequestHeader("X-Persona-Id") Long personaId){
        CarritoDTO carrito = carritoServ.traerCarritoPorPersona(personaId);
        if (carrito == null) {
            carrito = carritoServ.crearCarritoDTOParaPersona(personaId);
        }
        return carrito;
    }

    /**
     * Traer todos los carritos
     */
    @GetMapping
    public List<CarritoDTO> traerCarritos(){
        return carritoServ.traerTodosDTO();
    }

    /**
     * Agregar producto a un carrito
     */
    @PostMapping("/{carritoId}/agregar/{productoId}")
    public void agregarProducto(@PathVariable Long carritoId, @PathVariable Long productoId){
        carritoServ.agregarProducto(carritoId, productoId);
    }

    /**
     * Quitar producto de un carrito
     */
    @DeleteMapping("/{carritoId}/quitar/{productoId}")
    public void quitarProducto(@PathVariable Long carritoId, @PathVariable Long productoId){
        carritoServ.quitarProducto(carritoId, productoId);
    }
}