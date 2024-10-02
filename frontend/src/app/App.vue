<script setup lang="ts">
import { RouterView, useRouter } from 'vue-router'
import { VueQueryDevtools } from '@tanstack/vue-query-devtools'
import "@/app/styles/index.css"
import "./providers"
import { useAuthStore } from '@/entities/user';
import { onMounted, onUnmounted } from 'vue';

const { isAuthenticated } = useAuthStore()
const router = useRouter()
let intervalId: number


onMounted(() => {
  intervalId = setInterval(() => {
    if (!isAuthenticated()) {
      router.push({ name: 'login' })
    }
  }, 10000) // 1분마다 체크
})

onUnmounted(() => {
  clearInterval(intervalId)
})

</script>

<template>
  <RouterView />
  <VueQueryDevtools />
</template>