package com.example.ms_carrito.service;

import com.example.ms_carrito.dto.CarritoDTO;
import com.example.ms_carrito.dto.ItemCarritoDTO;
import com.example.ms_carrito.dto.ProductoDTO;
import com.example.ms_carrito.entity.Carrito;
import com.example.ms_carrito.exception.ProductoServiceUnavailableException;
import com.example.ms_carrito.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class CarritoService implements ICarritoService {

    @Autowired
    private CarritoRepository carritoRepo;

    @Autowired
    private ProductoClientService productoClient;

    @Override
    @Transactional
    public CarritoDTO crearCarritoDTO() {
        Carrito carrito = new Carrito();
        carrito.setProductosIds(new ArrayList<>());
        carrito.setTotal(0.0);

        carritoRepo.save(carrito);
        return mapToDTO(carrito);
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoDTO traerCarritoDTO(Long id) {
        Carrito carrito = carritoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));
        return mapToDTO(carrito);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarritoDTO> traerCarritosDTO() {
        return carritoRepo.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public void agregarProducto(Long carritoId, Long productoId) {
        Carrito carrito = carritoRepo.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        ProductoDTO producto = productoClient.obtenerProducto(productoId);

        if (producto == null || producto.getPrecio() == null) {
            throw new ProductoServiceUnavailableException(
                    "No se puede agregar el producto: servicio de productos caído"
            );
        }

        if (carrito.getProductosIds() == null) carrito.setProductosIds(new ArrayList<>());
        carrito.getProductosIds().add(productoId);
        carrito.setTotal(Optional.ofNullable(carrito.getTotal()).orElse(0.0) + producto.getPrecio());

        carritoRepo.save(carrito);
    }

    @Override
    @Transactional
    public void quitarProducto(Long carritoId, Long productoId) {
        Carrito carrito = carritoRepo.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        ProductoDTO producto = productoClient.obtenerProducto(productoId);

        if (carrito.getProductosIds() != null && carrito.getProductosIds().contains(productoId)) {
            carrito.getProductosIds().remove(productoId);
            carrito.setTotal(Optional.ofNullable(carrito.getTotal()).orElse(0.0) -
                    (producto != null ? producto.getPrecio() : 0.0));
            carritoRepo.save(carrito);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductosDelCarrito(Long carritoId) {
        Carrito carrito = carritoRepo.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        List<ProductoDTO> productos = new ArrayList<>();
        if (carrito.getProductosIds() != null) {
            for (Long productoId : carrito.getProductosIds()) {
                try {
                    productos.add(productoClient.obtenerProducto(productoId));
                } catch (Exception e) {
                    productos.add(null); // fallback
                }
            }
        }
        return productos;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CarritoDTO> traerTodosDTO() {
        return carritoRepo.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    @Transactional
    public CarritoDTO crearCarritoDTOParaPersona(Long personaId) {
        Carrito carrito = new Carrito();
        carrito.setProductosIds(new ArrayList<>());
        carrito.setTotal(0.0);
        carrito.setPersonaId(personaId);

        carritoRepo.save(carrito);
        return mapToDTO(carrito);
    }

    @Override
    @Transactional(readOnly = true)
    public CarritoDTO traerCarritoPorPersona(Long personaId) {
        return carritoRepo.findByPersonaId(personaId)
                .map(this::mapToDTO)
                .orElse(null);
    }

    /* ======================
       MAPPER PRIVADO
       ====================== */
    private CarritoDTO mapToDTO(Carrito carrito) {
        Map<Long, Integer> contador = new HashMap<>();

        if (carrito.getProductosIds() != null) {
            for (Long productoId : carrito.getProductosIds()) {
                contador.put(productoId, contador.getOrDefault(productoId, 0) + 1);
            }
        }

        List<ItemCarritoDTO> items = new ArrayList<>();
        for (var entry : contador.entrySet()) {
            items.add(new ItemCarritoDTO(entry.getKey(), entry.getValue()));
        }

        return new CarritoDTO(
                carrito.getId(),
                carrito.getPersonaId(),
                items,
                Optional.ofNullable(carrito.getTotal()).orElse(0.0)
        );
    }
}