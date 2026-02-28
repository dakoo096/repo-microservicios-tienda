<template>
  <footer class="bg-gray-900 text-gray-300 mt-auto">
    <div class="container mx-auto px-4 py-8">
      <div class="flex flex-col md:flex-row items-center justify-between gap-4">
        
        <!-- Copyright -->
        <div class="text-sm">
          <span>© 2026 E-Commerce. Todos los derechos reservados.</span>
        </div>

        <!-- Socials -->
        <div class="flex items-center gap-6">
          <a href="#" class="hover:text-primary transition-colors">Facebook</a>
          <a href="#" class="hover:text-primary transition-colors">Instagram</a>
        </div>

      </div>
    </div>
  </footer>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent({
  name: 'Footer'
})
</script>


