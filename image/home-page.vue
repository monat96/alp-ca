<script setup lang="ts">
import { CCTVTable, CCTVCardList } from '@/features/cctv';
import { PingStatusList } from '@/features/ping';
import { MainHeader } from '@/widgets/layout';
</script>

<template>
  <MainHeader />
  <main class="m-10">
    <div class="flex flex-col gap-4">
      <PingStatusList />
      <CCTVCardList />
      <CCTVTable />
    </div>
  </main>
</template>
