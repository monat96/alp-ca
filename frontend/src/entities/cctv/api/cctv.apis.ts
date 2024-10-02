import { createAPIRequest, type APIResponse } from '@/shared/lib/ky'

import type { CCTVListSchema, CCTVSchema } from '../model'

// const { authorizationHeader } = useAuthStore()

const URL_LIST = {
  ROOT: () => '/api/cctv',
  DETAIL: (id: string) => [URL_LIST.ROOT(), id].join('/')
}

export function getCCTVList(headers?: Record<string, string>) {
  // console.log('authorizationHeader', authorizationHeader())
  return createAPIRequest<APIResponse<typeof CCTVListSchema>>({
    request: {
      method: 'get',
      url: URL_LIST.ROOT(),
      headers: { ...headers }
    }
  })
}

export function getCCTVDetailById(id: string, headers?: Record<string, string>) {
  return createAPIRequest<APIResponse<typeof CCTVSchema>>({
    request: {
      method: 'get',
      url: URL_LIST.DETAIL(id),
      headers: { ...headers }
    }
  })
}
