<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/shared/ui/card';

import type { PingStatus } from '@/entities/ping';
import type { FunctionalComponent } from 'vue';
import { Avatar } from '@/shared/ui/avatar';
import { pingStatusColor } from '@/entities/ping/model';

interface StatusCardProps {
  title: PingStatus | string;
  icon?: FunctionalComponent | null;
  value: number;
  description?: string;
}

const { description, icon, title, value } = defineProps<StatusCardProps>();

</script>
<template>
  <Card>
    <CardHeader class="flex flex-row items-center justify-between space-y-0 pb-2 min-h-[72px]">
      <div>
        <CardTitle class="text-sm font-medium capitalize">
          {{ title.toLowerCase() }}
        </CardTitle>
        <CardDescription v-if="description" class="text-xs text-muted-foreground">
          {{ description }}
        </CardDescription>
      </div>
      <Avatar v-if="icon" :class="`bg-${pingStatusColor[title as PingStatus]}-500`">
        <component :is="icon" class="h-4 w-4 text-muted-foreground text-white" />
      </Avatar>
    </CardHeader>
    <CardContent>
      <div class="text-2xl font-bold">
        +{{ value }}
      </div>
    </CardContent>
  </Card>
</template>