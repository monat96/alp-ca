import { z } from 'zod'

export const LoginFormSchema = z.object({
  username: z.string().min(1, 'Username must be entered'),
  password: z.string().min(8, 'Password must be at least 8 character long')
})

export const TokenSchema = z.object({
  access_token: z.string(),
  refresh_token: z.string()
})
