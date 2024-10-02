<script setup lang="ts">
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/shared/ui/card';
import { Button } from '@/shared/ui/button';
import { InfoIcon } from 'lucide-vue-next';
import { CCTVDetailDialog, CCTVStreamingPlayer } from '.';

import type { CCTV } from '@/entities/cctv';
import { useCCTVDialogStore } from '@/entities/cctv/model/cctv.stores';
import { Dialog, DialogDescription, DialogHeader, DialogScrollContent, DialogTitle, DialogTrigger } from '@/shared/ui/dialog';
import { PingLineChart } from '@/widgets/ping';
import PingTable from '@/widgets/ping/ui/ping-table.vue';
import { pingStatusColor } from '@/entities/ping';
import { Badge } from '@/shared/ui/badge';

interface CCTVCardProps {
  cctv: CCTV
}

const { cctv } = defineProps<CCTVCardProps>();

</script>
<template>
  <Dialog>
    <Card>
      <CardHeader class="p-4">
        <div class="flex gap-2 justify-between items-center">
          <CardTitle class="items-center flex gap-2">
            <Badge :class="`bg-${pingStatusColor[cctv.status]}-500 hover:bg-${pingStatusColor[cctv.status]}-500/2`">
              {{ cctv.status }}
            </Badge>
            {{ cctv.locationName }}
          </CardTitle>
          <DialogTrigger as-child>
            <Button variant="ghost">
              <InfoIcon />
            </Button>
          </DialogTrigger>
        </div>
        <CardDescription class="text-xs text-ellipsis">
          {{ cctv.locationAddress }}
        </CardDescription>
      </CardHeader>
      <CardContent class="p-0">
        <CCTVStreamingPlayer :hlsAddress="cctv.hlsAddress" />
      </CardContent>
    </Card>
    <DialogScrollContent class="max-w-screen-xl">
      <DialogHeader>
        <DialogTitle>
          {{ cctv.locationName }} ({{ cctv.cctvId }})
        </DialogTitle>
        <DialogDescription class="text-xs">
          {{ cctv.locationAddress }}
        </DialogDescription>
      </DialogHeader>
      <div class="flex flex-row">
        <CCTVStreamingPlayer class="flex-1" :hlsAddress="cctv.hlsAddress" />
        <PingLineChart class="flex-1" :id="cctv.cctvId" />
      </div>
      <PingTable :id="cctv.cctvId" />
    </DialogScrollContent>
  </Dialog>

</template>