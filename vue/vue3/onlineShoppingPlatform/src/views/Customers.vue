<template>
  <el-dialog 
    title="客户信息" 
    v-model="dialogVisible" 
    width="800px"
    top="10vh"
    :append-to-body="true"
    :destroy-on-close="true"
    :close-on-click-modal="false"
  >
    <CustomerCard :customer="selectedUser" />
  </el-dialog>

  <el-dialog
    title="添加用户"
    v-model="addDialogVisible"
    width="600px"
    :destroy-on-close="true"
    :close-on-click-modal="false"
  >
    <el-form
      :model="form"
      :rules="rules"
      ref="addFormRef"
      label-width="100px"
    >
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>

      <el-form-item label="电话" prop="phone">
        <el-input v-model="form.phone" autocomplete="off" />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" autocomplete="off" />
      </el-form-item>

      <el-form-item label="地址" prop="address">
        <el-input v-model="form.address" autocomplete="off" />
      </el-form-item>

      <el-form-item label="地区" prop="place">
        <el-input v-model="form.place" autocomplete="off" />
      </el-form-item>

      <el-form-item label="会员等级" prop="vip">
        <el-select v-model="form.vip" placeholder="请选择VIP等级">
          <el-option label="普通" value="普通" />
          <el-option label="白银" value="白银" />
          <el-option label="黄金" value="黄金" />
          <el-option label="铂金" value="铂金" />
          <el-option label="钻石" value="钻石" />
        </el-select>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择状态">
          <el-option label="已激活" value="已激活" />
          <el-option label="未激活" value="未激活" />
          <el-option label="注销" value="注销" />
          <el-option label="封禁" value="封禁" />
        </el-select>
      </el-form-item>

      <el-form-item label="头像URL" prop="url">
        <el-input v-model="form.url" placeholder="可选" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="addDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitAddUser">确认添加</el-button>
    </template>
  </el-dialog>

  <el-dialog
    title="编辑用户"
    v-model="editDialogVisible"
    width="600px"
    :destroy-on-close="true"
    :close-on-click-modal="false"
  >
    <el-form
      :model="form"
      :rules="rules"
      ref="editFormRef"
      label-width="100px"
    >
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name" autocomplete="off" />
      </el-form-item>

      <el-form-item label="电话" prop="phone">
        <el-input v-model="form.phone" autocomplete="off" />
      </el-form-item>

      <el-form-item label="邮箱" prop="email">
        <el-input v-model="form.email" autocomplete="off" />
      </el-form-item>

      <el-form-item label="地址" prop="address">
        <el-input v-model="form.address" autocomplete="off" />
      </el-form-item>

      <el-form-item label="地区" prop="place">
        <el-input v-model="form.place" autocomplete="off" />
      </el-form-item>

      <el-form-item label="会员等级" prop="vip">
        <el-select v-model="form.vip" placeholder="请选择VIP等级">
          <el-option label="普通" value="普通" />
          <el-option label="白银" value="白银" />
          <el-option label="黄金" value="黄金" />
          <el-option label="铂金" value="铂金" />
          <el-option label="钻石" value="钻石" />
        </el-select>
      </el-form-item>

      <el-form-item label="状态" prop="status">
        <el-select v-model="form.status" placeholder="请选择状态">
          <el-option label="已激活" value="已激活" />
          <el-option label="未激活" value="未激活" />
          <el-option label="注销" value="注销" />
          <el-option label="封禁" value="封禁" />
        </el-select>
      </el-form-item>

      <el-form-item label="头像URL" prop="url">
        <el-input v-model="form.url" placeholder="可选" autocomplete="off" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="editDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitEditUser">确认修改</el-button>
    </template>
  </el-dialog>

  <el-container class="main">
    <div>
      <el-header class="header">
          <el-icon :size="25" style="padding:0 10px; color:#FF6B6B;">
              <Files />  
          </el-icon>
          <div class="logo">用户管理</div>
      </el-header>
    </div>
    
    <el-row :gutter="20">
      <el-col :span="6" v-for="stat in stats" :key="stat.title">
        <el-card shadow="hover">
          <div >
            <el-icon :style="{ fontSize: '28px', color: stat.color }">
              <component :is="stat.icon" />
            </el-icon>
            <div>
              <div>{{ stat.value }}</div>
              <div>{{ stat.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-form :inline="true" :model="filters" style="justify-content: space-between;margin-left: 20px;margin-top: 10px;">
      <el-form-item label="用户 ID/名称">
        <el-input v-model="filters.userId" placeholder="输入用户 ID 或名称" />
      </el-form-item>
      <el-form-item label="手机号码">
        <el-input v-model="filters.phone" placeholder="输入手机号" />
      </el-form-item>
      <el-form-item label="注册时间">
        <el-date-picker v-model="filters.date" type="date" placeholder="选择日期" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="filters.status" placeholder="选择状态">
          <el-option label="全部" value="" />
          <el-option label="已激活" value="已激活" />
          <el-option label="未激活" value="未激活" />
          <el-option label="注销" value="注销" />
          <el-option label="封禁" value="封禁" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="resetFilters">重置</el-button>
        <el-button type="primary" @click="search">搜索</el-button>
      </el-form-item>
      <div style="float: right;">
        <el-button type="primary" icon="Plus" @click="addDialogVisible = true">添加用户</el-button>
        <el-button type="success"icon="Download":disabled="selectedUsers.length === 0"@click="exportSelectedUsers">导出数据</el-button>
      </div>  
    </el-form>

    <el-table 
      :data="pagedUsers" 
      stripe 
      border 
      style="width: 100%"
      @selection-change="handleSelectionChange"
      row-key="userId"
      v-loading="loading"
      :default-sort="{ prop: 'createTime', order: 'descending' }"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column label="头像 / 用户信息" min-width="200">
        <template #default="{ row }">
          <div>
            <el-avatar :src="row.url" />
            <div>
              <div>{{ row.name }}</div>
              <div>ID: {{ row.userId }}</div>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="联系方式" min-width="180">
        <template #default="{ row }">
          <div>{{ row.phone }}</div>
          <div>{{ row.email }}</div>
        </template>
      </el-table-column>
      <el-table-column label="注册信息" min-width="150">
        <template #default="{ row }">
          <div>{{ row.createTime }}</div>
          <div>{{ row.place }}</div>
        </template>
      </el-table-column>
      <el-table-column label="会员等级" width="120">
        <template #default="{ row }">
          <el-tag :type="getVipTagType(row.vip)">
            {{ row.vip }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag
            :type="row.status === '已激活' ? 'success' : row.status === '未激活' ? 'warning' : 'danger'"
          >
            {{ row.status }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="{ row }">
          <el-button type="primary" size="small" icon="View" circle @click="view(row.userId)" />
          <el-button type="warning" size="small" icon="Edit" circle @click="edit(row.userId)" />
          <el-button type="danger" size="small" icon="Delete" circle @click="remove(row.userId)" />
        </template>
      </el-table-column>
    </el-table>

    <div>
      <el-pagination
        background
        layout="prev, pager, next"
        :page-size="5"
        :total="filteredUsers.length"
        :current-page="currentPage"
        @current-change="handlePageChange"
      />
    </div>
  </el-container>

</template>

<script setup lang="ts">
import { ref, onMounted, toRaw } from 'vue'
import { User, Check, ShoppingCart, Coin, Files} from '@element-plus/icons-vue'
import axios from 'axios'
import CustomerCard from '@/components/CustomerCard.vue'
import { computed } from 'vue'
import PinyinMatch from 'pinyin-match'
import { ElMessage } from 'element-plus'

interface UserT {
  userId: string;
  name: string;
  phone: string;
  email: string;
  address: string;
  place: string;
  vip: string;
  createTime: string;
  status: string;
  url: string;
}

const selectedUser = ref<UserT | null>(null)
const dialogVisible = ref(false)
const users = ref<UserT[]>([])
const currentPage = ref(1)
const pageSize = 5
const stats = [
  { title: '总用户数', value: '8,452', icon: User, color: '#FF6B6B' },
  { title: '活跃用户', value: '7,201', icon: Check, color: '#4CAF50' },
  { title: '本月订单', value: '12,849', icon: ShoppingCart, color: '#2196F3' },
  { title: '本月营收', value: '¥856K', icon: Coin, color: '#FFC107' },
]
const addDialogVisible = ref(false)
const editDialogVisible = ref(false)
const selectedUsers = ref<UserT[]>([])
const loading = ref(false)

const handleSelectionChange = (selection: UserT[]) => {
  selectedUsers.value = selection
}

const exportSelectedUsers = () => {
  const content = selectedUsers.value.map(user => {
    return `用户ID: ${user.userId}
            姓名: ${user.name}
            手机号: ${user.phone}
            邮箱: ${user.email}
            注册时间: ${user.createTime}
            地址: ${user.place}
            会员等级: ${user.vip}
            状态: ${user.status}
            -------------------------`
  }).join('\n')

  const blob = new Blob([content], { type: 'text/plain;charset=utf-8' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = '选中用户导出.txt'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
}

const form = ref<UserT>({
  userId: '',
  name: '',
  phone: '',
  email: '',
  address: '',
  place: '',
  vip: '普通',
  createTime: new Date().toISOString().split('T')[0],
  status: '未激活',
  url: ''
})

const rules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    {
      validator(rule: any, value: string, callback: (error?: Error) => void) {
        const phoneRegex = /^\d{11}$/
        if (!phoneRegex.test(value)) {
          callback(new Error('手机号必须为11位数字'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
  email: [
    {
      validator(rule: any, value: string, callback: (error?: Error) => void) {
        if (value && !value.includes('@')) {
          callback(new Error('邮箱格式不正确'))
        } else {
          callback()
        }
      },
      trigger: 'blur',
    },
  ],
}

const addFormRef = ref()

const submitAddUser = () => {
  addFormRef.value.validate(async (valid: boolean) => {
    if (!valid) return
    try {
      const response = await axios.post('http://localhost:8080/ct',toRaw(form.value))
      if (response.data.code === 200) {
        ElMessage.success('添加成功')
        addDialogVisible.value = false
        Object.assign(form, {
          userId: '', //后端自增
          name: '',
          phone: '',
          email: '',
          address: '',
          place: '',
          vip: '普通',
          createTime: new Date().toISOString().split('T')[0],
          status: '未激活',
          url: ''
        })
        await queryUsers()
      } else {
        ElMessage.error('添加失败：' + response.data.msg)
      }
    } catch (error) {
      ElMessage.error('请求失败，请检查网络或服务器')
      console.error(error)
    }
  })
}

const filters = ref<{
  userId: string
  phone: string
  date: Date | null
  status: string
}>({
  userId: '',
  phone: '',
  date: null,
  status: ''
})

const formatDate = (date: Date): string => {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

const filteredUsers = computed(() => {
  return users.value.filter(user => {
    const userIdInput = filters.value.userId.toLowerCase()

    const matchUserId =
      userIdInput === '' ||
      user.userId.toLowerCase().includes(userIdInput) ||
      PinyinMatch.match(user.name, userIdInput)

    const matchPhone =
      filters.value.phone === '' || user.phone.includes(filters.value.phone)

    const matchDate =
      !filters.value.date ||
      user.createTime.startsWith(formatDate(filters.value.date))

    const matchStatus =
      filters.value.status === '' || user.status === filters.value.status

    return matchUserId && matchPhone && matchDate && matchStatus
  })
})

const queryUsers = async () => {
  try {
    const response = await axios.get('http://localhost:8080/ct/list')  
    users.value = response.data.data
  } catch (error) {
    console.error(error)
  }
}

const querySearch = async (id: string) => {
  try {
    const response = await axios.get(`http://localhost:8080/ct/i/${id}`)  
    selectedUser.value = response.data.data
    Object.assign(form.value, response.data.data)
  } catch (error) {
    console.error(error)
  }
}

const queryDelete = async (id: string) => {
  try {
    const response = await axios.delete(`http://localhost:8080/ct/${id}`)  
    if (response.data.code === 200) {
      ElMessage.success('删除成功')
      await queryUsers()
    } else {
      ElMessage.error('删除失败：' + response.data.msg)
    }
    } catch (error) {
    console.error(error)
  }
}

const resetFilters = () => {
  filters.value = {
    userId: '',
    phone: '',
    date: null,
    status: ''
  }
}

const pagedUsers = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return filteredUsers.value.slice(start, end)
})

const handlePageChange = (page: number) => {
  currentPage.value = page
}

const getVipTagType = (vip: string): string => {
  switch (vip) {
    case '白银':
      return 'info'       // 蓝色
    case '黄金':
      return 'warning'    // 黄色
    case '钻石':
      return 'primary'     // 红色
    case '铂金':
      return 'success'    // 绿色
    default:
      return 'default'    // 灰色或默认色
  }
}

const search = () => {
  if (isAllFiltersEmpty()) {
    ElMessage.info('请输入搜索条件')
    return
  }

  if (filteredUsers.value.length === 0) {
    ElMessage.warning('未搜索到对应用户')
  } else {
    ElMessage.success('搜索成功')
    currentPage.value = 1
  }
}

const isAllFiltersEmpty = () => {
  return (
    filters.value.userId === '' &&
    filters.value.phone === '' &&
    filters.value.date === null &&
    filters.value.status === ''
  )
}


const view = (id: string) => {
  querySearch(id);
  dialogVisible.value = true
}

const edit = (id: string) => {
  querySearch(id);
  editDialogVisible.value = true
}

const editFormRef = ref()

const submitEditUser = () => {
  editFormRef.value.validate(async (valid: boolean) => {
    if (!valid) return

    try {
      const response = await axios.put('http://localhost:8080/ct',toRaw(form.value))
      console.log(toRaw(form.value));
      if (response.data.code === 200) {
        ElMessage.success('修改成功')
        editDialogVisible.value = false
        Object.assign(form, {
          userId: '', 
          name: '',
          phone: '',
          email: '',
          address: '',
          place: '',
          vip: '普通',
          createTime: new Date().toISOString().split('T')[0],
          status: '未激活',
          url: ''
        })
        await queryUsers()
      } else {
        ElMessage.error('修改失败：' + response.data.msg)
      }
    } catch (error) {
      ElMessage.error('请求失败，请检查网络或服务器')
      console.error(error)
    }
    queryUsers();
  })
}

const remove = (id: string) => {
  queryDelete(id);
}

onMounted(() => {
  queryUsers();
})

</script>

<style scoped>
.main{
    flex-direction: column;
    display: flex;
    gap: 20px; 
}

.header{
    height: 60px;
    display: flex;
    align-items: center;
    z-index: 999;
}

.header .logo {
    color: #FF6B6B;
    font-size: 32px;
    font-weight: bold;
}
</style>