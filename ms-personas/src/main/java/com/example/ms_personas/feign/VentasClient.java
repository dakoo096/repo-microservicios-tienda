package com.example.ms_personas.feign;

import com.example.ms_personas.dto.VentaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "ms-ventas")
public interface VentasClient {

    @GetMapping("/ventas/persona/{personaId}")
    List<VentaDTO> obtenerVentasPorPersona(@PathVariable Long personaId);
}
