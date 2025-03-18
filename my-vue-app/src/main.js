// import './assets/main.css'

// import { createApp } from 'vue'
// import App from './App.vue'

// createApp(App).mount('#app')

import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router'  // 导入刚刚创建的 router 配置

createApp(App).use(router).mount('#app')  // 使用 Vue Router