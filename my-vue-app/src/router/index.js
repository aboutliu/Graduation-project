import { createRouter, createWebHistory } from 'vue-router';
import Login from '@/Login.vue';  // 导入 Login 组件
import Register from '@/Register.vue';  // 如果有注册页，可以导入注册组件
import Success from '@/Success.vue';  
import test from '@/test.vue';  
import activity from '@/activity.vue';  
import resource from '@/resource.vue';  
import person from '@/person.vue';  
import personalactivity from '@/personalactivity.vue';  
import personalresource from '@/personalresource.vue';  
import personalhelp from '@/personalhelp.vue';  
import personalvirtual from '@/personalvirtual.vue';  
import help from '@/help.vue';  
import virtual from '@/virtual.vue';  

const routes = [
  {
    path: '/',
    redirect: '/Login'  // 默认跳转到 /login
  },
  {
    path: '/login',
    name: 'Login',
    component: Login
  },
  {
    path: '/register',
    name: 'Register',
    component: Register
  },
  {
    path: '/success',
    name: 'Success',
    component: Success
  },
  {
    path: '/test',
    name: 'test',
    component: test
  },
  {
    path: '/activity',
    name: 'activity',
    component: activity
  },
  {
    path: '/resource',
    name: 'resource',
    component: resource
  },
  {
    path: '/person',
    name: 'person',
    component: person
  },
  {
    path: '/personalactivity',
    name: 'personalactivity',
    component: personalactivity
  },
  {
    path: '/personalresource',
    name: 'personalresource',
    component: personalresource
  },
  {
    path: '/personalhelp',
    name: 'personalhelp',
    component: personalhelp
  },
  {
    path: '/personalvirtual',
    name: 'personalvirtual',
    component: personalvirtual
  },
  {
    path: '/help',
    name: 'help',
    component: help
  },
  {
    path: '/virtual',
    name: 'virtual',
    component: virtual
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes
});

export default router;