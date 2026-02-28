<template>
    <section class="container mx-auto px-4 py-12 min-h-screen flex justify-center items-start">
        <div class="w-full max-w-2xl">
            <div class="bg-white shadow-xl rounded-3xl overflow-hidden border border-gray-100">
                <div class="bg-gray-50/50 px-8 py-6 border-b border-gray-100">
                    <h2 class="text-2xl font-bold text-gray-800">🌿 Nuevo Producto</h2>
                    <p class="text-gray-500 text-sm mt-1">Ingresa los detalles de la nueva planta</p>
                </div>

                <div class="p-8">
                    <form @submit.prevent="handleGuardar" class="space-y-6">
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

                        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                            <div>
                                <label
                                    class="block text-sm font-semibold text-gray-600 uppercase tracking-wide mb-2">Precio
                                    ($)</label>
                                <input v-model.number="form.precio" type="number" step="0.01"
                                    class="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-primary focus:ring-4 focus:ring-primary/10 outline-none transition-all font-mono"
                                    placeholder="0.00" required />
                            </div>

                            <div>
                                <label
                                    class="block text-sm font-semibold text-gray-600 uppercase tracking-wide mb-2">Stock
                                    Disponible</label>
                                <input v-model.number="form.stock" type="number"
                                    class="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-primary focus:ring-4 focus:ring-primary/10 outline-none transition-all font-mono"
                                    placeholder="0" required />
                            </div>
                        </div>

                        <div>
                            <label
                                class="block text-sm font-semibold text-gray-600 uppercase tracking-wide mb-2">Descripción</label>
                            <textarea v-model="form.descripcion" rows="3"
                                class="w-full px-4 py-3 rounded-xl border border-gray-200 focus:border-primary focus:ring-4 focus:ring-primary/10 outline-none transition-all resize-none"
                                placeholder="Describe el producto (cuidados, origen, etc...)" required></textarea>
                        </div>

                        <div>
                            <label class="block text-sm font-semibold text-gray-600 uppercase tracking-wide mb-2">Código
                                de Sistema</label>
                            <div class="flex">
                                <span
                                    class="inline-flex items-center px-4 rounded-l-xl border border-r-0 border-gray-200 bg-gray-50 text-gray-500 font-mono">#</span>
                                <input :value="proximoCodigo" type="text"
                                    class="w-full px-4 py-3 rounded-r-xl border border-gray-200 bg-gray-50 text-gray-500 font-mono cursor-not-allowed"
                                    disabled placeholder="Autogenerado" />
                            </div>
                            <small class="text-gray-400 mt-1 block text-xs">Asignado automáticamente por el
                                vivero</small>
                        </div>

                        <!-- SECCIÓN DE IMAGEN PREMIUM -->
                        <div>
                            <label class="block text-sm font-semibold text-gray-600 uppercase tracking-wide mb-3">
                                Imagen del Producto
                            </label>

                            <div class="relative group">
                                <!-- Área de Carga / Dropzone -->
                                <div @click="fileInput?.click()" @dragover.prevent="isDragging = true"
                                    @dragleave.prevent="isDragging = false" @drop.prevent="handleDrop" :class="[
                                        'relative flex flex-col items-center justify-center w-full h-64 border-2 border-dashed rounded-3xl cursor-pointer transition-all overflow-hidden',
                                        isDragging ? 'border-emerald-500 bg-emerald-50' : 'border-gray-200 hover:border-emerald-400 hover:bg-gray-50/50'
                                    ]">

                                    <input type="file" ref="fileInput" class="hidden" accept="image/*"
                                        @change="handleFileChange" />

                                    <!-- Estado: Sin Imagen -->
                                    <div v-if="!previewUrl" class="flex flex-col items-center text-center px-4">
                                        <div
                                            class="w-16 h-16 bg-emerald-50 text-emerald-600 rounded-2xl flex items-center justify-center mb-4 group-hover:scale-110 transition-transform">
                                            <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8" fill="none"
                                                viewBox="0 0 24 24" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                                    d="M4 16l4.586-4.586a2 2 0 012.828 0L16 16m-2-2l1.586-1.586a2 2 0 012.828 0L20 14m-6-6h.01M6 20h12a2 2 0 002-2V6a2 2 0 00-2-2H6a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                            </svg>
                                        </div>
                                        <p class="text-gray-700 font-semibold mb-1">Pulsa para seleccionar o arrastra
                                            una
                                            foto</p>
                                        <p class="text-gray-400 text-xs mt-1">PNG, JPG o WEBP hasta 5MB</p>
                                    </div>

                                    <!-- Estado: Con Imagen (Preview) -->
                                    <div v-else class="w-full h-full relative">
                                        <img :src="previewUrl" class="w-full h-full object-cover" />
                                        <div
                                            class="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition-opacity flex items-center justify-center">
                                            <p class="text-white font-medium">Cambiar imagen</p>
                                        </div>
                                    </div>
                                </div>

                                <!-- Botón de eliminar (si hay imagen) -->
                                <button v-if="previewUrl" @click.stop="removerImagen" type="button"
                                    class="absolute -top-3 -right-3 w-8 h-8 bg-red-500 text-white rounded-full flex items-center justify-center shadow-lg hover:bg-red-600 transition-colors z-10 border-2 border-white">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" viewBox="0 0 20 20"
                                        fill="currentColor">
                                        <path fill-rule="evenodd"
                                            d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                                            clip-rule="evenodd" />
                                    </svg>
                                </button>
                            </div>
                        </div>

                        <div class="pt-4 flex flex-col gap-3">
                            <button type="submit"
                                class="w-full bg-emerald-600 text-white py-4 rounded-xl font-bold text-lg hover:bg-emerald-700 transition-colors shadow-lg hover:shadow-emerald-600/30 flex justify-center items-center gap-2"
                                :disabled="loading">
                                <div v-if="loading"
                                    class="animate-spin h-5 w-5 border-2 border-white border-t-transparent rounded-full">
                                </div>
                                {{ loading ? 'Enviando...' : 'Guardar Producto' }}
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
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { crearProducto, subirImagenProducto, getProductos } from '../Productos/services/productos.service'
import type { Producto } from '../types/types'

const router = useRouter()
const loading = ref(false)
const ultimoId = ref(0)
const selectedFile = ref<File | null>(null)
const previewUrl = ref<string | null>(null)
const isDragging = ref(false)
const fileInput = ref<HTMLInputElement | null>(null)

const form = ref<Producto>({
    codigo: 0,
    nombre: '',
    marca: '',
    precio: 0,
    stock: 0,
    descripcion: '',
    urlImagen: ''
})

// ID sugerido solo visual
const proximoCodigo = computed(() => {
    return ultimoId.value > 0 ? ultimoId.value + 1 : '...'
})

onMounted(async () => {
    try {
        const productos = await getProductos()
        if (productos.length > 0) {
            ultimoId.value = Math.max(...productos.map((p: Producto) => p.codigo))
        }
    } catch (e) {
        console.error("No se pudo predecir el próximo ID")
    }
})

const handleFileChange = (event: Event) => {
    const target = event.target as HTMLInputElement
    const file = target.files?.[0]
    if (file) {
        procesarArchivo(file)
    }
}

const handleDrop = (event: DragEvent) => {
    isDragging.value = false
    const file = event.dataTransfer?.files[0]
    if (file && file.type.startsWith('image/')) {
        procesarArchivo(file)
    }
}

const procesarArchivo = (file: File) => {
    selectedFile.value = file
    // Crear preview local
    if (previewUrl.value) URL.revokeObjectURL(previewUrl.value)
    previewUrl.value = URL.createObjectURL(file)
}

const removerImagen = () => {
    selectedFile.value = null
    if (previewUrl.value) URL.revokeObjectURL(previewUrl.value)
    previewUrl.value = null
    if (fileInput.value) fileInput.value.value = ''
}

const handleGuardar = async () => {
    loading.value = true
    try {
        // 1️⃣ Crear producto primero
        const productoCreado = await crearProducto(form.value)

        // 2️⃣ Subir imagen si hay archivo seleccionado
        if (selectedFile.value) {
            await subirImagenProducto(productoCreado.codigo, selectedFile.value)
        }

        await Swal.fire({
            title: '¡Producto Guardado!',
            text: `Se ha registrado "${form.value.nombre}" correctamente.`,
            icon: 'success',
            confirmButtonText: 'Genial',
            confirmButtonColor: '#10b981'
        })

        router.push('/productos')
    } catch (error) {
        Swal.fire({
            title: 'Error',
            text: 'No se pudo guardar el producto o subir la imagen.',
            icon: 'error',
            confirmButtonText: 'Entendido'
        })
    } finally {
        loading.value = false
    }
}
</script>