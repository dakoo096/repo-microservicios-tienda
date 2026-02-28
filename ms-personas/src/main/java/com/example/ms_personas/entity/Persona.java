package com.example.ms_personas.entity;

import jakarta.persistence.*;
import lombok.*;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(unique = true)
    private String dni;

    @Column(unique = true)
    private String username;

    private String password;

    private String calle;
    private String numero;
    private String localidad;
    private String departamento;
    private String provincia;
    private String pais;
    private String email;

    @Enumerated(EnumType.STRING)
    private Rol rol;
}