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
                                placeholder="Describe el producto..." required></textarea>
                        </div>

                        <!-- SECCIÓN DE IMAGEN PREMIUM -->
                        <div>
                            <!-- Toggle URL/Archivo -->
                            <div class="flex justify-between items-center mb-3">
                                <label class="block text-sm font-semibold text-gray-600 uppercase tracking-wide">
                                    Imagen del Producto
                                </label>
                                <button type="button" @click="showUrlInput = !showUrlInput" 
                                    class="text-xs font-bold text-blue-600 hover:text-blue-700 underline flex items-center gap-1 transition-colors">
                                    <svg v-if="!showUrlInput" xmlns="http://www.w3.org/2000/svg" class="h-3 w-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101" />
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.172 13.828a4 4 0 015.656 0l4-4a4 4 0 00-5.656-5.656l-1.102 1.101" />
                                    </svg>
                                    {{ showUrlInput ? 'Ocultar URL' : 'Usar URL en vez de archivo' }}
                                </button>
                            </div>

                            <!-- Campo URL Directa -->
                            <div v-if="showUrlInput" class="mb-4 animate-in slide-in-from-top-2 duration-300">
                                <div class="relative flex items-center">
                                    <div class="absolute inset-y-0 left-0 pl-3 flex items-center pointer-events-none">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101" />
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10.172 13.828a4 4 0 015.656 0l4-4a4 4 0 00-5.656-5.656l-1.102 1.101" />
                                        </svg>
                                    </div>
                                    <input 
                                        v-model="form.urlImagen" 
                                        type="url" 
                                        class="block w-full pl-10 pr-4 py-3 border border-gray-200 rounded-xl focus:ring-4 focus:ring-blue-500/10 focus:border-blue-500 outline-none transition-all text-sm"
                                        placeholder="O pega una URL de imagen aquí (https://...)"
                                        @input="handleUrlInput"
                                    />
                                </div>
                            </div>

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
                                    <div v-if="!effectivePreviewUrl" class="flex flex-col items-center text-center px-4">
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
                                        <p class="text-gray-400 text-xs mt-1">O usa el campo de URL arriba</p>
                                    </div>

                                    <!-- Estado: Con Imagen (Preview) -->
                                    <div v-else class="w-full h-full relative">
                                        <img :src="effectivePreviewUrl" class="w-full h-full object-cover" @error="handleImageError" />
                                        <div
                                            class="absolute inset-0 bg-black/40 opacity-0 hover:opacity-100 transition-opacity flex items-center justify-center">
                                            <p class="text-white font-medium">{{ selectedFile ? 'Cambiar archivo' : 'La URL se previsualiza aquí' }}</p>
                                        </div>
                                    </div>
                                </div>

                                <!-- Botón de eliminar (si hay imagen seleccionada por archivo) -->
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
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { getProductoByCodigo, actualizarProducto, subirImagenProducto } from '../Productos/services/productos.service'
import type { Producto } from '../types/types'

const router = useRouter()
const route = useRoute()
const loading = ref(false)
const loadingData = ref(true)

const selectedFile = ref<File | null>(null)
const previewUrl = ref<string | null>(null)
const isDragging = ref(false)
const fileInput = ref<HTMLInputElement | null>(null)
const showUrlInput = ref(false)

const form = ref<Producto>({
    codigo: 0,
    nombre: '',
    marca: '',
    precio: 0,
    descripcion: '',
    stock: 0,
    urlImagen: ''
})

// Preview efectiva que prioriza el archivo si existe
const effectivePreviewUrl = computed(() => {
    return previewUrl.value || form.value.urlImagen
})

const handleUrlInput = () => {
    if (form.value.urlImagen && selectedFile.value) {
        removerImagen()
    }
}

const handleImageError = () => {
    if (form.value.urlImagen && !selectedFile.value) {
        console.warn("La URL de imagen no parece ser válida")
    }
}

const handleFileChange = (event: Event) => {
    const target = event.target as HTMLInputElement
    const file = target.files?.[0]
    if (file) {
        form.value.urlImagen = ''
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
    if (previewUrl.value) URL.revokeObjectURL(previewUrl.value)
    previewUrl.value = URL.createObjectURL(file)
}

const removerImagen = () => {
    selectedFile.value = null
    if (previewUrl.value) URL.revokeObjectURL(previewUrl.value)
    previewUrl.value = null
    if (fileInput.value) fileInput.value.value = ''
}

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

        // Subir imagen si hay archivo seleccionado (esto sobrescribirá la URL en el backend si el backend lo maneja así)
        if (selectedFile.value) {
            await subirImagenProducto(form.value.codigo, selectedFile.value)
        }

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
