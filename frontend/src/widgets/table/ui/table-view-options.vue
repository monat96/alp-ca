<script setup lang="ts">
import { Button } from '@/shared/ui/button';
import { DropdownMenu, DropdownMenuCheckboxItem, DropdownMenuContent, DropdownMenuLabel, DropdownMenuSeparator, DropdownMenuTrigger } from '@/shared/ui/dropdown-menu';
import { MixerHorizontalIcon } from '@radix-icons/vue';
import { computed } from 'vue';

import type { Table } from '@tanstack/vue-table';

interface TableViewOptionsProps {
  table: Table<any>
}

const { table } = defineProps<TableViewOptionsProps>();

const columns = computed(() => table.getAllColumns()
  .filter(
    column =>
      typeof column.accessorFn !== 'undefined' && column.getCanHide(),
  ))

</script>
<template>
  <DropdownMenu>
    <DropdownMenuTrigger as-child>
      <Button variant="outline" size="sm" class="hidden h-8 ml-auto lg:flex">
        <MixerHorizontalIcon class="w-4 h-4 mr-2" />
        Columns
      </Button>
    </DropdownMenuTrigger>
    <DropdownMenuContent align="end" class="w-[150px]">
      <DropdownMenuLabel>Toggle columns</DropdownMenuLabel>
      <DropdownMenuSeparator />
      <DropdownMenuCheckboxItem v-for="column in columns" :key="column.id" class="capitalize"
        :checked="column.getIsVisible()" @update:checked="(value) => column.toggleVisibility(!!value)">
        {{ column.id }}
      </DropdownMenuCheckboxItem>
    </DropdownMenuContent>
  </DropdownMenu>
</template>