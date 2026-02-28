package com.example.ms_ventas.feign;

import com.example.ms_ventas.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "ms-productos")
public interface ProductoFeignClient {

    @GetMapping("/productos/{codigo}")
    ProductoDTO obtenerProducto(@PathVariable("codigo") Long codigo);

    @PutMapping("/productos/{codigo}/descontar")
    void descontarStock(@PathVariable("codigo") Long codigo,
                        @RequestParam("cantidad") Integer cantidad);
}
