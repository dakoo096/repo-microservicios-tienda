import { defineStore } from "pinia";
import type { Producto } from "../types/types";
import {
  agregarProducto,
  quitarProducto,
  crearCarrito,
  traerCarrito,
  traerCarritoDelUsuario,
} from "../Carrito/services/carrito.service";
import { getProductoByCodigo } from "../Productos/services/productos.service";
import { crearVenta } from "../Ventas/ventas.service";

export const useCartStore = defineStore("cart", {
  state: () => ({
    carritoId: localStorage.getItem("cart_id")
      ? Number(localStorage.getItem("cart_id"))
      : null,
    productos: [] as Producto[],
  }),

  getters: {
    productosAgrupados: (state) => {
      const mapa = state.productos.reduce(
        (acc, producto) => {
          if (!acc[producto.codigo])
            acc[producto.codigo] = { ...producto, cantidad: 0 };
          (acc[producto.codigo] as Producto & { cantidad: number }).cantidad++;
          return acc;
        },
        {} as Record<number, Producto & { cantidad: number }>,
      );
      return Object.values(mapa);
    },
    totalItems: (state) => state.productos.length,
    total: (state) =>
      state.productos.reduce((sum, item) => sum + item.precio, 0),
  },

  actions: {
    async fetchCarrito() {
      try {
        const personaId = localStorage.getItem("personaId");
        let res;

        if (personaId && !isNaN(Number(personaId))) {
          // Si hay persona logueada, preferimos su carrito
          res = await traerCarritoDelUsuario(Number(personaId));
        } else if (this.carritoId) {
          // Si no, usamos el carritoId local
          res = await traerCarrito(this.carritoId);
        } else {
          return;
        }

        if (!res) {
          this.clearLocalData();
          return;
        }

        // Sincronizamos el carritoId por si cambió (ej: el usuario se logueó y recuperó uno viejo)
        this.carritoId = res.id;
        localStorage.setItem("cart_id", this.carritoId.toString());

        const productosCompletos = await Promise.all(
          res.items.map(async (item: any) => {
            try {
              const prod = await getProductoByCodigo(item.productoId);
              return { ...prod, cantidad: item.cantidad };
            } catch (e) {
              console.error("Error hidratando producto", item.productoId);
              return null;
            }
          }),
        );

        this.productos = productosCompletos
          .filter(Boolean)
          .flatMap((p: any) => {
            const baseProduct = {
              codigo: p.codigo,
              nombre: p.nombre,
              marca: p.marca,
              precio: p.precio,
              stock: p.stock,
              descripcion: p.descripcion,
              urlImagen: p.url_imagen || p.urlImagen,
            };
            return Array(p.cantidad).fill(baseProduct);
          });
      } catch (error: any) {
        console.error("Error al sincronizar el carrito:", error);

        if (error.response && error.response.data) {
          console.error("Detalle del servidor:", error.response.data);
        }

        // Si el error es 404 (gracias a la corrección del backend) o 500, limpiamos
        if (error.response?.status === 404 || error.response?.status === 500) {
          this.clearLocalData();
        }
      }
    },

    async addItem(producto: Producto) {
      if (!this.carritoId) {
        const nuevoCarrito = await crearCarrito();
        this.carritoId = nuevoCarrito.id;
        if (this.carritoId !== null) {
          localStorage.setItem("cart_id", this.carritoId.toString());
        }
      }
      await agregarProducto(this.carritoId!, producto.codigo);
      await this.fetchCarrito();
    },

    async removeItem(productoId: number) {
      if (!this.carritoId) return;
      await quitarProducto(this.carritoId, productoId);
      await this.fetchCarrito();
    },

    async cambiarCantidad(codigo: number, accion: "sumar" | "restar") {
      if (!this.carritoId) return;
      if (accion === "sumar") {
        await agregarProducto(this.carritoId, codigo);
      } else {
        await quitarProducto(this.carritoId, codigo);
      }
      await this.fetchCarrito();
    },

    async checkout() {
      if (!this.carritoId || this.productos.length === 0) return false;
      try {
        await crearVenta(this.carritoId);
        this.clearLocalData();
        return true;
      } catch (error) {
        console.error("Error en checkout:", error);
        return false;
      }
    },

    clearLocalData() {
      localStorage.removeItem("cart_id");
      this.carritoId = null;
      this.productos = [];
    },
  },
});
