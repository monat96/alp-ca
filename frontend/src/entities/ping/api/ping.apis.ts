import { createAPIRequest, type APIResponse } from '@/shared/lib/ky'
import type { PingListSchema, PingStatusCountSchema } from '../model'

const URL_LIST = {
  ROOT: () => '/api/ping',
  LIST: (id: string) => [URL_LIST.ROOT(), id].join('/'),
  STATUS: () => [URL_LIST.ROOT(), 'status'].join('/')
} as const

export function getPingStatus(headers?: Record<string, string>) {
  return createAPIRequest<APIResponse<typeof PingStatusCountSchema>>({
    request: {
      method: 'get',
      url: URL_LIST.STATUS(),
      headers: headers
    }
  })
}

export function getPingListByCctvId(id: string, headers?: Record<string, string>) {
  return createAPIRequest<APIResponse<typeof PingListSchema>>({
    request: {
      method: 'get',
      url: URL_LIST.LIST(id),
      headers: headers
    }
  })
}
