import { z, ZodType } from 'zod'

export const APIResponseSchema = <T extends ZodType<any, any, any>>(dataSchema: T) =>
  z.object({
    status: z.string(),
    message: z.string(),
    timestamp: z.number().int(),
    data: dataSchema.optional()
  })
