<template>
    <div class="home-container">
      <h2 class="welcome-text">Welcome to Community Service Platform</h2>
  
      <div class="help-box" @click="goTo('/help')">紧急求助</div>
      <div class="talk-box" @click="goTo('/virtual')">虚拟空间</div>
      <div class="share-box" @click="goTo('/resource')">资源分享</div>
      <div class="activity-box" @click="goTo('/activity')">活动日历</div>
    </div>

    <!-- 用户界面 -->
    <div class="avatar-wrapper" @click="goTo('/person')">
      <img src="/images/background.jpg" alt="avatar" />
    </div>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router'
  
  const router = useRouter()
  const goTo = (path) => {
    router.push(path)
  }

  
  </script>
  
  <style scoped>
  .home-container {
    /* max-width: 1000px;
    margin: 0 auto;
    padding: 2rem;
    font-family: Arial, sans-serif; */
    min-height: 100vh;
    width: 86vw;
    padding: 2rem;
    font-family: Arial, sans-serif;

    background-image: url('images/b.png');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;

    color: #fff; /* 白色字体更清晰 */
    backdrop-filter: brightness(0.9); /* 可选：让内容更易读 */

  }
  
  .title {
    text-align: center;
    font-size: 32px;
    margin-bottom: 2rem;
  }
  
  .help-box {
    position: fixed;
    left: 72vw;
    top: 5vh;
    color: #fff;
    padding: 8px 16px;
    border-radius: 7px;
    display: inline-block;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent; /* 初始无边框 */
  }
  .help-box:hover {
    transform: translateY(-2px);
    background-color: transparent; /* hover时背景透明 */
    border: 2px solid #fff; /* 显示白色边框 */
    color: #fff; /* 字体保持白色 */
  }
  .talk-box {
    position: fixed;
    left: 65vw;
    top: 5vh;
    color: #fff;
    padding: 8px 16px;
    border-radius: 7px;
    display: inline-block;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent; /* 初始无边框 */
  }
  .talk-box:hover {
    transform: translateY(-2px);
    background-color: transparent; /* hover时背景透明 */
    border: 2px solid #fff; /* 显示白色边框 */
    color: #fff; /* 字体保持白色 */
  }
  .share-box {
    position: fixed;
    left: 58vw;
    top: 5vh;
    color: #fff;
    padding: 8px 16px;
    border-radius: 7px;
    display: inline-block;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent; /* 初始无边框 */
  }
  .share-box:hover {
    transform: translateY(-2px);
    background-color: transparent; /* hover时背景透明 */
    border: 2px solid #fff; /* 显示白色边框 */
    color: #fff; /* 字体保持白色 */
  }
  .activity-box {
    position: fixed;
    left: 51vw;
    top: 5vh;
    color: #fff;
    padding: 8px 16px;
    border-radius: 7px;
    display: inline-block;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent; /* 初始无边框 */
  }
  .activity-box:hover {
    transform: translateY(-2px);
    background-color: transparent; /* hover时背景透明 */
    border: 2px solid #fff; /* 显示白色边框 */
    color: #fff; /* 字体保持白色 */
  }
  .welcome-text {
    position: fixed;
    left: 5vw;
    top: 50vh;
    text-align: center;
    font-size: 60px;
    font-weight: bold;
    color: #fff;
    margin-bottom: 2rem;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.6);
  }
  /* <style scoped> */
.avatar-wrapper {
  position: fixed;
  top: 2vh;
  left: 87vw;
  width: 75px;
  height: 75px;
  border: 4px solid #fff;
  border-radius: 50%;
  overflow: hidden;
  cursor: pointer;
}
.avatar-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
  </style>
