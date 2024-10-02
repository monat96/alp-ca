<script setup lang="ts">
import { getPingStatus, PingStatusSchema, type PingStatus } from '@/entities/ping';
import { useAuthStore } from '@/entities/user';
import { PingStatusCard } from '@/widgets/ping';
import { useQuery } from '@tanstack/vue-query';
import { CheckCheckIcon, TimerOffIcon, TrendingDownIcon, XIcon } from 'lucide-vue-next';
import { type FunctionalComponent } from 'vue';

const STATUS_ICON: Record<PingStatus, FunctionalComponent> = {
  'SUCCESS': CheckCheckIcon,
  'FAIL': XIcon,
  'LOSS': TrendingDownIcon,
  'TIMEOUT': TimerOffIcon
} as const;

const { authorizationHeader, isAuthenticated } = useAuthStore();

const { data: pingStatusCountData, isLoading, isError } = useQuery({
  queryKey: ['ping', 'status'],
  queryFn: () => getPingStatus(authorizationHeader()).then(response => response!.data),
  enabled: () => isAuthenticated(),

})

</script>
<template>
  <div class="grid gap-4 md:grid-cols-2 lg:grid-cols-5">
    <PingStatusCard title="total"
      :value="((pingStatusCountData?.status?.FAIL ?? 0) + (pingStatusCountData?.status?.LOSS ?? 0) + (pingStatusCountData?.status?.SUCCESS ?? 0) + (pingStatusCountData?.status?.TIMEOUT ?? 0)) ?? 0"
      :description="new Date(pingStatusCountData?.requestTime!).toLocaleString()" />
    <PingStatusCard :title="PingStatusSchema.enum.SUCCESS" :icon="STATUS_ICON.SUCCESS"
      :value="pingStatusCountData?.status?.SUCCESS ?? 0" />
    <PingStatusCard :title="PingStatusSchema.enum.LOSS" :icon="STATUS_ICON.LOSS"
      :value="pingStatusCountData?.status?.LOSS ?? 0" />
    <PingStatusCard :title="PingStatusSchema.enum.FAIL" :icon="STATUS_ICON.FAIL"
      :value="pingStatusCountData?.status?.FAIL ?? 0" />
    <PingStatusCard :title="PingStatusSchema.enum.TIMEOUT" :icon="STATUS_ICON.TIMEOUT"
      :value="pingStatusCountData?.status?.TIMEOUT ?? 0" />
  </div>
</template>
