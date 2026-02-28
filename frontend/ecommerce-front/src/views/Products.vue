<template>
  <section class="container mx-auto px-4 py-8 min-h-screen">

    <!-- Header -->
    <div class="mb-10 flex flex-col md:flex-row justify-between items-center gap-4">
      <div class="text-center md:text-left">
        <h1 class="text-4xl font-extrabold mb-3 text-gray-900 tracking-tight">🌿 <span
            class="bg-clip-text text-transparent bg-gradient-to-r from-emerald-800 to-green-600">Nuestras Plantas</span>
        </h1>
        <p class="text-gray-500 text-lg max-w-2xl">
          Explorá nuestra selección exclusiva de orquídeas y plantas para tu hogar.
        </p>
      </div>

      <!-- Filters -->
      <div class="w-full md:w-auto flex flex-col sm:flex-row gap-4">
        <!-- Nursery Filter -->
        <select v-model="marcaSeleccionada"
          class="w-full sm:w-64 px-4 py-3 rounded-xl border border-gray-200 bg-white text-gray-700 font-bold focus:border-emerald-600 focus:ring-4 focus:ring-emerald-50 outline-none transition-all appearance-none cursor-pointer shadow-sm hover:border-gray-300">
          <option value="">Todos los viveros</option>
          <option v-for="marca in marcas" :key="marca" :value="marca">
            {{ marca }}
          </option>
        </select>

        <!-- Sort Dropdown -->
        <select v-model="orden"
          class="w-full sm:w-64 px-4 py-3 rounded-xl border border-gray-200 bg-white text-gray-700 font-bold focus:border-emerald-600 focus:ring-4 focus:ring-emerald-50 outline-none transition-all appearance-none cursor-pointer shadow-sm hover:border-gray-300">
          <option value="">Predeterminado</option>
          <option value="menor_mayor">Precio: Menor a Mayor</option>
          <option value="mayor_menor">Precio: Mayor a Menor</option>
          <option value="nombre_az">Nombre: A - Z</option>
          <option value="nombre_za">Nombre: Z - A</option>
        </select>
      </div>
    </div>

    <!-- Loader -->
    <div v-if="loading" class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <SkeletonCard v-for="i in 8" :key="i" />
    </div>

    <!-- Empty state -->
    <div v-else-if="productosOrdenados.length === 0"
      class="flex flex-col items-center justify-center py-24 text-center text-gray-400 bg-gray-50 rounded-2xl border border-dashed border-gray-200">
      <span class="text-6xl mb-6">😕</span>
      <h2 class="text-2xl font-bold text-gray-700 mb-2">
        No se encontraron productos
      </h2>
      <p class="text-gray-500">Probá con otro nombre o marca 🌱</p>
    </div>


    <!-- Productos -->
    <div v-else class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 gap-6">
      <ProductCard v-for="producto in productosOrdenados" :key="producto.codigo" :producto="producto" />
    </div>

  </section>
</template>

<script lang="ts">
import { defineComponent, ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import ProductCard from '../Productos/ProductCard.vue'
import SkeletonCard from '../Productos/SkeletonCard.vue'
import { getProductos } from '../Productos/services/productos.service'
import type { Producto } from '../types/types'

export default defineComponent({
  name: 'Products',
  components: { ProductCard, SkeletonCard },
  setup() {
    const route = useRoute()

    const productos = ref<Producto[]>([])
    const loading = ref(true)
    const orden = ref('') // Estado para el ordenamiento
    const marcaSeleccionada = ref('') // Estado para el filtro de marca

    const fetchProductos = async () => {
      try {
        productos.value = await getProductos()
      } catch (e) {
        console.error(e)
      } finally {
        loading.value = false
      }
    }

    const marcas = computed(() => {
      const allMarcas = productos.value.map(p => p.marca).filter(Boolean)
      return [...new Set(allMarcas)].sort()
    })

    const productosFiltrados = computed(() => {
      const q = route.query.q?.toString().toLowerCase() || ''

      let res = productos.value

      // Filtro por búsqueda
      if (q) {
        res = res.filter(p =>
          p.nombre.toLowerCase().includes(q) ||
          p.marca.toLowerCase().includes(q)
        )
      }

      // Filtro por marca seleccionada
      if (marcaSeleccionada.value) {
        res = res.filter(p => p.marca === marcaSeleccionada.value)
      }

      return res
    })

    // Lógica de ordenamiento sobre los filtrados
    const productosOrdenados = computed(() => {
      // Creamos una copia para no mutar el array original accidentalmente
      let lista = [...productosFiltrados.value]

      switch (orden.value) {
        case 'menor_mayor':
          return lista.sort((a, b) => a.precio - b.precio)
        case 'mayor_menor':
          return lista.sort((a, b) => b.precio - a.precio)
        case 'nombre_az':
          return lista.sort((a, b) => a.nombre.localeCompare(b.nombre))
        case 'nombre_za':
          return lista.sort((a, b) => b.nombre.localeCompare(a.nombre))
        default:
          return lista
      }
    })


    onMounted(fetchProductos)

    return { productosOrdenados, loading, orden, marcas, marcaSeleccionada }
  }
})
</script>
<style scoped></style>
