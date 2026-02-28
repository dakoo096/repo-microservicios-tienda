package com.example.ms_personas.repository;

import com.example.ms_personas.entity.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByUsername(String username);
}
