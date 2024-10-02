import type { z, ZodType } from 'zod'
import type { APIResponseSchema } from './ky.schemas'
import type { HttpMethod, KyHeadersInit } from 'node_modules/ky/distribution/types/options'

export type APIResponse<T extends ZodType<any, any, any>> = z.infer<
  ReturnType<typeof APIResponseSchema<T>>
>

export type RequestBody = Blob | BufferSource | FormData | string

export type KyAPIRecord = Record<string, string | string[] | number | boolean | null | undefined>

export interface APIRequest {
  method: HttpMethod
  body?: RequestBody
  headers?: KyHeadersInit
  query?: KyAPIRecord
  url: string
}

export interface APIConfig {
  request: APIRequest
  abort?: AbortSignal
}

export type GenericError<T extends string> = {
  errorType: T
  explanation: string
}

export const INVALID_DATA = 'INVALID_DATA'
export interface InvalidDataError extends GenericError<typeof INVALID_DATA> {
  validationErrors: string[]
  response: unknown
}

export const PREPARATION = 'PREPARATION'
export interface PreparationError extends GenericError<typeof PREPARATION> {
  response: string
  reason: string | null
}

export const HTTP = 'HTTP'
export interface HttpError<Status extends number = number> extends GenericError<typeof HTTP> {
  status: Status
  statusText: string
  response: string | JSON | null
}

export const NETWORK = 'NETWORK'
export interface NetworkError extends GenericError<typeof NETWORK> {
  reason: string | null
  cause?: unknown
}
