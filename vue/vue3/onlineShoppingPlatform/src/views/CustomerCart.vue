<template>
    <el-dialog
        title="确认支付"
        v-model="payDialogVisible"
        width="400px"
        @close="onPayDialogClose"
        >
        <p>请确认是否支付订单？</p>

        <span slot="footer" class="dialog-footer">
            <el-button @click="onPayCancel">返回修改订单</el-button>
            <el-button type="primary" @click="confirmPayment">确认支付</el-button>
        </span>
    </el-dialog>

    <el-dialog v-model="dialogVisible" title="填写订单信息" width="30%" center :before-close="handleClose">
        <el-form :model="form" label-width="120px" :rule="rules" ref="formRef">
            <el-form-item label="收货人姓名">
                <el-input v-model="form.name" />
            </el-form-item>
            <el-form-item label="收货人电话">
                <el-input v-model="form.phone" />
            </el-form-item>
            <el-form-item label="收货地址">
                <el-input v-model="form.address" />
            </el-form-item>
            <el-form-item label="支付方式">
                <el-radio-group v-model="form.payment">
                    <el-radio label="支付宝" />
                    <el-radio label="微信" />
                    <el-radio label="银行卡" />
                </el-radio-group>
            </el-form-item>
        </el-form>
        <template #footer>
            <span class="dialog-footer">
                <el-button @click="dialogVisible = false; currentStep = 0">取消</el-button>
                <el-button
                    type="primary"
                    :disabled="!isFormValid"
                    @click="submitOrder"
                >确认</el-button>
            </span>
        </template>
    </el-dialog>
    <el-container class="mainPage" style="height: 100vh;display: flex;">
        <el-header class="mainHeader">
            <div class="logo">简洁电商</div>
            <div class="search-box">
            </div>
            <div class="user-nav">
                <el-link :icon="User" href="/Customer" underline="always" type="info" style="font-size: 15px;">{{username}}</el-link>
                <el-link :icon="ShoppingCart" style="font-size: 15px;">购物车</el-link>
                <el-link :icon="SwitchButton" style="font-size: 15px; color: #FFFFFF;" @click="logout">退出</el-link>
                <el-link :icon="HomeFilled" href="/Main" :underline="false" style="color: #FFFFFF; font-size: 16px;"
            >
            返回首页
            </el-link>
            </div>
        </el-header>
        <el-header class="header">
            <div class="header-left">
                <el-icon><ShoppingCart /></el-icon>
                <span>我的购物车</span>
            </div>
            <el-steps
                class="header-steps"
                :space="100"
                :active="currentStep"   
                finish-status="success"
                align-center
                simple
            >
                <el-step title="我的购物车"/>
                <el-step title="填写订单" />
                <el-step title="完成支付" />
            </el-steps>
        </el-header>
        <div style="background-color: #FF8E8E;height: 2px;"></div>
        <el-table 
            :data="pagedTableData" 
            class="table" 
            align a
            border 
            stripe 
            size="large"
            show-summary
            :summary-method="getSummaries"
            @selection-change="handleSelectionChange"
        >
            <el-table-column type="selection" :selectable="isRowSelectable" :row-class-name="getRowClass" width="65" />
            <el-table-column prop="name" label="商品名称" width="302" />
            <el-table-column prop="description" label="商品描述" width="322" />
            <el-table-column prop="price" label="单价(元)" width="120" >
            <template #default="{ row }">
                {{ row.price }}
            </template>
            </el-table-column>
            <el-table-column prop="num" label="数量" width="180">
            <template #default="{ row }">
                <el-input-number 
                v-model="row.num" 
                :min="1" 
                size="small"
                align
                @change="updateTotal(row)"
                />
            </template>
            </el-table-column>
            <el-table-column prop="total" label="总价(元)" width="150">
                <template #default="{ row }">
                    {{ row.total.toFixed(2) }}
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="{ row }">
                    <el-button type="danger" size="small" @click="deleteRow(row.id)">删除</el-button>
                </template>
            </el-table-column>
         </el-table>

         <el-pagination
          style="margin: 20px auto; width: fit-content;"
          background
          layout="prev, pager, next, jumper"
          :page-size="pageSize"
          :current-page="currentPage"
          :total="total"
          @current-change="handlePageChange"
        />
    </el-container>
</template>

<script lang="ts" setup>
import { h ,onMounted, ref, computed } from 'vue'
import { ElButton, ElMessageBox, ElMessage } from 'element-plus'
import type { VNode } from 'vue'
import type { TableColumnCtx, FormInstance } from 'element-plus'
import { ShoppingCart, User, Star, SwitchButton, HomeFilled} from '@element-plus/icons-vue'
import { useRoute, useRouter } from 'vue-router'
import axios from 'axios'
import OpenAI from 'openai'

const openai = new OpenAI({
  baseURL: 'https://api.deepseek.com/v1',
  apiKey: 'sk-3d86b47e0af64f34aae2017c3e0be844', 
  dangerouslyAllowBrowser: true 
});

async function simplifyProductDescription(productName:string, productDescription:string) {
  try {
    const response = await openai.chat.completions.create({
      model: "deepseek-chat",
      messages: [
        {
          role: "system",
          content: "你是一个订单助手，请将商品名称和描述总结成15字以内的简短描述，只保留关键信息。"
        },
        {
          role: "user",
          content: `商品名称: ${productName}\n商品描述: ${productDescription}`
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

const username = localStorage.getItem('userName') as string
const dialogVisible = ref(false)
const formRef = ref<FormInstance>()
const route = useRoute()
const router = useRouter()
const currentStep = ref(0) 
const id = route.query.id;
const form = ref(
    {
        name: '',
        address: '',
        phone: '',
        payment: '',
    }
)
const currentPage = ref(1)
const pageSize = 5 // 每页显示的条数
const total = ref(0)

interface Cart {
  id: number;               // 来自 CartItem
  commodity: number;        // 来自 CartItem
  name: string;
  description: string;
  price: number;            // 原始价格（数字类型）
  accounts: number;         // 商品数量（来自 CartItem）
  
  priceStr?: string;        // price.toFixed(2) 格式的字符串
  num?: number;             // 映射 accounts，为了兼容现有表格逻辑
  total?: number;           // price * accounts 计算得来
  status?: string;          // 是否可以购买，来自 Commodity
}

const selectedRows = ref<Cart[]>([]);

const payDialogVisible = ref(false);   
const createdOrderIds = ref<number[]>([]); 

const tableData = ref<Cart[]>([]);

const pagedTableData = computed(() => {
  const start = (currentPage.value - 1) * pageSize
  const end = start + pageSize
  return tableData.value.slice(start, end)
})

const handlePageChange = (page: number) => {
  currentPage.value = page
}

const queryList = async () => {
  try {
    const params = { UserId: id };
    const result = await axios.get('http://localhost:8080/uc', { params });

    const rawData: Cart[] = result.data.data;

    // 并行请求商品详情，并组合最终数据
    const cartWithStatus = await Promise.all(
      rawData.map(async (item) => {
        try {
          const res = await axios.get(`http://localhost:8080/cmd/${item.commodity}`);
          const commodity = res.data.data;

          const isAvailable = commodity.account > 0 && commodity.status === '上架';

          return {
            ...item,
            priceStr: item.price.toFixed(2),
            num: item.accounts,
            total: parseFloat((item.price * item.accounts).toFixed(2)),
            status: isAvailable ? '可购买' : '不可购买',
          };
        } catch (e) {
          console.error(`商品 ${item.commodity} 获取失败`, e);
          return {
            ...item,
            priceStr: item.price.toFixed(2),
            num: item.accounts,
            total: parseFloat((item.price * item.accounts).toFixed(2)),
            status: '不可购买',
          };
        }
      })
    );
    total.value = cartWithStatus.length

    tableData.value = cartWithStatus;
    resetForm();
  } catch (error) {
    console.error('请求失败:', error);
  }
};

const updateTotal = (row: Cart) => {
    row.total = Number(row.price) * (row.num ?? 0);
}

const logout = () => {
  localStorage.clear()
  router.push('/login')
}

const deleteRow = (id: number) => {
    ElMessageBox.confirm('确定删除该商品吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning',
    })
    .then(async () => {
      try {
        await axios.delete(`http://localhost:8080/cr/ ${id}`);
        ElMessage({
          type: 'success',
          message: '删除成功!',
        });
        queryList();
      } catch (error) {
        console.error('删除失败:', error);
        ElMessage({
          type: 'error',
          message: '删除失败，请稍后重试。',
        });
      }
    })
    .catch(() => {
      ElMessage({
        type: 'info',
        message: '已取消删除',
      });
    });
}

interface SummaryMethodProps<T = Cart> {
  columns: TableColumnCtx<T>[]
  data: T[]
}

const handleSelectionChange = (val: Cart[]) => {
  selectedRows.value = val;
};

const getRowClass = (row: { row: Cart }) => {
  return row.row.status === '不可购买' ? 'row-disabled' : ''
}

const isRowSelectable = (row: Cart) => {  
  return row.status === '可购买'
}

const getSummaries = (param: SummaryMethodProps) => {
  const { columns } = param

  const sums: (string | VNode)[] = columns.map((_, index) => {
    if (index === 0) {
      return h('div', { style: { fontWeight: 'bold' } }, '总计')
    }

    if (index === columns.length - 2) {
      const total = selectedRows.value.reduce((sum, item) => {
        if (item.status !== '可购买') return sum
        return sum + (item.price * item.accounts || 0)
      }, 0)
      return h('div', { style: { fontWeight: 'bold' } }, total.toFixed(2))
    }

    if (index === columns.length - 1) {
      return h(
        ElButton,
        {
          type: 'primary',
          size: 'small',
          disabled: selectedRows.value.length === 0,
          onClick: () => payNow()
        },
        () => '付款'
      )
    }

    return ''
  })

  return sums
}


const resetForm = () => {
  form.value = {
    name: '',
    address: '',
    phone: '',
    payment: '',
  };
};


const rules = {
  name: [{ required: true, message: '请输入收货人姓名', trigger: 'blur' }],
  phone: [{ required: true, message: '请输入收货人电话', trigger: 'blur' }],
  address: [{ required: true, message: '请输入收货地址', trigger: 'blur' }],
  payment: [{ required: true, message: '请选择支付方式', trigger: 'change' }],
}

const isFormValid = computed(() => {
  return (
    form.value.name.trim() !== '' &&
    form.value.phone.trim() !== '' &&
    form.value.address.trim() !== '' &&
    form.value.payment.trim() !== ''
  )
})

const submitOrder = async () => {
  formRef.value?.validate(async (valid) => {
    if (!valid) return;

    try {
      createdOrderIds.value = []; 

      for (const item of selectedRows.value) {
        const description = await simplifyProductDescription(item.name, item.description);

        const orderPayload = {
            name: form.value.name,
            commodityId: item.commodity,
            description,
            accounts: item.accounts,
            status: '未付款',
            totalPrice: item.price * item.accounts,
            deliveryStatus: '未发货',
            createTime: new Date().toISOString().slice(0, 19).replace('T', ' '),
        };
        console.log(orderPayload);

        const resOrder = await axios.post('http://localhost:8080/od',orderPayload);

        if (resOrder.data.code === 200) {
          const orderId = resOrder.data.data;

          createdOrderIds.value.push(orderId);

          const resRelation = await axios.post('http://localhost:8080/uo/add', {
            UserId: id,
            OrderId: orderId,
          });

          if (resRelation.data.code !== 200) {
            console.warn(`订单关联失败，orderId=${orderId}`);
          }
        } else {
          throw new Error('订单插入失败');
        }
      }

      ElMessage.success('订单提交成功，等待付款');
      currentStep.value = 2;
      dialogVisible.value = false;
      payDialogVisible.value = true;
    } catch (err) {
      console.error('订单异常:', err);
      ElMessage.error('订单提交出错');
    }
  });
};

const payNow = () => {
  if (selectedRows.value.length === 0) {
    ElMessage.warning('请先选择商品再付款！')
    return
  }

  currentStep.value = 1
  dialogVisible.value = true
}

const onPayCancel = () => {
  payDialogVisible.value = false;
  dialogVisible.value = true;
};

const onPayDialogClose = () => {
  createdOrderIds.value = [];
};

const confirmPayment = async () => {
  try {
    if (createdOrderIds.value.length === 0) {
      ElMessage.error('没有可支付的订单！');
      return;
    }

    // 模拟确认付款（这里只处理“付款成功”情况）
    const res = await axios.post('http://localhost:8080/od/pay', {
      orderIds: createdOrderIds.value,
      status: '已付款'
    });

    if (res.data.code === 200) {
      ElMessage.success('支付成功！');
    } else {
      ElMessage.error('支付失败，请稍后再试');
    }

    const cartIds = selectedRows.value.map(item => item.id);
    if (cartIds.length > 0) {
        await axios.delete(`http://localhost:8080/cr?ids=${cartIds.join(',')}`);
    }

    payDialogVisible.value = false;
    selectedRows.value = [];
    currentStep.value = 0;
    
    await queryList();
  } catch (err) {
    console.error('支付异常:', err);
    ElMessage.error('支付出错');

    const cartIds = selectedRows.value.map(item => item.id);
    if (cartIds.length > 0) {
      await axios.delete(`http://localhost:8080/cr?ids=${cartIds.join(',')}`);
    }

    payDialogVisible.value = false;
    selectedRows.value = [];
    currentStep.value = 0;
    await queryList();
  }
};


const handleClose = (done: () => void) => {
    ElMessageBox.confirm('确定关闭此窗口?', '提示', {
        confirmButtonText: '确定关闭',
        cancelButtonText: '取消关闭',
        type: 'warning',
    })
    .then(() => {
        done()
        currentStep.value = 0
    })
    .catch(() => {

    })
}

onMounted(() => {
    queryList();
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
    font-size: 32px;
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

.user-nav a {
    color: white;
    text-decoration: none;
    margin-left: 20px;
    font-size: 20px;
}

.main-content{
    height: 100vh;
    width: 100%; 
    display: flex; 
    flex-direction: column; 
    align-items: center;
    overflow: scroll;
}

.user-nav a i {
    margin-right: 5px;
}

.header {
  display: flex;
  justify-content: left; 
  align-items: center; 
  height: 60px;
  margin-top: 30px;
  color:#FFFFFF;
}

.header-left {
  display: flex;
  align-items: center;
  gap: 10px; /* 图标和文字的间距 */
  margin-left: 150px;
  font-size: 25px;
  font-weight: bold;
  color: #FF8E8E;
}

.header-steps {
  flex: 1;
  max-width: 600px; /* 控制步骤条最大宽度 */
  background-color: #FFFFFF;
}

.main-nav{
    height: 60px;
    width: 100%;
    background-color: #FFFFFF;
    border-bottom: 1px solid #E4E7ED;
    display: flex;
}

.main-nav a {
    text-decoration: none;
    color: #666;
    font-size: 20px;
    transition: color 0.3s;
}

.table {
  margin: 50px auto 0;
  width: 80%; /* 可以根据需要设置 */
}

.row-disabled {
  background-color: #f5f5f5 !important;
  pointer-events: none;
  opacity: 0.6;
  text-decoration: line-through;
}

</style>