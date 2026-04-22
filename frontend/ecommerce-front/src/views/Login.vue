<template>
  <div class="min-h-[80vh] flex items-center justify-center py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8 bg-white p-10 rounded-2xl shadow-xl border border-gray-100">
      <div>
        <div class="flex justify-center">
          <span class="text-5xl">🛒</span>
        </div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          Iniciar Sesión
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          Ingresa tus credenciales para continuar
        </p>
      </div>

      <form class="mt-8 space-y-6" @submit.prevent="handleLogin">
        <div class="rounded-md shadow-sm -space-y-px">
          <div class="mb-4">
            <label for="username" class="block text-sm font-medium text-gray-700 mb-1">Usuario</label>
            <input v-model="username" id="username" name="username" type="text" required
              class="appearance-none rounded-lg relative block w-full px-3 py-3 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm transition-all"
              placeholder="Tu nombre de usuario" />
          </div>
          <div>
            <label for="password" class="block text-sm font-medium text-gray-700 mb-1">Contraseña</label>
            <input v-model="password" id="password" name="password" type="password" required
              class="appearance-none rounded-lg relative block w-full px-3 py-3 border border-gray-300 placeholder-gray-500 text-gray-900 focus:outline-none focus:ring-primary focus:border-primary focus:z-10 sm:text-sm transition-all"
              placeholder="••••••••" />
          </div>
        </div>

        <div>
          <button type="submit" :disabled="loading"
            class="group relative w-full flex justify-center py-3 px-4 border border-transparent text-sm font-bold rounded-lg text-white bg-primary hover:bg-primary-600 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-all disabled:opacity-50 disabled:cursor-not-allowed">
            <span v-if="loading" class="animate-spin mr-2">⏳</span>
            {{ loading ? 'Iniciando...' : 'Entrar' }}
          </button>
        </div>
      </form>

      <div class="text-center mt-4">
        <span class="text-sm text-gray-500">¿No tenés cuenta?</span>
        <router-link to="/register" class="ml-1 text-sm font-semibold text-primary hover:underline">
          Crear usuario
        </router-link>
      </div>

      <div class="text-center mt-4 text-xs text-gray-400">
        Demo: Prueba con admin/admin o cliente/cliente
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from '../store/auth';
import { useCartStore } from '../store/cart';
import { authService } from '../services/authService';
import Swal from 'sweetalert2';
import { jwtDecode } from 'jwt-decode';
import { crearCarrito } from '../Carrito/services/carrito.service';

const router = useRouter();
const authStore = useAuthStore();
const cartStore = useCartStore();

const username = ref('');
const password = ref('');
const loading = ref(false);

const handleLogin = async () => {
  loading.value = true;

  try {
    const response = await authService.login(username.value, password.value);

    // Decodificamos token para obtener personaId
    const decoded: any = jwtDecode(response.token);
    const personaId = decoded.personaId;

    // 1️⃣ Guardar auth
    authStore.setAuth(
      response.token,     // string: JWT
      response.rol,       // string: "ADMIN" | "CLIENTE"
      response.username,  // string
      personaId           // number
    );

    // 2️⃣ Solo sincronizar si ya existe carrito
    if (cartStore.carritoId) {
      await cartStore.fetchCarrito();
    }

    await Swal.fire({
      icon: 'success',
      title: '¡Bienvenido!',
      text: `Hola ${response.username}, sesión iniciada correctamente.`,
      timer: 2000,
      showConfirmButton: false
    });

    router.push('/');
  } catch (error) {
    Swal.fire({
      icon: 'error',
      title: 'Error',
      text: 'Credenciales inválidas o problema de conexión.'
    });
  } finally {
    loading.value = false;
  }
};
</script>