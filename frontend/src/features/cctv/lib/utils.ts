import type { PingStatus } from '@/entities/ping'

export function getStatusBadgeClass(status: PingStatus) {
  switch (status) {
    case 'SUCCESS':
      return 'bg-green-500 text-white' // 성공 시 초록색 뱃지
    case 'FAIL':
      return 'bg-red-500 text-white' // 실패 시 빨간색 뱃지
    case 'LOSS':
      return 'bg-yellow-500 text-white' // 손실 시 노란색 뱃지
    default:
      return 'bg-gray-500 text-white' // 기본 회색 뱃지
  }
}
