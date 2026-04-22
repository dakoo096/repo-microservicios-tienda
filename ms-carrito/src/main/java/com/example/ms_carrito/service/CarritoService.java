package com.example.ms_carrito.service;

import com.example.ms_carrito.dto.CarritoDTO;
import com.example.ms_carrito.dto.ItemCarritoDTO;
import com.example.ms_carrito.dto.ProductoDTO;
import com.example.ms_carrito.entity.Carrito;
import com.example.ms_carrito.entity.ItemCarrito;
import com.example.ms_carrito.exception.ProductoServiceUnavailableException;
import com.example.ms_carrito.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

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
        carrito.setItems(new ArrayList<>());
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
                    "No se puede agregar el producto: servicio de productos caído o producto inválido");
        }

        if (carrito.getItems() == null) {
            carrito.setItems(new ArrayList<>());
        }

        Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                .filter(item -> item.getProductoId().equals(productoId))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemCarrito item = itemExistente.get();
            item.setCantidad(item.getCantidad() + 1);
            item.setPrecioUnitario(producto.getPrecio()); // Actualizar precio por si cambió
        } else {
            ItemCarrito nuevoItem = new ItemCarrito();
            nuevoItem.setProductoId(productoId);
            nuevoItem.setCantidad(1);
            nuevoItem.setPrecioUnitario(producto.getPrecio());
            nuevoItem.setCarrito(carrito);
            carrito.getItems().add(nuevoItem);
        }

        recalcularTotal(carrito);
        carritoRepo.save(carrito);
    }

    @Override
    @Transactional
    public void quitarProducto(Long carritoId, Long productoId) {
        Carrito carrito = carritoRepo.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        if (carrito.getItems() != null) {
            Optional<ItemCarrito> itemExistente = carrito.getItems().stream()
                    .filter(item -> item.getProductoId().equals(productoId))
                    .findFirst();

            if (itemExistente.isPresent()) {
                ItemCarrito item = itemExistente.get();
                if (item.getCantidad() > 1) {
                    item.setCantidad(item.getCantidad() - 1);
                } else {
                    carrito.getItems().remove(item);
                }
                recalcularTotal(carrito);
                carritoRepo.save(carrito);
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> obtenerProductosDelCarrito(Long carritoId) {
        Carrito carrito = carritoRepo.findById(carritoId)
                .orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        List<ProductoDTO> productos = new ArrayList<>();
        if (carrito.getItems() != null) {
            for (ItemCarrito item : carrito.getItems()) {
                for (int i = 0; i < item.getCantidad(); i++) {
                    try {
                        productos.add(productoClient.obtenerProducto(item.getProductoId()));
                    } catch (Exception e) {
                        productos.add(null);
                    }
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
        carrito.setItems(new ArrayList<>());
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

    private void recalcularTotal(Carrito carrito) {
        double total = 0.0;
        if (carrito.getItems() != null) {
            total = carrito.getItems().stream()
                    .mapToDouble(item -> item.getCantidad() * item.getPrecioUnitario())
                    .sum();
        }
        carrito.setTotal(total);
    }

    private CarritoDTO mapToDTO(Carrito carrito) {
        List<ItemCarritoDTO> itemsDTO = new ArrayList<>();
        if (carrito.getItems() != null) {
            itemsDTO = carrito.getItems().stream()
                    .map(item -> new ItemCarritoDTO(item.getProductoId(), item.getCantidad()))
                    .collect(Collectors.toList());
        }

        return new CarritoDTO(
                carrito.getId(),
                carrito.getPersonaId(),
                itemsDTO,
                Optional.ofNullable(carrito.getTotal()).orElse(0.0));
    }
}