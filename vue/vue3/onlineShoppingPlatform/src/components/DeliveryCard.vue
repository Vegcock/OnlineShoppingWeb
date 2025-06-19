<template>
  <el-card
    :title="`📦 物流详情 - ${delivery?.deliveryId || ''}`"
    width="620px"
  >
    <el-descriptions :column="1" border class="desc-box">
      <el-descriptions-item label="订单号">
        <el-icon><Document /></el-icon>
        <span class="info-text">{{ delivery?.orderId }}</span>
      </el-descriptions-item>

      <el-descriptions-item label="物流单号">
        <el-icon><Ticket /></el-icon>
        <span class="info-text">{{ delivery?.deliveryId }}</span>
      </el-descriptions-item>

      <el-descriptions-item label="收货人">
        <el-icon><User /></el-icon>
        <span class="info-text">{{ delivery?.receiverName }}</span>
      </el-descriptions-item>

      <el-descriptions-item label="地址">
        <el-icon><Location /></el-icon>
        <span class="info-text">{{ delivery?.address }}</span>
      </el-descriptions-item>

      <el-descriptions-item label="电话">
        <el-icon><Phone /></el-icon>
        <span class="info-text">{{ delivery?.phone }}</span>
      </el-descriptions-item>

      <el-descriptions-item label="物流公司">
        <el-icon><ShoppingCart /></el-icon>
        <span class="info-text company">{{ getCompanyName(delivery?.company || '') }}</span>
      </el-descriptions-item>

      <el-descriptions-item label="状态">
        <el-tag :type="statusTagType(delivery?.status)" class="status-tag">
          {{ delivery?.status || '未知' }}
        </el-tag>
      </el-descriptions-item>

      <el-descriptions-item label="发货时间">
        <el-icon><Clock /></el-icon>
        <span class="info-text">{{ delivery?.shipTime }}</span>
      </el-descriptions-item>

      <el-descriptions-item label="备注">
        <span class="remarks-text">{{ delivery?.remarks || '无' }}</span>
      </el-descriptions-item>

      <el-descriptions-item label="状态更新时间">
        <el-icon><Timer /></el-icon>
        <span class="info-text">{{ delivery?.statusUpdateTime || '无' }}</span>
      </el-descriptions-item>
    </el-descriptions>
  </el-card>
</template>

<script setup lang="ts">
import {
  Document,
  Ticket,
  User,
  Location,
  Phone,
  ShoppingCart,
  Clock,
  Timer
} from '@element-plus/icons-vue'

interface Delivery {
  orderId: number | string
  deliveryId: string
  receiverName: string
  address: string
  phone: string
  company: string
  status: string
  shipTime: string
  remarks?: string
  statusUpdateTime?: string
}

const props = defineProps<{
  delivery: Delivery 
}>()

const emit = defineEmits(['update:visible'])

const getCompanyName = (code: string) => {
  const map: Record<string, string> = {
    SF: '顺丰快递',
    YTO: '圆通快递',
    ZTO: '中通快递',
    JD: '京东物流',
    HTKY: '百世快递'
  }
  return map[code] || code || '未知物流公司'
}

const statusTagType = (status?: string) => {
  if (!status) return 'info'
  const s = status.toLowerCase()
  if (s.includes('已签收')) return 'success'
  if (s.includes('派送中') || s.includes('运输中')) return 'warning'
  if (s.includes('异常') || s.includes('延误')) return 'danger'
  return 'info'
}

</script>

<style scoped>
.custom-delivery-dialog .el-dialog__header {
  background-color: #FF6B6B;
  color: white;
  font-weight: 700;
  font-size: 18px;
  padding: 15px 20px;
  border-radius: 8px 8px 0 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.desc-box {
  background: #fff;
  border-radius: 8px;
  padding: 20px 25px;
  color: #333;
  font-size: 15px;
}

.el-descriptions__label {
  color: #FF6B6B;
  font-weight: 600;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 6px;
}

.info-text {
  margin-left: 8px;
  color: #444;
  font-weight: 500;
}

.company {
  font-weight: 700;
  color: #FF6B6B;
}

.status-tag {
  font-weight: 600;
  font-size: 14px;
  border-radius: 12px;
}

.remarks-text {
  font-style: italic;
  color: #666;
}

.close-btn {
  background-color: #FF6B6B;
  border-color: #FF6B6B;
  color: white;
  font-weight: 600;
  padding: 6px 25px;
  border-radius: 20px;
  transition: background-color 0.3s ease;
}

.close-btn:hover {
  background-color: #e65555;
  border-color: #e65555;
}

/* Icon颜色统一珊瑚红 */
.el-icon {
  color: #FF6B6B;
  font-size: 18px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
}
</style>
