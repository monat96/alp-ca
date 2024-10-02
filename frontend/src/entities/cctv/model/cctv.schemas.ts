import { PingStatusSchema } from '@/entities/ping'
import { z } from 'zod'

export const CCTVSchema = z.object({
  cctvId: z.string(),
  ipAddress: z.string(),
  locationName: z.string(),
  locationAddress: z.string(),
  hlsAddress: z.string(),
  status: PingStatusSchema,
  lat: z.number(),
  lng: z.number()
})

export const CCTVListSchema = z.array(CCTVSchema)
