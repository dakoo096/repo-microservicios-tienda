<template>
  <div class="min-h-screen bg-gray-50/50 p-4 md:p-8">
    <!-- Header Section -->
    <header class="mb-10 flex flex-col md:flex-row md:items-center justify-between gap-4">
      <div>
        <h1 class="text-3xl font-extrabold tracking-tight text-gray-900 sm:text-4xl">
          Panel de <span
            class="bg-clip-text text-transparent bg-gradient-to-r from-emerald-800 to-green-600">Administración</span>
        </h1>
        <p class="mt-2 text-gray-600 flex items-center gap-2">
          <span class="relative flex h-2 w-2">
            <span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
            <span class="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
          </span>
          Sesión de Administrador Activa
        </p>
      </div>

      <button @click="openModal = true"
        class="inline-flex items-center justify-center px-8 py-4 bg-emerald-900 text-white rounded-[2rem] font-bold shadow-xl shadow-emerald-100 hover:bg-emerald-800 hover:-translate-y-1 active:scale-95 transition-all duration-300 group">
        <svg xmlns="http://www.w3.org/2000/svg"
          class="w-5 h-5 mr-3 group-hover:rotate-90 transition-transform duration-500" fill="none" viewBox="0 0 24 24"
          stroke="currentColor">
          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2.5" d="M12 4v16m8-8H4" />
        </svg>
        Nuevo Admin
      </button>
    </header>

    <!-- Stats Grid -->
    <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-10">
      <div class="bg-white p-6 rounded-3xl border border-gray-100 shadow-sm hover:shadow-md transition-shadow">
        <div class="flex items-center gap-4">
          <div class="p-3 bg-blue-50 rounded-2xl text-blue-600">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
            </svg>
          </div>
          <div>
            <p class="text-sm font-medium text-gray-500 uppercase tracking-wider">Total Usuarios</p>
            <h3 class="text-2xl font-bold text-gray-900">{{ personas.length }}</h3>
          </div>
        </div>
      </div>

      <div class="bg-white p-6 rounded-3xl border border-gray-100 shadow-sm hover:shadow-md transition-shadow">
        <div class="flex items-center gap-4">
          <div class="p-3 bg-emerald-50 rounded-2xl text-emerald-600">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
            </svg>
          </div>
          <div>
            <p class="text-xs font-bold text-gray-400 uppercase tracking-widest">Administradores</p>
            <h3 class="text-2xl font-bold text-gray-900">{{personas.filter(p => p.rol === 'ADMIN').length}}</h3>
          </div>
        </div>
      </div>

      <div class="bg-white p-6 rounded-3xl border border-gray-100 shadow-sm hover:shadow-md transition-shadow">
        <div class="flex items-center gap-4">
          <div class="p-3 bg-green-50 rounded-2xl text-green-600">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24"
              stroke="currentColor">
              <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                d="M13 7h8m0 0v8m0-8l-8 8-4-4-6 6" />
            </svg>
          </div>
          <div>
            <p class="text-xs font-bold text-gray-400 uppercase tracking-widest">Actividad Vivero</p>
            <h3 class="text-2xl font-bold text-emerald-700">En Línea</h3>
          </div>
        </div>
      </div>
    </div>

    <!-- Table Section -->
    <div class="bg-white rounded-[2rem] shadow-xl shadow-gray-200/50 border border-gray-100 overflow-hidden">
      <div class="px-8 py-6 border-b border-gray-50 flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <h2 class="text-xl font-bold text-gray-800">Cuentas Registradas</h2>
          <p class="text-sm text-gray-500">Gestiona y visualiza todos los usuarios del sistema</p>
        </div>
        <div class="flex items-center gap-2 px-4 py-2 bg-gray-50 rounded-xl border border-gray-100">
          <span class="w-2 h-2 bg-emerald-500 rounded-full"></span>
          <span class="text-xs font-bold text-gray-600 uppercase tracking-widest">{{ personas.length }} Registros</span>
        </div>
      </div>

      <div class="overflow-x-auto">
        <table class="w-full">
          <thead>
            <tr class="bg-gray-50/50">
              <th class="px-8 py-4 text-left text-xs font-bold text-gray-400 uppercase tracking-widest">ID</th>
              <th class="px-8 py-4 text-left text-xs font-bold text-gray-400 uppercase tracking-widest">Usuario</th>
              <th class="px-8 py-4 text-left text-xs font-bold text-gray-400 uppercase tracking-widest">Rol</th>
              <th class="px-8 py-4 text-right text-xs font-bold text-gray-400 uppercase tracking-widest">Acciones</th>
            </tr>
          </thead>

          <tbody class="divide-y divide-gray-50">
            <tr v-for="p in personas" :key="p.id" class="group hover:bg-emerald-50/30 transition-colors">
              <td class="px-8 py-5">
                <span
                  class="inline-flex items-center justify-center w-10 h-10 bg-gray-100 text-gray-500 text-xs font-bold rounded-xl group-hover:bg-white transition-colors">
                  #{{ p.id }}
                </span>
              </td>

              <td class="px-8 py-5">
                <div class="flex items-center gap-3">
                  <div
                    class="w-10 h-10 rounded-full bg-gradient-to-br from-gray-200 to-gray-100 flex items-center justify-center text-gray-600 font-bold uppercase">
                    {{ p.username.charAt(0) }}
                  </div>
                  <div>
                    <div class="font-bold text-gray-900">{{ p.username }}</div>
                    <div class="text-xs text-gray-400">{{ p.rol === 'ADMIN' ? 'Acceso Total' : 'Acceso Limitado' }}
                    </div>
                  </div>
                </div>
              </td>

              <td class="px-8 py-5">
                <span :class="p.rol === 'ADMIN'
                  ? 'bg-emerald-100 text-emerald-700 ring-4 ring-emerald-50'
                  : 'bg-blue-100 text-blue-700 ring-4 ring-blue-50'"
                  class="inline-flex items-center px-4 py-1.5 text-xs font-bold rounded-full transition-all">
                  <span class="w-1.5 h-1.5 rounded-full mr-2"
                    :class="p.rol === 'ADMIN' ? 'bg-emerald-600' : 'bg-blue-600'"></span>
                  {{ p.rol }}
                </span>
              </td>

              <td class="px-8 py-5 text-right">
                <button @click="handleDelete(p.id, p.username)"
                  class="group/btn relative p-2.5 text-gray-400 hover:text-rose-600 hover:bg-rose-50 rounded-2xl transition-all duration-300"
                  title="Eliminar Cuenta">
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 group-hover/btn:scale-110 transition-transform"
                    fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                  </svg>
                  <span
                    class="absolute right-full mr-3 px-2 py-1 bg-gray-900 text-white text-[10px] font-bold rounded-lg opacity-0 group-hover/btn:opacity-100 transition-opacity pointer-events-none whitespace-nowrap">
                    Eliminar usuario
                  </span>
                </button>
              </td>
            </tr>

            <tr v-if="personas.length === 0">
              <td colspan="4" class="px-8 py-16 text-center">
                <div class="flex flex-col items-center gap-3 opacity-30">
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-16 h-16" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M12 4.354a4 4 0 110 5.292M15 21H3v-1a6 6 0 0112 0v1zm0 0h6v-1a6 6 0 00-9-5.197M13 7a4 4 0 11-8 0 4 4 0 018 0z" />
                  </svg>
                  <p class="font-bold text-gray-900">No hay usuarios registrados</p>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- Modal -->
    <div v-if="openModal" class="fixed inset-0 z-50 flex items-center justify-center p-4 sm:p-6">
      <div @click="closeModal" class="absolute inset-0 bg-gray-900/60 backdrop-blur-md transition-opacity"></div>

      <div
        class="relative bg-white w-full max-w-lg rounded-[2.5rem] shadow-2xl overflow-hidden transform transition-all animate-in fade-in zoom-in duration-300">
        <!-- Close Button -->
        <button @click="closeModal"
          class="absolute top-6 right-6 p-2 text-gray-400 hover:text-gray-900 hover:bg-gray-100 rounded-2xl transition-all">
          <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>

        <div class="p-8 sm:p-10">
          <div class="mb-10">
            <div
              class="inline-flex items-center justify-center w-16 h-16 bg-emerald-100 text-emerald-600 rounded-3xl mb-6">
              <svg xmlns="http://www.w3.org/2000/svg" class="w-8 h-8" fill="none" viewBox="0 0 24 24"
                stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                  d="M18 9v3m0 0v3m0-3h3m-3 0h-3m-2-5a4 4 0 11-8 0 4 4 0 018 0zM3 20a6 6 0 0112 0v1H3v-1z" />
              </svg>
            </div>
            <h2 class="text-3xl font-extrabold text-gray-900">Crear Administrador</h2>
            <p class="text-gray-500 mt-2">Personal del vivero con acceso total.</p>
          </div>

          <div class="space-y-6">
            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2 ml-1">Nombre de Usuario</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none text-gray-400">
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                  </svg>
                </div>
                <input v-model="newUsername" placeholder="ej. noelia_vivero"
                  class="w-full pl-12 pr-4 py-4 bg-gray-50 border-2 border-transparent rounded-[1.5rem] focus:bg-white focus:border-emerald-600 outline-none transition-all"
                  type="text" />
              </div>
            </div>

            <div>
              <label class="block text-sm font-bold text-gray-700 mb-2 ml-1">Contraseña Segura</label>
              <div class="relative">
                <div class="absolute inset-y-0 left-0 pl-4 flex items-center pointer-events-none text-gray-400">
                  <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5" fill="none" viewBox="0 0 24 24"
                    stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                      d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                  </svg>
                </div>
                <input v-model="newPassword" type="password" placeholder="••••••••"
                  class="w-full pl-12 pr-4 py-4 bg-gray-50 border-2 border-transparent rounded-[1.5rem] focus:bg-white focus:border-emerald-600 outline-none transition-all" />
              </div>
            </div>

            <div class="pt-4 flex flex-col-reverse sm:flex-row gap-4">
              <button @click="closeModal"
                class="flex-1 px-8 py-4 rounded-2xl font-bold bg-gray-100 text-gray-600 hover:bg-gray-200 transition-all">
                Cancelar
              </button>
              <button @click="createAdmin" :disabled="loading"
                class="flex-1 px-8 py-4 rounded-2xl font-bold bg-emerald-900 text-white shadow-xl shadow-emerald-100 hover:bg-emerald-800 disabled:opacity-50 disabled:cursor-not-allowed transition-all">
                <span v-if="!loading">Crear Cuenta</span>
                <span v-else class="flex items-center justify-center gap-2">
                  <svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none"
                    viewBox="0 0 24 24">
                    <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                    <path class="opacity-75" fill="currentColor"
                      d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z">
                    </path>
                  </svg>
                  Creando...
                </span>
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import { ref, onMounted } from "vue"
import axios from "../services/axiosConfig"
import { useAuthStore } from "../store/auth"
import { eliminarPersona } from "../Personas/services/personas.service"
import Swal from "sweetalert2"

interface Persona {
  id: number
  username: string
  rol: string
}

const auth = useAuthStore()

const personas = ref<Persona[]>([])
const newUsername = ref("")
const newPassword = ref("")
const loading = ref(false)

const fetchPersonas = async () => {
  const response = await axios.get("/ms-personas/personas")
  personas.value = response.data
}

const createAdmin = async () => {
  if (!newUsername.value || !newPassword.value) return

  loading.value = true

  await axios.post(
    "/ms-personas/personas",
    {
      username: newUsername.value,
      password: newPassword.value,
      rol: "ADMIN"
    },
    {
      headers: {
        "X-Rol": auth.role // 👈 CLAVE
      }
    }
  )

  await fetchPersonas()

  loading.value = false
  closeModal()
}

const openModal = ref(false)

const closeModal = () => {
  openModal.value = false
  newUsername.value = ""
  newPassword.value = ""
}

const handleDelete = async (id: number, username: string) => {
  const result = await Swal.fire({
    title: '¿Eliminar usuario?',
    text: `Estás a punto de eliminar permanentemente a "${username}". Esta acción no se puede deshacer.`,
    icon: 'warning',
    showCancelButton: true,
    confirmButtonColor: '#065f46',
    cancelButtonColor: '#6b7280',
    confirmButtonText: 'Sí, eliminar cuenta',
    cancelButtonText: 'Cancelar',
    reverseButtons: true,
    background: '#ffffff',
    customClass: {
      popup: 'rounded-[2rem] border-none shadow-2xl',
      confirmButton: 'rounded-2xl px-6 py-3 font-bold',
      cancelButton: 'rounded-2xl px-6 py-3 font-bold'
    }
  })

  if (result.isConfirmed) {
    try {
      await eliminarPersona(id)

      await Swal.fire({
        title: '¡Eliminado!',
        text: 'El usuario ha sido removido correctamente.',
        icon: 'success',
        timer: 2000,
        showConfirmButton: false,
        iconColor: '#059669',
        customClass: {
          popup: 'rounded-[2rem]'
        }
      })

      await fetchPersonas()
    } catch (error) {
      Swal.fire({
        title: 'Error',
        text: 'No se pudo eliminar el usuario. Inténtalo de nuevo.',
        icon: 'error',
        customClass: {
          popup: 'rounded-[2rem]'
        }
      })
    }
  }
}
onMounted(fetchPersonas)
</script>
