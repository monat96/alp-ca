<script setup lang="ts">
import { LineChart } from '@/shared/ui/chart-line'
import { useQuery } from '@tanstack/vue-query';
import { getPingListByCctvId } from '@/entities/ping';
import { useAuthStore } from '@/entities/user';
import pingChartTooltip from './ping-chart-tooltip.vue';

interface PingLineChartProps {
  id: string;
}

const { authorizationHeader, isAuthenticated } = useAuthStore();

const { id } = defineProps<PingLineChartProps>();

const { data: pingListByCctvIdData = [], isLoading } = useQuery({
  queryKey: ['ping', 'cctv', id],
  queryFn: () => getPingListByCctvId(id, authorizationHeader()).then(response => response!.data),
  enabled: () => isAuthenticated(),
  select: (data) => data?.map(ping => ({
    RTT_MAX: ping.rttMax,
    RTT_AVG: ping.rttAvg,
    RTT_MIN: ping.rttMin,
    PACKET_LOSS_RATE: ping.packetLossRate,
    STATUS: ping.status,
    requestTime: new Date(ping.requestTime).toLocaleString(),
  })),
})
</script>
<template>
  <LineChart v-if="!isLoading" :data="pingListByCctvIdData ?? []" index="requestTime"
    :categories="['RTT_MAX', 'RTT_AVG', 'RTT_MIN', 'PACKET_LOSS_RATE', 'STATUS']" :custom-tooltip="pingChartTooltip" />
</template>