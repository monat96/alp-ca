import type { ColumnDef } from '@tanstack/vue-table'
import { h } from 'vue'
import { Badge } from '@/shared/ui/badge'
import { pingStatusColor, type Ping, type PingStatus } from '@/entities/ping'

export const columns: ColumnDef<Ping>[] = [
  {
    accessorKey: 'requestTime',
    header: '요청시간',
    enableSorting: true,
    enableHiding: true,
    cell: ({ row }) => {
      const date = new Date(row.getValue('requestTime'))
      return h('span', date.toLocaleString())
    }
  },
  {
    accessorKey: 'status',
    header: '상태',
    enableSorting: true,
    enableHiding: true,
    cell: ({ row }) => {
      const status = row.getValue('status')
      const bgColor = `bg-${pingStatusColor[status as PingStatus]}-500 hover:bg-${pingStatusColor[status as PingStatus]}-500/2`
      return h(Badge, { class: bgColor }, () => status)
    }
  },
  {
    accessorKey: 'rttMax',
    header: '최대RTT',
    enableSorting: true,
    enableHiding: true,
    cell: ({ row }) => h('span', `${row.getValue('rttMax')} ms`)
  },
  {
    accessorKey: 'rttAvg',
    header: '평균RTT',
    enableSorting: true,
    enableHiding: true,
    cell: ({ row }) => h('span', `${row.getValue('rttAvg')} ms`)
  },
  {
    accessorKey: 'rttMin',
    header: '최소RTT',
    enableSorting: true,
    enableHiding: true,
    cell: ({ row }) => h('span', `${row.getValue('rttMin')} ms`)
  },
  {
    accessorKey: 'packetLossRate',
    header: '패킷손실율',
    enableSorting: true,
    enableHiding: true,
    cell: ({ row }) => h('span', `${row.getValue('packetLossRate')} %`)
  }
]
