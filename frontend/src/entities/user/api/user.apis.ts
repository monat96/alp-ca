import ky from 'ky'
import { createAPIRequest, type APIResponse } from '@/shared/lib/ky'
import type { LoginForm, TokenSchema } from '../model'

const URL_LIST = {
  ROOT: () => '/api/auth',
  LOGIN: () => [URL_LIST.ROOT(), 'authenticate'].join('/'),
  REFRESH_TOKEN: () => [URL_LIST.ROOT(), 'refresh-token'].join('/'),
  REGISTER: () => [URL_LIST.ROOT(), 'register'].join('/'),
  LOGOUT: () => [URL_LIST.ROOT(), 'logout'].join('/')
} as const

export function login({ username, password }: LoginForm) {
  return createAPIRequest<APIResponse<typeof TokenSchema>>({
    request: {
      method: 'post',
      url: URL_LIST.LOGIN(),
      body: JSON.stringify({ userId: username, password: password })
    }
  })
}

export function logout(headers?: Record<string, string>) {
  return ky.post(URL_LIST.LOGOUT(), {})
}

export function refreshToken(headers?: Record<string, string>) {
  return createAPIRequest<APIResponse<typeof TokenSchema>>({
    request: {
      method: 'post',
      url: URL_LIST.REFRESH_TOKEN(),
      headers: headers
    }
  })
}
