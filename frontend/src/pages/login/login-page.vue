<script setup lang="ts">
import { AuthRegister, AuthLoginForm } from '@/features/auth/ui';
</script>
<template>
  <div class="min-h-screen flex">
    <!-- background image -->
    <div class="hidden md:block w-1/2 bg-cover bg-center" style="background-image: url('/network.jpg');"></div>
    <div class="w-full md:w-1/2 flex flex-col items-center justify-center p-4">
      <AuthLoginForm />
    </div>
  </div>

</template>
