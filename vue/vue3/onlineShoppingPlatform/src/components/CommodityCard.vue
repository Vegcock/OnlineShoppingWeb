<template>
  <el-card 
    style="max-width: 360px;"
    :body-style="{ padding: '0px' }"
  >
    <template #header>
      <div class="card-header" style="position: relative; height: 50px; padding: 10px;">
        <span style="display: block; margin-bottom: 5px;font-weight: bold;font-size: 15px;">{{ commodity.name }}</span>
      </div>
    </template>

    <el-image
      :src="commodity.url"
      style="width: 80%; height: 200px; object-fit: cover; display: block; margin: 0 auto;"
    />

    <div style="padding: 14px">
      <p style="font-size: 14px;color:grey">{{ commodity.description }}</p>
      <div class="bottom">
        <span class="price">${{ commodity.price.toFixed(2) }}</span>
        <el-button type="primary" size="small" @click="handleAddToCart($event)" style="margin-left: 10px;"> 
          添加至购物车
        </el-button>
        <el-button
          type="warning"
          size="small"
          :disabled="isFavorited"
          @click="addToFavorite"
        >
          {{ isFavorited ? '已收藏' : '收藏' }}
        </el-button>
      </div>
    </div>
  </el-card>
  <div id="fly-dot-container" style="position: fixed; top: 0; left: 0; pointer-events: none; z-index: 9999;"></div>
</template>

<script setup lang="ts">
import { flyDotToCart } from '../utils/flyDotToCart'
import { ref, watch } from 'vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

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

const emit = defineEmits(['add-to-cart'])

const handleAddToCart = async (event: MouseEvent) => {
    const cartIcon = document.querySelector('.cart-float')
    if (cartIcon) {
      const endRect = (cartIcon as HTMLElement).getBoundingClientRect()
      flyDotToCart(event.clientX, event.clientY, endRect.left + 10, endRect.top + 10)
    }

    emit('add-to-cart', props.commodity)
}

const isFavorited = ref(false)

const checkFavorite = async () => {
  const res = await axios.get('http://localhost:8080/favorite/check', {
    params: {
      userId: localStorage.getItem('userId'),
      commodityId: props.commodity.comdId
    }
  });
  isFavorited.value = res.data.data;
};

const addToFavorite = async () => {
  const params = new URLSearchParams();
  params.append('userId', localStorage.getItem('userId') || '');
  params.append('commodityId', props.commodity.comdId.toString());

  await axios.post('http://localhost:8080/favorite/add', params, {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
  isFavorited.value = true;
  ElMessage.success('收藏成功');
};

watch(
  () => props.commodity,
  (newCommodity) => {
    if (newCommodity && newCommodity.comdId) {
      console.log(newCommodity)
      checkFavorite()
    }
  },
  { immediate: true }
)
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