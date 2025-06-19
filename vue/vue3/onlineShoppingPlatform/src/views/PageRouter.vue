<template>
    <el-header class="mainHeader">
        <div class="logo">简洁电商</div>
        <div style="width: 500px;">

        </div>
        <div class="user-nav">
            <el-link :icon="User" href="/Customer" underline="hover" type="info" style="font-size: 15px;">管理员</el-link>
            <el-link :icon="Back" href="/Login" underline="hover" type="info" style="font-size: 15px;">退出登录</el-link>
        </div>
    </el-header>
    <el-menu default-active="$route.path" class="main-nav" mode="horizontal" :ellipsis="true" router >
        <el-menu-item index="/admin/Main" style="margin-left: 120px;">首页</el-menu-item>
        <el-menu-item index="/admin/Storage">商品管理</el-menu-item>
        <el-menu-item index="/admin/Customers">用户管理</el-menu-item>
        <el-menu-item index="/admin/Orders">订单管理</el-menu-item>
        <el-menu-item index="/admin/Delivery">物流管理</el-menu-item>
    </el-menu>
    <el-main>
      <router-view />
    </el-main>
</template>

<script setup lang="ts">
import { Back, User } from '@element-plus/icons-vue';
</script>

<style scoped>
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
}

.search-box input {
    flex: 1;
    padding: 6px 6px;
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

.main-nav {
    background-color: white;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    padding: 15px 0;
}

.main-nav a {
    text-decoration: none;
    color: #666;
    font-size: 16px;
    transition: color 0.3s;
}

.main-nav a:hover, .main-nav a.active {
    color: #FF6B6B;
}
</style>