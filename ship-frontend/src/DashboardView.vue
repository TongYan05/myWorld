<script setup>
import { ref, computed } from 'vue'
import ShipInfoPanel from './ShipInfoPanel.vue'
import CrewPanel from './CrewPanel.vue'
import VoyagePanel from './VoyagePanel.vue'
import AssignmentPanel from './AssignmentPanel.vue'
import UserPanel from './UserPanel.vue'

const props = defineProps({ user: Object })
const emit = defineEmits(['logout'])

const activeTab = ref('ship')
const sidebarCollapsed = ref(false)

const tabs = [
  { key: 'ship', label: '船舶信息', icon: 'ship', sub: 'Ship Info' },
  { key: 'crew', label: '船员管理', icon: 'crew', sub: 'Crew Members' },
  { key: 'voyage', label: '航次记录', icon: 'voyage', sub: 'Voyages' },
  { key: 'assign', label: '任务分配', icon: 'assign', sub: 'Assignments' },
  { key: 'user', label: '用户管理', icon: 'user', sub: 'Users' }
]

const currentTabLabel = computed(() => tabs.find(t => t.key === activeTab.value)?.label || '')
const currentTabSub = computed(() => tabs.find(t => t.key === activeTab.value)?.sub || '')

const now = ref(new Date())
setInterval(() => { now.value = new Date() }, 1000)

const formattedTime = computed(() => {
  return now.value.toLocaleTimeString('zh-CN', { hour12: false })
})
const formattedDate = computed(() => {
  return now.value.toLocaleDateString('zh-CN', { year: 'numeric', month: '2-digit', day: '2-digit', weekday: 'short' })
})
</script>

<template>
  <div class="dashboard">
    <div class="bg-effects">
      <div class="grid-bg"></div>
      <div class="glow-orb orb-1"></div>
      <div class="glow-orb orb-2"></div>
    </div>

    <aside class="sidebar" :class="{ collapsed: sidebarCollapsed }">
      <div class="sidebar-header">
        <div class="brand" v-show="!sidebarCollapsed">
          <span class="brand-icon">⬡</span>
          <span class="brand-text">NEXMAR</span>
        </div>
        <button class="collapse-btn" @click="sidebarCollapsed = !sidebarCollapsed">
          <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path v-if="!sidebarCollapsed" d="M15 18l-6-6 6-6"/>
            <path v-else d="M9 18l6-6-6-6"/>
          </svg>
        </button>
      </div>

      <nav class="sidebar-nav">
        <button
          v-for="tab in tabs"
          :key="tab.key"
          class="nav-item"
          :class="{ active: activeTab === tab.key }"
          @click="activeTab = tab.key"
        >
          <div class="nav-icon">
            <svg v-if="tab.icon === 'ship'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M2 20l.8-2.2C3.2 16.6 4.3 16 5.5 16h13c1.2 0 2.3.6 2.7 1.8L22 20"/>
              <path d="M4 16V8a2 2 0 012-2h12a2 2 0 012 2v8"/>
              <path d="M12 2v4M8 6V4M16 6V4"/>
            </svg>
            <svg v-if="tab.icon === 'crew'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M17 21v-2a4 4 0 00-4-4H5a4 4 0 00-4 4v2"/>
              <circle cx="9" cy="7" r="4"/>
              <path d="M23 21v-2a4 4 0 00-3-3.87M16 3.13a4 4 0 010 7.75"/>
            </svg>
            <svg v-if="tab.icon === 'voyage'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <circle cx="12" cy="12" r="10"/>
              <path d="M2 12h20M12 2a15.3 15.3 0 014 10 15.3 15.3 0 01-4 10 15.3 15.3 0 01-4-10 15.3 15.3 0 014-10z"/>
            </svg>
            <svg v-if="tab.icon === 'assign'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2"/>
              <rect x="9" y="3" width="6" height="4" rx="1"/>
              <path d="M9 14l2 2 4-4"/>
            </svg>
            <svg v-if="tab.icon === 'user'" width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.5">
              <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2"/>
              <circle cx="12" cy="7" r="4"/>
            </svg>
          </div>
          <div class="nav-text" v-show="!sidebarCollapsed">
            <span class="nav-label">{{ tab.label }}</span>
            <span class="nav-sub">{{ tab.sub }}</span>
          </div>
          <div class="active-indicator" v-if="activeTab === tab.key"></div>
        </button>
      </nav>

      <div class="sidebar-footer" v-show="!sidebarCollapsed">
        <div class="sys-status">
          <div class="status-dot"></div>
          <span>系统运行中</span>
        </div>
      </div>
    </aside>

    <div class="main-area">
      <header class="top-bar">
        <div class="top-left">
          <h2 class="page-title">{{ currentTabLabel }}</h2>
          <span class="page-sub">{{ currentTabSub }}</span>
        </div>
        <div class="top-right">
          <div class="datetime">
            <span class="time">{{ formattedTime }}</span>
            <span class="date">{{ formattedDate }}</span>
          </div>
          <div class="user-badge">
            <div class="avatar">{{ props.user?.name?.charAt(0) || props.user?.username?.charAt(0) || 'A' }}</div>
            <span class="user-name">{{ props.user?.name || props.user?.username || 'Admin' }}</span>
            <button class="logout-btn" @click="emit('logout')" title="退出登录">
              <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <path d="M9 21H5a2 2 0 01-2-2V5a2 2 0 012-2h4M16 17l5-5-5-5M21 12H9"/>
              </svg>
            </button>
          </div>
        </div>
      </header>

      <main class="content">
        <Transition name="panel" mode="out-in">
          <ShipInfoPanel v-if="activeTab === 'ship'" key="ship" />
          <CrewPanel v-else-if="activeTab === 'crew'" key="crew" />
          <VoyagePanel v-else-if="activeTab === 'voyage'" key="voyage" />
          <AssignmentPanel v-else-if="activeTab === 'assign'" key="assign" />
          <UserPanel v-else-if="activeTab === 'user'" key="user" />
        </Transition>
      </main>
    </div>
  </div>
</template>

<style scoped>
.dashboard {
  display: flex;
  width: 100%;
  height: 100vh;
  position: relative;
  overflow: hidden;
}

.bg-effects {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 0;
}

.grid-bg {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(0, 212, 255, 0.02) 1px, transparent 1px),
    linear-gradient(90deg, rgba(0, 212, 255, 0.02) 1px, transparent 1px);
  background-size: 40px 40px;
}

.glow-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.3;
}

.orb-1 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(0, 212, 255, 0.15), transparent);
  top: -100px;
  right: 20%;
  animation: float 8s ease-in-out infinite;
}

.orb-2 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(59, 130, 246, 0.1), transparent);
  bottom: -50px;
  left: 30%;
  animation: float 10s ease-in-out infinite reverse;
}

.sidebar {
  width: 240px;
  height: 100vh;
  background: rgba(6, 10, 20, 0.95);
  border-right: 1px solid rgba(0, 212, 255, 0.08);
  display: flex;
  flex-direction: column;
  transition: width 0.3s ease;
  position: relative;
  z-index: 10;
  flex-shrink: 0;
}

.sidebar.collapsed {
  width: 68px;
}

.sidebar-header {
  padding: 20px 16px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.brand {
  display: flex;
  align-items: center;
  gap: 10px;
}

.brand-icon {
  font-size: 22px;
  color: var(--cyan);
  text-shadow: 0 0 10px var(--cyan-dim);
}

.brand-text {
  font-family: var(--font-display);
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 3px;
  background: linear-gradient(135deg, #fff, var(--cyan));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.collapse-btn {
  background: none;
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 6px;
  color: var(--text-secondary);
  cursor: pointer;
  padding: 6px;
  display: flex;
  align-items: center;
  transition: all 0.3s;
}

.collapse-btn:hover {
  border-color: var(--cyan-dim);
  color: var(--cyan);
}

.sidebar-nav {
  flex: 1;
  padding: 12px 8px;
  display: flex;
  flex-direction: column;
  gap: 4px;
  overflow-y: auto;
}

.nav-item {
  position: relative;
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 14px;
  background: transparent;
  border: 1px solid transparent;
  border-radius: 10px;
  color: var(--text-secondary);
  cursor: pointer;
  font-family: var(--font-body);
  transition: all 0.3s;
  text-align: left;
}

.nav-item:hover {
  background: rgba(0, 212, 255, 0.04);
  border-color: rgba(0, 212, 255, 0.08);
  color: var(--text-primary);
}

.nav-item.active {
  background: rgba(0, 212, 255, 0.08);
  border-color: rgba(0, 212, 255, 0.2);
  color: var(--cyan);
}

.active-indicator {
  position: absolute;
  left: -8px;
  top: 50%;
  transform: translateY(-50%);
  width: 3px;
  height: 24px;
  background: var(--cyan);
  border-radius: 0 3px 3px 0;
  box-shadow: 0 0 8px var(--cyan-dim);
}

.nav-icon {
  display: flex;
  align-items: center;
  flex-shrink: 0;
}

.nav-text {
  display: flex;
  flex-direction: column;
}

.nav-label {
  font-size: 13px;
  font-weight: 500;
}

.nav-sub {
  font-size: 10px;
  opacity: 0.5;
  letter-spacing: 1px;
  text-transform: uppercase;
}

.sidebar-footer {
  padding: 16px;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
}

.sys-status {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 11px;
  color: var(--text-dim);
}

.sys-status .status-dot {
  width: 6px;
  height: 6px;
  background: var(--green);
  border-radius: 50%;
  box-shadow: 0 0 6px var(--green);
  animation: pulse-glow 2s ease-in-out infinite;
}

.main-area {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
  position: relative;
  z-index: 5;
}

.top-bar {
  padding: 16px 28px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  background: rgba(6, 10, 20, 0.6);
  backdrop-filter: blur(10px);
  flex-shrink: 0;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  margin: 0;
  color: var(--text-primary);
}

.page-sub {
  font-size: 11px;
  color: var(--text-dim);
  letter-spacing: 2px;
  text-transform: uppercase;
  margin-left: 12px;
}

.top-right {
  display: flex;
  align-items: center;
  gap: 24px;
}

.datetime {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
}

.datetime .time {
  font-family: var(--font-display);
  font-size: 18px;
  color: var(--cyan);
  letter-spacing: 2px;
}

.datetime .date {
  font-size: 11px;
  color: var(--text-dim);
}

.user-badge {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px;
  background: var(--bg-glass);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 10px;
}

.avatar {
  width: 32px;
  height: 32px;
  border-radius: 8px;
  background: linear-gradient(135deg, var(--cyan), var(--blue));
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: 700;
  font-size: 14px;
  color: #fff;
}

.user-name {
  font-size: 13px;
  font-weight: 500;
}

.logout-btn {
  background: none;
  border: none;
  color: var(--text-dim);
  cursor: pointer;
  padding: 4px;
  display: flex;
  transition: color 0.3s;
}

.logout-btn:hover {
  color: var(--red);
}

.content {
  flex: 1;
  padding: 24px 28px;
  overflow-y: auto;
}

.panel-enter-active {
  animation: fadeIn 0.35s ease;
}
.panel-leave-active {
  animation: fadeIn 0.2s ease reverse;
}
</style>
