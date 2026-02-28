package com.example.ms_personas.controller;

import com.example.ms_personas.dto.ActualizarPersonaDTO;
import com.example.ms_personas.dto.LoginRequest;
import com.example.ms_personas.dto.PersonaLoginResponse;
import com.example.ms_personas.dto.VentaDTO;
import com.example.ms_personas.entity.Persona;
import com.example.ms_personas.service.PersonaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/personas")
@RequiredArgsConstructor
public class PersonaController {

    private final PersonaService personaService;

    @PostMapping
    public Persona crear(
            @RequestBody Persona persona,
            @RequestHeader(value = "X-Rol", required = false) String rolHeader) {

        return personaService.crear(persona, rolHeader);
    }

    @PostMapping("/login")
    public PersonaLoginResponse login(@RequestBody LoginRequest request) {

        Persona persona = personaService.validarLogin(request);

        System.out.println("LOGIN OK → ID: " + persona.getId());

        return new PersonaLoginResponse(
                persona.getId(),
                persona.getUsername(),
                persona.getRol().name()
        );
    }


    @GetMapping
    public List<Persona> listar() {
        return personaService.listar();
    }

    @GetMapping("/{id}")
    public Persona buscar(@PathVariable Long id) {
        return personaService.buscarPorId(id);
    }

    @GetMapping("/compras")
    public List<VentaDTO> obtenerCompras(
            @RequestHeader("X-Persona-Id") Long personaId) {

        return personaService.obtenerCompras(personaId);
    }
    @PutMapping("/me")
    public Persona actualizar(
            @RequestHeader("X-Persona-Id") Long personaId,
            @RequestBody ActualizarPersonaDTO dto) {

        return personaService.actualizar(personaId, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(
            @PathVariable Long id,
            @RequestHeader(value = "X-Rol", required = false) String rolHeader) {

        if (!"ADMIN".equals(rolHeader)) {
            throw new RuntimeException("No autorizado");
        }

        personaService.eliminar(id);
    }
}
