<template>
  <section class="cart-page py-12 bg-gray-50 min-h-screen">
    <div class="container mx-auto px-4">

      <div v-if="compraRealizada" class="text-center py-12 animate-fade-in-up">
        <div class="bg-white shadow-2xl rounded-3xl p-10 mx-auto max-w-2xl transform transition-all">
          <div class="mx-auto w-24 h-24 rounded-full bg-emerald-50 flex items-center justify-center mb-6">
            <svg class="w-12 h-12 text-emerald-600" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M20 6L9 17l-5-5" stroke="currentColor" stroke-width="2.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <h2 class="text-3xl font-extrabold mt-2 text-gray-900">Compra confirmada</h2>
          <p class="text-gray-600 text-lg mt-3 mb-8 leading-relaxed">
            Tu pedido se procesó correctamente. Recibirás un correo con los detalles y el seguimiento.
          </p>
          <div class="flex items-center justify-center gap-4">
            <router-link to="/productos"
              class="inline-flex items-center justify-center px-6 py-3 bg-emerald-900 text-white rounded-full font-semibold text-md hover:bg-emerald-800 transition-colors shadow">
              Seguir comprando
            </router-link>
            <button @click="compraRealizada = false; cart.clearLocalData()"
              class="inline-flex items-center justify-center px-6 py-3 bg-gray-100 text-gray-800 rounded-full font-semibold text-md hover:bg-gray-200 transition-colors border">
              Ver mis compras
            </button>
          </div>
        </div>
      </div>

      <template v-else>
        <div v-if="cart.productos.length === 0 && !compraRealizada" class="text-center py-20">
          <div class="mx-auto w-40 h-40 rounded-2xl bg-gradient-to-br from-emerald-50 to-white flex items-center justify-center mb-6 shadow-inner border border-gray-100">
            <svg class="w-20 h-20 text-emerald-400" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
              <path d="M3 3h2l1 14a2 2 0 002 2h8a2 2 0 002-2l1-14h2" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
              <path d="M16 11a4 4 0 01-8 0" stroke="currentColor" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round"/>
            </svg>
          </div>
          <h2 class="text-2xl font-bold text-gray-700">Tu carrito está vacío</h2>
          <p class="text-gray-500 mt-2">Agrega productos que te gusten y los verás aquí.</p>
          <div class="mt-6">
            <router-link to="/productos"
              class="inline-flex items-center justify-center px-5 py-3 bg-emerald-900 text-white rounded-full font-semibold hover:bg-emerald-800 transition-colors shadow">
              Explorar productos
            </router-link>
          </div>
        </div>

        <div v-else class="grid grid-cols-1 lg:grid-cols-3 gap-8">

          <!-- Product List -->
          <div class="lg:col-span-2 space-y-6">
            <div v-for="(productos, tienda) in productosAgrupadosPorTienda" :key="tienda"
              class="bg-white shadow-sm rounded-2xl overflow-hidden border border-gray-100">

              <div class="bg-gray-50/50 border-b border-gray-100 px-6 py-4">
                <div class="flex items-center text-gray-800 font-semibold">
                  <span>Productos de {{ tienda }}</span>
                </div>
              </div>

              <div class="p-6">
                <div v-for="p in productos" :key="p.codigo"
                  class="flex gap-4 mb-6 pb-6 border-b border-gray-100 last:mb-0 last:pb-0 last:border-0">

                  <!-- Image -->
                  <div
                    class="relative w-24 h-24 flex-shrink-0 bg-gray-50 rounded-lg overflow-hidden border border-gray-200">
                    <img
                      :src="p.urlImagen || 'https://acdn-us.mitiendanube.com/stores/913/526/products/orquidea-phalaenopsis-21-22a390334a473bd9e816409664852998-640-0.webp'"
                      class="w-full h-full object-contain p-1" />
                    <div v-if="p.cantidad > 5"
                      class="absolute bottom-0 inset-x-0 bg-red-50 text-red-500 text-[10px] font-bold text-center py-0.5 border-t border-red-100">
                      ¡ÚLTIMOS!</div>
                  </div>

                  <!-- Details -->
                  <div class="flex-grow flex flex-col justify-between">
                    <div>
                      <div class="flex justify-between items-start">
                        <h6 class="text-gray-800 font-medium line-clamp-1 pr-4">{{ p.nombre }}</h6>
                        <button @click="eliminar(p.codigo)"
                          class="text-gray-400 hover:text-red-500 transition-colors p-1">
                          <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24"
                            stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                          </svg>
                        </button>
                      </div>
                      <small class="text-gray-500 block text-sm">{{ p.marca }}</small>
                    </div>

                    <div class="flex items-end justify-between mt-2">
                      <!-- Quantity -->
                      <div class="flex items-center border border-gray-300 rounded-lg">
                        <button class="px-3 py-1 text-gray-600 hover:bg-emerald-50 disabled:opacity-50"
                          :disabled="loadingItems[p.codigo] || loading" @click="cambiar(p.codigo, 'restar')">
                          <span v-if="!loadingItems[p.codigo]">-</span>
                          <div v-else class="animate-spin h-3 w-3 border-b-2 border-emerald-600 rounded-full"></div>
                        </button>
                        <span class="px-2 font-medium text-gray-800 w-8 text-center">{{ p.cantidad }}</span>
                        <button class="px-3 py-1 text-gray-600 hover:bg-emerald-50 disabled:opacity-50"
                          :disabled="loadingItems[p.codigo] || loading" @click="cambiar(p.codigo, 'sumar')">
                          <span v-if="!loadingItems[p.codigo]">+</span>
                          <div v-else class="animate-spin h-3 w-3 border-b-2 border-emerald-600 rounded-full"></div>
                        </button>
                      </div>

                      <span class="text-lg font-bold text-gray-900">$ {{ (p.precio * p.cantidad).toLocaleString()
                      }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Summary -->
          <div class="lg:col-span-1">
            <div class="bg-white p-6 rounded-2xl shadow-sm border border-gray-100 sticky top-24">
              <h5 class="text-xl font-bold text-gray-800 mb-6">Resumen de compra</h5>

              <div class="flex justify-between mb-3 text-gray-600">
                <span>Productos ({{ cart.productos.length }})</span>
                <span>$ {{ cart.total.toLocaleString() }}</span>
              </div>

              <div class="flex justify-between mb-3">
                <span class="text-gray-600">Envíos</span>
                <span class="text-emerald-500 font-bold">Gratis</span>
              </div>

              <div class="border-t border-gray-100 my-4 pt-4">
                <div class="flex justify-between items-end">
                  <span class="text-lg font-bold text-gray-800">Total</span>
                  <span class="text-2xl font-bold text-gray-900">$ {{ cart.total.toLocaleString() }}</span>
                </div>
              </div>

              <button
                class="w-full mt-6 bg-emerald-900 text-white py-4 rounded-xl font-black text-lg hover:bg-emerald-800 active:scale-[0.98] transition-all disabled:opacity-70 disabled:cursor-not-allowed shadow-xl shadow-emerald-100 flex justify-center items-center gap-2"
                :disabled="loading" @click="handleCheckout">
                <div v-if="loading"
                  class="animate-spin h-5 w-5 border-2 border-white border-t-transparent rounded-full"></div>
                <span>{{ loading ? 'Procesando pago...' : 'Finalizar compra' }}</span>
              </button>

            </div>
          </div>
        </div>
      </template>
    </div>
  </section>
      <div v-if="mostrarDialogPersona"
    class="fixed inset-0 bg-black/50 backdrop-blur-sm flex items-center justify-center z-50 animate-fade-in">

    <div class="bg-white w-full max-w-3xl rounded-3xl shadow-2xl p-8 space-y-6">

      <div class="flex justify-between items-center border-b pb-4">
        <h2 class="text-2xl font-bold text-gray-800">
          Datos para el envío
        </h2>
        <button @click="mostrarDialogPersona = false" class="text-gray-400 hover:text-gray-600 text-xl">
          ✕
        </button>
      </div>

      <!-- Datos personales -->
      <div>
        <h3 class="text-lg font-semibold text-gray-700 mb-4">
          Datos personales
        </h3>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="label">Nombre</label>
            <input v-model="personaForm.nombre" class="input" />
          </div>

          <div>
            <label class="label">Apellido</label>
            <input v-model="personaForm.apellido" class="input" />
          </div>

          <div class="col-span-2">
            <label class="label">DNI</label>
            <input v-model="personaForm.dni" class="input" />
          </div>
        </div>
      </div>

      <!-- Dirección -->
      <div>
        <h3 class="text-lg font-semibold text-gray-700 mb-4">
          Dirección de envío
        </h3>

        <div class="grid grid-cols-2 gap-4">
          <div>
            <label class="label">Calle</label>
            <input v-model="personaForm.calle" class="input" />
          </div>

          <div>
            <label class="label">Número</label>
            <input v-model="personaForm.numero" class="input" />
          </div>

          <div>
            <label class="label">Localidad</label>
            <input v-model="personaForm.localidad" class="input" />
          </div>

          <div>
            <label class="label">Departamento</label>
            <input v-model="personaForm.departamento" class="input" />
          </div>

          <div>
            <label class="label">Provincia</label>
            <input v-model="personaForm.provincia" class="input" />
          </div>

          <div>
            <label class="label">País</label>
            <input v-model="personaForm.pais" class="input" />
          </div>
        </div>
      </div>

      <!-- Botones -->
      <div class="flex justify-end gap-4 pt-4 border-t">
        <button @click="mostrarDialogPersona = false"
          class="px-6 py-2 rounded-xl bg-gray-200 hover:bg-gray-300 transition">
          Cancelar
        </button>

        <button @click="guardarPersonaYContinuar"
          class="px-6 py-2 rounded-xl bg-emerald-900 text-white hover:bg-emerald-800 transition shadow-xl shadow-emerald-100 font-bold">
          Confirmar compra
        </button>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { useCartStore } from '../store/cart'
import { actualizarPersona } from '../Personas/services/personas.service'

const cart = useCartStore()
const loading = ref(false)
const compraRealizada = ref(false)
const loadingItems = ref<Record<number, boolean>>({})

const mostrarDialogPersona = ref(false)

const personaForm = ref({
  nombre: '',
  apellido: '',
  dni: '',
  calle: '',
  numero: '',
  localidad: '',
  departamento: '',
  provincia: '',
  pais: ''
})

onMounted(() => {
  cart.fetchCarrito()
})

const productosAgrupadosPorTienda = computed(() => {
  return cart.productosAgrupados.reduce((acc, producto) => {
    const tienda = producto.marca || 'Tienda Oficial'
    if (!acc[tienda]) acc[tienda] = []
    acc[tienda].push(producto)
    return acc
  }, {} as Record<string, any[]>)
})

const cambiar = async (codigo: number, accion: 'sumar' | 'restar') => {
  loadingItems.value[codigo] = true
  await cart.cambiarCantidad(codigo, accion)
  loadingItems.value[codigo] = false
}

const eliminar = async (codigo: number) => {
  loadingItems.value[codigo] = true
  await cart.cambiarCantidad(codigo, 'restar')
  loadingItems.value[codigo] = false
}

const handleCheckout = () => {
  mostrarDialogPersona.value = true
}

const guardarPersonaYContinuar = async () => {
  loading.value = true

  try {
    await actualizarPersona(personaForm.value)

    mostrarDialogPersona.value = false

    const ok = await cart.checkout()

    if (ok) {
      setTimeout(() => {
        compraRealizada.value = true
        loading.value = false

        setTimeout(() => {
          compraRealizada.value = false
          cart.clearLocalData()
        }, 1000)
      }, 1000)
    }

  } catch (error) {
    console.error(error)
    loading.value = false
  }
}

</script>
<style scoped>
.label {
  display: block;
  font-size: 0.875rem;
  font-weight: 500;
  color: #4b5563;
  margin-bottom: 0.25rem;
}

.input {
  width: 100%;
  border: 1px solid #d1d5db;
  border-radius: 0.75rem;
  padding: 0.5rem 0.75rem;
  outline: none;
}

.input:focus {
  outline: 2px solid transparent;
  outline-offset: 2px;
  ring: 2px;
  border-color: #065f46;
}
</style>