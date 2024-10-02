import { z } from 'zod'

export const PingStatusSchema = z.enum(['SUCCESS', 'FAIL', 'LOSS', 'TIMEOUT'])

export const PingStatusCountSchema = z.object({
  requestTime: z.string(),
  status: z.record(PingStatusSchema, z.number())
})

export const PingSchema = z.object({
  rttMax: z.number(),
  rttMin: z.number(),
  rttAvg: z.number(),
  packetLossRate: z.number(),
  status: PingStatusSchema,
  requestTime: z.string()
})

export const PingListSchema = z.array(PingSchema)
