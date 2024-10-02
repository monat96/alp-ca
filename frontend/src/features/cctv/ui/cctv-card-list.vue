<script setup lang="ts">
import AutoPlay from 'embla-carousel-autoplay';
import { Carousel, CarouselContent, CarouselItem } from '@/shared/ui/carousel'

import { useQuery } from '@tanstack/vue-query';
import { getCCTVList, type CCTV } from '@/entities/cctv';
import { useAuthStore } from '@/entities/user';
import { computed, ref } from 'vue';
import { useIntersectionObserver } from '@vueuse/core';
import { chunkArray } from '@/shared/lib/utils';
import CCTVCard from './cctv-card.vue';
const { authorizationHeader, isAuthenticated } = useAuthStore();

const CHUNK_SIZE = 8;

const { data: cctvList = ref([]), isError, isLoading } = useQuery({
  queryKey: ['cctv', 'list'],
  queryFn: () => getCCTVList(authorizationHeader()).then(response => response!.data),
  enabled: () => isAuthenticated(),
})

const plugin = AutoPlay({
  delay: 10 * 1000,
  stopOnMouseEnter: false,
  stopOnInteraction: false,
})

const isVisible = ref(false);
const target = ref(null);

useIntersectionObserver(target, ([{ isIntersecting }]) => {
  isVisible.value = isIntersecting;
}, {
  threshold: 0.5,
})

const groupedCctvList = computed(() => {
  if (cctvList.value && cctvList.value.length > 0) {
    return chunkArray<CCTV>(cctvList.value as CCTV[], CHUNK_SIZE);
  }
  return [];
});

</script>
<template>
  <Carousel class="h-auto p-5" :plugins="[plugin]" @mouseenter="plugin.stop()"
    @mouseleave="[plugin.reset(), plugin.play()]">
    <CarouselContent>
      <CarouselItem v-for="(group, index) in groupedCctvList" :key="index" class="w-full grid gap-4 lg:grid-cols-4">
        <CCTVCard v-for="(cctv) in group" :key="cctv.cctvId" :cctv="cctv" />
      </CarouselItem>
    </CarouselContent>
  </Carousel>
</template>