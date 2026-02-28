<template>
  <div class="min-h-screen flex flex-col bg-[#fdfcf7]">
    <Navbar />
    <main class="flex-grow container mx-auto px-4 py-8">
      <router-view />
    </main>
    <Footer />
  </div>
</template>


<script lang="ts">
import { defineComponent } from 'vue';
import Navbar from './components/Navbar.vue';
import Footer from './components/Footer.vue';

export default defineComponent({
  name: 'App',
  components: { Navbar, Footer }
});
</script>
