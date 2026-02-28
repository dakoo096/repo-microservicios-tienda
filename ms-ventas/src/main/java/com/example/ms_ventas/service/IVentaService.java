package com.example.ms_ventas.service;

import com.example.ms_ventas.dto.VentaDetalleDTO;
import com.example.ms_ventas.entity.Venta;

import java.util.List;

public interface IVentaService {

    Venta crearVenta(Long carritoId, Long personaId);

    Venta traerVenta(Long id);

    List<Venta> traerVentas();

    VentaDetalleDTO traerVentaDetalle(Long id);

    List<Venta> traerVentasPorPersona(Long personaId);

   List<Venta> traerTodasLasVentas();
}
