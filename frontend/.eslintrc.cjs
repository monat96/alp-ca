/* eslint-env node */
require('@rushstack/eslint-patch/modern-module-resolution')
require('@tanstack/eslint-plugin-query')

module.exports = {
  root: true,
  extends: [
    'plugin:vue/vue3-essential',
    'eslint:recommended',
    '@vue/eslint-config-typescript',
    '@vue/eslint-config-prettier/skip-formatting',
    'plugin:@tanstack/eslint-plugin-query/recommended'
  ],
  parserOptions: {
    ecmaVersion: 'latest'
  }
}
