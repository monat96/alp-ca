import { decodeJwt, type JWTPayload } from 'jose'
import { defineStore } from 'pinia'
import { TokenSchema } from './user.schemas'
import { useStorage } from '@vueuse/core'

import type { AccessToken, RefreshToken, Token } from './user.types'

const STORE_NAME = 'tokens'

export const useAuthStore = defineStore(STORE_NAME, {
  state: () => ({
    tokens: useStorage(
      STORE_NAME,
      {
        accessToken: null as AccessToken | null,
        refreshToken: null as RefreshToken | null
      },
      localStorage,
      { mergeDefaults: true }
    ),
    expirationTimer: null as ReturnType<typeof setTimeout> | null
  }),
  actions: {
    setTokens(token: Token) {
      TokenSchema.parse(token)
      console.log(decodeJwt(token.access_token))

      const decodedAccessToken = decodeJwt(token.access_token)
      const decodedRefreshToken = decodeJwt(token.refresh_token)

      // TODO: validate token
      this.tokens.accessToken = token.access_token
      this.tokens.refreshToken = token.refresh_token

      // Schedule token expiration
      this.scheduleTokenExpiration(decodedAccessToken)
    },
    clearTokens() {
      this.tokens = {
        accessToken: null,
        refreshToken: null
      }
      if (this.expirationTimer) {
        clearTimeout(this.expirationTimer)
        this.expirationTimer = null
      }
    },
    getAccessToken() {
      return this.tokens.accessToken
    },
    getRefreshToken() {
      return this.tokens.refreshToken
    },
    isAuthenticated() {
      return Boolean(this.tokens.accessToken)
    },
    scheduleTokenExpiration(decodedToken: JWTPayload) {
      if (this.expirationTimer) {
        clearTimeout(this.expirationTimer)
      }

      // const expirationTime = Date.now() + 1000 // Convert to milliseconds
      const expirationTime = decodedToken.exp! * 1000 // Convert to milliseconds
      const currentTime = Date.now()
      const timeUntilExpiration = expirationTime - currentTime

      if (timeUntilExpiration > 0) {
        this.expirationTimer = setTimeout(() => {
          this.clearTokens()
        }, timeUntilExpiration)
      } else {
        // Token is already expired
        this.clearTokens()
      }
    },
    authorizationHeader() {
      if (this.isAuthenticated()) {
        return { Authorization: `Bearer ${this.getAccessToken()}` }
      }
    },
    refreshTokenHeader() {
      if (this.isAuthenticated()) {
        return { Authorization: `Bearer ${this.getRefreshToken()}` }
      }
    }
  }
})
