<script setup lang="ts">
import { getCCTVDetailById, type CCTV } from '@/entities/cctv';
import { useCCTVDialogStore } from '@/entities/cctv/model/cctv.stores';
import { useAuthStore } from '@/entities/user';
import { Button } from '@/shared/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/shared/ui/card';
import { Dialog, DialogContent, DialogFooter, DialogHeader, DialogTitle } from '@/shared/ui/dialog';
import { PingLineChart } from '@/widgets/ping';
import { useQuery } from '@tanstack/vue-query';

// interface CCTVDetailDialogProps {
//   cctv: CCTV;
// }

// const { cctv } = defineProps<CCTVDetailDialogProps>();

const { close, isDialogOpen } = useCCTVDialogStore();

</script>
<template>
  <Dialog v-model:open="isDialogOpen">
    <DialogContent>
      <DialogHeader>
        <DialogTitle>
          CCTV 상세 정보
        </DialogTitle>
      </DialogHeader>
      TEST
      <DialogFooter>
        <Button @click="close">
          확인
        </Button>
      </DialogFooter>
    </DialogContent>
  </Dialog>
</template>