import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

import MainPage from '../views/MainPage.vue';
import CommodityStorage from '../views/CommodityStorage.vue';
import PageRouter from '../views/PageRouter.vue';
import CustomerInfo from '../views/CustomerInfo.vue';
import CustomerCart from '../views/CustomerCart.vue';
import Customers from '../views/Customers.vue';
import Orders from '../views/Orders.vue';
import AdminMain from '../views/AdminMain.vue';
import Login from '../views/Login.vue';
import DeliveryManagement from '../views/DeliveryManagement.vue';

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    component: Login
  },
  {
    path: '/',
    redirect: () => {
      // 你可以替换这里为身份判断逻辑
      const isAdmin = localStorage.getItem('role') === 'admin'
      return isAdmin ? '/admin/Main' : '/Main'
    }
  },
  {
    path: '/admin',
    component: PageRouter,  // 管理员看到的界面结构
    children: [
      {
        path: 'Main',
        component: AdminMain
      },
      {
        path: 'Storage',
        component: CommodityStorage
      },
      {
        path: 'Customers',
        component: Customers
      },
      {
        path: 'Orders',
        component: Orders
      },
      {
        path: 'Delivery',
        component: DeliveryManagement
      }
    ]
  },
  {
    path: '/Main',
    name: 'Main',
    component: MainPage
  },
  {
    path: '/Customer',
    name: 'Customer',
    component: CustomerInfo
  },
  { 
    path: '/Cart',
    name: 'Cart',
    component: CustomerCart
  }
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL), 
  routes
})

router.beforeEach((to, from, next) => {
  const role = localStorage.getItem('role')
  if (!role && to.path !== '/login') {
    next('/login') // 未登录强制跳转登录页
  } else {
    next()
  }
})

router.push('/')

export default router