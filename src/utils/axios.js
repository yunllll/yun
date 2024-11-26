import axios from 'axios';

// 创建 Axios 实例
const axiosInstance = axios.create({
  baseURL: 'http://localhost:9090', // 替换为你的后端 API 基础 URL
  timeout: 10000, // 请求超时时间
});

// 请求拦截器
axiosInstance.interceptors.request.use(
  (config) => {
    // 在请求发送之前做些什么，比如添加 token
    const token = localStorage.getItem('token'); // 假设你将 token 存储在 localStorage
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`; // 添加 Authorization 头
    }
    return config;
  },
  (error) => {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

// 响应拦截器
axiosInstance.interceptors.response.use(
  (response) => {
    // 对响应数据做点什么
    return response.data; // 直接返回数据
  },
  (error) => {
    // 对响应错误做点什么
    if (error.response) {
      // 请求已发出，服务器响应了状态码
      // 你可以根据状态码进行不同的处理
      switch (error.response.status) {
        case 401:
          // 未授权，可能需要重定向到登录页面
          break;
        case 404:
          // 找不到资源
          break;
        case 500:
          // 服务器错误
          break;
        default:
          break;
      }
    } else {
      // 其他错误
      console.error('请求失败:', error.message);
    }
    return Promise.reject(error);
  }
);

export default axiosInstance;
