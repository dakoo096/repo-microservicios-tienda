package com.example.ms_ventas.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoVentaDTO {

    private Long codigo;
    private String nombre;
    private Double precio;
    private int cantidad;
}
