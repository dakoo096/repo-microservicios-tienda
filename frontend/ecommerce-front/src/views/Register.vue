<template>
  <div class="min-h-[80vh] flex items-center justify-center py-12 px-4">
    <div class="max-w-md w-full space-y-8 bg-white p-10 rounded-2xl shadow-xl border border-gray-100">

      <div class="text-center">
        <span class="text-5xl">✨</span>
        <h2 class="mt-4 text-3xl font-extrabold text-gray-900">
          Crear Cuenta
        </h2>
        <p class="mt-2 text-sm text-gray-600">
          Completá los datos para registrarte
        </p>
      </div>

      <form class="mt-8 space-y-4" @submit.prevent="handleRegister">

        <input v-model="username" type="text" placeholder="Usuario" required
          class="w-full px-3 py-3 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary" />

        <input v-model="password" type="password" placeholder="Contraseña" required
          class="w-full px-3 py-3 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary" />

        <button type="submit" :disabled="loading"
          class="w-full py-3 bg-primary text-white font-bold rounded-lg hover:bg-primary-600 transition disabled:opacity-50">
          {{ loading ? 'Creando...' : 'Crear Cuenta' }}
        </button>

      </form>

      <div class="text-center text-sm text-gray-500">
        ¿Ya tenés cuenta?
        <router-link to="/login" class="text-primary font-semibold hover:underline">
          Iniciar sesión
        </router-link>
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import Swal from 'sweetalert2'

const router = useRouter()

const username = ref('')
const password = ref('')
const loading = ref(false)

// ⚡ Creamos un axios independiente sin el interceptor
const apiNoAuth = axios.create({
  baseURL: 'http://localhost:8080'
})

const handleRegister = async () => {
  loading.value = true
  try {
    await apiNoAuth.post('/ms-personas/personas', {
      username: username.value,
      password: password.value,
      rol: 'CLIENTE'
    })

    await Swal.fire({
      icon: 'success',
      title: 'Cuenta creada',
      text: 'Ahora podés iniciar sesión 🎉',
      timer: 1800,
      showConfirmButton: false
    })

    router.push('/login')

  } catch (error) {
    console.error(error)
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: 'No se pudo crear el usuario'
    })
  } finally {
    loading.value = false
  }
}
</script>