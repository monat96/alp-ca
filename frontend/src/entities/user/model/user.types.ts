import type { z } from 'zod'
import type { LoginFormSchema, TokenSchema } from './user.schemas'

export type LoginForm = z.infer<typeof LoginFormSchema>

export type AccessToken = z.infer<typeof TokenSchema>['access_token']
export type RefreshToken = z.infer<typeof TokenSchema>['refresh_token']
export type Token = z.infer<typeof TokenSchema>
