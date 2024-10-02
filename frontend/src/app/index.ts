import { createApp } from 'vue'
import App from './App.vue'
import { pinia, router } from './providers'
import { VueQueryPlugin } from '@tanstack/vue-query'

export const app = createApp(App).use(pinia).use(router).use(VueQueryPlugin)
