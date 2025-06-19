<template>

  <el-dialog v-model="editDialogVisible" title="编辑物流信息" width="600px">
    <el-form :model="form" label-width="100px">
      <el-form-item label="订单号">
        <el-input v-model="form.orderId" disabled />
      </el-form-item>

      <el-form-item label="物流单号">
        <el-input v-model="form.deliveryId" disabled />
      </el-form-item>

      <el-form-item label="收货人姓名">
        <el-input v-model="form.posterName" />
      </el-form-item>

      <el-form-item label="收货人姓名">
        <el-input v-model="form.receiverName" />
      </el-form-item>

      <el-form-item label="地址">
        <el-input v-model="form.address" />
      </el-form-item>

      <el-form-item label="电话">
        <el-input v-model="form.phone" />
      </el-form-item>

      <el-form-item label="物流公司">
        <el-input v-model="form.company" />
      </el-form-item>

      <el-form-item label="发货时间">
        <el-date-picker
          v-model="form.shipTime"
          type="datetime"
          value-format="YYYY-MM-DD HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="状态">
        <el-select v-model="form.status" placeholder="请选择状态">
          <el-option label="未发货" value="未发货" />
          <el-option label="运输中" value="运输中" />
          <el-option label="已签收" value="已签收" />
        </el-select>
      </el-form-item>

      <el-form-item label="备注">
        <el-input v-model="form.remarks" placeholder="如为空将自动生成" />
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="editDialogVisible = false">取消</el-button>
      <el-button type="primary" @click="submitEditForm">保存修改</el-button>
    </template>
  </el-dialog>


  <el-dialog v-model="dialogVisible" title="新增物流订单" width="600px">
    <el-form :model="form" label-width="100px" :rule="rules">
      <el-form-item label="订单编号">
        <el-select v-model="form.orderId" placeholder="请选择订单编号" @change="handleOrderChange">
          <el-option
            v-for="order in availableOrders"
            :key="order.id"
            :label="`订单 #${order.id}`"
            :value="order.id"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="发货人">
        <el-input v-model="form.posterName" disabled />
      </el-form-item>

      <el-form-item label="收货人">
        <el-input v-model="form.receiverName" disabled />
      </el-form-item>

      <el-form-item label="收货地址">
        <el-input v-model="form.address" disabled />
      </el-form-item>

      <el-form-item label="联系电话">
        <el-input v-model="form.phone" disabled />
      </el-form-item>

      <el-form-item label="快递公司">
        <el-select v-model="form.company" placeholder="请选择快递公司" @change="handleCompanyChange(form.company,form.orderId)">
          <el-option label="顺丰快递" value="顺丰快递" />
          <el-option label="申通快递" value="申通快递" />
          <el-option label="圆通快递" value="圆通快递" />
          <el-option label="邮政EMS" value="邮政EMS" />
          <el-option label="京东物流" value="京东物流" />
        </el-select>
      </el-form-item>
      <el-form-item label="物流单号">
        <el-input v-model="form.deliveryId" disabled />
      </el-form-item>
      <el-form-item label="发货时间">
        <el-date-picker
          v-model="form.shipTime"
          type="datetime"
          value-format="YYYY-MM-DD HH:mm:ss"
          placeholder="选择发货时间"
          style="width: 100%"
          :disabled-date="disabledDate"
        />
      </el-form-item>
      <el-form-item label="物流状态">
        <el-select v-model="form.status" placeholder="请选择物流状态" @change="handleOrderChange(form.orderId)">
          <el-option label="未发货" value="未发货" />
          <el-option label="运输中" value="运输中" />
          <el-option label="已签收" value="已签收" />
          <el-option label="异常" value="异常" />
        </el-select>
      </el-form-item>

      <el-form-item label="备注">
        <el-input
          v-model="form.remarks"
          type="textarea"
          :disabled="loadingRemarks"
          :placeholder="loadingRemarks ? 'AI 正在生成中...' : '请输入备注'"
        >
          <template #suffix v-if="loadingRemarks">
            <el-icon class="is-loading"><Loading /></el-icon>
          </template>
        </el-input>
      </el-form-item>
    </el-form>

    <template #footer>
      <el-button @click="dialogVisible = false">取消</el-button>
      <el-button type="primary" :disabled="!isFormValid" @click="submitForm">提交</el-button>
    </template>
  </el-dialog>

  <el-container class="mainContainer">
    <el-header class="header">
        <el-icon :size="25" style="padding: 0 20px; color:#FF6B6B;margin-left: 20px;">
            <box />  
        </el-icon>
        <div class="logo">物流管理</div>
    </el-header>
    <div style="display: flex; justify-content: flex-end;">
      <el-button type="danger" icon="Plus" @click="openCreateDialog">新增发货</el-button>
    </div>

    <div style="display: flex; gap: 20px;width: 100%;">
      <el-card style="flex: 1; text-align: center;">
        <el-icon style="font-size:28px; color:grey"><Clock /></el-icon>
        <div style="font-weight: bold;font-size: 20px;">128</div>
        <div style="font-size: 15px;opacity: 60%;">未发货订单</div>
      </el-card>
      <el-card style="flex: 1; text-align: center;">
        <el-icon style="font-size:28px; color:blue"><Van /></el-icon>
        <div style="font-weight: bold;font-size: 20px;">342</div>
        <div style="font-size: 15px;opacity: 60%;">运输中订单</div>
      </el-card>
      <el-card style="flex: 1; text-align: center;">
        <el-icon style="font-size:28px; color:green"><Check /></el-icon>
        <div style="font-weight: bold;font-size: 20px;">1,208</div>
        <div style="font-size: 15px;opacity: 60%;">已送达订单</div>
      </el-card>
      <el-card style="flex: 1; text-align: center;">
        <el-icon style="font-size:28px; color:red"><Warning /></el-icon>
        <div style="font-weight: bold;font-size: 20px;">18</div>
        <div style="font-size: 15px;opacity: 60%;">问题订单</div>
      </el-card>
    </div>

    <el-card  style="margin-top: 20px; padding: 20px;">
      <div style="  display: flex;gap: 10px;">
        <el-input v-model="filters.deliveryId" placeholder="输入物流单号" style="width: 280px" />
        <el-input v-model="filters.orderId" placeholder="输入订单号" style="width: 280px" />
        <el-input v-model="filters.posterName" placeholder="输入发货人姓名" style="width: 280px" />
        <el-input v-model="filters.receiverName" placeholder="输入收货人姓名" style="width: 280px" />
        
        <el-select v-model="filters.status" placeholder="物流状态" style="width: 200px">
          <el-option label="全部状态" value="" />
          <el-option label="未发货" value="未发货" />
          <el-option label="已发货" value="已发货" />
          <el-option label="配送中" value="配送中" />
          <el-option label="已送达" value="已送达" />
          <el-option label="已签收" value="已签收" />
        </el-select>
        
        <el-select v-model="filters.company" placeholder="物流公司" style="width: 200px">
          <el-option label="全部公司" value="" />
          <el-option label="顺丰快递" value="sf" />
          <el-option label="申通快递" value="sto" />
          <el-option label="圆通快递" value="yto" />
          <el-option label="邮政EMS" value="ems" />
          <el-option label="京东物流" value="jd" />
        </el-select>
        
        <el-date-picker v-model="filters.date" type="date" placeholder="发货日期" style="width: 200px" />
      </div>

      <div style="float: right;margin-top: 16px;">
        <el-button icon="Refresh" @click="resetFilters">重置条件</el-button>
        <el-button type="danger" icon="Search" @click="search">搜索物流</el-button>
        <el-button 
          type="success" 
          icon="Download" 
          :disabled="selectedDeliveries.length === 0" 
          @click="exportSelectedDeliveries"
          >
          导出数据
        </el-button>
      </div>
    </el-card>

    <el-table :data="tableData" border stripe style="width: 100%" @selection-change="selectedDeliveries = $event">
      <el-table-column type="selection" width="65"/>
      <el-table-column prop="deliveryId" label="物流单号" width="160"/>
      <el-table-column prop="orderId" label="订单号" width="75"/>
      <el-table-column prop="orderDate" label="下单时间" width="240"/>
      <el-table-column prop="posterName" label="发货人" width="80"/>
      <el-table-column prop="receiverName" label="收货人" width="80"/>
      <el-table-column label="收货信息">
        <template #default="scope">
          <div>{{ scope.row.address }}</div>
          <div>{{ scope.row.phone }}</div>
        </template>
      </el-table-column>
      <el-table-column prop="company" label="物流公司" width="100"/>
      <el-table-column prop="shipTime" label="发货时间" />
      <el-table-column prop="status" label="物流状态" width="100">
        <template #default="scope">
          <el-tag :type="statusType(scope.row.status)">{{ statusLabel(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180">
        <template #default="scope">
          <el-button 
            size="small" 
            icon="View" 
            circle 
            type="primary" 
            @click="showDelivery(scope.row)">
          </el-button>
          <el-button
            size="small"
            icon="Edit"
            circle
            type="warning"
            title="更新状态"
            @click="handleUpdateStatus(scope.row)"
          />
          <el-button
            size="small"
            icon="Delete"
            circle
            type="danger"
            title="删除订单"
            @click="handleDeleteOrder(scope.row)"
          />
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      background
      layout="prev, pager, next"
      :total="tableData.length"
      :page-size="10"
    />
  </el-container>

  <el-dialog v-model="detailVisible">
    <DeliveryCard
      v-model="detailVisible"
      :delivery="currentDelivery"
    />
  </el-dialog>
  
</template>

<script lang="ts" setup>
import { onMounted, ref, watch, computed, toRaw } from 'vue';
import { Clock, Van, Check, Warning } from '@element-plus/icons-vue';
import axios from 'axios';
import PinyinMatch from 'pinyin-match'
import { ElMessage, ElMessageBox } from 'element-plus';
import OpenAI from "openai";
import DeliveryCard from '@/components/DeliveryCard.vue'

const openai = new OpenAI({
  baseURL: 'https://api.deepseek.com/v1',
  apiKey: 'sk-3d86b47e0af64f34aae2017c3e0be844', 
  dangerouslyAllowBrowser: true 
});

async function simplifyProductDescription(productName: string, productDescription: string, deliveryStatus: string) {
  try {
    const response = await openai.chat.completions.create({
      model: "deepseek-chat",
      messages: [
        {
          role: "system",
          content: "你是一个订单助手，请将商品名称和描述结合物流状态，总结成一句15字以内的简短中文描述，只保留关键内容。"
        },
        {
          role: "user",
          content: `商品名称: ${productName}\n商品描述: ${productDescription}\n物流状态: ${deliveryStatus}`
        }
      ],
      max_tokens: 30,
      temperature: 0.3
    });

    return response.choices[0].message.content;
  } catch (error) {
    console.error("API调用失败:", error);
    return "描述生成失败，请重试";
  }
}

interface Delivery {
    id: number
    deliveryId: string 
    orderId: number
    orderDate: string
    posterName: string
    receiverName: string
    address: string
    phone: string
    company: string
    shipTime: string
    status: string
    statusUpdateTime: string
    remarks: string
    createdAt: string
    updatedAt: string
}

interface Order {
  id: number
  name: string
  create_time: string
  description: string
}

interface OrderUser {
  posterName: string
  address: string
  phone: string
}

const rules = {
  orderId: [{ required: true, message: '请选择订单编号', trigger: 'change' }],
  company: [{ required: true, message: '请选择快递公司', trigger: 'change' }],
  shipTime: [{ required: true, message: '请选择发货时间', trigger: 'change' }],
  status: [{ required: true, message: '请选择物流状态', trigger: 'change' }]
};

const isFormValid = computed(() => {
  return (
    form.value.orderId &&
    form.value.posterName &&
    form.value.receiverName &&
    form.value.address &&
    form.value.phone &&
    form.value.company &&
    form.value.deliveryId &&
    form.value.shipTime &&
    form.value.status &&
    (!loadingRemarks.value)
  );
});

const allData = ref<Delivery[]>([]);
const detailVisible = ref(false)
const currentDelivery = ref<Delivery | null>(null)
const tableData = ref<Delivery[]>([]);
const selectedDeliveries = ref<Delivery[]>([])

const filters = ref({
  deliveryId: '',
  orderId: '',
  posterName: '',
  receiverName: '',
  status: '',
  company: '',
  date: null as Date | null
});

const dialogVisible = ref(false)
const form = ref<Delivery>({
  id: 0,
  deliveryId: '',
  orderId: 1,
  orderDate: '',
  receiverName: '',
  address: '',
  phone: '',
  company: '',
  shipTime: '',
  status: '',
  statusUpdateTime: '',
  remarks: '',
  createdAt: '',
  updatedAt: '',
  posterName:''
});

const orderList = ref<Order[]>([]) 
const user = ref<OrderUser>({
  posterName: '',
  address: '',
  phone: ''
})

const availableOrders = computed(() => {
  const usedOrderIds = new Set(allData.value.map(d => d.orderId));
  return orderList.value.filter(order => !usedOrderIds.has(order.id));
});


const openCreateDialog = () => {
  dialogVisible.value = true
  form.value = {
    id: 0,
    deliveryId: '',
    orderId: 1,
    orderDate: '',
    posterName:'',
    receiverName: '',
    address: '',
    phone: '',
    company: '',
    shipTime: '',
    statusUpdateTime: '',
    status: '未发货',
    remarks: '',
    createdAt: getNowDateTime(),
    updatedAt: getNowDateTime(),
  }
}

const showDelivery = (row: Delivery) => {
  currentDelivery.value = row
  detailVisible.value = true
}

const editDialogVisible = ref(false)

const handleEdit = (row: Delivery) => {
  form.value = { ...row }
  form.value.statusUpdateTime = getNowDateTime();
  editDialogVisible.value = true
}

const disabledDate = (time: Date) => {
  const now = new Date();
  now.setHours(0, 0, 0, 0);
  return time.getTime() < now.getTime();
};

const exportSelectedDeliveries = () => {
  if (selectedDeliveries.value.length === 0) {
    ElMessage.warning('请先选择需要导出的物流订单')
    return
  }

  const content = selectedDeliveries.value.map(delivery => {
    return `物流单号: ${delivery.deliveryId}
            订单号: ${delivery.orderId}
            发货人: ${delivery.posterName}
            收货人: ${delivery.receiverName}
            手机号: ${delivery.phone}
            地址: ${delivery.address}
            物流状态: ${delivery.status}
            状态更新时间: ${delivery.statusUpdateTime}
            备注: ${delivery.remarks}
            -------------------------`
    }).join('\n')

  const blob = new Blob([content], { type: 'text/plain;charset=utf-8' })
  const url = URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.download = '选中物流订单导出.txt'
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  URL.revokeObjectURL(url)
}

const loadingRemarks = ref(false)

const handleOrderChange = async (orderId: number) => {
  try {
    // 1. 获取用户相关信息
    const res = await axios.get(`http://localhost:8080/ou`, {
      params: { OrderId: orderId }
    });

    const userData = res.data.data;
    user.value = {
      posterName: userData.name,
      address: userData.address,
      phone: userData.phone
    };
    form.value.posterName = userData.name;
    form.value.address = userData.address;
    form.value.phone = userData.phone;

    // 2. 查找订单
    const order = orderList.value.find(o => o.id === orderId);
    if (order) {
      form.value.orderId = orderId;
      form.value.orderDate = order.create_time;
      form.value.receiverName = order.name;

      // 3. 清空备注 & 启动loading动画
      form.value.remarks = '';
      loadingRemarks.value = true;

      try {
        const summary = await simplifyProductDescription(
          order.name,
          order.description,
          form.value.status
        );
        form.value.remarks = summary+"";
      } catch (err) {
        ElMessage.error('生成备注失败，请稍后重试');
        form.value.remarks = '自动备注失败，请手动填写';
      } finally {
        loadingRemarks.value = false;
      }
    } else {
      ElMessage.warning('找不到对应订单');
      form.value.orderDate = '';
    }

    // 4. 更新时间
    form.value.statusUpdateTime = getNowDateTime();

  } catch (e) {
    ElMessage.error('获取用户信息失败');
    loadingRemarks.value = false;
  }
};



const handleCompanyChange = (company: string, orderId: number) => {
  const prefix = getCompanyCode(company).toUpperCase(); // 比如 SF
  const date = new Date();
  const yyyy = date.getFullYear();
  const MM = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  const datePart = `${yyyy}${MM}${dd}`;
  const orderIdStr = String(orderId).padStart(4, '0'); // 补足4位
  form.value.deliveryId = `${prefix}${datePart}${orderIdStr}`;
};

const handleUpdateStatus = (row:Delivery) => {
  editDialogVisible.value = true
  handleEdit(row)
};

const handleDeleteOrder = async (row:Delivery) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除订单「${row.id}」吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    );
    await axios.delete('http://localhost:8080/dr', {
      params: { id: row.id }
    })  
    ElMessage.success('删除成功');
    queryList();
  } catch (err) {
    if (err !== 'cancel') {
      ElMessage.error('删除失败');
    }
  }
};

const getNowDateTime = () => {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const hour = String(now.getHours()).padStart(2, '0');
  const minute = String(now.getMinutes()).padStart(2, '0');
  const second = String(now.getSeconds()).padStart(2, '0');
  return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
}

const submitEditForm = async () => {
  try {
    await axios.put('http://localhost:8080/dr', toRaw(form.value))
    ElMessage.success('更新物流订单成功')
    editDialogVisible.value = false
    resetFilters()
    queryList()
  } catch (e) {
    ElMessage.error('提交失败，请稍后再试')
  }
}

const submitForm = async () => {
  try {
    await axios.post('http://localhost:8080/dr', toRaw(form.value))
    ElMessage.success('新增物流订单成功')
    resetFilters()
    queryList()
    dialogVisible.value = false
  } catch (e) {
    console.log(form.value)
    ElMessage.error('提交失败，请稍后再试')
  }
}

watch(filters, () => {
  tableData.value = allData.value.filter(item => {
    return (
      (!filters.value.deliveryId || item.deliveryId.includes(filters.value.deliveryId) ||  item.deliveryId.toLowerCase().includes(filters.value.deliveryId)) &&
      (!filters.value.orderId || item.orderId.toString().includes(filters.value.orderId)) &&
      (!filters.value.posterName || item.posterName.includes(filters.value.posterName) || item.posterName.toLowerCase().includes(filters.value.posterName) || PinyinMatch.match(item.posterName, filters.value.posterName)) &&
      (!filters.value.receiverName || item.receiverName.includes(filters.value.receiverName) || item.receiverName.toLowerCase().includes(filters.value.receiverName) || PinyinMatch.match(item.receiverName, filters.value.receiverName)) &&
      (!filters.value.status || item.status === filters.value.status) &&
      (!filters.value.company || getCompanyCode(item.company) === filters.value.company) &&
      (!filters.value.date || item.shipTime.startsWith(formatDate(filters.value.date)))
    );
  });
}, { deep: true });

const statusLabel = (status: string) => {
  switch (status) {
    case '未发货': return '未发货';
    case '已发货': return '已发货';
    case '配送中': return '配送中';
    case '已送达': return '已送达';
    case '已签收': return '已签收';
    default: return '问题件';
  }
};

const statusType = (status: string) => {
  switch (status) {
    case '未发货': return 'danger';
    case '已发货': return 'warning';
    case '配送中': return 'primary';
    case '已送达': return 'success';
    case '已签收': return 'info';
    default: return 'danger';
  }
};

const queryList = async () => {
  const response = await axios.get('http://localhost:8080/dr/list');
  allData.value = response.data.data;
  tableData.value = [...allData.value];
}

const queryOrderList = async () => {
  const response = await axios.get('http://localhost:8080/od/list');
  orderList.value = response.data.data.map((item: any) => ({
    id: item.id,
    name: item.name,
    create_time: item.createTime.substring(0, 10),
    description: item.description
  }))
}

const search = () => {
  if (isAllDeliveryFiltersEmpty()) {
    ElMessage.info('请输入搜索条件')
    return
  }

  if (tableData.value.length === 0) {
    ElMessage.warning('未搜索到对应物流订单')
  } else {
    ElMessage.success('搜索成功')
  }
};

const isAllDeliveryFiltersEmpty = () => {
  return !filters.value.deliveryId && !filters.value.orderId && !filters.value.receiverName && !filters.value.status
}

const resetFilters = () => {
  filters.value = {
    deliveryId: '',
    orderId: '',
    posterName: '',
    receiverName: '',
    status: '',
    company: '',
    date: null
  };
  tableData.value = [...allData.value];
};

const getCompanyCode = (name: string) => {
  if (name.includes('顺丰')) return 'sf';
  if (name.includes('申通')) return 'sto';
  if (name.includes('圆通')) return 'yto';
  if (name.includes('EMS')) return 'ems';
  if (name.includes('京东')) return 'jd';
  return '';
};

const formatDate = (date: Date | null): string => {
  if (!date) return '';
  const yyyy = date.getFullYear();
  const mm = String(date.getMonth() + 1).padStart(2, '0');
  const dd = String(date.getDate()).padStart(2, '0');
  return `${yyyy}-${mm}-${dd}`;
};

onMounted(() => {
  queryList();
  queryOrderList();
})

</script>


<style scoped>
.mainContainer{
  flex-direction: column;
  gap: 25px;
  display: flex;
}

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
</style>
