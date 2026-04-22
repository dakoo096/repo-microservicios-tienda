package com.example.ms_carrito.service;

import com.example.ms_carrito.dto.ProductoDTO;
import com.example.ms_carrito.entity.Carrito;
import com.example.ms_carrito.entity.ItemCarrito;
import com.example.ms_carrito.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepo;

    @Mock
    private ProductoClientService productoClient;

    @InjectMocks
    private CarritoService carritoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAgregarProductoNuevo() {
        // Arrange
        Long carritoId = 1L;
        Long productoId = 100L;
        Carrito carrito = new Carrito();
        carrito.setId(carritoId);
        carrito.setItems(new ArrayList<>());
        carrito.setTotal(0.0);

        ProductoDTO producto = new ProductoDTO();
        producto.setId(productoId);
        producto.setPrecio(50.0);

        when(carritoRepo.findById(carritoId)).thenReturn(Optional.of(carrito));
        when(productoClient.obtenerProducto(productoId)).thenReturn(producto);

        // Act
        carritoService.agregarProducto(carritoId, productoId);

        // Assert
        assertEquals(1, carrito.getItems().size());
        assertEquals(50.0, carrito.getTotal());
        assertEquals(1, carrito.getItems().get(0).getCantidad());
        verify(carritoRepo, times(1)).save(carrito);
    }

    @Test
    void testAgregarProductoExistente() {
        // Arrange
        Long carritoId = 1L;
        Long productoId = 100L;
        Carrito carrito = new Carrito();
        carrito.setId(carritoId);
        ItemCarrito item = new ItemCarrito();
        item.setProductoId(productoId);
        item.setCantidad(1);
        item.setPrecioUnitario(50.0);
        item.setCarrito(carrito);
        
        List<ItemCarrito> items = new ArrayList<>();
        items.add(item);
        carrito.setItems(items);
        carrito.setTotal(50.0);

        ProductoDTO producto = new ProductoDTO();
        producto.setId(productoId);
        producto.setPrecio(50.0);

        when(carritoRepo.findById(carritoId)).thenReturn(Optional.of(carrito));
        when(productoClient.obtenerProducto(productoId)).thenReturn(producto);

        // Act
        carritoService.agregarProducto(carritoId, productoId);

        // Assert
        assertEquals(1, carrito.getItems().size());
        assertEquals(100.0, carrito.getTotal());
        assertEquals(2, carrito.getItems().get(0).getCantidad());
    }

    @Test
    void testQuitarProducto() {
        // Arrange
        Long carritoId = 1L;
        Long productoId = 100L;
        Carrito carrito = new Carrito();
        carrito.setId(carritoId);
        ItemCarrito item = new ItemCarrito();
        item.setProductoId(productoId);
        item.setCantidad(2);
        item.setPrecioUnitario(50.0);
        item.setCarrito(carrito);

        List<ItemCarrito> items = new ArrayList<>();
        items.add(item);
        carrito.setItems(items);
        carrito.setTotal(100.0);

        when(carritoRepo.findById(carritoId)).thenReturn(Optional.of(carrito));

        // Act
        carritoService.quitarProducto(carritoId, productoId);

        // Assert
        assertEquals(1, carrito.getItems().size());
        assertEquals(50.0, carrito.getTotal());
        assertEquals(1, carrito.getItems().get(0).getCantidad());
    }
}
