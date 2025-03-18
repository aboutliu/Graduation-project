<template>
    <div>
      <h2>用户注册</h2>
      <form @submit.prevent="register">
        <div>
          <label for="username">用户名:</label>
          <input v-model="username" type="text" id="username" placeholder="输入用户名" />
        </div>
        <div>
          <label for="password">密码:</label>
          <input v-model="password" type="password" id="password" placeholder="输入密码" />
        </div>
        <div>
          <label for="confirmPassword">确认密码:</label>
          <input v-model="confirmPassword" type="password" id="confirmPassword" placeholder="确认密码" />
        </div>
        <button type="submit">注册</button>
      </form>
      <p>已有账号？ <router-link to="/login">登录</router-link></p>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        username: "",
        password: "",
        confirmPassword: "",
      };
    },
    methods: {
      async register() {
        if (!this.username || !this.password || !this.confirmPassword) {
          alert("请填写所有字段！");
          return;
        }
        if (this.password !== this.confirmPassword) {
          alert("密码和确认密码不一致！");
          return;
        }
        try {
          const response = await axios.post("http://localhost:8080/register", {
            username: this.username,
            password: this.password,
          });
          if (response.data.success) {
            this.$router.push("/login"); // 注册成功后跳转到登录页面
          } else {
            alert("注册失败！");
          }
        } catch (error) {
          console.error("注册请求失败", error);
        }
      },
    },
  };
  </script>