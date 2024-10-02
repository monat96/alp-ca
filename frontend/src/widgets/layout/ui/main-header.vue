<!-- TODO: nav bar 수정하기 -->
<script setup lang="ts">
import AuthLogoutFormButton from '@/features/auth/ui/auth-logout-form-button.vue';

</script>
<template>
  <header class="bg-white shadow-md py-4">
    <div class="container mx-auto flex justify-between items-center px-4">
      <!-- Logo -->
      <div class="text-xl font-bold">N.M.S</div>

      <!-- Auth Buttons -->
      <div class="flex space-x-4">
        <AuthLogoutFormButton />
      </div>
    </div>
  </header>
</template>