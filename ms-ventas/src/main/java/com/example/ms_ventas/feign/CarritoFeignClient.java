package com.example.ms_ventas.feign;


import com.example.ms_ventas.dto.CarritoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "ms-carrito")
public interface CarritoFeignClient {

    @GetMapping("/api/carritos/{id}")
    CarritoDTO obtenerCarrito(@PathVariable("id") Long id);
}

