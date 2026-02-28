package com.example.ms_ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VentaDetalleDTO {

    private Long id;
    private LocalDate fecha;
    private Long carritoId;
    private Double total;
    private List<ProductoVentaDTO> productos;
}

