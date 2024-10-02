import type { z } from 'zod'
import type { CCTVListSchema, CCTVSchema } from './cctv.schemas'

export type CCTV = z.infer<typeof CCTVSchema>
export type CCTVList = z.infer<typeof CCTVListSchema>
