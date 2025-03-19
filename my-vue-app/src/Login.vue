<template>
    <div class="login-container">
      <div class="login-card">
        <h2 class="login-title">用户登录</h2>
        <form @submit.prevent="login" class="login-form">
          <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
          <div class="input-group">
            <label for="name">用户名:</label>
            <input v-model="name" type="text" id="name" placeholder="请输入用户名" />
          </div>
          <div class="input-group">
            <label for="password">密码:</label>
            <div class="input-container">
              <input v-model="password" :type="showPassword ? 'text' : 'password'" id="password" placeholder="请输入密码" />
              <span @click="showPassword = !showPassword" class="toggle-password">
                {{ showPassword ? "👁" : "🙈" }}
              </span>
            </div>
          </div>
          <button type="submit" class="login-button">登录</button>
        </form>
        <p class="register-link">还没有账号？ <router-link to="/register">注册</router-link></p>
      </div>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        name: "",
        password: "",
        showPassword: false,  // 控制密码显示或隐藏
        errorMessage: "",  // 存储错误信息
      };
    },
    methods: {
      async login() {
        if (!this.name || !this.password) {
          this.errorMessage = "请填写用户名和密码！";
          return;
        }
        try {
          const response = await axios.post("http://localhost:8080/login", {
            name: this.name,
            password: this.password,
          });
          if (response.data.success) {
            this.$router.push("/Success"); // 登录成功后跳转到用户主页
          } else {
            this.errorMessage = response.data.message || "登录失败";
          }
        } catch (error) {
          this.errorMessage = error.response?.data?.message || "服务器错误，请稍后再试！";
        }
      },
      // 切换密码显示状态
      togglePasswordVisibility() {
        this.showPassword = !this.showPassword;
      },
    },
  };
  </script>
  
  <style scoped>
    /* 确保整个页面的背景为渐变色 */
    html, body {
        height: 100% !important;
        margin: 0 !important;  /* 去掉默认的边距 */
        background: linear-gradient(135deg, #6b8abf, #d0d9e2) !important;  /* 设置渐变背景 */
    }

    /* 保证登录容器居中 */
    .login-container {
        display: flex;
        justify-content: center;
        align-items: center;
        height: 100vh;  /* 确保容器占满整个视口高度 */
        width: 170vh;
        margin: 0;
        padding: 0;
        transform: translate(-80px, -50px); /* 向左移动 50px，向上移动 50px */
    }

    .login-card {
        background-color: white;
        padding: 40px;
        border-radius: 10px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        width: 100%;
        max-width: 400px;
        text-align: center;
    }
  
  .login-title {
    font-size: 24px;
    margin-bottom: 20px;
    color: #333;
  }
  
  .login-form {
    display: flex;
    flex-direction: column;
  }
  
  .input-group {
    margin-bottom: 15px;
  }

  .input-container {
      position: relative;
      display: flex;
      align-items: center;
  }

  .toggle-password {
      position: absolute;
      right: 10px;
      cursor: pointer;
  }

  .input-group input {
      width: 100%;
      padding-right: 30px;
  }
  
  input {
    padding: 10px;
    border-radius: 5px;
    border: 1px solid #ccc;
    margin-top: 5px;
    font-size: 16px;
  }
  
  button.login-button {
    background: linear-gradient(135deg, #6b8abf, #4a65a1);
    color: white;
    border: none;
    padding: 10px;
    border-radius: 5px;
    font-size: 16px;
    cursor: pointer;
    transition: background 0.3s ease;
  }
  
  button.login-button:hover {
    background: linear-gradient(135deg, #4a65a1, #6b8abf);
  }
  
  .register-link {
    margin-top: 15px;
    font-size: 14px;
  }
  
  .register-link a {
    color: #4a65a1;
    text-decoration: none;
  }
  
  .register-link a:hover {
    text-decoration: underline;
  }
  
  .error-message {
    color: red;
    margin-bottom: 10px;
    font-size: 14px;
  }
  </style>