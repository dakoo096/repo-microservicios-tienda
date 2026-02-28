package com.example.ms_ventas.service;

import com.example.ms_ventas.dto.CarritoDTO;
import com.example.ms_ventas.dto.ItemCarritoDTO;
import com.example.ms_ventas.dto.ProductoDTO;
import com.example.ms_ventas.dto.ProductoVentaDTO;
import com.example.ms_ventas.dto.VentaDetalleDTO;
import com.example.ms_ventas.entity.Venta;
import com.example.ms_ventas.feign.CarritoFeignClient;
import com.example.ms_ventas.feign.ProductoFeignClient;
import com.example.ms_ventas.repository.VentaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class VentaService implements IVentaService {

    @Autowired
    private VentaRepository ventaRepo;

    @Autowired
    private CarritoFeignClient carritoFeign;

    @Autowired
    private ProductoFeignClient productoFeign;

    @Override
    public Venta crearVenta(Long carritoId, Long personaId) {

        CarritoDTO carrito = carritoFeign.obtenerCarrito(carritoId);

        if (carrito.getItems() == null || carrito.getItems().isEmpty()) {
            throw new RuntimeException("El carrito está vacío");
        }

        for (ItemCarritoDTO item : carrito.getItems()) {

            ProductoDTO producto = productoFeign.obtenerProducto(item.getProductoId());

            if (producto == null) {
                throw new RuntimeException("Producto no encontrado: " + item.getProductoId());
            }

            productoFeign.descontarStock(producto.getCodigo(), item.getCantidad());
        }

        Venta venta = new Venta();
        venta.setFecha(LocalDate.now());
        venta.setCarritoId(carritoId);
        venta.setPersonaId(personaId);
        venta.setTotal(carrito.getTotal());

        return ventaRepo.save(venta);
    }

    @Override
    public Venta traerVenta(Long id) {
        return ventaRepo.findById(id).orElse(null);
    }

    @Override
    public List<Venta> traerVentas() {
        return ventaRepo.findAll();
    }

    @Override
    public VentaDetalleDTO traerVentaDetalle(Long id) {

        Venta venta = ventaRepo.findById(id).orElse(null);
        if (venta == null) {
            return null;
        }

        CarritoDTO carrito = carritoFeign.obtenerCarrito(venta.getCarritoId());

        List<ProductoVentaDTO> productos = new ArrayList<>();

        for (ItemCarritoDTO item : carrito.getItems()) {

            ProductoDTO prod = productoFeign.obtenerProducto(item.getProductoId());

            productos.add(new ProductoVentaDTO(
                    prod.getCodigo(),
                    prod.getNombre(),
                    prod.getPrecio(),
                    item.getCantidad()
            ));
        }

        return new VentaDetalleDTO(
                venta.getId(),
                venta.getFecha(),
                venta.getCarritoId(),
                venta.getTotal(),
                productos
        );
    }

    @Override
    public List<Venta> traerVentasPorPersona(Long personaId) {
        return ventaRepo.findByPersonaId(personaId);
    }

    @Override
    public List<Venta> traerTodasLasVentas() {
        return ventaRepo.findAll();
    }

}
