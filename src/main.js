import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
// 包括CSS
import "vue-data-ui/style.css"

// 您可以全局声明 Vue Data UI 组件
import { VueUiRadar } from "vue-data-ui"
const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }
app.component("VueUiRadar", VueUiRadar)

app.use(store).use(router).use(ElementPlus).mount('#app');

// 路由守卫
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title;
  } else {
    document.title = '帮忙贷'; // 设置一个默认标题
  }
  next();
});
