import api from "../../services/api";
import type { Producto } from "../../types/types";

const API_URL = "/ms-productos/productos";

export const getProductos = async () => {
  const res = await api.get(API_URL);
  return res.data.map((p: any) => ({
    ...p,
    // La propiedad que devuelve el backend ya incluye /productos/imagenes/
    urlImagen: p.urlImagen,
  }));
};

export const getProductoByCodigo = async (codigo: number): Promise<Producto> => {
  const res = await api.get(`${API_URL}/${codigo}`);
  return res.data;
};

export const crearProducto = async (producto: Producto): Promise<Producto> => {
  const { codigo, ...datos } = producto;
  const res = await api.post(API_URL, datos);
  // 🔹 Retorna el producto con el ID generado por el backend
  return res.data;
};

export const actualizarProducto = async (codigo: number, producto: Producto): Promise<Producto> => {
  const res = await api.put(`${API_URL}/${codigo}`, producto);
  return res.data;
};

export const eliminarProducto = async (codigo: number) => {
  await api.delete(`${API_URL}/${codigo}`);
};

// -------------------------------------------
// NUEVO: Subir imagen de producto
export const subirImagenProducto = async (codigo: number, file: File) => {
  const formData = new FormData();
  formData.append("file", file);

  // 🔹 Endpoint correcto para la imagen
  const res = await api.post(`${API_URL}/${codigo}/imagen`, formData, {
    headers: { "Content-Type": "multipart/form-data" },
  });

  // 🔹 Devuelve el producto actualizado con urlImagen que apunta al endpoint real
  return res.data;
};