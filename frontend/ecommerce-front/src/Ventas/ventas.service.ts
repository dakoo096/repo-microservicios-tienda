import api from "../services/axiosConfig";

// 🛒 Crear venta
export const crearVenta = async (carritoId: number) => {
  const { data } = await api.post("/ms-ventas/ventas", { carritoId });
  return data;
};

// 👤 Ventas del usuario logueado
export const traerMisVentas = async () => {
  
  const { data } = await api.get("/ms-ventas/ventas/mis-ventas");
  return data;
};

// 👑 TODAS las ventas (solo admin — el gateway decide)
export const traerTodasLasVentas = async () => {
  const { data } = await api.get("/ms-ventas/ventas");
  return data;
};

// 📄 Detalle de una venta
export const traerVentaDetalle = async (id: number) => {
  const { data } = await api.get(`/ms-ventas/ventas/detalle/${id}`);
  return data;
};