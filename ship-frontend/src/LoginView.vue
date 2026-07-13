<script setup>
import { ref, onMounted } from 'vue'

const emit = defineEmits(['login-success'])

const isRegister = ref(false)
const username = ref('')
const password = ref('')
const confirmPassword = ref('')
const regName = ref('')
const regEmail = ref('')
const regPhone = ref('')
const loading = ref(false)
const errorMsg = ref('')
const successMsg = ref('')
const particles = ref([])

// 全局请求配置：完整公网地址 + 300秒超时（和Nginx匹配）
const baseUrl = "http://8.134.113.34"
const REQUEST_TIMEOUT = 300000

// 封装带超时中断的fetch方法
function fetchWithTimeout(url, options = {}) {
    const abortController = new AbortController()
    const timer = setTimeout(() => abortController.abort(), REQUEST_TIMEOUT)
    return fetch(baseUrl + url, {
        ...options,
        signal: abortController.signal
    }).finally(() => clearTimeout(timer))
}

onMounted(() => {
  for (let i = 0; i < 50; i++) {
    particles.value.push({
      id: i,
      x: Math.random() * 100,
      y: Math.random() * 100,
      size: Math.random() * 3 + 1,
      duration: Math.random() * 20 + 10,
      delay: Math.random() * 10
    })
  }
})

const switchMode = () => {
  isRegister.value = !isRegister.value
  errorMsg.value = ''
  successMsg.value = ''
}

const handleLogin = async () => {
  if (!username.value || !password.value) {
    errorMsg.value = '请输入用户名和密码'
    return
  }
  loading.value = true
  errorMsg.value = ''

  try {
    const res = await fetchWithTimeout('/api/userInfo/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: username.value, password: password.value })
    })
    if (!res.ok) throw new Error('服务器连接失败')
    const user = await res.json()
    if (user) {
      if (user.status === 0) {
        errorMsg.value = '账户已被锁定，请联系管理员'
        loading.value = false
        return
      }
      emit('login-success', user)
    } else {
      errorMsg.value = '用户名或密码错误'
    }
  } catch (e) {
    if (e.name === 'AbortError') {
      errorMsg.value = '请求超时，请稍后重试'
    } else {
      errorMsg.value = '无法连接到服务器，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  errorMsg.value = ''
  successMsg.value = ''

  if (!username.value || !password.value) {
    errorMsg.value = '请输入用户名和密码'
    return
  }
  if (password.value !== confirmPassword.value) {
    errorMsg.value = '两次密码输入不一致'
    return
  }
  if (password.value.length < 6) {
    errorMsg.value = '密码长度不能少于6位'
    return
  }

  loading.value = true
  try {
    const checkRes = await fetchWithTimeout(`/api/userInfo/check?username=${encodeURIComponent(username.value)}`)
    const available = await checkRes.json()
    if (!available) {
      errorMsg.value = '用户名已被注册'
      loading.value = false
      return
    }

    const res = await fetchWithTimeout('/api/userInfo', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        username: username.value,
        password: password.value,
        name: regName.value || username.value,
        email: regEmail.value,
        phone: regPhone.value,
        status: 1,
        loginFailCount: 0,
        isDeleted: 0
      })
    })
    const ok = await res.json()
    if (ok) {
      successMsg.value = '注册成功，请登录'
      setTimeout(() => {
        isRegister.value = false
        errorMsg.value = ''
        successMsg.value = ''
        confirmPassword.value = ''
        regName.value = ''
        regEmail.value = ''
        regPhone.value = ''
      }, 1200)
    } else {
      errorMsg.value = '注册失败，请重试'
    }
  } catch (e) {
    if (e.name === 'AbortError') {
      errorMsg.value = '请求超时，请稍后重试'
    } else {
      errorMsg.value = '无法连接到服务器，请稍后重试'
    }
  } finally {
    loading.value = false
  }
}

const handleSubmit = () => {
  if (isRegister.value) {
    handleRegister()
  } else {
    handleLogin()
  }
}
</script>

<template>
  <div class="login-container">
    <div class="particle-field">
      <div
        v-for="p in particles"
        :key="p.id"
        class="particle"
        :style="{
          left: p.x + '%',
          top: p.y + '%',
          width: p.size + 'px',
          height: p.size + 'px',
          animationDuration: p.duration + 's',
          animationDelay: p.delay + 's'
        }"
      />
    </div>

    <div class="scan-line"></div>
    <div class="hex-grid"></div>

    <div class="login-card">
      <div class="card-border-glow"></div>

      <div class="logo-section">
        <div class="logo-icon">
          <svg viewBox="0 0 60 60" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path d="M30 5L55 20V45L30 55L5 45V20L30 5Z" stroke="url(#grad1)" stroke-width="1.5" fill="none"/>
            <path d="M30 15L45 24V40L30 47L15 40V24L30 15Z" stroke="url(#grad1)" stroke-width="1" fill="rgba(0,212,255,0.05)"/>
            <circle cx="30" cy="30" r="5" fill="url(#grad1)"/>
            <defs>
              <linearGradient id="grad1" x1="5" y1="5" x2="55" y2="55">
                <stop offset="0%" stop-color="#00d4ff"/>
                <stop offset="100%" stop-color="#3b82f6"/>
              </linearGradient>
            </defs>
          </svg>
        </div>
        <h1 class="title">NEXMAR</h1>
        <p class="subtitle">船舶数智管控中枢</p>
        <div class="version-tag">v2.0 PRO</div>
      </div>

      <div class="mode-tabs">
        <button :class="['tab-btn', { active: !isRegister }]" @click="isRegister = false; errorMsg = ''; successMsg = ''">登 录</button>
        <button :class="['tab-btn', { active: isRegister }]" @click="isRegister = true; errorMsg = ''; successMsg = ''">注 册</button>
      </div>

      <form class="login-form" @submit.prevent="handleSubmit">
        <div class="input-group">
          <div class="input-icon">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
          </div>
          <input v-model="username" type="text" :placeholder="isRegister ? '设置用户名' : '用户名 / Username'" autocomplete="username" />
          <div class="input-line"></div>
        </div>

        <div class="input-group">
          <div class="input-icon">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
              <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
            </svg>
          </div>
          <input v-model="password" type="password" :placeholder="isRegister ? '设置密码（至少6位）' : '密码 / Password'" autocomplete="current-password" />
          <div class="input-line"></div>
        </div>

        <Transition name="slide">
          <div v-if="isRegister" class="extra-fields">
            <div class="input-group">
              <div class="input-icon">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <rect x="3" y="11" width="18" height="11" rx="2" ry="2"/>
                  <path d="M7 11V7a5 5 0 0 1 10 0v4"/>
                </svg>
              </div>
              <input v-model="confirmPassword" type="password" placeholder="确认密码" autocomplete="new-password" />
              <div class="input-line"></div>
            </div>

            <div class="input-group">
              <div class="input-icon">
                <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M20 21v-2a4 4 0 0 0-4-4H8a4 4 0 0 0-4 4v2"/>
                  <circle cx="12" cy="7" r="4"/>
                </svg>
              </div>
              <input v-model="regName" type="text" placeholder="姓名（选填）" />
              <div class="input-line"></div>
            </div>

            <div class="input-row">
              <div class="input-group">
                <div class="input-icon">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M4 4h16c1.1 0 2 .9 2 2v12c0 1.1-.9 2-2 2H4c-1.1 0-2-.9-2-2V6c0-1.1.9-2 2-2z"/>
                    <polyline points="22,6 12,13 2,6"/>
                  </svg>
                </div>
                <input v-model="regEmail" type="email" placeholder="邮箱（选填）" />
                <div class="input-line"></div>
              </div>

              <div class="input-group">
                <div class="input-icon">
                  <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                    <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72c.127.96.361 1.903.7 2.81a2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45c.907.339 1.85.573 2.81.7A2 2 0 0 1 22 16.92z"/>
                  </svg>
                </div>
                <input v-model="regPhone" type="text" placeholder="电话（选填）" />
                <div class="input-line"></div>
              </div>
            </div>
          </div>
        </Transition>

        <Transition name="shake">
          <div v-if="errorMsg" class="error-msg">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <circle cx="12" cy="12" r="10"/>
              <line x1="15" y1="9" x2="9" y2="15"/>
              <line x1="9" y1="9" x2="15" y2="15"/>
            </svg>
            {{ errorMsg }}
          </div>
        </Transition>

        <Transition name="shake">
          <div v-if="successMsg" class="success-msg">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M22 11.08V12a10 10 0 1 1-5.93-9.14"/>
              <polyline points="22 4 12 14.01 9 11.01"/>
            </svg>
            {{ successMsg }}
          </div>
        </Transition>

        <button type="submit" class="login-btn" :disabled="loading">
          <span v-if="!loading">{{ isRegister ? '立 即 注 册' : '安 全 登 录' }}</span>
          <span v-else class="loading-spinner">
            <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
              <path d="M21 12a9 9 0 1 1-6.219-8.56"/>
            </svg>
            {{ isRegister ? '注册中...' : '验证中...' }}
          </span>
          <div class="btn-shine"></div>
        </button>
      </form>

      <div class="switch-mode">
        <span>{{ isRegister ? '已有账号？' : '还没有账号？' }}</span>
        <a @click="switchMode">{{ isRegister ? '去登录' : '立即注册' }}</a>
      </div>

      <div class="card-footer">
        <div class="status-dot"></div>
        <span>系统安全连接已就绪</span>
      </div>
    </div>

    <div class="bottom-bar">
      <span>© 2026 NEXMAR Maritime Intelligence Platform</span>
      <span class="separator">|</span>
      <span>Encrypted Connection</span>
    </div>
  </div>
</template>

<style scoped>
.login-container {
  width: 100%;
  height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: radial-gradient(ellipse at 50% 0%, #0a1628 0%, #060a14 70%);
  position: relative;
  overflow: hidden;
}

.particle-field { position: absolute; inset: 0; pointer-events: none; }

.particle {
  position: absolute;
  background: var(--cyan);
  border-radius: 50%;
  opacity: 0;
  animation: particleFloat linear infinite;
}

@keyframes particleFloat {
  0% { opacity: 0; transform: translateY(0) scale(0); }
  10% { opacity: 0.6; transform: scale(1); }
  90% { opacity: 0.2; }
  100% { opacity: 0; transform: translateY(-100px) scale(0.5); }
}

.scan-line {
  position: absolute;
  top: 0; left: 0;
  width: 100%; height: 2px;
  background: linear-gradient(90deg, transparent, var(--cyan-dim), transparent);
  animation: scan-line 4s linear infinite;
  pointer-events: none;
}

.hex-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(0, 212, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 212, 255, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
  pointer-events: none;
}

.login-card {
  position: relative;
  width: 440px;
  padding: 40px 40px;
  background: rgba(8, 15, 35, 0.85);
  border: 1px solid rgba(0, 212, 255, 0.15);
  border-radius: 16px;
  backdrop-filter: blur(20px);
  animation: fadeIn 0.8s ease, borderGlow 3s ease-in-out infinite;
  z-index: 10;
}

.card-border-glow {
  position: absolute;
  inset: -1px;
  border-radius: 16px;
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.2), transparent 40%, transparent 60%, rgba(59, 130, 246, 0.2));
  pointer-events: none;
  z-index: -1;
}

.logo-section { text-align: center; margin-bottom: 28px; }

.logo-icon { width: 56px; height: 56px; margin: 0 auto 12px; animation: float 4s ease-in-out infinite; }
.logo-icon svg { width: 100%; height: 100%; }

.title {
  font-family: var(--font-display);
  font-size: 26px;
  font-weight: 900;
  letter-spacing: 8px;
  background: linear-gradient(135deg, #ffffff 0%, #00d4ff 50%, #3b82f6 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  margin: 0;
}

.subtitle { font-size: 12px; color: var(--text-secondary); letter-spacing: 4px; margin-top: 6px; text-transform: uppercase; }

.version-tag {
  display: inline-block;
  margin-top: 10px;
  padding: 2px 12px;
  font-size: 10px;
  font-family: var(--font-display);
  color: var(--cyan);
  border: 1px solid var(--cyan-dim);
  border-radius: 20px;
  letter-spacing: 2px;
}

.mode-tabs {
  display: flex;
  gap: 0;
  margin-bottom: 24px;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 10px;
  overflow: hidden;
}

.tab-btn {
  flex: 1;
  padding: 10px;
  background: transparent;
  border: none;
  color: var(--text-dim);
  font-size: 13px;
  font-weight: 500;
  font-family: var(--font-body);
  letter-spacing: 4px;
  cursor: pointer;
  transition: all 0.3s;
}

.tab-btn.active {
  background: rgba(0, 212, 255, 0.08);
  color: var(--cyan);
  border-bottom: 2px solid var(--cyan);
}

.tab-btn:hover:not(.active) {
  background: rgba(255, 255, 255, 0.03);
  color: var(--text-secondary);
}

.login-form { display: flex; flex-direction: column; gap: 18px; }

.input-group { position: relative; display: flex; align-items: center; }

.input-icon { position: absolute; left: 16px; color: var(--text-dim); display: flex; transition: color 0.3s; z-index: 2; }
.input-group:focus-within .input-icon { color: var(--cyan); }

.input-group input {
  width: 100%;
  padding: 13px 16px 13px 48px;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 10px;
  color: var(--text-primary);
  font-size: 13px;
  font-family: var(--font-body);
  outline: none;
  transition: all 0.3s;
}

.input-group input::placeholder { color: var(--text-dim); font-size: 13px; }

.input-group input:focus {
  border-color: var(--cyan-dim);
  background: rgba(0, 212, 255, 0.03);
  box-shadow: 0 0 20px rgba(0, 212, 255, 0.08);
}

.input-line {
  position: absolute;
  bottom: 0; left: 50%;
  width: 0; height: 2px;
  background: linear-gradient(90deg, transparent, var(--cyan), transparent);
  transition: all 0.4s;
  transform: translateX(-50%);
  border-radius: 0 0 10px 10px;
}

.input-group input:focus ~ .input-line { width: 100%; }

.extra-fields { display: flex; flex-direction: column; gap: 18px; }

.input-row { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }

.error-msg {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 14px;
  background: rgba(239, 68, 68, 0.1);
  border: 1px solid rgba(239, 68, 68, 0.2);
  border-radius: 8px;
  color: #fca5a5;
  font-size: 13px;
}

.success-msg {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 14px;
  background: rgba(16, 185, 129, 0.1);
  border: 1px solid rgba(16, 185, 129, 0.2);
  border-radius: 8px;
  color: #6ee7b7;
  font-size: 13px;
}

.login-btn {
  position: relative;
  width: 100%;
  padding: 13px;
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.15), rgba(59, 130, 246, 0.15));
  border: 1px solid var(--cyan-dim);
  border-radius: 10px;
  color: var(--cyan);
  font-size: 14px;
  font-weight: 600;
  font-family: var(--font-body);
  letter-spacing: 6px;
  cursor: pointer;
  transition: all 0.3s;
  overflow: hidden;
}

.login-btn:hover:not(:disabled) {
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.25), rgba(59, 130, 246, 0.25));
  box-shadow: 0 0 30px rgba(0, 212, 255, 0.2);
  transform: translateY(-1px);
}

.login-btn:disabled { opacity: 0.7; cursor: not-allowed; }

.btn-shine {
  position: absolute;
  top: 0; left: -100%;
  width: 100%; height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.08), transparent);
  transition: left 0.5s;
}

.login-btn:hover .btn-shine { left: 100%; }

.loading-spinner { display: flex; align-items: center; justify-content: center; gap: 8px; }
.loading-spinner svg { animation: spin 1s linear infinite; }
@keyframes spin { to { transform: rotate(360deg); } }

.switch-mode {
  text-align: center;
  margin-top: 20px;
  font-size: 12px;
  color: var(--text-dim);
}

.switch-mode a {
  color: var(--cyan);
  cursor: pointer;
  margin-left: 4px;
  transition: all 0.2s;
  border-bottom: 1px solid transparent;
}

.switch-mode a:hover {
  border-bottom-color: var(--cyan);
  text-shadow: 0 0 8px var(--cyan-dim);
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  margin-top: 24px;
  font-size: 11px;
  color: var(--text-dim);
}

.status-dot {
  width: 6px; height: 6px;
  background: var(--green);
  border-radius: 50%;
  box-shadow: 0 0 8px var(--green);
  animation: pulse-glow 2s ease-in-out infinite;
}

.bottom-bar {
  position: absolute;
  bottom: 24px;
  display: flex;
  gap: 12px;
  font-size: 11px;
  color: var(--text-dim);
  letter-spacing: 1px;
}

.separator { opacity: 0.3; }

.shake-enter-active { animation: shake 0.4s ease; }
@keyframes shake {
  0%, 100% { transform: translateX(0); }
  25% { transform: translateX(-8px); }
  75% { transform: translateX(8px); }
}

.slide-enter-active { animation: slideDown 0.35s ease; }
.slide-leave-active { animation: slideDown 0.25s ease reverse; }
@keyframes slideDown {
  from { opacity: 0; max-height: 0; transform: translateY(-10px); }
  to { opacity: 1; max-height: 400px; transform: translateY(0); }
}
</style>