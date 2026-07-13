<script setup>
import { ref, onMounted, watch } from 'vue'

const voyages = ref([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showModal = ref(false)
const editingVoyage = ref(null)
const form = ref({})

const keyword = ref('')
const filterDeparture = ref('')
const filterArrival = ref('')
const startDate = ref('')
const endDate = ref('')
const sortField = ref('')
const sortOrder = ref('')
const showFilter = ref(false)

const fetchVoyages = async () => {
  loading.value = true
  try {
    const params = new URLSearchParams()
    params.set('page', currentPage.value)
    params.set('count', pageSize.value)
    if (keyword.value) params.set('keyword', keyword.value)
    if (filterDeparture.value) params.set('departurePort', filterDeparture.value)
    if (filterArrival.value) params.set('arrivalPort', filterArrival.value)
    if (startDate.value) params.set('startDate', startDate.value)
    if (endDate.value) params.set('endDate', endDate.value)
    if (sortField.value) {
      params.set('sortField', sortField.value)
      params.set('sortOrder', sortOrder.value || 'asc')
    }

    const url = `/api/shipVoyage/list?${params.toString()}`
    console.log('🔍 [Voyage] 请求URL:', url)

    const res = await fetch(url)
    console.log('📡 [Voyage] 响应状态:', res.status, res.statusText)

    const data = await res.json()
    console.log('📦 [Voyage] 完整返回数据:', JSON.stringify(data, null, 2))
    console.log('📋 [Voyage] records数组:', Array.isArray(data.records) ? `✅ 长度${data.records.length}` : '❌ 不是数组', data.records)
    console.log('🔢 [Voyage] total값:', data.total, typeof data.total)

    voyages.value = Array.isArray(data.records) ? data.records : []
    total.value = Number(data.total) || 0

    console.log('✅ [Voyage] 最终voyages数组长度:', voyages.value.length)
  } catch (e) {
    console.error('❌ [Voyage] 获取航次数据失败', e)
  } finally {
    loading.value = false
  }
}

const totalPages = ref(1)
watch(total, (v) => { totalPages.value = Math.max(1, Math.ceil(v / pageSize.value)) })

const handleSearch = () => {
  currentPage.value = 1
  fetchVoyages()
}

const handleReset = () => {
  keyword.value = ''
  filterDeparture.value = ''
  filterArrival.value = ''
  startDate.value = ''
  endDate.value = ''
  sortField.value = ''
  sortOrder.value = ''
  currentPage.value = 1
  fetchVoyages()
}

const toggleSort = (field) => {
  if (sortField.value === field) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : sortOrder.value === 'desc' ? '' : 'asc'
    if (!sortOrder.value) sortField.value = ''
  } else {
    sortField.value = field
    sortOrder.value = 'asc'
  }
  fetchVoyages()
}

const getSortIcon = (field) => {
  if (sortField.value !== field) return '⇅'
  return sortOrder.value === 'asc' ? '↑' : '↓'
}

const openAdd = () => {
  editingVoyage.value = null
  form.value = { shipId: null, voyageNo: '', departurePort: '', arrivalPort: '', etd: '', ata: '', cargoWeight: null }
  showModal.value = true
}

const openEdit = (voyage) => {
  editingVoyage.value = voyage
  form.value = { ...voyage }
  showModal.value = true
}

const handleSave = async () => {
  try {
    if (editingVoyage.value) {
      await fetch('/api/shipVoyage', { method: 'PUT', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(form.value) })
    } else {
      await fetch('/api/shipVoyage', { method: 'POST', headers: { 'Content-Type': 'application/json' }, body: JSON.stringify(form.value) })
    }
    showModal.value = false
    fetchVoyages()
  } catch (e) {
    console.error('航次保存失败', e)
  }
}

const handleDelete = async (voyageId) => {
  if (!confirm('确认删除该航次记录？')) return
  try {
    await fetch(`/api/shipVoyage/${voyageId}`, { method: 'DELETE' })
    fetchVoyages()
  } catch (e) {
    console.error('航次删除失败', e)
  }
}

const changePage = (p) => {
  if (p < 1 || p > totalPages.value) return
  currentPage.value = p
  fetchVoyages()
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return dateStr.substring(0, 16)
}

onMounted(fetchVoyages)
</script>

<template>
  <div class="panel">
    <div class="panel-header">
      <div class="search-box">
        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
          <circle cx="11" cy="11" r="8"/><path d="M21 21l-4.35-4.35"/>
        </svg>
        <input v-model="keyword" @keyup.enter="handleSearch" placeholder="搜索航次编号、船舶ID..." />
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
        新增航次
      </button>
    </div>

    <div class="filter-bar" v-show="showFilter">
      <div class="filter-group">
        <label>出发港</label>
        <input
          v-model="filterDeparture"
          placeholder="输入港口名称搜索..."
          list="departure-ports"
        />
        <datalist id="departure-ports">
          <option value="上海港"></option>
          <option value="宁波舟山港"></option>
          <option value="深圳港"></option>
          <option value="广州港"></option>
          <option value="青岛港"></option>
          <option value="天津港"></option>
          <option value="大连港"></option>
          <option value="厦门港"></option>
          <option value="苏州港"></option>
          <option value="营口港"></option>
          <option value="连云港"></option>
          <option value="烟台港"></option>
          <option value="日照港"></option>
          <option value="南通港"></option>
          <option value="镇江港"></option>
          <option value="南京港"></option>
          <option value="福州港"></option>
          <option value="泉州港"></option>
          <option value="海口港"></option>
          <option value="三亚港"></option>
          <option value="威海港"></option>
          <option value="北海港"></option>
          <option value="防城港"></option>
          <option value="钦州港"></option>
          <option value="新加坡港"></option>
          <option value="鹿特丹港"></option>
          <option value="汉堡港"></option>
          <option value="洛杉矶港"></option>
          <option value="长滩港"></option>
          <option value="釜山港"></option>
          <option value="东京港"></option>
          <option value="横滨港"></option>
          <option value="神户港"></option>
          <option value="高雄港"></option>
          <option value="香港港"></option>
          <option value="迪拜港"></option>
          <option value="安特卫普港"></option>
          <option value="费利克斯托港"></option>
          <option value="勒阿弗尔港"></option>
          <option value="比雷埃夫斯港"></option>
        </datalist>
      </div>
      <div class="filter-group">
        <label>到达港</label>
        <input
          v-model="filterArrival"
          placeholder="输入港口名称搜索..."
          list="arrival-ports"
        />
        <datalist id="arrival-ports">
          <option value="上海港"></option>
          <option value="宁波舟山港"></option>
          <option value="深圳港"></option>
          <option value="广州港"></option>
          <option value="青岛港"></option>
          <option value="天津港"></option>
          <option value="大连港"></option>
          <option value="厦门港"></option>
          <option value="苏州港"></option>
          <option value="营口港"></option>
          <option value="连云港"></option>
          <option value="烟台港"></option>
          <option value="日照港"></option>
          <option value="南通港"></option>
          <option value="镇江港"></option>
          <option value="南京港"></option>
          <option value="福州港"></option>
          <option value="泉州港"></option>
          <option value="海口港"></option>
          <option value="三亚港"></option>
          <option value="威海港"></option>
          <option value="北海港"></option>
          <option value="防城港"></option>
          <option value="钦州港"></option>
          <option value="新加坡港"></option>
          <option value="鹿特丹港"></option>
          <option value="汉堡港"></option>
          <option value="洛杉矶港"></option>
          <option value="长滩港"></option>
          <option value="釜山港"></option>
          <option value="东京港"></option>
          <option value="横滨港"></option>
          <option value="神户港"></option>
          <option value="高雄港"></option>
          <option value="香港港"></option>
          <option value="迪拜港"></option>
          <option value="安特卫普港"></option>
          <option value="费利克斯托港"></option>
          <option value="勒阿弗尔港"></option>
          <option value="比雷埃夫斯港"></option>
        </datalist>
      </div>
      <div class="filter-group">
        <label>开航时间从</label>
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
            <th class="sortable" @click="toggleSort('voyageId')">航次ID <span class="sort-icon">{{ getSortIcon('voyageId') }}</span></th>
            <th class="sortable" @click="toggleSort('shipId')">船舶ID <span class="sort-icon">{{ getSortIcon('shipId') }}</span></th>
            <th class="sortable" @click="toggleSort('voyageNo')">航次编号 <span class="sort-icon">{{ getSortIcon('voyageNo') }}</span></th>
            <th class="sortable" @click="toggleSort('departurePort')">出发港 <span class="sort-icon">{{ getSortIcon('departurePort') }}</span></th>
            <th class="sortable" @click="toggleSort('arrivalPort')">到达港 <span class="sort-icon">{{ getSortIcon('arrivalPort') }}</span></th>
            <th class="sortable" @click="toggleSort('etd')">预计开航ETD <span class="sort-icon">{{ getSortIcon('etd') }}</span></th>
            <th class="sortable" @click="toggleSort('ata')">实际到港ATA <span class="sort-icon">{{ getSortIcon('ata') }}</span></th>
            <th class="sortable" @click="toggleSort('cargoWeight')">载货吨数 <span class="sort-icon">{{ getSortIcon('cargoWeight') }}</span></th>
            <th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in voyages" :key="item.voyageId" class="data-row">
            <td class="id-cell">{{ item.voyageId }}</td>
            <td><code class="ref-code">{{ item.shipId }}</code></td>
            <td><span class="tag">{{ item.voyageNo }}</span></td>
            <td>{{ item.departurePort }}</td>
            <td>{{ item.arrivalPort }}</td>
            <td class="time-cell">{{ formatDate(item.etd) }}</td>
            <td class="time-cell">{{ formatDate(item.ata) }}</td>
            <td class="salary-cell">{{ item.cargoWeight ? item.cargoWeight + ' 吨' : '-' }}</td>
            <td class="action-cell">
              <button class="act-btn edit" @click="openEdit(item)" title="编辑航次">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"/>
                  <path d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"/>
                </svg>
              </button>
              <button class="act-btn delete" @click="handleDelete(item.voyageId)" title="删除航次">
                <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                  <path d="M3 6h18M19 6v14a2 2 0 01-2 2H7a2 2 0 01-2-2V6m3 0V4a2 2 0 012-2h4a2 2 0 012 2v2"/>
                </svg>
              </button>
            </td>
          </tr>
          <tr v-if="voyages.length === 0 && !loading">
            <td colspan="9" class="empty-cell">暂无航次数据</td>
          </tr>
        </tbody>
      </table>
    </div>

    <div class="pagination">
      <span class="page-info">共 {{ total }} 条航次记录</span>
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
            <h3>{{ editingVoyage ? '编辑航次' : '新增航次' }}</h3>
            <button class="close-btn" @click="showModal = false">×</button>
          </div>
          <div class="modal-body">
            <div class="form-grid">
              <div class="form-field">
                <label>船舶ID</label>
                <input v-model.number="form.shipId" type="number" placeholder="请输入船舶ID" />
              </div>
              <div class="form-field">
                <label>航次编号</label>
                <input v-model="form.voyageNo" placeholder="请输入航次编号" />
              </div>
              <div class="form-field">
                <label>出发港口</label>
                <input v-model="form.departurePort" placeholder="例：上海港" />
              </div>
              <div class="form-field">
                <label>到达港口</label>
                <input v-model="form.arrivalPort" placeholder="例：新加坡港" />
              </div>
              <div class="form-field">
                <label>预计开航ETD</label>
                <input v-model="form.etd" type="datetime-local" />
              </div>
              <div class="form-field">
                <label>实际到港ATA</label>
                <input v-model="form.ata" type="datetime-local" />
              </div>
              <div class="form-field" style="grid-column: 1 / -1;">
                <label>载货重量（吨）</label>
                <input v-model="form.cargoWeight" type="number" step="0.01" placeholder="货物总吨位" />
              </div>
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

.ref-code {
  padding: 2px 8px;
  background: rgba(139, 92, 246, 0.08);
  border: 1px solid rgba(139, 92, 246, 0.15);
  border-radius: 4px;
  font-family: var(--font-display);
  font-size: 11px;
  color: #a78bfa;
}

.tag {
  padding: 3px 10px;
  background: rgba(0, 212, 255, 0.08);
  border: 1px solid rgba(0, 212, 255, 0.15);
  border-radius: 20px;
  font-size: 11px;
  color: var(--cyan);
}

.salary-cell { color: var(--green); font-weight: 500; font-family: var(--font-display); font-size: 12px; }
.time-cell { font-size: 12px; font-family: var(--font-display); letter-spacing: 0.5px; }

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