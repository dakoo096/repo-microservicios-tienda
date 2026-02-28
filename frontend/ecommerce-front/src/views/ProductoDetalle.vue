<template>
    <div class="min-h-screen bg-[#fdfcf7] py-12 px-4 sm:px-6 lg:px-8">
        <!-- Loader -->
        <div v-if="loading" class="flex flex-col items-center justify-center py-32 space-y-4">
            <div class="text-6xl animate-bounce">🌿</div>
            <p class="text-emerald-800 font-bold tracking-widest uppercase text-xs">Cultivando detalles...</p>
        </div>

        <!-- Error -->
        <div v-else-if="error"
            class="max-w-md mx-auto bg-white p-8 rounded-[2rem] shadow-xl text-center border border-rose-100">
            <div class="text-5xl mb-4">🍂</div>
            <h2 class="text-2xl font-black text-gray-900 mb-2">¡Ups! Algo marchitó</h2>
            <p class="text-gray-500 mb-6">No logramos encontrar este producto en nuestro jardín.</p>
            <router-link to="/productos"
                class="inline-flex items-center justify-center px-8 py-3 bg-emerald-900 text-white rounded-2xl font-bold hover:bg-emerald-800 transition-all">
                Volver al catálogo
            </router-link>
        </div>

        <!-- Contenido del Producto -->
        <div v-if="producto && !loading" class="max-w-6xl mx-auto">
            <!-- Botón Volver -->
            <router-link to="/productos"
                class="group inline-flex items-center gap-2 mb-8 text-gray-400 hover:text-emerald-700 font-bold transition-all">
                <div
                    class="p-2 bg-white rounded-xl border border-gray-100 group-hover:border-emerald-100 group-hover:shadow-sm transition-all">
                    <svg xmlns="http://www.w3.org/2000/svg"
                        class="w-5 h-5 group-hover:-translate-x-1 transition-transform" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M15 19l-7-7 7-7" />
                    </svg>
                </div>
                <span>Volver al Catálogo</span>
            </router-link>

            <div
                class="bg-white/70 backdrop-blur-xl rounded-[2.5rem] shadow-2xl shadow-emerald-900/5 border border-white overflow-hidden">
                <div class="grid lg:grid-cols-2">

                    <!-- Lado Imagen -->
                    <div class="p-4 lg:p-10">
                        <div
                            class="relative group aspect-square rounded-[2rem] overflow-hidden bg-gray-50 border border-gray-100">
                            <img :src="producto.urlImagen" :alt="producto.nombre"
                                class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-700" />
                            <div class="absolute top-6 left-6">
                                <span
                                    class="px-4 py-2 bg-white/90 backdrop-blur-md rounded-full text-[10px] font-black uppercase tracking-widest text-emerald-800 shadow-sm border border-emerald-50">
                                    {{ producto.marca }}
                                </span>
                            </div>
                        </div>
                    </div>

                    <!-- Lado Info -->
                    <div class="p-8 lg:p-16 lg:pl-0 flex flex-col justify-center">
                        <div>
                            <div class="flex items-center gap-2 mb-4 text-emerald-600">
                                <span class="w-8 h-[2px] bg-emerald-200"></span>
                                <span class="text-[10px] font-black uppercase tracking-[0.2em]">Detalles de la
                                    Especie</span>
                            </div>

                            <h1 class="text-4xl lg:text-5xl font-black text-gray-900 leading-tight mb-4 tracking-tight">
                                {{ producto.nombre }}
                            </h1>

                            <div class="flex items-center gap-4 mb-8">
                                <p
                                    class="text-4xl font-extrabold bg-clip-text text-transparent bg-gradient-to-r from-emerald-800 to-green-600">
                                    ${{ producto.precio }}
                                </p>
                                <div :class="producto.stock > 0 ? 'bg-emerald-50 text-emerald-700 border-emerald-100' : 'bg-rose-50 text-rose-700 border-rose-100'"
                                    class="px-4 py-1.5 rounded-full text-xs font-bold border flex items-center gap-2">
                                    <span class="w-2 h-2 rounded-full"
                                        :class="producto.stock > 0 ? 'bg-emerald-500 animate-pulse' : 'bg-rose-500'"></span>
                                    {{ producto.stock > 0 ? `En Stock (${producto.stock})` : 'Agotado' }}
                                </div>
                            </div>

                            <p class="text-gray-600 text-lg leading-relaxed mb-10 max-w-xl">
                                {{ producto.descripcion }}
                            </p>
                        </div>

                        <!-- Acciones -->
                        <div class="mt-auto pt-8 border-t border-gray-50 flex flex-col sm:flex-row items-center gap-4">
                            <button :disabled="producto.stock <= 0" @click="agregarCarrito"
                                class="w-full sm:flex-1 group relative px-8 py-5 bg-emerald-900 text-white rounded-2xl font-bold shadow-xl shadow-emerald-100 hover:bg-emerald-800 hover:-translate-y-1 transition-all active:scale-95 disabled:opacity-50 disabled:cursor-not-allowed disabled:transform-none">
                                <div class="relative flex items-center justify-center gap-3">
                                    <svg v-if="producto.stock > 0" xmlns="http://www.w3.org/2000/svg"
                                        class="w-6 h-6 group-hover:rotate-12 transition-transform" fill="none"
                                        viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                            d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
                                    </svg>
                                    <span class="text-lg">
                                        {{ producto.stock > 0 ? 'Agregar al carrito' : 'Sin stock disponible' }}
                                    </span>
                                </div>
                            </button>
                        </div>                      
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import axios from '../services/axiosConfig'
import Swal from 'sweetalert2'
import { useCartStore } from '../store/cart'
import { useAuthStore } from '../store/auth'
import { useRouter } from 'vue-router'

const route = useRoute()
const producto = ref(null)
const loading = ref(true)
const error = ref(false)
const cartStore = useCartStore()
const auth = useAuthStore()
const router = useRouter()

const agregarCarrito = async () => {

    if (!auth.isAuthenticated) {
        router.push('/login')
        return
    }

    await cartStore.addItem(producto.value)

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
onMounted(async () => {
    try {
        const codigo = route.params.codigo

        const response = await axios.get(
            `/ms-productos/productos/${codigo}`
        )

        producto.value = response.data

    } catch (e) {
        error.value = true
    } finally {
        loading.value = false
    }
})
</script>