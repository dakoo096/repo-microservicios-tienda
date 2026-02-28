package com.example.ms_carrito.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Carrito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Guardamos los ids de productos, no los productos completos
    @ElementCollection
    private List<Long> productosIds;

    private Double total;

    // Guardamos solo el ID del usuario, no toda la entidad
    private Long personaId;
}