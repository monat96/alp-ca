import type { z } from 'zod'
import type {
  PingListSchema,
  PingSchema,
  PingStatusCountSchema,
  PingStatusSchema
} from './ping.schemas'

export type Ping = z.infer<typeof PingSchema>
export type PingList = z.infer<typeof PingListSchema>
export type PingStatus = z.infer<typeof PingStatusSchema>
export type PingStatusCount = z.infer<typeof PingStatusCountSchema>
