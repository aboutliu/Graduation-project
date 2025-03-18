import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/Login.vue';  // 导入 Login 组件
import Register from '@/Register.vue';  // 如果有注册页，可以导入注册组件
import Success from '@/Success.vue';  

const routes = [
  {
    path: '/',
    name: 'Login',
    component: Login  // 默认显示 Login.vue
  },
  {
    path: '/Register',
    name: 'Register',
    component: Register  // 注册页面
  },
  {
    path: '/Success',
    name: 'Success',
    component: Success  // 注册页面
  }
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router;