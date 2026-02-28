package com.example.ms_personas.dto;

import lombok.Data;

@Data
public class ActualizarPersonaDTO {

    private String nombre;
    private String apellido;
    private String email;
    private String calle;
    private String numero;
    private String localidad;
    private String departamento;
    private String provincia;
    private String pais;
}