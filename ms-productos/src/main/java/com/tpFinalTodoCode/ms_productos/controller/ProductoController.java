package com.tpFinalTodoCode.ms_productos.controller;

import com.tpFinalTodoCode.ms_productos.entity.Producto;
import com.tpFinalTodoCode.ms_productos.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.List;

@RestController
@RequestMapping("/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoServ;

    @GetMapping
    public List<Producto> listarProductos(){
        return productoServ.traerProductos();
    }

    @GetMapping("/{codigo}")
    public Producto traerProducto(@PathVariable Long codigo){
        return productoServ.traerProducto(codigo);
    }

    @PostMapping
    public Producto guardarProducto(@RequestBody Producto producto){
        return productoServ.guardarProducto(producto);
    }

    @PutMapping("/{codigo}")
    public void editarProducto(@PathVariable Long codigo, @RequestBody Producto producto){
        producto.setCodigo(codigo);
        productoServ.editarProducto(producto);
    }

    @DeleteMapping("/{codigo}")
    public void eliminarProducto(@PathVariable Long codigo){
        productoServ.eliminarProducto(codigo);
    }

    @PostMapping("/{codigo}/imagen")
    public ResponseEntity<Producto> subirImagen(@PathVariable Long codigo, @RequestParam("file") MultipartFile file) throws IOException {
        Producto producto = productoServ.subirImagen(codigo, file);
        if (producto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(producto);
    }

    @GetMapping("/imagenes/{filename:.+}")
    public ResponseEntity<Resource> getImagen(@PathVariable String filename) throws IOException {
        Path file = Paths.get("uploads/" + filename);
        Resource resource = new UrlResource(file.toUri());
        if (resource.exists() || resource.isReadable()) {
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, Files.probeContentType(file))
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{codigo}/descontar")
    public void descontarStock(@PathVariable Long codigo,
                               @RequestParam Integer cantidad) {
        productoServ.descontarStock(codigo, cantidad);
    }
}