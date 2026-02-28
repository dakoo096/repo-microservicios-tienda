<template>
  <div class="container mx-auto px-4 py-12 min-h-screen">
    <h1 class="text-3xl font-extrabold mb-8 text-gray-900 tracking-tight">Finalizar <span
      class="bg-clip-text text-transparent bg-gradient-to-r from-emerald-800 to-green-600">Compra</span></h1>

    <div v-if="cart.productos.length === 0"
      class="text-center py-20 bg-gray-50 rounded-xl border border-dashed border-gray-200">
      <div class="mx-auto w-28 h-28 rounded-xl bg-emerald-50 flex items-center justify-center mb-4">
        <svg class="w-14 h-14 text-emerald-400" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M3 3h2l1 14a2 2 0 002 2h8a2 2 0 002-2l1-14h2" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
          <path d="M16 11a4 4 0 01-8 0" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
        </svg>
      </div>
      <p class="text-gray-600 text-lg font-semibold">Tu carrito está vacío</p>
      <router-link to="/productos"
        class="inline-flex mt-4 px-4 py-2 bg-emerald-900 text-white rounded-full font-medium hover:bg-emerald-800 transition">Explorar productos</router-link>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 gap-12">

      <!-- Formulario de datos -->
      <form @submit.prevent="finalizarCompra" class="flex flex-col gap-6">
        <div class="bg-white p-6 rounded-2xl shadow-sm border border-gray-100">
          <h2 class="text-xl font-bold mb-4 text-gray-800">Datos del Cliente</h2>

          <div class="space-y-4">
            <input v-model="cliente.nombre" type="text" placeholder="Nombre completo" required
              class="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-emerald-600 focus:ring-4 focus:ring-emerald-50 outline-none transition-all" />

            <input v-model="cliente.email" type="email" placeholder="Email" required
              class="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-emerald-600 focus:ring-4 focus:ring-emerald-50 outline-none transition-all" />

            <input v-model="cliente.direccion" type="text" placeholder="Dirección" required
              class="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-emerald-600 focus:ring-4 focus:ring-emerald-50 outline-none transition-all" />
          </div>
        </div>

        <button type="submit"
          class="bg-emerald-900 text-white w-full py-4 rounded-xl font-black text-lg hover:bg-emerald-800 transition-all shadow-xl shadow-emerald-100 active:scale-95">
          Confirmar pedido
        </button>

        <div v-if="mensaje"
          class="p-4 bg-emerald-50 text-emerald-700 rounded-xl border border-emerald-100 font-medium text-center">
          {{ mensaje }}
        </div>

        <div v-if="error" class="p-4 bg-red-50 text-red-700 rounded-xl border border-red-100 font-medium text-center">
          {{ error }}
        </div>
      </form>

      <!-- Resumen del carrito -->
      <div class="bg-gray-50 p-8 rounded-3xl h-fit border border-gray-100">
        <h2 class="text-xl font-bold mb-6 text-gray-800">Resumen de tu compra</h2>
        <ul class="space-y-4 mb-8">
          <li v-for="item in cart.productosAgrupados" :key="item.codigo"
            class="flex justify-between items-center pb-4 border-b border-gray-200 last:border-0 last:pb-0">
            <div>
              <span class="font-medium text-gray-800 block">{{ item.nombre }}</span>
              <span class="text-sm text-gray-500">x {{ item.cantidad }}</span>
            </div>
            <span class="font-bold text-gray-800">${{ (item.precio * item.cantidad).toFixed(2) }}</span>
          </li>
        </ul>
        <div class="border-t border-gray-200 pt-6">
          <p class="flex justify-between items-center">
            <span class="text-lg text-gray-600">Total</span>
            <span class="text-2xl font-bold text-gray-900">${{ cart.total.toFixed(2) }}</span>
          </p>
        </div>
      </div>

    </div>
  </div>
</template>

<script lang="ts">
import { defineComponent, reactive, ref } from 'vue';
import { useCartStore } from '../store/cart';
export default defineComponent({
  name: 'Checkout',
  setup() {
    const cart = useCartStore();

    const cliente = reactive({
      nombre: '',
      email: '',
      direccion: ''
    });

    const mensaje = ref('');
    const error = ref('');

    const finalizarCompra = async () => {
      if (cart.productos.length === 0) return;

      const venta = {
        cliente: { ...cliente },
        productos: cart.productosAgrupados.map((item: { codigo: any; cantidad: any; }) => ({
          codigo: item.codigo,
          cantidad: item.cantidad
        })),
        total: cart.total
      };

      console.log('Procesando compra manual:', venta);
      // Aquí se llamaría al servicio de ventas si esta vista se usara realmente
      alert('Funcionalidad de checkout manual no implementada completamente. Usar carrito.');
    };

    return { cart, cliente, mensaje, error, finalizarCompra };
  }
});
</script>
