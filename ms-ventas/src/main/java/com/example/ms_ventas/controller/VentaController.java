package com.example.ms_ventas.controller;

import com.example.ms_ventas.dto.CrearVentaDTO;
import com.example.ms_ventas.dto.VentaDetalleDTO;
import com.example.ms_ventas.entity.Venta;
import com.example.ms_ventas.service.IVentaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    @Autowired
    private IVentaService ventaService;

    @PostMapping
    public Venta crearVenta(
            @RequestHeader("X-Persona-Id") Long personaId,
            @RequestBody CrearVentaDTO dto) {
        return ventaService.crearVenta(dto.getCarritoId(), personaId);
    }

    @GetMapping("/mis-ventas")
    public List<Venta> traerMisVentas(@RequestHeader("X-Persona-Id") Long personaId) {
        return ventaService.traerVentasPorPersona(personaId);
    }

    // 👑 SOLO ADMIN
    @GetMapping
    public List<Venta> traerTodasLasVentas(@RequestHeader(value = "X-Rol", required = false) String rol) {
        // Ignoramos X-Persona-Id, solo permitimos admin
        if ("ADMIN".equalsIgnoreCase(rol)) {
            return ventaService.traerTodasLasVentas();
        }
        // Si no es admin, devolvemos vacío por seguridad
        return List.of();
    }

    @GetMapping("/{id}")
    public Venta traerVenta(@PathVariable Long id) {
        return ventaService.traerVenta(id);
    }

    @GetMapping("/detalle/{id}")
    public VentaDetalleDTO traerVentaDetalle(@PathVariable Long id) {
        return ventaService.traerVentaDetalle(id);
    }
}