import type { CCTV } from '@/entities/cctv/model'
import { Checkbox } from '@/shared/ui/checkbox'
import type { ColumnDef } from '@tanstack/vue-table'
import { h } from 'vue'
import CCTVTableColumnHeader from './ui/cctv-table-column-header.vue'
import CCTVTableDropdown from './ui/cctv-table-dropdown.vue'
import { Badge } from '@/shared/ui/badge'
import { getStatusBadgeClass } from './lib/utils'
import { pingStatusColor, type PingStatus } from '@/entities/ping'

export const columns: ColumnDef<CCTV>[] = [
  {
    id: 'select',
    header: ({ table }) =>
      h(Checkbox, {
        checked:
          table.getIsAllPageRowsSelected() ||
          (table.getIsSomePageRowsSelected() && 'indeterminate'),
        'onUpdate:checked': (value) => table.toggleAllPageRowsSelected(!!value),
        ariaLabel: 'Select all'
      }),
    cell: ({ row }) =>
      h(Checkbox, {
        checked: row.getIsSelected(),
        'onUpdate:checked': (value) => row.toggleSelected(!!value),
        ariaLabel: 'Select row'
      }),
    enableSorting: false,
    enableHiding: false
  },
  {
    accessorKey: 'cctvId',
    header: ({ column }) =>
      h(CCTVTableColumnHeader, {
        column,
        title: 'CCTV관리번호'
      }),
    enableSorting: true,
    enableHiding: false
  },
  {
    accessorKey: 'status',
    enableSorting: true,
    enableHiding: true,
    header: ({ column }) =>
      h(CCTVTableColumnHeader, {
        column,
        title: '상태'
      }),
    cell: ({ row }) => {
      const status = row.getValue('status')
      const bgColor = `bg-${pingStatusColor[status as PingStatus]}-500 hover:bg-${pingStatusColor[status as PingStatus]}-500/2`
      return h(Badge, { class: bgColor }, () => status)
    }
  },
  {
    accessorKey: 'locationName',
    header: '설치위치명',
    enableSorting: true,
    enableHiding: true
  },
  {
    accessorKey: 'locationAddress',
    header: '설치위치주소',
    enableSorting: true,
    enableHiding: true
  },
  {
    accessorKey: 'ipAddress',
    header: 'IP주소',
    enableSorting: true,
    enableHiding: true
  },
  {
    id: 'actions',
    enableHiding: false,
    cell: ({ row }) => {
      const cctv = row.original

      return h(
        'div',
        { class: 'relative' },
        h(CCTVTableDropdown, {
          cctv,
          onExpand: row.toggleExpanded
        })
      )
    }
  }
]
