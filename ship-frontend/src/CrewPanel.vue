<script setup>
import { ref, onMounted, watch } from 'vue'

const crews = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showModal = ref(false)
const editingCrew = ref(null)
const form = ref({})

const keyword = ref('')
const filterPosition = ref('')
const startDate = ref('')
const endDate = ref('')
const sortField = ref('')
const sortOrder = ref('')
const showFilter = ref(false)

const fetchCrews = async () => {
  loading.value = true
  try {
    const params = new URLSearchParams()
    params.set('page', currentPage.value)
    params.set('count', pageSize.value)
    if (keyword.value) params.set('keyword', keyword.value)
    if (filterPosition.value) params.set('jobPosition', filterPosition.value)
    if (startDate.value) params.set('startDate', startDate.value)
    if (endDate.value) params.set('endDate', endDate.value)
    if (sortField.value) {
      params.set('sortField', sortField.value)
      params.set('sortOrder', sortOrder.value || 'asc')
    }

    const url = `/api/shipCrew/list?${params.toString()}`
    console.log('🔍 [Crew] 请求URL:', url)

    const res = await fetch(url)
    console.log('📡 [Crew] 响应状态:', res.status, res.statusText)

    const data = await res.json()
    console.log('📦 [Crew] 完整返回数据:', JSON.stringify(data, null, 2))
    console.log('📋 [Crew] records数组:', Array.isArray(data.records) ? `✅ 长度${data.records.length}` : '❌ 不是数组', data.records)
    console.log('🔢 [Crew] total값:', data.total, typeof data.total)

    crews.value = Array.isArray(data.records) ? data.records : []
    total.value = Number(data.total) || 0

    console.log('✅ [Crew] 最终crews数组长度:', crews.value.length)
  } catch (e) {
    console.error('❌ [Crew] 获取船员数据失败', e)
  } finally {
    loading.value = false
  }
}

const totalPages = ref(1)
watch(total, (v) => { totalPages.value = Math.max(1, Math.ceil(v / pageSize.value)) })

const handleSearch = () => {
  currentPage.value = 1
  fetchCrews()
}

const handleReset = () => {
  keyword.value = ''
  filterPosition.value = ''
  startDate.value = ''
  endDate.value = ''
  sortField.value = ''
  sortOrder.value = ''
  currentPage.value = 1
  fetchCrews()
}

const toggleSort = (field) => {
  if (sortField.value === field) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : sortOrder.value === 'desc' ? '' : 'asc'
    if (!sortOrder.value) sortField.value = ''
  } else {
    sortField.value = field
    sortOrder.value = 'asc'
  }
  fetchCrews()
}

const getSortIcon = (field) => {
  if (sortField.value !== field) return '⇅'
  return sortOrder.value === 'asc' ? '↑' : '↓'
}

const openAdd = () => {
  editingCrew.value = null
  form.value = { fullName: '', jobPosition: '', phone: '', emergencyContact: '', emergencyPhone: '', certificate: [] }
  showModal.value = true
}

const openEdit = (crew) => {
  editingCrew.value = crew
  form.value = { ...crew, certificate: crew.certificate ? [...crew.certificate] : [] }
  showModal.value = true
}

const addCert = () => {
  if (!form.value.certificate) form.value.certificate = []
  form.value.certificate.push({ certName: '', certNo: '' })
}

const removeCert = (index) => {
  form.value.certificate.splice(index, 1)
}

const handleSave = async () => {
  try {
    if (editingCrew.value) {
      await fetch('/api/shipCrew', { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(form.value) })
    } else {
      await fetch('/api/shipCrew', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(form.value) })
    }
    showModal.value = false
    fetchCrews()
  } catch (e) {
    console.error('保存失败', e)
  }
}

const handleDelete = async (id) => {
  if (!confirm('确认删除该船员记录？')) return
  try {
    await fetch(`/api/shipCrew/${id}`, { method: 'DELETE' })
    fetchCrews()
  } catch (e) {
    console.error('删除失败', e)
  }
}

const changePage = (p) => {
  if (p < 1 || p > totalPages.value) return
  currentPage.value = p
  fetchCrews()
}

onMounted(fetchCrews)
</script>

<template>
  <div class="panel">
    <div class="panel-header">
      <div class="search-box">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/><path d="M21 21l-4.35-4.35"/>
        </svg>
        <input v-model="keyword" @keyup.enter="handleSearch" placeholder="搜索姓名、职务、电话..." />
        <button class="filter-toggle-btn" @click="showFilter = !showFilter" :class="{ active: showFilter }">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <path d="M22 3H2l8 9.46V19l4 2v-8.54L22 3z"/>
          </svg>
          筛选
        </button>
      </div>
      <button class="add-btn" @click="openAdd">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <path d="M12 5v14M5 12h14"/>
        </svg>
        新增船员
      </button>
    </div>

    <div class="filter-bar" v-show="showFilter">
      <div class="filter-group">
        <label>职务</label>
        <select v-model="filterPosition">
          <option value="">全部</option>
          <option value="船长">船长</option>
          <option value="大副">大副</option>
          <option value="二副">二副</option>
          <option value="三副">三副</option>
          <option value="轮机长">轮机长</option>
          <option value="大管轮">大管轮</option>
          <option value="二管轮">二管轮</option>
          <option value="三管轮">三管轮</option>
          <option value="水手">水手</option>
          <option value="机工">机工</option>
          <option value="厨师">厨师</option>
          <option value="政委">政委</option>
        </select>
      </div>
      <div class="filter-group">
        <label>创建时间从</label>
        <input type="date" v-model="startDate" />
      </div>
      <div class="filter-group">
        <label>到</label>
        <input type="date" v-model="endDate" />
      </div>
      <div class="filter-actions">
        <button class="filter-search-btn" @click="handleSearch">查询</button>
        <button class="filter-reset-btn" @click="handleReset">重置</button>
      </div>
    </div>

    <div class="table-wrapper">
      <table class="data-table">
        <thead>
          <tr>
            <th class="sortable" @click="toggleSort('crewId')">ID <span class="sort-icon">{{ getSortIcon('crewId') }}</span></th>
            <th class="sortable" @click="toggleSort('fullName')">姓名 <span class="sort-icon">{{ getSortIcon('fullName') }}</span></th>
            <th class="sortable" @click="toggleSort('jobPosition')">职务 <span class="sort-icon">{{ getSortIcon('jobPosition') }}</span></th>
            <th class="sortable" @click="toggleSort('phone')">联系电话 <span class="sort-icon">{{ getSortIcon('phone') }}</span></th>
            <th>紧急联系人</th>
            <th>紧急电话</th>
            <th>证书</th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="c in crews" :key="c.crewId" class="data-row">
            <td class="id-cell">{{ c.crewId }}</td>
            <td class="name-cell">{{ c.fullName }}</td>
            <td><span class="tag">{{ c.jobPosition }}</span></td>
            <td>{{ c.phone || '-' }}</td>
            <td>{{ c.emergencyContact || '-' }}</td>
            <td>{{ c.emergencyPhone || '-' }}</td>
            <td>
              <div class="cert-list" v-if="c.certificate && c.certificate.length">
                <span class="cert-tag" v-for="(cert, i) in c.certificate" :key="i" :title="cert.certNo">
                  {{ cert.certName }}
                </span>
              </div>
              <span v-else class="no-data">-</span>
            </td>
            <td class="action-cell">
              <button class="act-btn edit" @click="openEdit(c)" title="编辑">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
                  <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
              </button>
              <button class="act-btn delete" @click="handleDelete(c.crewId)" title="删除">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M3 6h18M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/>
                </svg>
              </button>
            </td>
          </tr>
          <tr v-if="crews.length === 0 && !loading">
            <td colspan="8" class="empty-cell">暂无数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination">
      <span class="page-info">共 {{ total }} 条记录</span>
      <div class="page-btns">
        <button :disabled="currentPage <= 1" @click="changePage(currentPage - 1)">‹ 上一页</button>
        <span class="current-page">{{ currentPage }} / {{ totalPages }}</span>
        <button :disabled="currentPage >= totalPages" @click="changePage(currentPage + 1)">下一页 ›</button>
      </div>
    </div>

    <Teleport to="body">
      <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
        <div class="modal-card">
          <div class="modal-header">
            <h3>{{ editingCrew ? '编辑船员' : '新增船员' }}</h3>
            <button class="close-btn" @click="showModal = false">×</button>
          </div>
          <div class="modal-body">
            <div class="form-grid">
              <div class="form-field">
                <label>姓名</label>
                <input v-model="form.fullName" placeholder="请输入姓名" />
              </div>
              <div class="form-field">
                <label>职务</label>
                <input v-model="form.jobPosition" placeholder="如：船长、大副" />
              </div>
              <div class="form-field">
                <label>联系电话</label>
                <input v-model="form.phone" placeholder="请输入电话" />
              </div>
              <div class="form-field">
                <label>紧急联系人</label>
                <input v-model="form.emergencyContact" placeholder="请输入紧急联系人" />
              </div>
              <div class="form-field full-width">
                <label>紧急联系电话</label>
                <input v-model="form.emergencyPhone" placeholder="请输入紧急联系电话" />
              </div>
            </div>
            <div class="cert-section">
              <div class="cert-header">
                <label>证书管理</label>
                <button class="add-cert-btn" @click="addCert">+ 添加证书</button>
              </div>
              <div v-for="(cert, index) in form.certificate" :key="index" class="cert-row">
                <input v-model="cert.certName" placeholder="证书名称" class="cert-input" />
                <input v-model="cert.certNo" placeholder="证书编号" class="cert-input" />
                <button class="remove-cert-btn" @click="removeCert(index)">×</button>
              </div>
              <div v-if="!form.certificate || form.certificate.length === 0" class="no-cert">暂无证书，点击上方按钮添加</div>
            </div>
          </div>
          <div class="modal-footer">
            <button class="modal-btn cancel" @click="showModal = false">取消</button>
            <button class="modal-btn confirm" @click="handleSave">确认保存</button>
          </div>
        </div>
      </div>
    </Teleport>
  </div>
</template>

<style scoped>
.panel { animation: fadeIn 0.35s ease; }

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  gap: 16px;
  flex-wrap: wrap;
}

.search-box {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 16px;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 10px;
  min-width: 320px;
  transition: border-color 0.3s;
  color: var(--text-dim);
}

.search-box:focus-within { border-color: var(--cyan-dim); }
.search-box input { background: none; border: none; outline: none; color: var(--text-primary); font-size: 13px; width: 100%; font-family: var(--font-body); }
.search-box input::placeholder { color: var(--text-dim); }

.filter-toggle-btn {
  display: flex;
  align-items: center;
  gap: 4px;
  padding: 4px 10px;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  color: var(--text-dim);
  font-size: 11px;
  cursor: pointer;
  white-space: nowrap;
  font-family: var(--font-body);
  transition: all 0.2s;
}

.filter-toggle-btn:hover,
.filter-toggle-btn.active { border-color: var(--cyan-dim); color: var(--cyan); }

.filter-bar {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  padding: 16px;
  margin-bottom: 16px;
  background: rgba(0, 0, 0, 0.25);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 10px;
  flex-wrap: wrap;
  animation: fadeIn 0.25s ease;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.filter-group label {
  font-size: 10px;
  font-weight: 500;
  color: var(--text-dim);
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.filter-group select,
.filter-group input {
  padding: 7px 12px;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.08);
  border-radius: 6px;
  color: var(--text-primary);
  font-size: 12px;
  font-family: var(--font-body);
  outline: none;
  min-width: 120px;
  transition: border-color 0.3s;
}

.filter-group select { appearance: none; cursor: pointer; }
.filter-group select:focus,
.filter-group input:focus { border-color: var(--cyan-dim); }

.filter-actions {
  display: flex;
  gap: 8px;
  margin-left: auto;
}

.filter-search-btn {
  padding: 7px 18px;
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.15), rgba(59, 130, 246, 0.15));
  border: 1px solid var(--cyan-dim);
  border-radius: 6px;
  color: var(--cyan);
  font-size: 12px;
  cursor: pointer;
  font-family: var(--font-body);
  transition: all 0.2s;
}

.filter-search-btn:hover { box-shadow: 0 0 12px rgba(0, 212, 255, 0.15); }

.filter-reset-btn {
  padding: 7px 18px;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.1);
  border-radius: 6px;
  color: var(--text-secondary);
  font-size: 12px;
  cursor: pointer;
  font-family: var(--font-body);
  transition: all 0.2s;
}

.filter-reset-btn:hover { border-color: rgba(255, 255, 255, 0.2); }

.add-btn {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 10px 20px;
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.12), rgba(59, 130, 246, 0.12));
  border: 1px solid var(--cyan-dim);
  border-radius: 10px;
  color: var(--cyan);
  font-size: 13px;
  font-weight: 500;
  cursor: pointer;
  font-family: var(--font-body);
  transition: all 0.3s;
}

.add-btn:hover {
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.2), rgba(59, 130, 246, 0.2));
  box-shadow: 0 0 20px rgba(0, 212, 255, 0.15);
}

.table-wrapper {
  background: var(--bg-card);
  border: 1px solid var(--border-glass);
  border-radius: 12px;
  overflow-x: auto;
}

.data-table { width: 100%; border-collapse: collapse; font-size: 13px; }

.data-table th {
  padding: 14px 16px;
  text-align: left;
  font-weight: 600;
  font-size: 11px;
  text-transform: uppercase;
  letter-spacing: 1px;
  color: var(--text-dim);
  background: rgba(0, 0, 0, 0.3);
  border-bottom: 1px solid var(--border-glass);
  white-space: nowrap;
  user-select: none;
}

.data-table th.sortable { cursor: pointer; transition: color 0.2s; }
.data-table th.sortable:hover { color: var(--cyan); }

.sort-icon { font-size: 10px; margin-left: 2px; opacity: 0.5; }
.sortable:hover .sort-icon { opacity: 0.8; }

.data-table td {
  padding: 12px 16px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.03);
  color: var(--text-secondary);
  white-space: nowrap;
}

.data-row { transition: background 0.2s; }
.data-row:hover { background: rgba(0, 212, 255, 0.03); }

.id-cell { color: var(--text-dim); font-size: 12px; }
.name-cell { color: var(--text-primary); font-weight: 500; }

.tag {
  padding: 3px 10px;
  background: rgba(139, 92, 246, 0.1);
  border: 1px solid rgba(139, 92, 246, 0.2);
  border-radius: 20px;
  font-size: 11px;
  color: #a78bfa;
}

.cert-list { display: flex; gap: 4px; flex-wrap: wrap; }

.cert-tag {
  padding: 2px 8px;
  background: rgba(0, 212, 255, 0.08);
  border: 1px solid rgba(0, 212, 255, 0.15);
  border-radius: 4px;
  font-size: 10px;
  color: var(--cyan);
}

.no-data { color: var(--text-dim); }

.action-cell { display: flex; gap: 6px; }

.act-btn {
  width: 30px; height: 30px;
  display: flex; align-items: center; justify-content: center;
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 6px;
  background: transparent;
  cursor: pointer;
  transition: all 0.2s;
  color: var(--text-dim);
}

.act-btn.edit:hover { border-color: var(--cyan-dim); color: var(--cyan); background: rgba(0, 212, 255, 0.05); }
.act-btn.delete:hover { border-color: rgba(239, 68, 68, 0.3); color: var(--red); background: rgba(239, 68, 68, 0.05); }

.empty-cell { text-align: center; padding: 40px !important; color: var(--text-dim); }

.pagination {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 16px;
  padding: 0 4px;
}

.page-info { font-size: 12px; color: var(--text-dim); }
.page-btns { display: flex; align-items: center; gap: 12px; }

.page-btns button {
  padding: 6px 14px;
  background: var(--bg-glass);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 6px;
  color: var(--text-secondary);
  font-size: 12px;
  cursor: pointer;
  font-family: var(--font-body);
  transition: all 0.2s;
}

.page-btns button:hover:not(:disabled) { border-color: var(--cyan-dim); color: var(--cyan); }
.page-btns button:disabled { opacity: 0.3; cursor: not-allowed; }

.current-page { font-size: 12px; color: var(--cyan); font-family: var(--font-display); }

.modal-overlay {
  position: fixed; inset: 0;
  background: rgba(0, 0, 0, 0.7);
  backdrop-filter: blur(4px);
  display: flex; align-items: center; justify-content: center;
  z-index: 1000;
  animation: fadeIn 0.2s ease;
}

.modal-card {
  width: 680px; max-height: 80vh;
  background: var(--bg-secondary);
  border: 1px solid var(--border-glass);
  border-radius: 16px;
  overflow: hidden;
  display: flex; flex-direction: column;
}

.modal-header {
  display: flex; justify-content: space-between; align-items: center;
  padding: 20px 24px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.modal-header h3 { font-size: 16px; font-weight: 600; margin: 0; color: var(--text-primary); }

.close-btn { background: none; border: none; color: var(--text-dim); font-size: 24px; cursor: pointer; padding: 0 4px; transition: color 0.2s; }
.close-btn:hover { color: var(--red); }

.modal-body { padding: 24px; overflow-y: auto; flex: 1; }

.form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; }
.form-field { display: flex; flex-direction: column; gap: 6px; }
.form-field.full-width { grid-column: 1 / -1; }

.form-field label {
  font-size: 11px; font-weight: 500; color: var(--text-dim);
  text-transform: uppercase; letter-spacing: 1px;
}

.form-field input {
  padding: 10px 14px;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 8px;
  color: var(--text-primary);
  font-size: 13px;
  font-family: var(--font-body);
  outline: none;
  transition: border-color 0.3s;
}

.form-field input:focus { border-color: var(--cyan-dim); }

.cert-section {
  margin-top: 20px;
  padding: 16px;
  background: rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.05);
  border-radius: 10px;
}

.cert-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.cert-header label {
  font-size: 11px; font-weight: 500; color: var(--text-dim);
  text-transform: uppercase; letter-spacing: 1px;
}

.add-cert-btn {
  padding: 4px 12px;
  background: rgba(0, 212, 255, 0.08);
  border: 1px solid rgba(0, 212, 255, 0.15);
  border-radius: 6px;
  color: var(--cyan);
  font-size: 11px;
  cursor: pointer;
  font-family: var(--font-body);
  transition: all 0.2s;
}

.add-cert-btn:hover { background: rgba(0, 212, 255, 0.15); }

.cert-row { display: flex; gap: 8px; margin-bottom: 8px; align-items: center; }

.cert-input {
  flex: 1;
  padding: 8px 12px;
  background: rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.06);
  border-radius: 6px;
  color: var(--text-primary);
  font-size: 12px;
  font-family: var(--font-body);
  outline: none;
  transition: border-color 0.3s;
}

.cert-input:focus { border-color: var(--cyan-dim); }

.remove-cert-btn {
  width: 28px; height: 28px;
  display: flex; align-items: center; justify-content: center;
  background: transparent;
  border: 1px solid rgba(239, 68, 68, 0.2);
  border-radius: 6px;
  color: var(--red);
  cursor: pointer;
  font-size: 16px;
  transition: all 0.2s;
}

.remove-cert-btn:hover { background: rgba(239, 68, 68, 0.1); }

.no-cert { text-align: center; padding: 16px; color: var(--text-dim); font-size: 12px; }

.modal-footer {
  display: flex; justify-content: flex-end; gap: 12px;
  padding: 16px 24px;
  border-top: 1px solid rgba(255, 255, 255, 0.05);
}

.modal-btn {
  padding: 10px 24px; border-radius: 8px;
  font-size: 13px; font-weight: 500;
  cursor: pointer; font-family: var(--font-body);
  transition: all 0.2s; border: none;
}

.modal-btn.cancel { background: transparent; border: 1px solid rgba(255, 255, 255, 0.1); color: var(--text-secondary); }
.modal-btn.cancel:hover { border-color: rgba(255, 255, 255, 0.2); }

.modal-btn.confirm {
  background: linear-gradient(135deg, rgba(0, 212, 255, 0.2), rgba(59, 130, 246, 0.2));
  border: 1px solid var(--cyan-dim); color: var(--cyan);
}

.modal-btn.confirm:hover { box-shadow: 0 0 20px rgba(0, 212, 255, 0.2); }
</style>
