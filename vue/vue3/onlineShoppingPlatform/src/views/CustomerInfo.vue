<template>
  <el-dialog v-model="detailDialogVisible" title="订单详情" width="600px">
    <template v-if="orderDetail">
      <el-descriptions title="用户信息" :column="2" border>
        <el-descriptions-item label="姓名">{{ orderDetail.customer.name }}</el-descriptions-item>
        <el-descriptions-item label="电话">{{ orderDetail.customer.phone }}</el-descriptions-item>
        <el-descriptions-item label="邮箱">{{ orderDetail.customer.email }}</el-descriptions-item>
      </el-descriptions>

      <el-descriptions title="订单信息" :column="2" border style="margin-top: 10px;">
        <el-descriptions-item label="订单号">{{ orderDetail.order.orderId }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ orderDetail.order.status }}</el-descriptions-item>
        <el-descriptions-item label="金额">{{ orderDetail.order.totalPrice }}</el-descriptions-item>
      </el-descriptions>

      <el-descriptions title="物流信息" :column="2" border style="margin-top: 10px;">
        <el-descriptions-item label="公司">{{ orderDetail.delivery.company }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ orderDetail.delivery.status }}</el-descriptions-item>
      </el-descriptions>
    </template>
  </el-dialog>

  <el-container class="container">
    <el-header class="header">
      <div class="header-left">
        <el-icon :size="25" style="padding: 0 20px; color:#fff">
          <HomeFilled />
        </el-icon>
        <div class="logo">个人中心</div>
      </div>
      <div class="header-right">
        <el-link :icon="User" href="/Customer" underline="always" type="info" style="font-size: 15px;">{{username}}</el-link>
        <el-link :icon="ShoppingCart" style="font-size: 15px;">购物车</el-link>
        <el-link :icon="SwitchButton" style="font-size: 15px; color: #FFFFFF;" @click="logout">退出</el-link>
        <el-link href="/Main" :underline="false" style="color: #fff; font-size: 16px">
          返回首页
        </el-link>
      </div>
    </el-header>

    <el-container>
      <el-aside class="aside">
        <el-menu
          :default-active="activeMenu"
          class="el-menu-vertical-demo"
          background-color="#f6a6a6"
          text-color="#fff"
          active-text-color="#ffd04b"
          @select="handleMenuSelect"
        >
          <el-menu-item index="1">
            <el-icon><Document /></el-icon>
            <span>个人信息</span>
          </el-menu-item>
          <el-menu-item index="2">
            <el-icon><Document /></el-icon>
            <span>我的订单</span>
          </el-menu-item>
          <el-menu-item index="3">
            <el-icon><Document /></el-icon>
            <span>我的收藏</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <el-main class="main">
        <div v-if="activeMenu === '1'">
            
          <div class="info-cards">
            <el-card class="stat-card" shadow="hover">
              <template #header><span>🎯 活跃天数</span></template>
              <div class="stat-value">128 天</div>
            </el-card>
            <el-card class="stat-card" shadow="hover">
              <template #header><span>❤️ 收藏商品</span></template>
              <div class="stat-value">17 件</div>
            </el-card>
            <el-card class="stat-card" shadow="hover">
              <template #header><span>🛒 历史订单</span></template>
              <div class="stat-value">45 单</div>
            </el-card>
          </div>

            <el-card shadow="hover" class="user-info-card">
                <template #header>
                <strong style="font-size: 18px;">👤 个人信息</strong>
                </template>

                <el-row gutter="20" align="top">
                <!-- 左边头像列 -->
                <el-col :span="6" class="avatar-col">
                    <el-image
                    style="width: 240px; height: 240px; border-radius: 8px; object-fit: cover;"
                    :src="user?.url || 'https://via.placeholder.com/120?text=头像'"
                    fit="cover"
                    lazy
                    />
                    <div class="avatar-icons">
                    <el-icon><User /></el-icon>
                    <el-icon><Star /></el-icon>
                    <el-icon><Location /></el-icon>
                    </div>
                </el-col>

                <!-- 右边信息表格列 -->
                <el-col :span="18">
                    <el-descriptions
                    :column="2"
                    border
                    direction="vertical"
                    size="default"
                    class="info-table"
                    >
                    <el-descriptions-item label="姓名">
                        <div class="value">{{ user?.name || '-' }}</div>
                    </el-descriptions-item>
                    <el-descriptions-item label="账号状态">
                        <el-tag :type="user?.status === '启用' ? 'success' : 'danger'" effect="dark" style="font-weight: bold">
                        {{ user?.status || '-' }}
                        </el-tag>
                    </el-descriptions-item>

                    <el-descriptions-item label="电话">
                        <div class="value">{{ user?.phone || '-' }}</div>
                    </el-descriptions-item>
                    <el-descriptions-item label="邮箱">
                        <div class="value">{{ user?.email || '-' }}</div>
                    </el-descriptions-item>

                    <el-descriptions-item label="地区">
                        <div class="value">{{ user?.place || '-' }}</div>
                    </el-descriptions-item>
                    <el-descriptions-item label="地址">
                        <div class="value">{{ user?.address || '-' }}</div>
                    </el-descriptions-item>

                    <el-descriptions-item label="VIP 等级">
                        <el-tag type="warning" effect="dark" style="font-weight: bold;">
                        {{ user?.vip || '-' }}
                        </el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="注册时间">
                        <div class="value">
                        {{ user?.create_time ? new Date(user.create_time).toLocaleDateString() : '-' }}
                        </div>
                    </el-descriptions-item>
                    </el-descriptions>
                </el-col>
                </el-row>
            </el-card>

        </div>

        <div v-else-if="activeMenu === '2'">
          <el-card shadow="hover">
            <template #header><strong>📦 我的订单</strong></template>

            <div v-if="orders.length > 0">
              <div class="order-stats">
                <el-card class="stat-card" shadow="hover">
                  <div class="stat-title">🧾 总订单数</div>
                  <div class="stat-value">{{ orders.length }}</div>
                </el-card>
                <el-card class="stat-card" shadow="hover">
                  <div class="stat-title">💰 累计金额</div>
                  <div class="stat-value">¥ {{ totalAmount.toFixed(2) }}</div>
                </el-card>
                <el-card class="stat-card" shadow="hover">
                  <div class="stat-title">📅 最近一单</div>
                  <div class="stat-value">{{ latestOrderDate }}</div>
                </el-card>
              </div>

              <el-table :data="orders" border style="margin-top: 20px">
                <el-table-column prop="orderId" label="订单号" width="120" />
                <el-table-column prop="name" label="收货人" />
                <el-table-column prop="totalPrice" label="金额" />
                <el-table-column prop="status" label="订单状态" />
                <el-table-column prop="deliveryStatus" label="物流状态" />
                <el-table-column
                  prop="createTime"
                  label="创建时间"
                  :formatter="formatDate"
                />
              <el-table-column label="操作" width="100">
                <template #default="scope">
                  <el-button size="small" @click="handleViewDetail(scope.row.orderId)">查看</el-button>
                </template>
              </el-table-column>
              </el-table>
            </div>

            <div v-else>
              <el-empty description="您还没有订单记录哦~" />
              <div class="order-hint">
                <el-card shadow="never">
                  <p>🎉 赶快去首页看看，选购第一件商品吧！</p>
                </el-card>
              </div>
            </div>
          </el-card>
        </div>

      <div v-else-if="activeMenu === '3'">
        <div style="padding: 20px">
          <h2>❤️ 我的收藏</h2>
          <el-row :gutter="20">
            <el-col :span="8" v-for="item in favoriteCommodities" :key="item.comdId">
              <CommodityCard :commodity="item" @add-to-cart="handleAddToCart" />
            </el-col>
          </el-row>
        </div>        
      </div>

      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { HomeFilled, Document, User, Star, Location, ShoppingCart,SwitchButton } from '@element-plus/icons-vue'
import CommodityCard from '@/components/CommodityCard.vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

interface Customer {
  user_id: string
  name: string
  phone: string
  email: string
  address: string
  place: string
  vip: string
  create_time: string
  status: string
  url: string
}

interface Order {
  orderId: number
  name: string
  commodityId: number
  description: string
  accounts: number
  totalPrice: number
  status: string
  deliveryStatus: string
  createTime: string
}

interface Commodity {
  name: string
  price: number
  description: string
  url: string
  status: string
  comdId: number
}

interface CartItem {
    name : string
    commodity : number
    description : string
    accounts : number
    price : number
}

interface Favorite {
  id: number
  userId: string
  commodityId: number
  createTime: string
}

interface Delivery {
  deliveryId: string
  orderId: number
  receiverName: string
  address: string
  phone: string
  company: string
  shipTime: string
  status: string
  statusUpdateTime: string
  remarks: string
  posterName: string
}

interface OrderDetail {
  order: Order
  customer: Customer
  delivery: Delivery
}

const activeMenu = ref('1')
const user = ref<Customer | null>(null)
const orders = ref<Order[]>([])
const favorites = ref<Favorite[]>([])
const commodities = ref<Commodity[]>([])
const detailDialogVisible = ref(false)    
const orderDetail = ref<OrderDetail>()  // 三表详情
const username = localStorage.getItem('userName') as string

const handleViewDetail = async (orderId: number) => {
  try {
    const res = await axios.get(`http://localhost:8080/order/detail/${orderId}`)
    orderDetail.value = res.data.data;
    detailDialogVisible.value = true;
  } catch (error) {
    ElMessage.error('获取订单详情失败');
  }
};

const handleMenuSelect = (index: string) => {
  console.log(favoriteCommodities)
  activeMenu.value = index
}

const totalAmount = computed(() =>
  orders.value.reduce((sum, order) => sum + (order.totalPrice || 0), 0)
)

const latestOrderDate = computed(() => {
  if (orders.value.length === 0) return '无'
  const sorted = [...orders.value].sort(
    (a, b) => new Date(b.createTime).getTime() - new Date(a.createTime).getTime()
  )
  return new Date(sorted[0].createTime).toLocaleDateString()
})

function formatDate(row: Order): string {
  return new Date(row.createTime).toLocaleString()
}

const convertCart = (commodity: Commodity) : CartItem  => {
  return {
    name: commodity.name,
    commodity: commodity.comdId,
    description: commodity.description,
    accounts: 1,
    price: commodity.price
  } 
}

const handleAddToCart = async (commodity: Commodity) => {
    const userId = localStorage.getItem('userId') || ''
    const cartItem = convertCart(commodity)
    const res = await axios.post('http://localhost:8080/cr', cartItem)
    const cartId = res.data.data

    const params = new URLSearchParams()
    params.append('UserId', userId)
    params.append('CartId', cartId)
    await axios.post('http://localhost:8080/uc/add', params)
    ElMessage({
      message: '添加成功',
      type: 'success'
    })
}

const favoriteCommodities = computed(() => {
  return favorites.value
    .map(fav => commodities.value.find(c => c.comdId === fav.commodityId))
    .filter((c): c is Commodity => c !== undefined)
})

onMounted(async () => {
  try {
    const userId = localStorage.getItem('userId') || ''
    console.log(userId)

    // 获取用户信息
    const userRes = await axios.get(`http://localhost:8080/ct/i/${userId}`);
    user.value = userRes.data.data

    // 获取订单列表
    const ordersRes = await axios.get(`http://localhost:8080/uo`, {
      params: { UserId: userId }
    })
    orders.value = ordersRes.data.data || []

    // 获取商品列表
    const commoditiesRes = await axios.get('http://localhost:8080/ocmd/list');
    commodities.value = commoditiesRes.data.data || [];

    // 获取收藏列表
    const res = await axios.get('http://localhost:8080/favorite/list', {
      params: { userId }
    })
    favorites.value = res.data.data

  } catch (error) {
    console.error(error)
    user.value = {
      user_id: '',
      name: '',
      phone: '',
      email: '',
      address: '',
      place: '',
      vip: '',
      create_time: '',
      status: '',
      url: ''
    }
    orders.value = []
  }
})

const router = useRouter()
const logout = () => {
  localStorage.clear()
  router.push('/login')
}

</script>

<style scoped>
.container {
  height: 100vh;
}

.header {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: #ff6b6b;
  color: white;
  padding: 0 20px;
}

.header-left {
  display: flex;
  align-items: center;
}

.header-right a{
    color: white;
    text-decoration: none;
    margin-left: 20px;
    font-size: 20px;
}

.menu {
  display: flex;
  gap: 20px;
  margin-left: 20px;
}

.menu-item {
  cursor: pointer;
  color: white;
  font-size: 16px;
}

.menu-item.active {
  font-weight: bold;
}

.user-info {
  display: flex;
}

.logo {
  font-size: 24px;
  font-weight: bold;
}

.aside {
  width: 200px;
  background-color: #f6a6a6;
}

.main {
  padding: 30px;
  background: #f5f5f5;
  overflow-y: auto;
}

.info-cards {
  display: flex;
  gap: 20px;
  margin-top: 20px;
}

.user-info-card {
  margin-top: 80px;
  margin-bottom: 30px;
}

.avatar-col {
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.avatar-icons {
  margin-top: 20px;
  display: flex;
  gap: 10px;
  justify-content: center;
}

.info-table {
  font-size: 14px;
  font-weight: 500;
}

.value {
  font-weight: bold;
  font-size: 15px;
  color: #333;
}

.order-stats {
  display: flex;
  gap: 20px;
  margin-top: 10px;
}

.stat-card {
  flex: 1;
  text-align: center;
}

.stat-title {
  font-size: 16px;
  color: #999;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409eff;
  margin-top: 5px;
}

.order-hint {
  margin-top: 20px;
  text-align: center;
}
</style>
