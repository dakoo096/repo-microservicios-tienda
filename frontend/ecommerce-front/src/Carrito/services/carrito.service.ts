import axios from "../../services/axiosConfig";
import type { CarritoDTO } from "../../types/types";

const API_URL = "/ms-carrito/api/carritos";

// Crear un carrito vacío (genérico)
export const crearCarrito = async (): Promise<CarritoDTO> => {
  const { data } = await axios.post(API_URL);
  return data;
};

// Crear carrito para el usuario actual
export const crearCarritoParaUsuario = async (personaId: number): Promise<CarritoDTO> => {
  const { data } = await axios.post(`${API_URL}/usuario`, null, {
    headers: { 'X-Persona-Id': personaId },
  });
  return data;
};

// Traer o crear carrito de un usuario
export const traerCarritoDelUsuario = async (personaId: number): Promise<CarritoDTO> => {
  const { data } = await axios.get(`${API_URL}/usuario`, {
    headers: { 'X-Persona-Id': personaId },
  });
  return data;
};
// Traer carrito por ID
export const traerCarrito = async (id: number): Promise<CarritoDTO> => {
  const { data } = await axios.get(`${API_URL}/${id}`);
  return data;
};


// Agregar producto al carrito
export const agregarProducto = async (
  carritoId: number,
  productoId: number,
): Promise<void> => {
  await axios.post(`${API_URL}/${carritoId}/agregar/${productoId}`);
};

// Quitar producto del carrito
export const quitarProducto = async (
  carritoId: number,
  productoId: number,
): Promise<void> => {
  await axios.delete(`${API_URL}/${carritoId}/quitar/${productoId}`);
};
