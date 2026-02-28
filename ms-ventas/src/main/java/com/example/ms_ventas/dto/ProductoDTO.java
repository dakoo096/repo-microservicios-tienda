package com.example.ms_ventas.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductoDTO {

    private Long codigo;
    private String nombre;
    private String marca;
    private Double precio;

}
