<template>
    <el-header class="header">
        <el-icon :size="25" style="padding: 0 20px; color:#FF6B6B;margin-left: 20px;">
            <box />  
        </el-icon>
        <div class="logo">商品管理</div>
    </el-header>

    <el-dialog
      v-model="dialogVisible"
      width="500"
      :before-close="handleClose"
    >
      <div style="font-size: 25px; font-weight: bold; margin-bottom: 5px;">添加一个商品</div>

      <el-form 
        :model="form" 
        :rules="rules"
        ref="productForm"
        label-width="auto" 
        label-position="left"
        style="max-width: 600px; margin-top: 10px;"
      >
        <el-form-item label="名字" prop="name">
          <el-input v-model="form.name" placeholder="输入商品名"/>
        </el-form-item>

        <el-form-item label="品牌名" prop="brandName">
          <el-input v-model="form.brandName" placeholder="输入品牌名"/>
        </el-form-item>

        <el-form-item label="公司名" prop="companyName">
          <el-input v-model="form.companyName" placeholder="输入公司名"/>
        </el-form-item>

        <el-form-item label="简介">
          <el-input v-model="form.description" placeholder="输入商品价格"/>
        </el-form-item>

        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="上架">上架</el-radio>
            <el-radio label="下架">下架</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="数量" prop="account">
          <el-input v-model.number="form.account" type="number"/>
        </el-form-item>

        <el-form-item label="价格" prop="price">
          <el-input 
            v-model.number="form.price" 
            type="number"
          />
        </el-form-item>
      </el-form>

      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleAdd">确认</el-button>
        </div>
      </template>
    </el-dialog>


    <el-dialog
      v-model="dialogEditVisible"
      width="500"
      :before-close="handleClose"
    >
      <div style="font-size: 25px; font-weight: bold; margin-bottom: 10px;">编辑商品</div>
      <el-form 
      :model="form" 
      label-width="auto" 
      label-position="left"
      :rules="rules"
      style="max-width: 600px;
            margin-top: 10px;">
        <el-form-item label="名字">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="品牌名">
          <el-input v-model="form.brandName" />
        </el-form-item>
        <el-form-item label="公司名">
          <el-input v-model="form.companyName" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.description" />
        </el-form-item>
        <el-form-item>
          <el-radio-group v-model="form.status">
            <el-radio label="上架">上架</el-radio>
            <el-radio label="下架">下架</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="数量">
          <el-input v-model="form.account" />
        </el-form-item>
        <el-form-item 
          label="价格"
          prop="price" 
        >
          <el-input 
            v-model.number="form.price" 
            type="number"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogEditVisible = false">取消</el-button>
          <el-button type="primary" @click="handleEdited">
            确认
          </el-button>
        </div>
      </template>
    </el-dialog>
    
    <el-main>
        <div style="float: right; margin-top: 20px;">
          <el-button type="danger" @click="dialogVisible = true">添加商品</el-button>
        </div>
        <el-table :data="pagedData" class="mainTable" border >
            <el-table-column fixed="left" prop="id" label="编号" width="80"></el-table-column>
            <el-table-column prop="name" label="名字" width="200"></el-table-column>    
            <el-table-column prop="brandName" label="品牌" width="120"></el-table-column>
            <el-table-column prop="companyName" label="公司" width="180"></el-table-column>
            <el-table-column prop="description" label="简介" width="400"></el-table-column>
            <el-table-column prop="status" label="状态" width="80"></el-table-column>
            <el-table-column prop="account" label="总量" width="80"></el-table-column>
            <el-table-column prop="price" label="价格" width="70"></el-table-column>
            <el-table-column fixed="right" label="操作" width="200">
                <template #header>
                    <el-input v-model="search" size="small" placeholder="搜索商品" />
                </template>
                <template #default="scope">
                    <el-button size="small" @click="handleEdit(scope.row)">
                    编辑
                    </el-button>
                    <el-button
                    size="small"
                    type="danger"
                    @click="handleDelete(scope.row)"
                    :loading="deleteLoading[scope.row.id]"
                    >
                    删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination-block">
            <el-pagination     
              background
              layout="prev, pager, next"
              :total="filterTableData.length"
              v-model:page-size="pageSize"
              v-model:current-page="currentPage"
              @current-change="handlePageChange"
            />
        </div>
    </el-main>
</template>

<script lang="ts" setup>
import { computed, ref, onMounted, reactive } from 'vue'
import { Box } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessageBox, ElMessage } from 'element-plus'

const search = ref('')
const dialogVisible = ref(false)
const dialogEditVisible = ref(false)
const pageSize = ref(10)
const currentPage = ref(1) 
const rules = reactive({
  name: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  brandName: [{ required: true, message: '请输入品牌名称', trigger: 'blur' }],
  companyName: [{ required: true, message: '请输入品牌公司', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }],
  account: [
    { required: true, message: '请输入数量', trigger: 'blur' },
    { type: 'number', message: '数量必须为数字', trigger: 'blur' }
  ],
  price: [
    { required: true, message: '请输入价格', trigger: 'blur' },
    { type: 'number', message: '价格必须为数字', trigger: 'blur' }
  ]
})
const productForm = ref(null);

interface Commodity {
  id: number;
  name: string;
  brandName: string;
  companyName: string;
  description: string;
  status: string;
  account: number;
  price: number;
}

const tableData = ref<Commodity[]>([]) 

const form = reactive({
  id: 0,
  name: '',
  brandName: '',
  companyName: '',
  description: '',
  status: '',
  account: 0,
  price: 0
})

const resetForm = () => {
  Object.assign(form, {
    name: '',
    brandName: '',
    companyName: '',
    description: '',
    status: '上架',
    account: 0,
    price: 0
  })
}

const filterTableData = computed(() =>
  tableData.value.filter(
    (data) =>
      !search.value ||
      data.name.toLowerCase().includes(search.value.toLowerCase())
  )
)

const pagedData = computed(() => {
  const start = (currentPage.value - 1) * pageSize.value
  return filterTableData.value.slice(start, start + pageSize.value)
})

const queryAdd = async (commodity: Commodity) => {
  return axios.post('http://localhost:8080/cmd', commodity)
}

// 异步请求数据并更新 tableData
const queryList = async () => {
  try {
    const result = await axios.get('http://localhost:8080/cmd/list')  
    tableData.value = result.data.data 
  } catch (error) {
    console.error('请求失败:', error)
  }
}

const queryDelete = async (id: number) => {
  return axios.delete(`http://localhost:8080/cmd/${id}`)
}

const queryUpdate = async (commodity: Commodity) => {
  return axios.put(`http://localhost:8080/cmd`, commodity)
}

const handleEdit = (row: Commodity) => {
  Object.assign(form, row);
  dialogEditVisible.value = true
}

const handleEdited = () => {
  if (!form.name || !form.price) {
    ElMessage.error('名称和价格不能为空')
    return
  }

  const commodity: Commodity = {
    ...form,
    account: Number(form.account) || 0, 
    price: Number(form.price) || 0
  }

  queryUpdate(commodity)
    .then(() => {
      ElMessage.success('修改成功')
      dialogEditVisible.value = false
      resetForm()
      queryList()
    })
    .catch(error => {
      ElMessage.error(`修改失败: ${error.message}`)
    })
}

const deleteLoading = ref<Record<number, boolean>>({})

const handleDelete = (row: Commodity) => {
  ElMessageBox.confirm('确定删除该商品吗？', '警告', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    deleteLoading.value[row.id] = true
    
    try {
      queryDelete(row.id)
      tableData.value = tableData.value.filter(item => item.id !== row.id)
      ElMessage.success('删除成功')
      
      // 如果当前页无数据且不是第一页，返回上一页
      if (pagedData.value.length === 0 && currentPage.value > 1) {
        currentPage.value--
      }
    } finally {
      deleteLoading.value[row.id] = false
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

const handleAdd = () => {

  if (!form.name || !form.price) {
    ElMessage.error('名称和价格不能为空')
    return
  }

  const commodity: Commodity = {
    ...form,
    account: Number(form.account) || 0, 
    price: Number(form.price) || 0
  }

  queryAdd(commodity)
    .then(() => {
      ElMessage.success('添加成功')
      dialogVisible.value = false
      resetForm() 
      queryList()
    })
    .catch(error => {
      ElMessage.error(`添加失败: ${error.message}`)
    })
}

const handlePageChange = (page: number) => {
  currentPage.value = page
}

const handleClose = (done: () => void) => {
    ElMessageBox.confirm('确定关闭此窗口?', '提示', {
        confirmButtonText: '确定关闭',
        cancelButtonText: '取消关闭',
        type: 'warning',
    })
    .then(() => {
      done()
    })
    .catch(() => {

    })
}

onMounted(() => {
  queryList()
})

</script>

<style scoped>
.header{
    height: 60px;
    display: flex;
    align-items: center;
    z-index: 999;
}

.header .logo {
    color: #FF6B6B;
    font-size: 32px;
    font-weight: bold;
}

.mainTable{
    padding: 50px;
    margin: 20px 0;
}

.mainTable :deep(.el-table__header .el-table__cell),
.mainTable :deep(.el-table__body .el-table__cell) {
  padding: 17px 0;
}

.pagination-block{
    display: flex;
    justify-content: end;
    margin-top: 20px;
}
</style>