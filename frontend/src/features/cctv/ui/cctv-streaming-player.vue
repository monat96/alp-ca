<script setup lang="ts">
import Hls from 'hls.js';
import { Skeleton } from '@/shared/ui/skeleton';
import { VideoOffIcon } from 'lucide-vue-next';
import { onMounted, onUnmounted, ref } from 'vue';

import type { CCTV } from '@/entities/cctv';

type HLSStreamingPlayerProps = {
  hlsAddress: CCTV['hlsAddress']
};

const { hlsAddress } = defineProps<HLSStreamingPlayerProps>();

const video = ref<HTMLVideoElement | null>(null);
const hasError = ref(false);
const isLoading = ref(true);

let hls: Hls | null = null;

const initializeHls = () => {
  if (Hls.isSupported() && video.value) {
    hls = new Hls();
    hls.loadSource(hlsAddress);
    hls.attachMedia(video.value);

    hls.on(Hls.Events.MANIFEST_PARSED, () => {
      video.value!.play().then(() => isLoading.value = false);
    });

    hls.on(Hls.Events.ERROR, (event, data) => {
      if (data.fatal) {
        hasError.value = true;
        isLoading.value = false;
        switch (data.type) {
          case Hls.ErrorTypes.NETWORK_ERROR:
            hls!.startLoad();
            break;
          case Hls.ErrorTypes.MEDIA_ERROR:
            hls!.recoverMediaError();
            break;
          default:
            hls!.destroy();
            break;
        }
      }
    });
  } else if (video.value) {
    video.value.src = hlsAddress;
    video.value.play()
      .then(() => isLoading.value = false)
      .catch(() => {
        hasError.value = true;
        isLoading.value = false;
      })
  }
};


onMounted(() => {
  initializeHls();
});

onUnmounted(() => {
  if (hls) {
    hls.destroy();
    hls = null; // 인스턴스가 중복되지 않도록 설정
  }
});


</script>
<template>
  <div class="relative w-full h-full">
    <video ref="video" autoplay></video>
    <Skeleton v-if="isLoading" class="absolute inset-0 flex items-center justify-center" />
    <VideoOffIcon v-if="hasError" class="absolute inset-0 flex items-center justify-center w-full h-full" />
  </div>
</template>