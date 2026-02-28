<template>
  <div class="space-y-12 pb-12">

    <!-- Hero / Banner -->
    <div class="relative h-[400px] md:h-[500px] rounded-3xl overflow-hidden shadow-2xl group">
      <img src="../assets/banner.png" alt="Banner principal"
        class="w-full h-full object-cover transform group-hover:scale-105 transition-transform duration-700" />
      <div
        class="absolute inset-0 bg-gradient-to-t from-black/80 via-black/40 to-transparent flex flex-col justify-center items-center text-center p-6">
        <h1 class="text-4xl md:text-6xl font-black text-white mb-4 tracking-tight drop-shadow-lg">
          Tu Vivero <br /> <span
            class="bg-clip-text text-transparent bg-gradient-to-r from-emerald-400 to-green-300">Online</span>
        </h1>
        <p class="text-lg md:text-xl text-gray-200 mb-8 max-w-2xl font-light">
          Llevá la naturaleza a tu hogar con las mejores orquídeas y plantas seleccionadas.
        </p>
        <router-link to="/productos"
          class="bg-white text-emerald-900 px-8 py-3 rounded-full font-bold text-lg hover:bg-emerald-600 hover:text-white transition-all transform hover:-translate-y-1 shadow-lg hover:shadow-emerald-900/20">
          Explorar Catálogo
        </router-link>
      </div>
    </div>

    <!-- Título -->
    <div class="flex items-center justify-between border-b border-gray-100 pb-4">
      <h2 class="text-3xl font-extrabold text-gray-900 tracking-tight">Plantas Destacadas</h2>
      <router-link to="/productos"
        class="text-emerald-700 font-bold hover:text-emerald-800 flex items-center gap-1 group">
        Ver todo el jardín
        <span class="group-hover:translate-x-1 transition-transform">→</span>
      </router-link>
    </div>

    <!-- Estados -->
    <div v-if="loading" class="flex justify-center items-center py-20">
      <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-emerald-600"></div>
    </div>

    <div v-else-if="productos.length === 0"
      class="text-center py-20 bg-gray-50 rounded-2xl border border-gray-100 border-dashed">
      <p class="text-gray-500 text-lg">No hay productos disponibles por el momento.</p>
    </div>

    <!-- Grid productos -->
    <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <div v-for="producto in productosDestacados" :key="producto.codigo" class="h-full">
        <ProductCard :producto="producto" />
      </div>
    </div>

  </div>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue'
import ProductCard from '../Productos/ProductCard.vue'
import { getProductos } from '../Productos/services/productos.service'
import type { Producto } from '../types/types'

export default defineComponent({
  name: 'Home',
  components: { ProductCard },
  setup() {
    const productos = ref<Producto[]>([])
    const loading = ref(true)

    const fetchProductos = async () => {
      try {
        productos.value = await getProductos()
      } catch (error) {
        console.error('Error al cargar productos:', error)
      } finally {
        loading.value = false
      }
    }

    const productosDestacados = computed(() =>
      productos.value.slice(0, 4)
    )

    onMounted(fetchProductos)

    return {
      productos,
      productosDestacados,
      loading
    }
  }
})
</script>
