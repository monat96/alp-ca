import { usePreferredDark } from '@vueuse/core'
import { watch } from 'vue'

const isPreferredDark = usePreferredDark()

// if (isPreferredDark.value) {
//   document.documentElement.classList.add('dark')
// }

// watch(isPreferredDark, (isDark) => {
//   if (isDark) {
//     document.documentElement.classList.add('dark')
//   } else {
//     document.documentElement.classList.remove('dark')
//   }
// })
