import { defineStore } from 'pinia'

const STORE_NAME = 'cctv-dialog'

export const useCCTVDialogStore = defineStore(STORE_NAME, {
  state: () => ({
    isOpen: false
  }),
  actions: {
    open() {
      this.isOpen = true
    },
    close() {
      this.isOpen = false
    }
  },
  getters: {
    isDialogOpen: (state) => state.isOpen
  }
})
