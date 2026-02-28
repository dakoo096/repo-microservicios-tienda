package com.example.ms_personas.service;

import com.example.ms_personas.dto.ActualizarPersonaDTO;
import com.example.ms_personas.dto.LoginRequest;
import com.example.ms_personas.dto.VentaDTO;
import com.example.ms_personas.entity.Persona;
import com.example.ms_personas.entity.Rol;
import com.example.ms_personas.feign.VentasClient;
import com.example.ms_personas.repository.PersonaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PersonaService {

    private final PersonaRepository personaRepository;
    private final VentasClient ventasClient;
    private final PasswordEncoder passwordEncoder;

    public Persona crear(Persona persona, String rolHeader) {

        // 🔐 Si no es admin quien crea, forzamos CLIENTE
        if (!"ADMIN".equals(rolHeader)) {
            persona.setRol(Rol.CLIENTE);
        }

        persona.setPassword(passwordEncoder.encode(persona.getPassword()));
        return personaRepository.save(persona);
    }

    public Persona validarLogin(LoginRequest request) {

        Persona persona = personaRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), persona.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        return persona;
    }

    public List<VentaDTO> obtenerCompras(Long personaId) {
        return ventasClient.obtenerVentasPorPersona(personaId);
    }

    public List<Persona> listar() {
        return personaRepository.findAll();
    }

    public Persona buscarPorId(Long id) {
        return personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));
    }

    public Persona actualizar(Long personaId, ActualizarPersonaDTO dto) {

        Persona persona = personaRepository.findById(personaId)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        persona.setNombre(dto.getNombre());
        persona.setApellido(dto.getApellido());
        persona.setCalle(dto.getCalle());
        persona.setNumero(dto.getNumero());
        persona.setLocalidad(dto.getLocalidad());
        persona.setDepartamento(dto.getDepartamento());
        persona.setProvincia(dto.getProvincia());
        persona.setPais(dto.getPais());
        persona.setEmail(dto.getEmail());

        return personaRepository.save(persona);
    }

    public void eliminar(Long id) {

        Persona persona = personaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Persona no encontrada"));

        personaRepository.delete(persona);
    }
}