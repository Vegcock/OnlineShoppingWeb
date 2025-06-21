<template>


  <div class="order-manager">
    <h2 class="title">订单管理</h2>

  <!-- 仪表卡片区域 -->
  <div class="dashboard" style="margin-bottom: 20px;">
    <el-row :gutter="20">
      <el-col :span="6" v-for="card in dashboardCards" :key="card.title">
        <el-card shadow="hover" style="text-align: center;">
          <el-icon :style="{ fontSize: '26px', color: card.color }">
            <component :is="card.icon" />
          </el-icon>
          <div style="font-weight: bold; font-size: 20px; margin: 8px 0;">
            {{ card.value }}
          </div>
          <div style="color: #888;">{{ card.title }}</div>
        </el-card>
      </el-col>
    </el-row>
  </div>

    <div class="toolbar">
      <el-button type="primary" @click="openCreateDialog">新增订单</el-button>
      <el-input v-model="searchQuery" placeholder="搜索客户名/订单号" style="width: 300px" clearable @input="handleSearch" />
    </div>

    <el-table :data="paginatedOrders" border style="width: 100%;margin-bottom: 10px;">
      <el-table-column prop="orderId" label="订单编号" width="160" />
      <el-table-column prop="name" label="客户名" width="120" />
      <el-table-column prop="commodityId" label="商品ID" width="120" />
      <el-table-column prop="description" label="描述" />
      <el-table-column prop="accounts" label="数量" width="80" />
      <el-table-column prop="price" label="总价" width="100">
        <template #default="scope">￥{{ scope.row.price.toFixed(2) }}</template>
      </el-table-column>
      <el-table-column prop="status" label="支付状态" width="120">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="deliveryStatus" label="配送状态" width="120">
        <template #default="scope">
          <el-tag :type="getDeliveryType(scope.row.deliveryStatus)">{{ scope.row.deliveryStatus }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="下单时间" width="180">
        <template #default="scope">{{ formatTime(scope.row.createTime) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="200">
        <template #default="scope">
          <el-button size="small" @click="editOrder(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteOrder(scope.row.orderId)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="prev, pager, next, jumper"
      :total="filteredOrders.length"
      :page-size="pageSize"
      :current-page="currentPage"
      @current-change="handlePageChange"
    />

    <!-- 创建/编辑对话框 -->
      <el-dialog :title="isEditing ? '编辑订单' : '新增订单'" v-model="dialogVisible" width="600px">
        <el-form :model="editForm" label-width="100px">
          <el-form-item label="客户名">
            <el-select
              v-model="editForm.name"
              placeholder="请选择客户"
              :label="`${editForm.name}`"
              @change="handleCustomerChange"
              style="width: 100%"
            >
              <el-option
                v-for="user in customerList"
                :key="user.id"
                :value="user.name"
                :label="`${user.name}`"
              >
                <div style="display: flex; justify-content: space-between;">
                  <span>{{ user.name }}</span>
                  <span style="color: #999;">{{ user.id }}</span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="商品">
            <el-select
              v-model="editForm.commodityId"
              placeholder="请选择商品"
              :label="editForm.commodityId" 
              @change="handleCommodityChange"
              style="width: 100%"
            >
              <el-option
                v-for="item in commodityList"
                :key="item.id"
                :value="item.id"
                :label="`${item.id}`"
              >
                <div style="display: flex; justify-content: space-between;">
                  <span>{{ item.id }}</span>
                  <span style="color: #999;">{{ item.name }}</span>
                </div>
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="描述">
            <el-input
              v-model="editForm.description"
              :disabled="isDescLoading"
              placeholder="描述自动生成中，请稍候"
            />
          </el-form-item>
          <el-form-item label="数量"><el-input-number v-model="editForm.accounts" :min="1" /></el-form-item>
          <el-form-item label="总价">
            <el-input-number v-model="editForm.totalPrice" :min="0" step="0.1" :disabled="true" />
          </el-form-item>
          <el-form-item label="支付状态">
            <el-select v-model="editForm.status">
              <el-option label="未付款" value="未付款" />
              <el-option label="已付款" value="已付款" />
            </el-select>
          </el-form-item>
          <el-form-item label="配送状态">
            <el-select
              v-model="editForm.deliveryStatus"
              :disabled="editForm.status === '未付款'"
            >
              <el-option label="未发货" value="未发货" />
              <el-option label="运输中" value="运输中" />
              <el-option label="已完成" value="已完成" />
            </el-select>
          </el-form-item>
        </el-form>

      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button
          type="primary"
          @click="saveOrder"
          :loading="isSubmitting"
          :disabled="isDescLoading"
        >
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue'
import axios from 'axios'
import PinyinMatch from 'pinyin-match'
import OpenAI from "openai";
import { ElMessage } from 'element-plus'
import { Coin, Clock, Van, List } from '@element-plus/icons-vue'

const dashboardCards = computed(() => {
  const paidOrders = orders.value.filter(order => order.status === '已付款')
  const unpaidDelivered = paidOrders.filter(order => order.deliveryStatus === '未发货')
  const latest = [...orders.value].sort((a, b) => new Date(b.createTime).getTime() - new Date(a.createTime).getTime())[0]

  return [
    {
      title: '本月营收',
      value: `￥${paidOrders.reduce((acc, o) => acc + o.totalPrice*o.accounts, 0).toFixed(2)}`,
      icon: Coin,
      color: '#FFC107'
    },
    {
      title: '最新订单',
      value: latest ? `#${latest.orderId}` : '暂无',
      icon: Clock,
      color: '#409EFF'
    },
    {
      title: '待发货订单',
      value: unpaidDelivered.length,
      icon: Van,
      color: '#67C23A'
    },
    {
      title: '总订单数',
      value: orders.value.length,
      icon: List,
      color: '#409EFF'
    }
  ]
})

const openai = new OpenAI({
  baseURL: 'https://api.deepseek.com/v1',
  apiKey: 'sk-3d86b47e0af64f34aae2017c3e0be844', 
  dangerouslyAllowBrowser: true 
});

async function simplifyProductDescription(productName:string, productDescription:string) {
  try {
    const response = await openai.chat.completions.create({
      model: "deepseek-chat",
      messages: [
        {
          role: "system",
          content: "你是一个订单助手，请将商品名称和描述总结成15字以内的简短描述，只保留关键信息。"
        },
        {
          role: "user",
          content: `商品名称: ${productName}\n商品描述: ${productDescription}`
        }
      ],
      max_tokens: 30, 
      temperature: 0.3 
    });
    
    return response.choices[0].message.content;
  } catch (error) {
    console.error("API调用失败:", error);
    return "描述生成失败，请重试";
  }
}

interface Order {
  orderId: number
  name: string
  commodityId: number | null
  description: string
  accounts: number
  totalPrice: number
  status: string
  deliveryStatus: string
  createTime: string
}

interface Commodity {
  id: number
  name: string
  description: string
  price: number
}

interface Customer{
  id: string
  name: string
}

const orders = ref<Order[]>([])
const dialogVisible = ref(false)
const isEditing = ref(false)
const editForm = ref<Order>({
  orderId: 1,
  name: '',
  commodityId: 1,
  description: '',
  accounts: 1,
  totalPrice: 0,
  status: '未付款',
  deliveryStatus: '待发货',
  createTime: ''
})
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = 10
const commodityList = ref<Commodity[]>([])
const customerList = ref<Customer[]>([])
const isDescLoading = ref(false)  
const isSubmitting = ref(false) 

const queryList = async () => {
  try {
    const result = await axios.get('http://localhost:8080/od/list')  
    orders.value = result.data.data 
  } catch (error) {
    console.error('请求失败:', error)
  }
}

const queryCommodityList = async () => {
  try {
    const res = await axios.get('http://localhost:8080/cmd/list')
    commodityList.value = res.data.data.map((item: any) => ({
      id: item.id,
      name: item.name,
      description: item.description,
      price: item.price
    }))
  } catch (error) {
    console.error('获取商品列表失败', error)
  }
}

const queryCustomerList = async () => {
  try {
    const res = await axios.get('http://localhost:8080/ct/list')
    customerList.value = res.data.data.map((item: any) => ({
      id: item.user_id,
      name: item.name
    }))
  } catch (error) {
    console.error('获取客户列表失败', error)
  }
}

const filteredOrders = computed(() => {
  return orders.value.filter(order => {
    return (
        order.name.includes(searchQuery.value) ||  order.name.toLowerCase().includes(searchQuery.value) ||
        PinyinMatch.match(order.name, searchQuery.value) ||  order.orderId.toString().includes(searchQuery.value)
    )
  })
})

const paginatedOrders = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  return filteredOrders.value.slice(start, start + pageSize)
})

const resetForm = () => {
  editForm.value = {
    orderId: 0,
    name: '',
    commodityId: null,
    description: '',
    accounts: 0,
    totalPrice: 0,
    status: '未付款',
    deliveryStatus: '待发货',
    createTime: ''
  }
}

const formatTime = (t: string) => new Date(t).toLocaleString()

const getStatusType = (s: string) => s === '已付款' ? 'success' : 'danger'
const getDeliveryType = (s: string) => s === '未发货' ? 'info' : s === '配送中' ? 'primary' : s === '已发货' ? 'process' : s === '已收货' ? 'success' : 'danger'

const handlePageChange = (p: number) => currentPage.value = p
const handleSearch = () => currentPage.value = 1
const handleCommodityChange = async (id: number) => {
  const selected = commodityList.value.find(item => item.id === id)
  if (selected) {
    isDescLoading.value = true 
    const desc = await simplifyProductDescription(selected.name, selected.description)
    editForm.value.description = desc ?? ''
    isDescLoading.value = false 
    updateTotalPrice()
  }
}

const handleCustomerChange = (userId: string) => {
  const selected = customerList.value.find(user => user.id === userId)
  if (selected) {
    console.log('选中用户：', selected.name)
  }
}

const updateTotalPrice = () => {
  const selected = commodityList.value.find(item => item.id === editForm.value.commodityId)
  const unitPrice = selected?.price || 0
  const count = editForm.value.accounts || 0
  editForm.value.totalPrice = parseFloat((unitPrice * count).toFixed(2))
}

const formatDateToMySQL = (date: Date): string => {
  const pad = (n: number) => n.toString().padStart(2, '0')
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ` +
         `${pad(date.getHours())}:${pad(date.getMinutes())}:${pad(date.getSeconds())}`
}

const openCreateDialog = () => {
  isEditing.value = false
  editForm.value = {
    orderId: 0,
    name: '',
    commodityId: null,
    description: '',
    accounts: 1,
    totalPrice: 0,
    status: '未付款',
    deliveryStatus: '未发货',
    createTime: formatDateToMySQL(new Date()) 
  }
  dialogVisible.value = true
}


const editOrder = (order: Order) => {
  isEditing.value = true
  editForm.value = { ...order }
  dialogVisible.value = true
}

const deleteOrder = async (id: number) => {
  await axios.delete(`http://localhost:8080/od/${id}`)
  queryList()
}

const saveOrder = async () => {
  if (isDescLoading.value) {
    ElMessage.warning('描述生成中，请稍后再提交')
    return
  }

  isSubmitting.value = true

  try {
    if (isEditing.value) {
      try{
        axios.put('http://localhost:8080/od', editForm.value)
      } catch (error) {
        console.error('请求失败:', error)
      }
    } else {
      axios.post('http://localhost:8080/od', editForm.value)
    }
    ElMessage.success('保存成功')
  } catch (err) {
    ElMessage.error('保存失败')
  } finally {
    isSubmitting.value = false
    resetForm()
    queryList()
  }
}

onMounted(() => {
  queryList();
  queryCommodityList();
  queryCustomerList();
})

watch(() => editForm.value.accounts, () => {
  updateTotalPrice()
})
</script>

<style scoped>
.order-manager {
  padding: 20px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}
.title {
  margin-top: 20px;
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 20px;
}
.toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}
</style>
