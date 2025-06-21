<template>
    <el-container class="mainPage" style="display: flex;flex-direction: column;">
        <el-header class="mainHeader">
            <div class="logo">简洁电商</div>
            <div class="search-box">
                <el-input v-model="searchText" placeholder="搜索商品..." @keyup.enter="handleSearch" />
                <el-button class="search-button" type="primary" @click="handleSearch">
                    <el-icon >
                        <Search/>
                    </el-icon>
                    <span style="vertical-align: right;color: #FFFFFF">搜索</span>
                </el-button>
            </div>
            <div class="user-nav">
                <el-link :icon="User" href="/Customer" underline="always" type="info" style="font-size: 15px;">{{username}}</el-link>
                <el-link :icon="ShoppingCart" @click="goToCart" style="font-size: 15px;">购物车</el-link>
                <el-link :icon="SwitchButton" style="font-size: 15px; color: #FFFFFF;" @click="logout">退出</el-link>
            </div>
        </el-header>
        
        <el-main class="main-content" >
            <el-image :src="src" style="width: 85%;margin-bottom: 50px;"/>
            <!-- 商品卡片网格 -->
            <div class="commodity-grid">
                <CommodityCard
                    v-for="(item, index) in pagedCommodities"
                    :key="index"
                    :commodity="item"
                    @add-to-cart="handleAddToCart"
                />
            </div>

            <!-- 分页控件 -->
            <el-pagination
                v-if="commodities.length > 0"
                style="margin-top: 30px; justify-content: center"
                :total="totalCommodities"
                :page-size="pageSize"
                v-model:current-page="currentPage"
            />
        </el-main>
        <div class="cart-float" @click="goToCart">
            <el-icon><ShoppingCart /></el-icon>
            <span class="cart-count" v-if="cartCount > 0">{{ cartCount }}</span>
        </div>
        <el-footer class="footer">
            <div class="container">
                <div class="footer-links">
                    <div class="link-group">
                        <h4>购物指南</h4>
                        <a href="#">购物流程</a>
                        <a href="#">会员介绍</a>
                        <a href="#">常见问题</a>
                    </div>
                    <div class="link-group">
                        <h4>配送方式</h4>
                        <a href="#">上门自提</a>
                        <a href="#">配送服务</a>
                        <a href="#">配送费标准</a>
                    </div>
                    <div class="link-group">
                        <h4>支付方式</h4>
                        <a href="#">在线支付</a>
                        <a href="#">分期付款</a>
                        <a href="#">货到付款</a>
                    </div>
                    <div class="link-group">
                        <h4>售后服务</h4>
                        <a href="#">退换货政策</a>
                        <a href="#">退款说明</a>
                        <a href="#">联系客服</a>
                    </div>
                </div>
                <div class="copyright">
                    © 2023 简洁电商平台 版权所有
                </div>
            </div>
        </el-footer>
    </el-container>
      <div id="fly-dot-container" style="position: fixed; top: 0; left: 0; pointer-events: none; z-index: 9999;"></div>
</template>

<script lang="ts" setup>
import { ElContainer, ElMessage } from 'element-plus';
import { ref, computed, onMounted } from 'vue';
import { Search, ShoppingCart, Star, User, SwitchButton} from '@element-plus/icons-vue'
// import advertisingImg from '@/assets/advertising.png'
import CommodityCard from '@/components/CommodityCard.vue';
import axios from 'axios'
import { useRouter } from 'vue-router'
import Pinyin from 'pinyin-match';

const router = useRouter()
// const activeIndex = ref('1') 
const src = '/assets/advertising.png'
const currentPage = ref(1)
const pageSize = ref(8)
const totalCommodities = computed(() => commodities.value.length)
const id = localStorage.getItem('userId') as string
const username = localStorage.getItem('userName') as string
const cartCount = ref(0)
const searchText = ref('')

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

const commodities = ref<Commodity[]>([])

const filteredCommodities = computed(() => {
  if (!searchText.value.trim()) {
    return commodities.value
  }
  const keyword = searchText.value.trim().toLowerCase()
  return commodities.value.filter(c => c.name.toLowerCase().includes(keyword) || Pinyin.match(c.name.toLowerCase(),keyword))
})

const logout = () => {
  localStorage.clear()
  router.push('/login')
}

const goToCart = () => {
  router.push({
    path: '/Cart',
    query: { id }
  });
}

const queryList = async () => {
  try {
    const result = await axios.get('http://localhost:8080/ocmd/list')  
    commodities.value = result.data.data 
  } catch (error) {
    console.error('请求失败:', error)
  }
}

const queryCartList = async () => {
    try{
        const result = await axios.get('http://localhost:8080/uc',{
            params:{
                UserId: id
            }
        }) 
        cartCount.value = result.data.data.length
    } catch (error) {
        console.error('请求失败:', error)
    }
}

// const handleSelect = (key: string, keyPath: string[]) => {

// }

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
    const cartItem = convertCart(commodity)
    const res = await axios.post('http://localhost:8080/cr', cartItem)
    const cartId = res.data.data

    const params = new URLSearchParams()
    params.append('UserId', id)
    params.append('CartId', cartId)
    await axios.post('http://localhost:8080/uc/add', params)
    cartCount.value++
    ElMessage({
      message: '添加成功',
      type: 'success'
    })
}

const pagedCommodities = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filteredCommodities.value.slice(start, start + pageSize.value)
})

const handleSearch = () => {
  currentPage.value = 1
  if (filteredCommodities.value.length === 0) {
    ElMessage({
      message: '未找到匹配的商品',
      type: 'warning'
    })
  } else{
    ElMessage({
      message: '搜索成功',
      type: 'success'
    })
  }
}

onMounted(() => {
  queryList();
  queryCartList();
})

</script>

<style scoped>
/* 顶部导航栏 */
.mainHeader {
    height: 80px;
    display: flex;
    flex-direction: row;
    align-items: center;
    background-color: #FF6B6B;
    justify-content: space-around;
    z-index: 999;
}

.logo {
    color: #FFFFFF;
    font-size: 28px;
    font-weight: bold;
}

.search-box {
    display: flex;
    width: 500px;
    align-items: center;
    margin-left: 15px;
}

.search-box input {
    flex: 1;
    border: none;
    border-radius: 4px 0 0 4px;
    font-size: 18px;
}

.search-button  {
    background-color: #FF8E8E;
    border: none;
    border-radius: 0 4px 4px 0;
    cursor: pointer;
}

.search-button .el-icon{
    font-size: 16px;
    color: #FFFFFF;
    z-index: 10;
}

.user-nav {
    display: flex;
    align-items: center;
}


.user-nav a{
    color: white;
    margin-left: 20px;
}

.main-content {
    display: flex; 
    flex-direction: column; 
    align-items: center;
}

.main-nav {
    background-color: white;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    padding: 15px 0;
}

.main-nav a {
    text-decoration: none;
    color: #666;
    font-size: 20px;
    transition: color 0.3s;
}

.main-nav a:hover, .main-nav a.active {
    color: #FF6B6B;
}

.commodity-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
  max-width: 1300px;
  margin: 0 auto;
}

.cart-float {
  position: fixed;
  bottom: 60px;
  right: 60px;
  width: 56px;
  height: 56px;
  background-color: #409EFF; /* Element Plus 主色 */
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  box-shadow: 0 0 12px #409EFFAA;
  z-index: 10000;
  color: white;
  user-select: none;
  font-size: 28px;
}

.cart-count {
  position: absolute;
  top: -2px;
  right: -5px;
  background: #f56c6c;
  border-radius: 12px;
  min-width: 20px;
  padding: 0 6px;
  font-size: 12px;
  color: white;
  font-weight: bold;
  line-height: 20px;
  text-align: center;
  pointer-events: none;
}

.footer {
    background-color: #333;
    color: #ccc;
    padding: 20px 0 50px;
    margin-top: 20px;
}

.footer-links {
    background-color: #333;
    padding: 10px 50px;
    display: flex;
    justify-content: space-between;

}

.link-group h4 {
    color: white;
    margin-bottom: 15px;
    font-size: 16px;
}

.link-group a {
    display: block;
    color: #ccc;
    text-decoration: none;
    margin-bottom: 10px;
    font-size: 14px;
    transition: color 0.3s;
}

.link-group a:hover {
    color: white;
}

.copyright {
    text-align: center;
    padding-top: 20px;
    border-top: 1px solid #ffffff;
    font-size: 14px;
    background-color: #333;
}
</style>