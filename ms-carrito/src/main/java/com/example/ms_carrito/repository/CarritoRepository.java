package com.example.ms_carrito.repository;

import com.example.ms_carrito.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CarritoRepository extends JpaRepository<Carrito,Long> {
    Optional<Carrito> findByPersonaId(Long personaId);
}
