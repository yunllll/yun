<template>
    <div class="common-layout">
        <el-container>
            <el-aside style="min-height: 100vh;" :width="asideWidth">
                <div class="aside"><img src="@/assets/logo.jpg" alt=""
                        style="width: 40px; height: 40px; border-radius: 50%;">
                    <transition name="el-fade-in">
                        <span class="logo-title" v-show="!isCollapse">不妨假设一下的练习</span>
                    </transition>
                </div>
                <el-menu active-text-color="#d3e0e2" background-color="#545c64" class="el-menu-vertical-demo" router
                    :default-active="$route.path" text-color="#cecece" @open="handleOpen" @close="handleClose"
                    style="border: none;" :collapse="isCollapse" :collapse-transition="false">
                    <el-sub-menu index="1">
                        <template #title>
                            <el-icon>
                                <message />
                            </el-icon>
                            <span>Try首页</span>
                        </template>
                        <el-menu-item-group title="效果页面">
                            <el-menu-item index="/vanTa">vanTa</el-menu-item>
                            <el-menu-item index="/">element</el-menu-item>
                        </el-menu-item-group>
                        <el-menu-item-group title="笔记">
                            <el-menu-item index="/xiTong">小袁速算OCR识别</el-menu-item>
                        </el-menu-item-group>
                        <el-sub-menu index="/xi">
                            <template #title>怎么写创新型发明专利</template>
                            <el-menu-item index="1-4-1">this</el-menu-item>
                        </el-sub-menu>
                    </el-sub-menu>
                </el-menu>
            </el-aside>
            <el-container>
                <el-header style="padding-left: 40px;padding-right: 40px;">
                    <el-breadcrumb :separator-icon="ArrowRight">
                        <el-breadcrumb-item :to="{ path: '/xiTong' }"><el-icon @click="handleCollapse">
                                <Fold v-if="!isCollapse" />
                                <Expand v-else />
                            </el-icon><span style="margin-left: 10px;">Try首页</span></el-breadcrumb-item>
                        <el-breadcrumb-item :to="{ path: '/' }">不知道</el-breadcrumb-item>
                        <el-breadcrumb-item :to="{ path: '/vanTa' }">vanTa</el-breadcrumb-item>
                        <el-breadcrumb-item :to="{ path: '/dataui' }">dataui</el-breadcrumb-item>
                    </el-breadcrumb>
                    <el-dropdown placement="bottom-start" style="margin-left: auto;">
                        <el-button style="display: flex; align-items: center; justify-content: center;"><img
                                src="@/assets/logo.jpg" alt=""
                                style="width: 30px; height: 30px; border-radius: 50%;">Admin</el-button>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item>个人信息</el-dropdown-item>
                                <el-dropdown-item>修改密码</el-dropdown-item>
                                <el-dropdown-item @click="Logout">退出登录</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </el-header>
                <el-main><el-descriptions class="margin-top" title="table" :column="3" :size="size" :style="blockMargin"
                        border>
                        <template #extra>
                            <el-button type="primary">Operation</el-button>
                        </template>
                        <el-descriptions-item label="Username">kooriookami</el-descriptions-item>
                        <el-descriptions-item label="Telephone">18100000000</el-descriptions-item>
                        <el-descriptions-item label="Place">Suzhou</el-descriptions-item>
                        <el-descriptions-item label="Remarks">
                            <el-tag size="small">School</el-tag>
                        </el-descriptions-item>
                        <el-descriptions-item label="Address">
                            No.1188, Wuzhong Avenue, Wuzhong District, Suzhou, Jiangsu Province
                        </el-descriptions-item>
                    </el-descriptions>
                    <el-table :data="users" style="width: 50%" :row-class-name="tableRowClassName">
                        <el-table-column prop="id" label="Id" width="180" />
                        <el-table-column prop="username" label="姓名" width="180" />
                        <el-table-column prop="age" label="年龄" />
                    </el-table>
                </el-main>
                <el-footer>Footer</el-footer>
            </el-container>
        </el-container>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { onMounted } from 'vue'
import axios from "axios"
import {
    Document,
    Menu as IconMenu,
    Location,
    Setting,
    Message
} from '@element-plus/icons-vue';

var users = []
// 使用 onMounted 生命周期钩子
onMounted(() => {
    // 在组件挂载后执行的代码
    // fetchData();
    axios.get('http://localhost:9090/user/inquire').then(res => {
        console.log(res.data)
        users = res.data.data
    })
});

// 定义 fetchData 函数
const fetchData = async () => {
    try {
        const response = await axios.get('http://localhost:9090/user/inquire'); // 替换为你的 API 地址
        console.log(response.data); // 处理返回的数据
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};



let isCollapse = ref(true);
let asideWidth = ref('64px'); // 也将 asideWidth 转换为响应式变量
const handleOpen = (key, keyPath) => {
    console.log(key, keyPath);
};
// 切换折叠状态的处理函数
const handleCollapse = () => {
    isCollapse.value = !isCollapse.value; // 使用 .value 来访问和修改
    asideWidth.value = isCollapse.value ? '64px' : '200px'; // 同样使用 .value
};
const handleClose = (key, keyPath) => {
    console.log(key, keyPath);
};


const tableRowClassName = ({ row, rowIndex }) => {
    if (rowIndex === 1) {
        return 'warning-row';
    } else if (rowIndex === 3) {
        return 'success-row';
    }
    return '';
}

const tableData = [
    {
        date: '2016-05-03',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-02',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-04',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
    {
        date: '2016-05-01',
        name: 'Tom',
        address: 'No. 189, Grove St, Los Angeles',
    },
];
</script>

<style lang="scss" scoped>
.aside {
    height: 60px;
    line-height: 60px;
    background-color: #8c939d;
    color: #d3e0e2;
    display: flex;
    justify-content: center;
    align-items: center;
    font-size: 14px;
}

// .el-menu--inline .el-menu-item{
//     background-color: #fff important;
// }
.el-menu-item:hover,
el-submenu__title:hover {
    color: #fff !important;
}

.el-submenu__title:hover i {
    color: #fff !important;
}

.el-menu-item.is-active {
    background-color: #5ab9f0 !important;
    border-radius: 5px !important;
}

.el-aside {
    transition: width 0.4s;
    box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
}

.logo-title {
    margin-left: 5px;
    font-size: 14px;
    transition: all 0.4s;
}

.el-header {
    box-shadow: 2px 0 6px rgba(0, 21, 41, .35);
    display: flex;
    align-items: center;
}

.el-descriptions {
    margin-top: 20px;
}

.cell-item {
    display: flex;
    align-items: center;
}

.margin-top {
    margin-top: 20px;
}

.el-table .warning-row {
    --el-table-tr-bg-color: var(--el-color-warning-light-9);
}

.el-table .success-row {
    --el-table-tr-bg-color: var(--el-color-success-light-9);
}
</style>