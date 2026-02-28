package com.example.ms_carrito.feign;

import com.example.ms_carrito.dto.ProductoDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

//usamos el nombre del microservicio
@FeignClient(name = "ms-productos")
public interface ProductoFeignClient {

    //pasamos por eureka + load balancer
    @GetMapping("/productos/{codigo}")
    ProductoDTO obtenerProducto(@PathVariable("codigo") Long codigo);

    //traer una lista de los productos disponibles
        @GetMapping("/productos")
        List<ProductoDTO> obtenerProductos();


}

