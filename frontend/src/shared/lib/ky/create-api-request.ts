import ky from 'ky'
import type { APIConfig } from './ky.types'
import { formatHeaders, formatUrl } from './ky.lib'
import { httpError, networkError, preparationError } from './ky.errors'
import { useAuthStore } from '@/entities/user'

export async function createAPIRequest<T>(config: APIConfig) {
  const response = await ky
    .create({
      method: config.request.method,
      headers: formatHeaders(config.request.headers ?? {}),
      body: config.request.body,
      signal: config?.abort
    })<T>(formatUrl({ href: config.request.url, query: config.request.query ?? {} }))
    .catch((error) => {
      throw networkError({
        reason: error?.message ?? null,
        cause: error
      })
    })

  if (!response.ok) {
    const { clearTokens } = useAuthStore()
    if (response.status === 401 || response.status === 403) {
      clearTokens()
    }
    throw httpError({
      status: response.status,
      statusText: response.statusText,
      response: (await response.text().catch(() => null)) ?? null
    })
  }

  const clonedResponse = response.clone()

  const data = !response.body
    ? null
    : await response.json().catch(async (error) => {
        throw preparationError({
          response: await clonedResponse.text(),
          reason: error?.message ?? null
        })
      })

  return data
}
