<template>
    <el-form ref="loginFormRef" :model="loginForm" :rules="loginFormRules">
        <el-form-item prop="phone">
            <el-input v-model="loginForm.phone" placeholder="请输入手机号"></el-input>
        </el-form-item>
        <el-form-item prop="verificationCode">
            <el-input v-model="loginForm.verificationCode" placeholder="请输入验证码" @blur="validateVerificationCode"></el-input>
            <el-button @click="getloginPhoneCode" :disabled="!loginForm.showloginCode">
                {{ loginForm.showloginCode ? '获取验证码' : loginForm.count + '秒' }}
            </el-button>
        </el-form-item>
        <el-button type="primary" @click="login">登录</el-button>
    </el-form>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus'; // 使用 ElMessage
import { useRouter } from 'vue-router';
import axios from '../utils/axios'; // 导入自定义的 axios 实例

// 表单引用
const loginFormRef = ref(null);

// 验证手机号是否合法
const checkMobile = (rules, value, callback) => {
    const regMobile = /^(0|86|17951)?(13[0-9]|15[0123456789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
    if (regMobile.test(value)) {
        return callback();
    } else {
        callback(new Error("请输入合法的手机号"));
    }
};

// 验证输入的手机号验证码是否和存储的验证码相同
const checkPhoneCode = (rules, value, callback) => {
    console.log("验证函数调用，输入的验证码:", value); // 调试信息
    console.log("存储的验证码:", loginForm.value.contenttext); // 调试信息
    if (String(value).trim() === String(loginForm.value.contenttext).trim()) { // 使用 trim() 去除空格
        return callback();
    } else {
        callback(new Error("验证码错误"));
    }
};

// 表单
const loginForm = ref({
    phone: "",
    verificationCode: "", // 表单中展示的验证码
    contenttext: "", // 向手机号发送的随机验证码
    timer: null,
    showloginCode: true, // 判断展示‘获取验证码’或‘倒计时’
    count: "", // 倒计时时间
});

// 验证规则
const loginFormRules = {
    phone: [
        { required: true, message: "请输入手机号", trigger: "blur" },
        { validator: checkMobile, trigger: "blur" },
    ],
    verificationCode: [
        { required: true, message: "请输入手机验证码", trigger: "blur" },
        { validator: checkPhoneCode, trigger: "blur" },
    ],
};

// 验证验证码
const validateVerificationCode = () => {
    const code = loginForm.value.verificationCode;
    checkPhoneCode(null, code, (error) => {
        if (error) {
            ElMessage.error(error.message);
        }
    });
};

// 获取手机验证码
const getloginPhoneCode = () => {
    if (loginForm.value.phone === "") {
        return;
    }

    // 向后端发送请求获取验证码
    axios.post(`http://localhost:9090/api/register?to=${loginForm.value.phone}`)
        .then((res) => {
            if (res.message) {
                console.log(res.data,res.message)
                loginForm.value.contenttext = res.data; // 存储发送的验证码
                const TIME_COUNT = 60;
                if (!loginForm.value.timer) {
                    loginForm.value.showloginCode = false;
                    loginForm.value.count = TIME_COUNT;
                    loginForm.value.timer = setInterval(() => {
                        if (loginForm.value.count > 0) {
                            loginForm.value.count -= 1;
                        } else {
                            loginForm.value.showloginCode = true;
                            clearInterval(loginForm.value.timer);
                            loginForm.value.timer = null;
                        }
                    }, 1000);
                }
            } else {
                ElMessage.error("验证码发送失败！");
            }
        })
        .catch(() => {
            ElMessage.error("请求失败，请重试！");
        });
};
const router = useRouter();
const login = () => {
    loginFormRef.value.validate((valid) => {
        if (valid) {
            const inputCode = loginForm.value.verificationCode; // 用户输入的验证码
            const storedCode = loginForm.value.contenttext; // 存储的验证码
            
            console.log("输入的验证码:", inputCode);
            console.log("存储的验证码:", storedCode);

            // 直接比较验证码
            if (inputCode === storedCode) {
                router.push("/"); // 跳转到主页
            } else {
                ElMessage.error("验证码错误，请重试");
            }
        } else {
            console.log("表单验证失败，错误信息:", loginFormRef.value);
        }
    });
};

</script>

<style scoped>
/* 你可以在这里添加样式 */
</style>