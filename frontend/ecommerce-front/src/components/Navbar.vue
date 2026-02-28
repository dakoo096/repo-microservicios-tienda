<template>
  <nav class="bg-white/70 backdrop-blur-xl border-b border-gray-100/50 sticky top-0 z-50 transition-all duration-300">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
      <div class="flex items-center justify-between h-20">

        <!-- Brand & Logo -->
        <router-link class="group flex items-center gap-3 no-underline" to="/">
          <div
            class="w-10 h-10 bg-gradient-to-br from-emerald-600 to-green-500 rounded-xl flex items-center justify-center shadow-lg shadow-emerald-200 group-hover:scale-105 transition-transform duration-300">
            <span class="text-xl">🌿</span>
          </div>
          <span class="text-2xl font-black tracking-tight text-gray-900 leading-none">
            E-<span class="bg-clip-text text-transparent bg-gradient-to-r from-emerald-700 to-green-600">Commerce</span>
          </span>
        </router-link>

        <!-- Desktop Navigation -->
        <div class="hidden lg:flex items-center gap-8">
          <!-- Links -->
          <div class="flex items-center gap-1">
            <router-link to="/"
              class="px-4 py-2 text-sm font-bold text-gray-600 hover:text-emerald-700 rounded-xl hover:bg-emerald-50 transition-all duration-200">
              Inicio
            </router-link>
            <router-link to="/productos"
              class="px-4 py-2 text-sm font-bold text-gray-600 hover:text-emerald-700 rounded-xl hover:bg-emerald-50 transition-all duration-200">
              Productos
            </router-link>
          </div>

          <!-- Search Bar -->
          <form class="relative group" @submit.prevent>
            <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none text-gray-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
            <input v-model="search"
              class="w-64 pl-11 pr-4 py-2.5 bg-gray-50/50 border-2 border-transparent rounded-[1.25rem] text-sm focus:bg-white focus:border-emerald-600 focus:ring-4 focus:ring-emerald-50 outline-none transition-all duration-300"
              type="search" placeholder="Buscar plantas..." />
          </form>

          <!-- Action Icons -->
          <div class="flex items-center gap-4 pl-4 border-l border-gray-100">
            <!-- Carrito -->
            <router-link to="/carrito"
              class="relative group p-2.5 bg-white hover:bg-emerald-50 border border-gray-100 rounded-2xl transition-all duration-300">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6 text-gray-600 group-hover:text-emerald-700"
                fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
              </svg>
              <span v-if="cart.totalItems > 0"
                class="absolute -top-1.5 -right-1.5 flex h-5 w-5 items-center justify-center rounded-lg bg-emerald-600 text-[10px] font-black text-white shadow-lg shadow-emerald-200 ring-2 ring-white animate-in zoom-in">
                {{ cart.totalItems }}
              </span>
            </router-link>

            <!-- Auth/Profile -->
            <template v-if="!auth.isAuthenticated">
              <router-link to="/login"
                class="px-6 py-2.5 rounded-2xl bg-gray-900 text-white text-sm font-bold shadow-lg shadow-gray-200 hover:bg-gray-800 hover:-translate-y-0.5 transition-all active:scale-95">
                Acceder
              </router-link>
            </template>

            <template v-else>
              <div class="relative">
                <button @click="toggleProfile"
                  class="flex items-center gap-3 pl-2 pr-4 py-1.5 bg-white border border-gray-100 rounded-2xl shadow-sm hover:shadow-md hover:border-emerald-100 transition-all duration-300">
                  <div
                    class="w-8 h-8 rounded-xl bg-gradient-to-br from-emerald-600 to-green-500 text-white flex items-center justify-center text-xs font-black shadow-inner">
                    {{ auth.user?.charAt(0).toUpperCase() }}
                  </div>
                  <span class="text-sm font-bold text-gray-700 max-w-[100px] truncate">
                    {{ auth.user }}
                  </span>
                  <svg xmlns="http://www.w3.org/2000/svg"
                    class="w-4 h-4 text-gray-400 transition-transform duration-300"
                    :class="{ 'rotate-180': openProfile }" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                  </svg>
                </button>

                <!-- Dropdown -->
                <div v-if="openProfile"
                  class="absolute right-0 mt-3 w-64 bg-white border border-gray-50 rounded-[2rem] shadow-2xl py-3 overflow-hidden animate-in fade-in slide-in-from-top-2 duration-200">
                  <div class="px-5 py-3 mb-2 border-b border-gray-50">
                    <p class="text-[10px] font-black uppercase tracking-widest text-gray-400">Cuenta de Usuario</p>
                    <p class="text-sm font-bold text-gray-900 truncate">{{ auth.user }}</p>
                  </div>

                  <div class="px-2 space-y-1">
                    <router-link v-if="auth.isClient" to="/mis-compras"
                      class="flex items-center gap-3 px-4 py-2.5 text-sm font-bold text-gray-600 hover:text-emerald-700 hover:bg-emerald-50 rounded-2xl transition-all"
                      @click="closeAll">
                      <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 opacity-60" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M16 11V7a4 4 0 00-8 0v4M5 9h14l1 12H4L5 9z" />
                      </svg>
                      Mis Compras
                    </router-link>

                    <router-link v-if="auth.isAdmin" to="/ventas"
                      class="flex items-center gap-3 px-4 py-2.5 text-sm font-bold text-gray-600 hover:text-emerald-700 hover:bg-emerald-50 rounded-2xl transition-all"
                      @click="closeAll">
                      <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 opacity-60" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                      </svg>
                      Ver Ventas
                    </router-link>

                    <router-link v-if="auth.isAdmin" to="/agregarProducto"
                      class="flex items-center gap-3 px-4 py-2.5 text-sm font-bold text-gray-600 hover:text-emerald-700 hover:bg-emerald-50 rounded-2xl transition-all"
                      @click="closeAll">
                      <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 opacity-60" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                      </svg>
                      Agregar Producto
                    </router-link>

                    <router-link v-if="auth.isAdmin" to="/admin"
                      class="flex items-center gap-3 px-4 py-2.5 text-sm font-bold text-gray-600 hover:text-emerald-700 hover:bg-emerald-50 rounded-2xl transition-all"
                      @click="closeAll">
                      <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M10.325 4.317c.426-1.756 2.924-1.756 3.35 0a1.724 1.724 0 002.573 1.066c1.543-.94 3.31.826 2.37 2.37a1.724 1.724 0 001.065 2.572c1.756.426 1.756 2.924 0 3.35a1.724 1.724 0 00-1.066 2.573c.94 1.543-.826 3.31-2.37 2.37a1.724 1.724 0 00-2.572 1.065c-.426 1.756-2.924 1.756-3.35 0a1.724 1.724 0 00-2.573-1.066c-1.543.94-3.31-.826-2.37-2.37a1.724 1.724 0 00-1.065-2.572c-1.756-.426-1.756-2.924 0-3.35a1.724 1.724 0 001.066-2.573c-.94-1.543.826-3.31 2.37-2.37.996.608 2.296.07 2.572-1.065z" />
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
                      </svg>
                      Panel Admin
                    </router-link>
                  </div>

                  <div class="mt-4 px-2 pt-2 border-t border-gray-50">
                    <button @click="handleLogout"
                      class="w-full flex items-center gap-3 px-4 py-2.5 text-sm font-bold text-gray-400 hover:text-emerald-700 hover:bg-emerald-50 rounded-2xl transition-all">
                      <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 opacity-60" fill="none" viewBox="0 0 24 24"
                        stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                          d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a3 3 0 01-3 3H6a3 3 0 01-3-3V7a3 3 0 013-3h4a3 3 0 013 3v1" />
                      </svg>
                      Cerrar Sesión
                    </button>
                  </div>
                </div>
              </div>
            </template>
          </div>
        </div>

        <!-- Mobile Toggle -->
        <button @click="open = !open"
          class="lg:hidden p-3 bg-white border border-gray-100 rounded-2xl text-gray-600 hover:bg-emerald-50 hover:text-emerald-700 transition-all active:scale-95">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path v-if="!open" stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
              d="M4 6h16M4 12h16m-7 6h7" />
            <path v-else stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <!-- Mobile Menu -->
      <transition enter-active-class="transition duration-300 ease-out"
        enter-from-class="transform -translate-y-4 opacity-0" enter-to-class="transform translate-y-0 opacity-100"
        leave-active-class="transition duration-200 ease-in" leave-from-class="transform translate-y-0 opacity-100"
        leave-to-class="transform -translate-y-4 opacity-0">
        <div v-show="open" class="lg:hidden pb-6 space-y-4 animate-in fade-in slide-in-from-top-4 duration-300">
          <div class="relative px-2">
            <div class="absolute inset-y-0 left-4 flex items-center pointer-events-none text-gray-400">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
              </svg>
            </div>
            <input v-model="search"
              class="w-full pl-11 pr-4 py-3 bg-white border-2 border-transparent rounded-2xl text-sm focus:bg-white focus:border-emerald-600 outline-none transition-all"
              type="search" placeholder="Buscar plantas..." />
          </div>

          <div class="grid grid-cols-2 gap-2 px-2">
            <router-link to="/" @click="closeAll"
              class="flex flex-col items-center gap-1 p-4 bg-white border border-gray-100 rounded-2xl font-bold text-gray-700 hover:bg-emerald-50 hover:text-emerald-700 transition-all">
              <span class="text-xl">🌿</span>
              <span class="text-xs">Inicio</span>
            </router-link>
            <router-link to="/productos" @click="closeAll"
              class="flex flex-col items-center gap-1 p-4 bg-white border border-gray-100 rounded-2xl font-bold text-gray-700 hover:bg-emerald-50 hover:text-emerald-700 transition-all">
              <span class="text-xl">🌱</span>
              <span class="text-xs">Catálogo</span>
            </router-link>
          </div>

          <!-- Mis Compras (Client Only) -->
          <div v-if="auth.isClient && auth.isAuthenticated" class="px-2">
            <router-link to="/mis-compras" @click="closeAll"
              class="flex items-center gap-3 p-4 bg-white border border-gray-100 rounded-2xl font-bold text-gray-700 hover:bg-emerald-50 transition-all">
              <span class="text-xl">📦</span>
              <span>Mis Pedidos</span>
            </router-link>
          </div>

          <div class="px-2">
            <router-link to="/carrito" @click="closeAll"
              class="flex items-center justify-between p-4 bg-emerald-700 rounded-2xl font-bold text-white shadow-lg shadow-emerald-100">
              <div class="flex items-center gap-3">
                <span class="text-xl">🛒</span>
                <span>Mi Carrito</span>
              </div>
              <span v-if="cart.totalItems > 0" class="px-3 py-1 bg-white/20 rounded-lg text-xs font-black">
                {{ cart.totalItems }}
              </span>
            </router-link>
          </div>

          <div v-if="auth.isAdmin && auth.isAuthenticated" class="px-2 pt-4 border-t border-gray-100 space-y-2">
            <p class="px-4 text-[10px] font-black text-gray-400 uppercase tracking-widest">Administración</p>
            <router-link v-if="auth.isAdmin" to="/ventas" @click="closeAll"
              class="flex items-center gap-3 p-4 bg-white border border-gray-100 rounded-2xl font-bold text-gray-700">
              Ver Ventas
            </router-link>
            <router-link v-if="auth.isAdmin" to="/agregarProducto" @click="closeAll"
              class="flex items-center gap-3 p-4 bg-white border border-gray-100 rounded-2xl font-bold text-gray-700">
              Agregar Producto
            </router-link>
            <router-link v-if="auth.isAdmin" to="/admin" @click="closeAll"
              class="flex items-center gap-3 p-4 bg-emerald-50 rounded-2xl font-bold text-emerald-800">
              Panel Admin
            </router-link>
            <button @click="handleLogout" class="w-full p-4 bg-gray-50 text-gray-400 font-bold rounded-2xl">
              Cerrar Sesión
            </button>
          </div>
          <div v-else class="px-2 pt-4">
            <router-link to="/login" @click="closeAll"
              class="flex items-center justify-center p-4 bg-emerald-900 text-white rounded-2xl font-bold">
              Acceder a mi cuenta
            </router-link>
          </div>
        </div>
      </transition>
    </div>
  </nav>
</template>

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useCartStore } from '../store/cart'
import { useAuthStore } from '../store/auth'
import Swal from 'sweetalert2'

const cart = useCartStore()
const auth = useAuthStore()
const router = useRouter()
const route = useRoute()

const open = ref(false)
const openProfile = ref(false)

const search = ref(route.query.q?.toString() || '')

const toggleProfile = () => {
  openProfile.value = !openProfile.value
}

const closeAll = () => {
  open.value = false
  openProfile.value = false
}

const handleLogout = async () => {
  const result = await Swal.fire({
    title: '¿Vas a salir del jardín?',
    text: 'Esperamos verte pronto entre nuestras plantas 🌿',
    icon: 'question',
    showCancelButton: true,
    confirmButtonColor: '#065f46',
    cancelButtonColor: '#6b7280',
    confirmButtonText: 'Sí, salir',
    cancelButtonText: 'Seguir aquí',
    background: '#ffffff',
    customClass: {
      popup: 'rounded-[1.5rem] border-none shadow-2xl',
      confirmButton: 'rounded-xl px-6 py-2.5 font-bold',
      cancelButton: 'rounded-xl px-6 py-2.5 font-bold'
    }
  })

  if (result.isConfirmed) {
    openProfile.value = false
    open.value = false

    await Swal.fire({
      title: 'Cerrando el jardín...',
      html: `
        <div class="space-y-4 py-4">
          <div class="text-4xl animate-pulse">🌿</div>
          <p class="text-gray-600 font-medium">¡Vuelve pronto para ver qué ha brotado! 🌱</p>
        </div>
      `,
      timer: 1800,
      timerProgressBar: true,
      allowOutsideClick: false,
      showConfirmButton: false,
      didOpen: () => {
        Swal.showLoading()
      },
      customClass: {
        popup: 'rounded-[2rem] border-none shadow-2xl',
        timerProgressBar: 'bg-emerald-500'
      }
    })

    auth.logout()
    router.push('/')
  }
}

watch(search, (value) => {
  router.replace({
    path: '/productos',
    query: value ? { q: value } : {}
  })
})

watch(route, () => {
  closeAll()
})

onMounted(() => {
  cart.fetchCarrito()
})
</script>