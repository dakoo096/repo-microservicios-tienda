package com.example.ms_personas.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VentaDTO {

    private Long id;
    private LocalDate fecha;
    private Long carritoId;
    private Long personaId;
    private Double total;
}