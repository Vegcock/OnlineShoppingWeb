<template>
  <el-card 
    style="max-width: 680px"
    :body-style="{ padding: '0px' }"
  >
    <template #header>
      <div class="card-header" style="position: relative; height: 50px; padding: 10px;">
        <span style="display: block; margin-bottom: 15px;">{{ commodity.name }}</span>
        <el-tag 
          type="success" 
          size="small"
          style="position: absolute; right: 10px; bottom: 0px;"
        >
          {{ commodity.status }}
        </el-tag>
      </div>
    </template>

    <el-image
      :src="getImageUrl(commodity.url)"
      style="width: 100%; height: 200px; object-fit: cover"
    />

    <div style="padding: 14px">
      <p>{{ commodity.description }}</p>
      <div class="bottom">
        <span class="price">${{ commodity.price.toFixed(2) }}</span>
        <el-button type="primary" size="small" @click="handleAddToCart($event)">
          添加至购物车
        </el-button>
      </div>
    </div>
  </el-card>
  <div id="fly-dot-container" style="position: fixed; top: 0; left: 0; pointer-events: none; z-index: 9999;"></div>
</template>

<script setup lang="ts">
import { flyDotToCart } from '../utils/flyDotToCart'

interface Commodity {
  name: string
  price: number
  description: string
  url: string
  status: string
  comdId: number
}

const props = defineProps<{
  commodity: Commodity
}>()

const getImageUrl = (url: string): string => {
  try {
    const filename = url.split('/').pop() || ''
    return new URL(`../assets/${filename}`, import.meta.url).href
  } catch {
    return '' 
  }
}

const emit = defineEmits(['add-to-cart'])

const handleAddToCart = async (event: MouseEvent) => {
    const cartIcon = document.querySelector('.cart-float')
    if (cartIcon) {
      const endRect = (cartIcon as HTMLElement).getBoundingClientRect()
      flyDotToCart(event.clientX, event.clientY, endRect.left + 10, endRect.top + 10)
    }

    emit('add-to-cart', props.commodity)
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.bottom {
  margin-top: 13px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.price {
  font-size: 18px;
  color: #f56c6c;
  font-weight: bold;
}
</style>