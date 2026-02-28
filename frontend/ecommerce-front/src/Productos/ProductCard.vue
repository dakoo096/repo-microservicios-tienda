✅ ProductoCard.vue corregido
<template>
  <div
    class="group relative bg-white rounded-2xl shadow-sm border border-gray-100 hover:shadow-xl transition-all duration-300 flex flex-col h-full overflow-hidden cursor-pointer"
    @click="irAlDetalle"
  >

    <!-- Badge -->
    <div class="absolute top-3 left-3 z-10">
      <span class="bg-white/90 backdrop-blur text-xs font-bold px-2 py-1 rounded-md shadow-sm text-gray-700">
        Nuevo
      </span>
    </div>

    <!-- Image -->
    <div class="relative aspect-square overflow-hidden bg-gray-50">
      <img
        :src="producto.urlImagen"
        class="w-full h-full object-contain p-1 transition-transform duration-300 group-hover:scale-105"
      />
    </div>

    <!-- Content -->
    <div class="p-4 flex flex-col flex-grow">
      <div class="mb-2">
        <p class="text-[10px] text-emerald-700 uppercase tracking-widest font-black">
          {{ producto.marca }}
        </p>
        <h3 class="text-lg font-bold text-gray-800 line-clamp-1 group-hover:text-emerald-700 transition-colors">
          {{ producto.nombre }}
        </h3>
      </div>

      <!-- Price & Actions -->
      <div class="mt-auto flex items-center justify-between gap-3 pt-3 border-t border-gray-50">
        <span class="text-xl font-black text-emerald-800">
          ${{ producto.precio.toFixed(2) }}
        </span>

        <div class="flex gap-2">

          <!-- Editar (solo admin) -->
          <button
            v-if="auth.isAdmin"
            class="bg-gray-50 text-gray-400 p-2.5 rounded-xl hover:bg-emerald-50 hover:text-emerald-700 transition-all"
            @click.stop="router.push(`/editar/${producto.codigo}`)"
            title="Editar producto"
          >
            <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M15.232 5.232l3.536 3.536m-2.036-5.036a2.5 2.5 0 113.536 3.536L6.5 21.036H3v-3.572L16.732 3.732z" />
            </svg>
          </button>

          <!-- Agregar -->
          <button
            class="bg-emerald-900 text-white px-5 py-2.5 rounded-xl text-sm font-bold hover:bg-emerald-800 transition-all shadow-lg shadow-emerald-100 flex-grow"
            @click.stop="agregarCarrito"
          >
            Agregar 🌱
          </button>

        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import Swal from 'sweetalert2'
import type { Producto } from '../types/types'
import { useCartStore } from '../store/cart'
import { useAuthStore } from '../store/auth'
import { useRouter } from 'vue-router'

const props = defineProps<{ producto: Producto }>()

const cartStore = useCartStore()
const auth = useAuthStore()
const router = useRouter()

const irAlDetalle = () => {
  router.push(`/productos/${props.producto.codigo}`)
}

const agregarCarrito = async () => {
  console.log("isAuthenticated:", auth.isAuthenticated)

  if (!auth.isAuthenticated) {
    router.push('/login')
    return
  }

  await cartStore.addItem(props.producto)

  const Toast = Swal.mixin({
    toast: true,
    position: 'top-end',
    showConfirmButton: false,
    timer: 2000,
    timerProgressBar: true,
  })

  Toast.fire({
    icon: 'success',
    title: '¡Agregado al carrito!'
  })
}
</script>
