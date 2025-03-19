<template>
    <div class="register-container">
      <form class="register-card" @submit.prevent="register">
        <h2 class="register-title">用户注册</h2>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
        <div class="form-group">
          <label for="name">用户名:</label>
          <input v-model="name" type="text" id="name" placeholder="输入用户名" />
        </div>
        <div class="form-group password-group">
          <label for="password">密码:</label>
          <div class="input-container">
            <input v-model="password" :type="showPassword ? 'text' : 'password'" id="password" placeholder="输入密码" />
            <span @click="showPassword = !showPassword" class="toggle-password">
              {{ showPassword ? "👁" : "🙈" }}
            </span>
          </div>
        </div>
        <div class="form-group">
          <label>性别:</label>
          <div class="gender-options">
            <label><input type="radio" v-model="sex" value="男" /> 男</label>
            <label><input type="radio" v-model="sex" value="女" /> 女</label>
          </div>
        </div>
        <div class="form-group">
          <label for="age">年龄:</label>
          <input v-model="age" type="number" id="age" placeholder="输入年龄" />
        </div>
        <div class="form-group">
          <label for="phone">电话:</label>
          <input v-model="phone" type="text" id="phone" placeholder="输入电话" />
        </div>
        <div class="form-group password-group">
          <label for="confirmPassword">确认密码:</label>
          <div class="input-container">
            <input v-model="confirmPassword" :type="showConfirmPassword ? 'text' : 'password'" id="confirmPassword" placeholder="确认密码" />
            <span @click="showConfirmPassword = !showConfirmPassword" class="toggle-password">
              {{ showConfirmPassword ? "👁" : "🙈" }}
            </span>
          </div>
        </div>
        <button type="submit" class="register-button">注册</button>
        <p class="login-prompt">已有账号？ <router-link to="/">登录</router-link></p>
      </form>
    </div>
  </template>
  
  <script>
  import axios from "axios";
  
  export default {
    data() {
      return {
        name: "",
        password: "",
        confirmPassword: "",
        sex: "男",
        age: "",
        phone: "",
        root: 0, // 默认值
        showPassword: false,
        showConfirmPassword: false,
        errorMessage: "",  // 存储错误信息
      };
    },
    methods: {
      async register() {
        if (!this.name || !this.password || !this.confirmPassword || !this.sex || !this.age || !this.phone) {
        //   alert("请填写所有字段！");
          this.errorMessage = "请填写所有字段！";
          return;
        }
        if (this.password !== this.confirmPassword) {
        //   alert("密码和确认密码不一致！");
          this.errorMessage = "密码和确认密码不一致！";
          return;
        }
        try {
          const response = await axios.post("http://localhost:8080/register", {
            name: this.name,
            password: this.password,
            sex: this.sex,
            age: this.age,
            phone: this.phone,
            root: this.root, // 默认值 0
          });
          if (response.data.success) {
            this.$router.push("/Success"); // 注册成功后跳转到登录页面
          } else {
            this.errorMessage = response.data.message || "注册失败！";
            // alert("注册失败！");
          }
        } catch (error) {
            this.errorMessage = error.response?.data?.message || "注册请求失败";
        //   console.error("注册请求失败", error);
        }
      },
    },
  };
  </script>

<style scoped>
.register-container {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100vh;
    width: 50vh;
    background: inherit; /* 使用父元素的背景样式 */
    transform: translateX(190px); /* 向右移动 50px */
}

.register-card {
    background: white;
    padding: 30px; /* 调整padding以匹配Login.vue */
    border-radius: 10px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    width: 100%;
    max-width: 500px; /* 调整max-width以匹配Login.vue */
    display: flex;
    flex-direction: column;
    align-items: center;
}

.register-title {
    text-align: center; /* 确保标题居中 */
    margin-bottom: 20px; /* 防止被挤压 */
}

.form-group {
    display: flex;
    flex-direction: column;
    margin-bottom: 15px;
    width: 40vh;
}

.password-group input {
    width: 100%;
    padding-right: 30px;
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

label {
    margin-bottom: 5px;
    font-weight: bold;
}

input, select {
    padding: 10px;
    border-radius: 5px;
    border: 1px solid #ccc;
}

.register-button {
    background: #4a67c3;
    color: white;
    padding: 10px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    width: 100%;
}

.register-button:hover {
    background: #3b56a2;
}

p {
    text-align: center;
    margin-top: 10px;
}

.login-prompt {
    margin-top: 20px; /* Adjust margin for consistency with Login.vue */
    text-align: center; /* 确保它水平居中 */
}

.gender-options {
    display: flex;
    gap: 245px; /* Increased spacing */
    align-items: center;
}

.gender-options label {
    display: flex;
    align-items: center;
    gap: 5px;
}

.router-link {
    color: #4a67c3;
    text-decoration: none;
}
</style>