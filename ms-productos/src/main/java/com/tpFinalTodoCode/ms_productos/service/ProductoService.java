package com.tpFinalTodoCode.ms_productos.service;

import com.tpFinalTodoCode.ms_productos.entity.Producto;
import com.tpFinalTodoCode.ms_productos.repository.IProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.io.IOException;
import java.util.List;

@Service
public class ProductoService implements IProductoService {

    @Autowired
    private IProductoRepository productoRepo;

    private final Path UPLOAD_DIR = Paths.get(System.getProperty("user.dir"), "uploads");

    @Override
    public List<Producto> traerProductos() {
        return productoRepo.findAll();
    }

    @Override
    public Producto traerProducto(Long codigo) {
        return productoRepo.findById(codigo).orElse(null);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        productoRepo.save(producto);
        return producto;
    }

    @Override
    public void eliminarProducto(Long codigo) {
        productoRepo.deleteById(codigo);
    }

    @Override
    public void editarProducto(Producto producto) {
        this.guardarProducto(producto);
    }

    // Nuevo método para subir imagen

    @Override
    public Producto subirImagen(Long codigo, MultipartFile file) throws IOException {
        Producto producto = traerProducto(codigo);
        if (producto == null) return null;

        Files.createDirectories(UPLOAD_DIR); // asegura que exista

        String nombreArchivo = "producto_" + codigo + "_" + System.currentTimeMillis() + ".png";
        Path filePath = UPLOAD_DIR.resolve(nombreArchivo);

        Files.write(filePath, file.getBytes());

        producto.setUrlImagen("/productos/imagenes/" + nombreArchivo);
        return productoRepo.save(producto);
    }

    @Override
    @Transactional
    public void descontarStock(Long codigo, Integer cantidad) {
        Producto producto = productoRepo.findById(codigo)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepo.save(producto);
    }
}
