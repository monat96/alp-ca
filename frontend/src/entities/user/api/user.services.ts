import { queryOptions as tanstackQueryOptions } from '@tanstack/vue-query'

const keys = {
  root: () => 'auth',
  login: () => [keys.root(), 'login']
} as const

export const authLoginService = {
  queryKey: keys.login
}
