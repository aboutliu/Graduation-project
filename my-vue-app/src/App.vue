<!-- <script setup>
import HelloWorld from './components/HelloWorld.vue'
import TheWelcome from './components/TheWelcome.vue'
</script>

<template>
  <h1>hello Jiangyuhuai!</h1>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style> -->

<template>
  <div>
    <h2>用户管理</h2>
    
    <!-- 显示用户列表 -->
    <ul>
      <li v-for="user in users" :key="user.id">
        {{ user.username }} 
        <button @click="deleteUser(user.id)">删除</button>
      </li>
    </ul>

    <!-- 添加新用户 -->
    <input v-model="newUser" placeholder="输入用户名" />
    <button @click="addUser">添加用户</button>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      users: [], // 用户列表
      newUser: "", // 新增用户的名字
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
      if (!this.newUser) return;
      try {
        const response = await axios.post("http://localhost:8080/users", {
          username: this.newUser,
        });
        this.users.push(response.data); // 直接更新本地数据
        this.newUser = "";
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
};
</script>