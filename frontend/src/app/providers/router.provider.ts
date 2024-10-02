import { useAuthStore } from '@/entities/user'
import { HomePage, LoginPage, CCTVPage } from '@/pages'
import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/',
    name: 'home',
    component: HomePage,
    meta: { requiresAuth: true },
    children: [
      {
        path: 'cctv',
        name: 'cctv',
        component: CCTVPage
      }
    ]
  },
  {
    path: '/login',
    name: 'login',
    component: LoginPage
  }
]

export const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: routes
})

// TODO: Add a beforeEach guard to the router that checks if the user is authenticated and redirects to the login page if not.
router.beforeEach((to, from, next) => {
  const { isAuthenticated } = useAuthStore()

  if (to.meta.requiresAuth && !isAuthenticated()) {
    next({ name: 'login' })
    return
  }

  if (to.name === 'login' && isAuthenticated()) {
    next({ name: 'home' })
    return
  }

  next()
})
