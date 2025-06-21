<template>
  <el-card class="customer-card" v-if="customer">
    <div class="card-content">
      <!-- 左侧头像 -->
      <div class="avatar">
        <el-avatar :size="100" :src="customer.url" />
      </div>

      <!-- 右侧信息 -->
      <div class="info">
        <h2 style="margin: 0 0 12px;">{{ customer.name }}</h2>

        <el-descriptions :column="1" border>
          <el-descriptions-item label="用户ID">{{ customer.userId }}</el-descriptions-item>

          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(customer.status)">
              {{ customer.status }}
            </el-tag>
          </el-descriptions-item>

          <el-descriptions-item label="VIP等级">
            <el-tag :type="getVipType(customer.vip)">
              {{ customer.vip }}
            </el-tag>
          </el-descriptions-item>

          <el-descriptions-item label="联系电话 / 邮箱">
            <div style="display: flex; flex-direction: column;">
              <span>{{ customer.phone }}</span>
              <span>{{ customer.email }}</span>
            </div>
          </el-descriptions-item>

          <el-descriptions-item label="地址">{{ customer.address }}</el-descriptions-item>
          <el-descriptions-item label="地区">{{ customer.place }}</el-descriptions-item>
          <el-descriptions-item label="创建时间">{{ formatDate(customer.createTime) }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </div>
  </el-card>
  <div v-else>暂无客户数据</div>
</template>

<script setup lang="ts">
const { customer } = defineProps<{
  customer: {
    name: string
    phone: string
    email: string
    address: string
    place: string
    vip: string
    status: string
    url: string
    userId: string
    createTime: string
  } | null
}>()

// 状态颜色映射
const getStatusType = (status: string) => {
  switch (status) {
    case '已激活': return 'success'
    case '未激活': return 'warning'
    case '封禁': return 'danger'
    default: return 'info'
  }
}

// VIP颜色映射
const getVipType = (vip: string) => {
  switch (vip) {
    case '普通': return 'default'
    case '白银': return 'info'
    case '黄金': return 'warning'
    case '铂金': return 'success'
    case '钻石': return 'primary'
    default: return 'info'
  }
}

// 格式化日期
const formatDate = (str: string) => {
  if (!str) return ''
  return new Date(str).toLocaleString()
}
</script>

<style scoped>
.customer-card {
  width: 700px;
  margin: 20px auto;
  padding: 20px;
}

.card-content {
  display: flex;
  align-items: flex-start;
  gap: 24px;
}

.avatar {
  flex-shrink: 0;
}

.info {
  flex-grow: 1;
}
</style>
