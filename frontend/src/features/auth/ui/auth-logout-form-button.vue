<script setup lang="ts">
import { Button } from '@/shared/ui/button';
import { useAuthStore, logout } from '@/entities/user';
import { useRouter } from 'vue-router';
import { useForm } from 'vee-validate';
import { LogOutIcon } from 'lucide-vue-next';

const router = useRouter();

const { clearTokens, refreshTokenHeader } = useAuthStore();

const { handleSubmit } = useForm();


const onSubmit = handleSubmit(
  () => logout(refreshTokenHeader())
    .then(clearTokens)
    .then(() => router.replace('/login'))
    .catch(console.error)
);

</script>
<template>
  <form class="w-2/3 space-y-6" @submit="onSubmit">
    <Button type="submit" variant="secondary">
      <LogOutIcon />
    </Button>
  </form>
</template>