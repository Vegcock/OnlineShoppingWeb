<template>
  <div class="admin-dashboard">
    <el-row :gutter="20">
      <el-col :span="6" v-for="card in stats" :key="card.title">
        <el-card shadow="hover">
          <div >
            <el-icon :style="{ fontSize: '28px', color: card.color }">
              <component :is="card.icon" />
            </el-icon>
            <div>
              <div >{{ card.value }}</div>
              <div >{{ card.title }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-card style="margin-top: 20px">
      <template #header>
        <span>快速导航</span>
      </template>
      <el-space wrap size="large">
        <el-button type="primary" icon="UserFilled" @click="go('/admin/Customers')">用户管理</el-button>
        <el-button type="warning" icon="Goods" @click="go('/admin/Storage')">商品管理</el-button>
        <el-button type="success" icon="Document" @click="go('/admin/Orders')">订单管理</el-button>
      </el-space>
    </el-card>

    <el-card style="margin-top: 20px">
      <template #header>
        <span>公告 / 待办事项</span>
      </template>
      <el-timeline>
        <el-timeline-item
          v-for="item in notices"
          :key="item.time"
          :timestamp="item.time"
          :type="item.type"
        >
          {{ item.text }}
        </el-timeline-item>
      </el-timeline>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed } from 'vue'
import { User, Coin, ShoppingCart, Document } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const customerQuantity = ref(0)
const orderQuantity = ref(0)
const revenue = ref(0)
const pendingOrders = ref(0)

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

interface Customer {
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

const orderList = ref<Order[]>([])
const customerList = ref<Customer[]>([])

const go = (path: string) => {
  router.push(path)
}

const revenueDisplay = computed(() =>
  `￥${revenue.value.toLocaleString('zh-CN', { minimumFractionDigits: 2 })}`
)

const stats = [
  { title: '用户总数', value: customerQuantity, icon: User, color: '#FF6B6B' },
  { title: '本月订单', value: orderQuantity, icon: ShoppingCart, color: '#2196F3' },
  { title: '本月营收', value: revenueDisplay , icon: Coin, color: '#FFC107' },
  { title: '待处理订单', value: pendingOrders, icon: Document, color: '#4CAF50' },
]

const notices = ref([
  { text: '本月订单量同比增长 8.3%', time: '2025-06-01', type: 'success' },
  { text: '新增功能：商品库存预警', time: '2025-05-28', type: 'info' },
  { text: '请管理员及时审核新注册商户用户', time: '2025-05-25', type: 'warning' },
  { text: '平台将于 6 月 10 日凌晨升级', time: '2025-05-21', type: 'danger' }
])

onMounted(async () => {
  const orderRes = await axios.get('http://localhost:8080/od/list')
  orderList.value = orderRes.data.data;
  const customerRes = await axios.get('http://localhost:8080/ct/list')
  customerList.value = customerRes.data.data;
  customerQuantity.value = customerList.value.length
  orderQuantity.value = orderList.value.length
  revenue.value = orderList.value.reduce((acc, order) => acc + order.totalPrice, 0)
  pendingOrders.value = orderList.value.filter(order => order.deliveryStatus === '未发货').length
})
</script>

<style scoped>
.admin-dashboard {
  padding: 20px;
}
</style>
