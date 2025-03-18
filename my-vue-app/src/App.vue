<template>
  <div>
  <div>
    <h2>用户管理</h2>
    
    <!-- 显示用户列表 -->
    <ul>
      <li v-for="user in users" :key="user.id">
        {{ user.name }} 
        <button @click="deleteUser(user.id)">删除</button>
      </li>
    </ul>

    <!-- 添加新用户 -->
    <input v-model="name" placeholder="输入用户名" />
    <input v-model="password" placeholder="输入密码" />
    <input v-model="age" placeholder="输入年龄" />
    <input v-model="phone" placeholder="输入电话" />
    <input v-model="sex" placeholder="输入性别" />
    <button @click="addUser">添加用户</button>
    </div>
    <div >
    <router-view></router-view>
    </div>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      users: [], // 用户列表
      name: "", // 新增用户的名字
      password: "",
      age: "",
      phone: "",
      sex: "",
    };
  },
  methods: {
    // 获取所有用户
    async fetchUsers() {
      try {
        const response = await axios.get("http://localhost:8080/users");
        this.users = response.data;
      } catch (error) {
        console.error("获取用户失败", error);
      }
    },

    // 添加用户
    async addUser() {
      if (!this.name) return;
      try {
        const response = await axios.post("http://localhost:8080/users", {
          name: this.name,
          password: this.password,
          age: this.age,
          phone: this.phone,
          sex: this.sex,
        });
        this.users.push(response.data); // 直接更新本地数据
        this.name = "";
        this.password = "";
        this.age = "";
        this.phone = "";
        this.sex = "";
      } catch (error) {
        console.error("添加用户失败", error);
      }
    },

    // 删除用户
    async deleteUser(id) {
      try {
        await axios.delete(`http://localhost:8080/users/${id}`);
        this.users = this.users.filter(user => user.id !== id);
      } catch (error) {
        console.error("删除用户失败", error);
      }
    },
  },
  mounted() {
    this.fetchUsers(); // 组件加载时获取用户
  },
  // mounted() {
  //   // 判断是否登录
  //   this.isLoggedIn = localStorage.getItem('isLoggedIn') === 'true';
  //   if (this.isLoggedIn) {
  //     this.fetchUsers(); // 获取用户列表
  //   } else {
  //     this.fetchUsers();
  //   }
  // },
};
</script>