<script setup>
import { ref, onMounted } from 'vue'
import LoginView from './LoginView.vue'
import DashboardView from './DashboardView.vue'

const isLoggedIn = ref(false)
const currentUser = ref(null)

onMounted(() => {
  const saved = localStorage.getItem('ship_user')
  if (saved) {
    currentUser.value = JSON.parse(saved)
    isLoggedIn.value = true
  }
})

const handleLogin = (user) => {
  currentUser.value = user
  isLoggedIn.value = true
  localStorage.setItem('ship_user', JSON.stringify(user))
}

const handleLogout = () => {
  currentUser.value = null
  isLoggedIn.value = false
  localStorage.removeItem('ship_user')
}
</script>

<template>
  <Transition name="fade" mode="out-in">
    <LoginView v-if="!isLoggedIn" @login-success="handleLogin" />
    <DashboardView v-else :user="currentUser" @logout="handleLogout" />
  </Transition>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.4s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>