import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 包括CSS
import "vue-data-ui/style.css"

// 您可以全局声明 Vue Data UI 组件
import { VueUiRadar } from "vue-data-ui"
const app = createApp(App)

app.component("VueUiRadar", VueUiRadar)

app.use(store).use(router).use(ElementPlus).mount('#app')
