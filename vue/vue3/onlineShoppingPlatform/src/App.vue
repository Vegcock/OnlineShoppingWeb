<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

onMounted(() => {
  const role = localStorage.getItem('role')

  // 如果用户未登录，强制跳转到 /login
  if (!role && route.path !== '/login') {
    router.push('/login')
  }

  // 如果用户已登录但停在登录页，自动跳转回首页
  if (role && route.path === '/login') {
    router.push('/')
  }
})
</script>

<style>
html, body, #app {
  margin: 0;
  padding: 0;
  height: 100%;
  width: 100%;
}
</style>
