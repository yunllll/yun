<template>
    <div
        style="height: 100vh; overflow: hidden;display: flex;justify-content: center;align-items: center;background-color: #cecece;">
        <div style="display: flex;background-color: white;width: 50%;border-radius: 5px;overflow: hidden;">
            <div style="flex: 1;">
                <img src="@/assets/AnKe.jpg" alt="" style="width: 100%;">
            </div>
            <div style="flex: 1;display: flex;align-items: center;justify-content: center;">
                <el-form :model="user" style="width: 80%;position: relative;">
                    <div style="font-size: 20px; font-weight: bold;margin-bottom: 20px;">欢迎登录帮忙贷</div>
                    <el-form-item prop="username">
                        <el-input size="medium" placeholder="请输入账号" v-model="user.username">
                            <template #prefix>
                                <el-icon>
                                    <User />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="password">
                        <el-input size="medium" placeholder="请输入密码" v-model="user.password" show-password>
                            <template #prefix>
                                <el-icon>
                                    <Lock />
                                </el-icon>
                            </template>
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="validCode">
                        <div style="display: flex;width: 100%;justify-content: space-between;">
                            <el-input v-model="sidentifyMode" size="medium" style="flex: 1.5;" clearable
                                placeholder="请输入验证码">
                                <template #prefix>
                                    <el-icon>
                                        <CircleCheck />
                                    </el-icon>
                                </template>
                            </el-input>
                            <div style="background-color: #cecece; flex: 0.5;">
                                <!-- 使用验证码组件 -->
                                <div style="text-align: center;display: flex;justify-content: center;line-height: 1;align-items: center;margin-bottom: -1px;border-top: 1px solid #cecece;border-right: 1px solid #cecece;"
                                    @click="refreshCode">
                                    <SIdentify :identifyCode="identifyCode"></SIdentify>
                                </div>
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" style="width: 100%;" @click="login">登录</el-button>
                        <!-- imgs是自定义的背景图，没有使用默认图片 -->
                        <Vcode :show="isShow" @success="success" @close="close" @fail="fail" :imgs="[Img]"></Vcode>
                    </el-form-item>
                    <el-form-item>
                        <div style="display: flex;justify-content: space-between;width: 100%;">
                            <div>还没有账号？请<span style="color: #0f9876;cursor: pointer;">注册</span></div>
                            <div><span style="color: #0f9876;cursor: pointer;">忘记密码</span></div>
                        </div>
                    </el-form-item>
                </el-form>
            </div>
        </div>
    </div>
</template>

<script setup>
import { reactive } from 'vue'

// do not use same name with ref
const user = reactive({
    username: '',
    password: ''
})


import SIdentify from '../components/validCode.vue'
import { ElMessage } from 'element-plus'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'


//自定义背景图，自己准备一张图片，引入即可
import Img from '@/assets/AnKe.jpg'
//引入'vue3-puzzle-vcode'插件
import Vcode from 'vue3-puzzle-vcode'

const isShow = ref(false)
//获取路由器
let $router = useRouter()
let sidentifyMode = ref('') //输入框验证码
let identifyCode = ref('') //图形验证码
let identifyCodes = ref('1234567890abcdefjhijklinopqrsduvwxyz') //验证码出现的数字和字母

//组件挂载
onMounted(() => {
    identifyCode.value = ''
    makeCode(identifyCodes.value, 4)
})

// 生成随机数
const randomNum = (min, max) => {
    max = max + 1
    return Math.floor(Math.random() * (max - min) + min)
}
// 随机生成验证码字符串
const makeCode = (o, l) => {
    for (let i = 0; i < l; i++) {
        identifyCode.value += o[randomNum(0, o.length)]
    }
}
// 更新验证码
const refreshCode = () => {
    identifyCode.value = ''
    makeCode(identifyCodes.value, 4)
}
//登录
const login = () => {
    isShow.value = true;
    //验证验证码不为空
    if (!sidentifyMode.value) {
        ElMessage({ type: 'error', message: '验证码不能为空！' })
        return
    }
    //验证验证码是否正确
    if (sidentifyMode.value != identifyCode.value) {
        ElMessage({ type: 'error', message: '验证码错误' })
        refreshCode()
        return
    } else {
        ElMessage({ type: 'success', message: '登录成功' })
        $router.push('/')
    }
}

//用户通过了验证
const success = (msg) => {
  isShow.value = false
  console.log('验证通过' + msg)
  //通过验证跳转到home首页
  $router.push('/home')
}
//用户点击遮罩层，关闭模态框
const close = () => {
  isShow.value = false
}
//用户验证失败
const fail = () => {
  console.log('验证失败')
}
</script>

<style lang="scss" scoped></style>