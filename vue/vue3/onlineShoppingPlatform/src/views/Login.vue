<template>
  <div class="login-container">
    <el-card class="login-card" shadow="hover">
      <h2>选择登录身份</h2>

      <!-- 普通用户表格 -->
      <el-table :data="simplifiedUsers" style="width: 100%; margin-bottom: 20px;">
        <el-table-column prop="id" label="ID" width="100" />
        <el-table-column prop="name" label="用户名" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="primary" size="small" @click="loginUser(scope.row)">登录</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-divider>或</el-divider>

      <!-- 管理员按钮 -->
      <el-button type="danger" @click="loginAdmin">管理员登录</el-button>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { useRouter } from 'vue-router'
import axios from 'axios'
import { ref, onMounted, computed } from 'vue'

const router = useRouter()

interface User {
  userId: string;
  name: string;
  phone: string;
  email: string;
  address: string;
  place: string;
  vip: string;
  status: string;
  url: string;
  createTime: string;
}

const users = ref<User[]>([]);
const simplifiedUsers = computed(() => 
  users.value.map(user => ({ id: user.userId, name: user.name }))
);

const queryUsers = async () => {
  const { data } = await axios.get('http://localhost:8080/ct/list');
  users.value = data.code === 200 ? data.data : [];
};

// 普通用户登录
function loginUser(user: { id: number; name: string }) {
    localStorage.setItem('role', 'user')
    localStorage.setItem('userId', String(user.id))
    localStorage.setItem('userName', user.name)
    router.push('/') 
}

// 管理员登录
function loginAdmin() {
  localStorage.setItem('role', 'admin')
  router.push('/') 
}

onMounted(() => {
  queryUsers();
})

</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
}

.login-card {
  width: 500px;
}
</style>
