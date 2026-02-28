export interface Producto {
  codigo: number;
  nombre: string;
  marca: string;
  precio: number;
  stock: number;
  descripcion: string;
  urlImagen?: string; // <- esto es importante, puede venir vacía
}

export interface ItemCarritoDTO {
  id: number;
  productoId: number;
  cantidad: number;
}

export interface CarritoDTO {
  id: number;
  personaId?: number;
  items: ItemCarritoDTO[];
}
