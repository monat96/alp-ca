<script setup lang="ts">
import { pingStatusColor } from '@/entities/ping';
import { Badge } from '@/shared/ui/badge';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/shared/ui/card'

interface PingChartTooltipProps {
  title?: string
  data: {
    name: string
    color: string
    value: any
  }[]
}

const { title, data } = defineProps<PingChartTooltipProps>()

const visibleDataNames = ['RTT_MAX', 'RTT_AVG', 'RTT_MIN']
const visibleData = data.filter(item => visibleDataNames.includes(item.name))

const status = data.find(item => item.name === 'STATUS')
const packet_loss_rate = data.find(item => item.name === 'PACKET_LOSS_RATE')



</script>

<template>
  <Card class="text-sm">
    <CardHeader v-if="title" class="p-3 border-b">
      <CardTitle>
        {{ title }}
        <Badge
          :class="`bg-${pingStatusColor[status?.value as keyof typeof pingStatusColor]}-500 w-20 items-center justify-center`">
          {{ status?.value }}
        </Badge>
      </CardTitle>
      <CardDescription>
        Packet Loss Rate: {{ packet_loss_rate?.value }}%
      </CardDescription>
    </CardHeader>
    <CardContent class="p-3 min-w-[180px] flex flex-col gap-1">
      <div v-for="(item, key) in visibleData" :key="key" class="flex justify-between">
        <div class="flex items-center">
          <span class="w-2.5 h-2.5 mr-2">
            <svg width="100%" height="100%" viewBox="0 0 30 30">
              <path d=" M 15 15 m -14, 0 a 14,14 0 1,1 28,0 a 14,14 0 1,1 -28,0" :stroke="item.color" :fill="item.color"
                stroke-width="1" />
            </svg>
          </span>
          <span>{{ item.name }}</span>
        </div>
        <span class="font-semibold ml-4">{{ item.value }} ms</span>
      </div>
    </CardContent>
  </Card>
</template>