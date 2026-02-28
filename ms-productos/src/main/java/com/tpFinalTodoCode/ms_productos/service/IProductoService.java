package com.tpFinalTodoCode.ms_productos.service;

import com.tpFinalTodoCode.ms_productos.entity.Producto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface IProductoService {

    List<Producto> traerProductos();

    Producto traerProducto(Long codigo);

    Producto guardarProducto(Producto producto);

    void eliminarProducto(Long codigo);

    void editarProducto(Producto producto);

    // Nuevo método para subir imagen
    Producto subirImagen(Long codigo, MultipartFile file) throws IOException;

    void descontarStock(Long codigo, Integer cantidad);
}