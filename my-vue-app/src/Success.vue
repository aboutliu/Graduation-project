<template>
    <div class="success-container">
      <div class="feature-list">
        <div v-for="feature in features" :key="feature.id" class="feature-card" @click="goToPage(feature.path)">
          <h3>{{ feature.title }}</h3>
          <p>{{ feature.description }}</p>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  import { useRouter } from 'vue-router';

  export default {
    name: "Success",
    setup() {
      const router = useRouter();

      const features = [
        { id: 1, title: '社区活动日历', description: '实现活动发布、查看、报名功能。', path: '/activity-calendar' },
        { id: 2, title: '社区资源共享库', description: '实现资源发布、借用、归还、评价功能。', path: '/resource-sharing' },
        { id: 3, title: '虚拟社区空间', description: '实现话题发布、评论、点赞、图片/视频分享。', path: '/virtual-community' },
        { id: 4, title: '紧急求助功能', description: '支持一键通知邻近志愿者并推送通知。', path: '/emergency-help' }
      ];

      const goToPage = (path) => {
        router.push(path);
      };

      return { features, goToPage };
    }
  };
  </script>
  
  <style scoped>
  .success-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    height: 100vh;
    font-size: 24px;
    color: #4a65a1;
    font-weight: bold;
  }

  .feature-list {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    align-items: stretch; /* 让子元素高度保持一致 */
    gap: 15px;
    margin-top: 20px;
    width: 80%;
  }

  .feature-card {
    flex: 0 1 220px; /* 固定最小宽度，防止无限扩展 */
    max-width: 300px; /* 限制最大宽度 */
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
    padding: 20px; /* 增加内边距，增强视觉效果 */
    border-radius: 10px;
    background: #f5f5f5;
    cursor: pointer;
    transition: background 0.3s;
  }

  .feature-card:hover {
    background: #e0e0e0;
  }
  </style>