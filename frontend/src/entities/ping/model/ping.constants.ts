import type { PingStatus } from './ping.types'

export const pingStatusColor: Record<PingStatus, string> = {
  SUCCESS: 'green',
  LOSS: 'yellow',
  FAIL: 'red',
  TIMEOUT: 'gray'
}
