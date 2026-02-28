<template>
    <section class="container mx-auto px-4 py-12 min-h-screen flex justify-center items-start">
        <div class="w-full max-w-2xl">
            <div class="bg-white shadow-xl rounded-3xl overflow-hidden border border-gray-100">
                <div class="bg-gray-50/50 px-8 py-6 border-b border-gray-100 flex justify-between items-center">
                    <div>
                        <h2 class="text-2xl font-bold text-gray-800">✏️ Editar Producto</h2>
                        <p class="text-gray-500 text-sm mt-1">Modifica los detalles de la planta</p>
                    </div>
                    <div class="bg-blue-100 text-blue-700 font-mono font-bold px-3 py-1 rounded-lg text-sm">
                        #{{ form.codigo }}
                    </div>
                </div>

                <div class="p-8">
                    <div v-if="loadingData" class="flex justify-center py-12">
                        <div class="animate-spin rounded-full h-12 w-12 border-b-2 border-primary"></div>
                    </div>

                    <form v-else @submit.prevent="handleGuardar" class="space-y-6">
                        <div>
                            <label
                                class="block text-sm font-semibold text-gray-600 uppercase tracking-wide mb-2">Nombre</label>
                            <input v-model="form.nombre" type="text"
                                class="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-primary focus:ring-4 focus:ring-primary/10 outline-none transition-all"
                                placeholder="Ej: Orquídea Cattleya" required />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-600 uppercase tracking-wide mb-2">Vivero
                                / Marca</label>
                            <input v-model="form.marca" type="text"
                                class="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-primary focus:ring-4 focus:ring-primary/10 outline-none transition-all"
                                placeholder="Ej: Botánica Viva" required />
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-600 uppercase tracking-wide mb-2">Precio
                                ($)</label>
                            <input v-model.number="form.precio" type="number" step="0.01"
                                class="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-primary focus:ring-4 focus:ring-primary/10 outline-none transition-all font-mono"
                                placeholder="0.00" required />
                        </div>

                        <div class="pt-4 flex flex-col gap-3">
                            <button type="submit"
                                class="w-full bg-blue-600 text-white py-4 rounded-xl font-bold text-lg hover:bg-blue-700 transition-colors shadow-lg hover:shadow-blue-600/30 flex justify-center items-center gap-2"
                                :disabled="loading">
                                <div v-if="loading"
                                    class="animate-spin h-5 w-5 border-2 border-white border-t-transparent rounded-full">
                                </div>
                                {{ loading ? 'Guardando...' : 'Actualizar Producto' }}
                            </button>
                            <button type="button" @click="$router.push('/productos')"
                                class="w-full py-3 text-gray-500 font-medium hover:text-gray-800 transition-colors">
                                Cancelar y volver
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>
</template>

<script setup lang="ts">
import Swal from 'sweetalert2'
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getProductoByCodigo, actualizarProducto } from '../Productos/services/productos.service'
import type { Producto } from '../types/types'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const loadingData = ref(true)

const form = ref<Producto>({
    codigo: 0,
    nombre: '',
    marca: '',
    precio: 0,
    descripcion: '',
    stock: 0
})

onMounted(async () => {
    try {
        const id = Number(route.params.id)
        if (!id) {
            router.push('/productos')
            return
        }

        const producto = await getProductoByCodigo(id)
        form.value = producto
    } catch (e) {
        Swal.fire('Error', 'No se pudo cargar el producto', 'error')
        router.push('/productos')
    } finally {
        loadingData.value = false
    }
})

const handleGuardar = async () => {
    loading.value = true
    try {
        await actualizarProducto(form.value.codigo, form.value)

        await Swal.fire({
            title: '¡Actualizado!',
            text: `El producto "${form.value.nombre}" ha sido modificado.`,
            icon: 'success',
            confirmButtonText: 'Genial',
            confirmButtonColor: '#2563eb'
        })

        router.push('/productos')
    } catch (error) {
        Swal.fire({
            title: 'Error',
            text: 'No se pudieron guardar los cambios.',
            icon: 'error',
            confirmButtonText: 'Entendido'
        })
    } finally {
        loading.value = false
    }
}
</script>
