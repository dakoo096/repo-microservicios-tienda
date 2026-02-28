import { defineStore } from "pinia";
import type { Producto } from "../types/types";
import {
  agregarProducto,
  quitarProducto,
  crearCarrito,
  traerCarrito,
} from "../Carrito/services/carrito.service";
import { getProductoByCodigo } from "../Productos/services/productos.service";

export const useCartStore = defineStore("cart", {
  state: () => ({
    carritoId: localStorage.getItem("cart_id")
      ? Number(localStorage.getItem("cart_id"))
      : null,
    productos: [] as Producto[],
  }),

  getters: {
    productosAgrupados: (state) => {
      const mapa = state.productos.reduce((acc, producto) => {
        if (!acc[producto.codigo]) acc[producto.codigo] = { ...producto, cantidad: 0 };
        (acc[producto.codigo] as Producto & { cantidad: number }).cantidad++;
        return acc;
      }, {} as Record<number, Producto & { cantidad: number }>);
      return Object.values(mapa);
    },
    totalItems: (state) => state.productos.length,
    total: (state) => state.productos.reduce((sum, item) => sum + item.precio, 0),
  },

  actions: {
    async fetchCarrito() {
      if (!this.carritoId) return;

      try {
        const res = await traerCarrito(this.carritoId);

        const productosCompletos = await Promise.all(
          res.items.map(async (item: any) => {
            try {
              const prod = await getProductoByCodigo(item.productoId);
              return { ...prod, cantidad: item.cantidad };
            } catch (e) {
              console.error("Error hidratando producto", item.productoId);
              return null;
            }
          })
        );

        this.productos = productosCompletos.filter(Boolean).map((p: any) => ({
            codigo: p.codigo,
            nombre: p.nombre,
            marca: p.marca,
            precio: p.precio,
            stock: p.stock,
            descripcion: p.descripcion,
            urlImagen: p.url_imagen,
          })
        );
      } catch (error) {
        console.error("Error al sincronizar el carrito:", error);
        this.clearLocalData();
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

    clearLocalData() {
      localStorage.removeItem("cart_id");
      this.carritoId = null;
      this.productos = [];
    },
  },
});