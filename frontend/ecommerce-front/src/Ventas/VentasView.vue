<template>
  <div class="container mx-auto px-4 py-8 min-h-screen">
    <div class="mb-8 flex flex-col md:flex-row md:items-end justify-between gap-6">
      <div>
        <h2 class="text-3xl font-extrabold tracking-tight text-gray-900 sm:text-4xl">
          📊 <span class="bg-clip-text text-transparent bg-gradient-to-r from-emerald-800 to-green-600">Ventas</span>
          Realizadas
        </h2>
        <p class="text-gray-500 mt-2">Historial de plantas y productos entregados.</p>
      </div>

      <!-- Panel de Filtros -->
      <div class="flex flex-wrap items-center gap-3">
        <div class="relative group">
          <select v-model="filterRange"
            class="appearance-none pl-4 pr-10 py-2.5 bg-white border border-gray-100 rounded-2xl text-sm font-bold text-gray-700 hover:border-emerald-200 focus:outline-none focus:ring-4 focus:ring-emerald-50 transition-all cursor-pointer shadow-sm">
            <option value="all">📅 Todo el tiempo</option>
            <option value="7days">⏳ Últimos 7 días</option>
            <option value="month">🗓️ Último mes</option>
            <option value="year">⭐ Último año</option>
          </select>
          <div class="absolute inset-y-0 right-3 flex items-center pointer-events-none text-gray-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M19 9l-7 7-7-7" />
            </svg>
          </div>
        </div>

        <div class="relative group">
          <select v-model="sortOrder"
            class="appearance-none pl-4 pr-10 py-2.5 bg-white border border-gray-100 rounded-2xl text-sm font-bold text-gray-700 hover:border-emerald-200 focus:outline-none focus:ring-4 focus:ring-emerald-50 transition-all cursor-pointer shadow-sm">
            <option value="desc">🆕 Más recientes</option>
            <option value="asc">⏳ Más antiguas</option>
          </select>
          <div class="absolute inset-y-0 right-3 flex items-center pointer-events-none text-gray-400">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M19 9l-7 7-7-7" />
            </svg>
          </div>
        </div>
      </div>
    </div>

    <!-- Lista de ventas -->
    <div v-if="ventasFiltradas.length" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div v-for="venta in ventasFiltradas" :key="venta.id"
        class="bg-white rounded-2xl shadow-sm border border-gray-100 hover:shadow-lg transition-all duration-300 p-0 overflow-hidden group flex flex-col h-full relative">

        <!-- Ticket Top decoration -->
        <div class="h-2 bg-gradient-to-r from-emerald-600 to-green-500"></div>

        <div class="p-6 flex-grow flex flex-col">
          <div class="flex justify-between items-start mb-4">
            <div class="bg-gray-100 text-gray-600 text-xs font-mono py-1 px-2 rounded-md">
              #{{ venta.id.toString().padStart(6, '0') }}
            </div>
            <span
              class="bg-emerald-100 text-emerald-700 text-xs font-bold px-2 py-1 rounded-full flex items-center gap-1">
              ✅ Entregado
            </span>
          </div>

          <div class="mb-4">
            <p class="text-xs text-gray-400 uppercase font-bold tracking-wider mb-1">Fecha</p>
            <h5 class="text-lg font-medium text-gray-700">
              {{ new Date(venta.fecha).toLocaleDateString() }}
            </h5>
          </div>

          <div class="mt-auto pt-4 border-t border-dashed border-gray-200">
            <div class="flex justify-between items-end">
              <div>
                <p class="text-xs text-gray-400 uppercase font-bold tracking-wider mb-0.5">Total</p>
                <p class="text-2xl font-black text-gray-800">
                  ${{ venta.total.toLocaleString() }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <div class="p-4 bg-gray-50 border-t border-gray-100">
          <button
            class="w-full py-2.5 px-4 bg-white border border-gray-200 text-gray-700 rounded-xl font-bold hover:border-emerald-600 hover:text-emerald-700 hover:shadow-md transition-all flex justify-center items-center gap-2 group-hover:scale-[1.02] transform"
            @click="verDetalle(venta.id)">
            <span>Ver factura</span>
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 transition-transform group-hover:translate-x-1"
              fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 5l7 7-7 7" />
            </svg>
          </button>
        </div>
      </div>
    </div>

    <div v-else
      class="flex flex-col items-center justify-center py-24 text-center bg-white rounded-3xl border border-dashed border-gray-200 shadow-sm">
      <div class="bg-gray-50 p-6 rounded-full mb-4">
        <span class="text-4xl" v-if="ventas.length">🔍</span>
        <span class="text-4xl" v-else>🧾</span>
      </div>
      <h3 class="text-xl font-bold text-gray-800 mb-2">
        {{ ventas.length ? 'Sin resultados para este filtro' : 'Aún no hay ventas' }}
      </h3>
      <p class="text-gray-500 max-w-sm">
        {{ ventas.length ? 'Prueba cambiando el rango de fechas para encontrar lo que buscas.' : 'Tus ventas realizadas aparecerán aquí para que puedas llevar un control.' }}
      </p>
    </div>

    <!-- Detalle Modal / Overlay -->
    <Transition enter-active-class="transition duration-300 ease-out" enter-from-class="opacity-0"
      enter-to-class="opacity-100" leave-active-class="transition duration-200 ease-in" leave-from-class="opacity-100"
      leave-to-class="opacity-0">
      <div v-if="detalle" class="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex justify-center items-center p-4"
        @click.self="detalle = null">

        <div class="bg-white rounded-2xl shadow-2xl max-w-lg w-full overflow-hidden transform transition-all scale-100">
          <div class="p-6 border-b border-gray-100 flex justify-between items-center bg-gray-50">
            <div>
              <p class="text-xs text-uppercase text-gray-500 font-bold tracking-wider mb-1">COMPROBANTE</p>
              <h5 class="text-xl font-black text-gray-800">
                #{{ detalle.idVenta }}
              </h5>
            </div>
            <button
              class="bg-white p-2 rounded-full text-gray-400 hover:text-gray-800 hover:bg-gray-100 transition-all shadow-sm border border-gray-200"
              @click="detalle = null">
              <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <div class="p-0 overflow-y-auto max-h-[60vh] bg-white">
            <ul class="divide-y divide-gray-50">
              <li v-for="p in detalle.productos" :key="p.codigo"
                class="p-5 flex justify-between items-center hover:bg-gray-50/50 transition-colors">
                <div class="flex items-center gap-4">
                  <div class="w-10 h-10 rounded-lg bg-gray-100 flex items-center justify-center text-xl">
                    🌱
                  </div>
                  <div>
                    <div class="font-bold text-gray-800">{{ p.nombre }}</div>
                    <div class="text-xs text-gray-500 mt-0.5">
                      <span class="font-medium text-gray-700">{{ p.cantidad }}</span> unidad(es) ×
                      ${{ p.precio }}
                    </div>
                  </div>
                </div>

                <div class="font-bold text-gray-900 text-lg">
                  ${{ (p.precio * p.cantidad).toLocaleString() }}
                </div>
              </li>
            </ul>
          </div>

          <div class="p-6 border-t border-gray-100 bg-gray-50 flex flex-col gap-2">
            <div class="flex justify-between items-center mb-2">
              <span class="text-gray-500 font-medium">Subtotal</span>
              <span class="text-gray-800 font-bold">${{ detalle.total.toLocaleString() }}</span>
            </div>
            <div class="h-px bg-gray-200 w-full my-1 border-dashed"></div>
            <div class="flex justify-between items-center">
              <span class="text-gray-800 font-bold text-lg">Total Pagado</span>
              <span class="text-2xl font-black text-emerald-700">${{ detalle.total.toLocaleString() }}</span>
            </div>
          </div>
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { jwtDecode } from 'jwt-decode'
import {
  traerMisVentas,
  traerTodasLasVentas,
  traerVentaDetalle
} from './ventas.service'

const ventas = ref<any[]>([])
const detalle = ref<any | null>(null)

// Filtros
const filterRange = ref('all')
const sortOrder = ref('desc')

const ventasFiltradas = computed(() => {
  let result = [...ventas.value]

  // 1. Filtrar por fecha
  if (filterRange.value !== 'all') {
    const now = new Date()
    let minDate = new Date()

    if (filterRange.value === '7days') {
      minDate.setDate(now.getDate() - 7)
    } else if (filterRange.value === 'month') {
      minDate.setMonth(now.getMonth() - 1)
    } else if (filterRange.value === 'year') {
      minDate.setFullYear(now.getFullYear() - 1)
    }

    result = result.filter(v => new Date(v.fecha) >= minDate)
  }

  // 2. Ordenar
  result.sort((a, b) => {
    const dateA = new Date(a.fecha).getTime()
    const dateB = new Date(b.fecha).getTime()
    return sortOrder.value === 'desc' ? dateB - dateA : dateA - dateB
  })

  return result
})

const cargarVentas = async () => {
  const token = localStorage.getItem('token')
  let rol = 'CLIENTE'

  if (token) {
    const payload: any = jwtDecode(token)
    rol = payload.rol // toma ADMIN o CLIENTE según el JWT
  }

  try {
    if (rol === 'ADMIN') {
      ventas.value = await traerTodasLasVentas()
    } else {
      ventas.value = await traerMisVentas()
    }
  } catch (error) {
    console.error('Error al cargar ventas', error)
  }
}

const verDetalle = async (id: number) => {
  try {
    const data = await traerVentaDetalle(id)
    detalle.value = { ...data, idVenta: id }
  } catch (error) {
    console.error('Error al traer detalle', error)
  }
}

onMounted(cargarVentas)
</script>
