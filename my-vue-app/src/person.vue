<template>
    <div class="home-container">
  
      <div class="help-box" @click="goTo('/help')">我的资源</div>
      <div class="talk-box" @click="showActivities = true">我的活动</div>
      <div class="add-box" @click="showAddForm = !showAddForm">{{ showAddForm ? '关闭' : '添加' }}</div>
      <!-- <div class="share-box" @click="goTo('/resource')">资源分享</div>
      <div class="activity-box" @click="goTo('/activity')">活动日历</div> -->
      <div v-if="showActivities" class="activity-viewer">
            <button v-if="currentActivities.length > 0" class="arrow-button left" @click="goToPrevActivity">&#8592;</button>
            <input class="search-box" v-model="searchQuery" placeholder="🔍 搜索"/>
            <div class="activity-container">
                <div class="activity-block" v-for="(activity, index) in currentActivities" :key="index">
                    <button class="delete-button" @click="deleteActivity(activity.id)">×</button>
                    <div class="activity-info">
                        <div class="activity-image">
                            <img :src="'http://localhost:8080' + '/images/' + (activity.image || 'activity.png')" alt="活动图片" />
                        </div>
                        <p><strong>名称：</strong>{{ activity.name }}</p>
                        <p><strong>日期：</strong>{{ activity.time }}</p>
                        <p><strong>地点：</strong>{{ activity.place }}</p>
                        <p><strong>编辑人：</strong>{{ activity.editor }}</p>
                        <p><strong>说明：</strong>{{ activity.introduction }}</p>
                    </div>
                </div>
            </div>
            <button v-if="currentActivities.length > 0" class="arrow-button right" @click="goToNextActivity">&#8594;</button>
      </div>

      <div v-if="showAddForm">
        <!-- <div class="overlay-backdrop" @click="closeAddForm"></div>
        <div class="activity-block add-activity-form">
          <button class="upload-image" @click="imageInput.click()">上传图片</button>
          <input type="file" ref="imageInput" style="display:none" @change="handleImageUpload" />
          <div class="activity-info">
            <input v-model="newActivity.name" placeholder="名称" />
            <input v-model="newActivity.time" placeholder="时间" />
            <input v-model="newActivity.place" placeholder="地点" />
            <input v-model="newActivity.editor" placeholder="编辑人" />
            <textarea v-model="newActivity.introduction" placeholder="说明"></textarea>
            <button @click="addActivity">添加</button>
          </div>
        </div> -->
        <div v-if="showAddForm" class="activity-container" style="z-index: 999;">
  <div class="activity-block">
    <button class="upload-image" @click="imageInput.click()">上传图片</button>
    <input type="file" ref="imageInput" style="display:none" @change="handleImageUpload" />
    <div class="activity-info">
      <div class="activity-image">
        <img :src="'http://localhost:8080/images/' + (newActivity.image || 'activity.png')" alt="上传图片" />
      </div>
      <input v-model="newActivity.name" placeholder="名称" />
      <input v-model="newActivity.time" placeholder="时间" />
      <input v-model="newActivity.place" placeholder="地点" />
      <input v-model="newActivity.editor" placeholder="编辑人" />
      <textarea v-model="newActivity.introduction" placeholder="说明"></textarea>
      <button @click="addActivity">添加</button>
    </div>
  </div>
</div>
            <!-- <div style="color: red; z-index: 9999;">fff</div> -->
      </div>
    </div>

    <!-- 用户界面 -->
    <div class="avatar-wrapper">
      <img src="/images/background.jpg" alt="avatar" />
    </div>
  </template>
  
  <script setup>

import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const showActivities = ref(false);
const showAddForm = ref(false);

const activities = ref([]);
const currentIndex = ref(0);
const itemsPerPage = 3;

const searchQuery = ref('');
const userId = localStorage.getItem('userId') || '未登录';

const currentActivities = computed(() => {
    // 先筛选出 idname 包含 userId 的活动
  const filtered = activities.value.filter(
    act => act.idname && act.idname.includes(userId)
  );
  return activities.value.filter(act =>
      act.idname && act.idname.includes(userId + searchQuery.value)
    );
});

const goToPrevActivity = () => {
  if (currentIndex.value > 0) {
    currentIndex.value -= itemsPerPage;
  }
};

const goToNextActivity = () => {
  if (currentIndex.value + itemsPerPage < activities.value.length) {
    currentIndex.value += itemsPerPage;
  }
};

const deleteActivity = async (id) => {
  try {
    await axios.delete(`http://localhost:8080/activities/${id}`);
    activities.value = activities.value.filter(act => act.id !== id);
  } catch (error) {
    console.error("Failed to delete activity:", error);
  }
};

onMounted(async () => {
  try {
    const response = await axios.get("http://localhost:8080/activities");
    activities.value = response.data;
  } catch (error) {
    console.error("Failed to fetch activities:", error);
  }
});

import { useRouter } from 'vue-router'

const router = useRouter()
const goTo = (path) => {
  router.push(path)
}

// 新增活动相关
const newActivity = ref({
  name: '',
  time: '',
  place: '',
  editor: '',
  introduction: '',
  image: '',
});

const imageInput = ref(null);
let uploadedImageName = '';

const handleImageUpload = async (e) => {
  const file = e.target.files[0];
  if (!file) return;

  const formData = new FormData();
  const filename = userId + newActivity.value.name + '.' + file.name.split('.').pop();
  formData.append('file', file, filename);

  try {
    await axios.post('http://localhost:8080/upload', formData);
    uploadedImageName = filename;
    alert('上传成功');
  } catch (error) {
    console.error('上传失败', error);
    alert('上传失败');
  }
};

const addActivity = async () => {
  const { name, time, place, editor, introduction } = newActivity.value;
  if (!name || !time || !place || !editor || !introduction) {
    alert('请填写所有必填字段');
    return;
  }

  const activityToAdd = {
    name,
    time,
    place,
    editor,
    introduction,
    image: uploadedImageName || '',
    idname: userId + name,
  };

  try {
    await axios.post('http://localhost:8080/activities', activityToAdd);
    activities.value.push(activityToAdd);
    alert('添加成功');
    Object.assign(newActivity.value, {
      name: '', time: '', place: '', editor: '', introduction: '', image: ''
    });
    uploadedImageName = '';
  } catch (error) {
    console.error('添加失败', error);
    alert('添加失败');
  }
};

const closeAddForm = () => {
  showAddForm.value = false;
};

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
.activity-viewer {
    position: fixed;
    top: 22vh;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 1rem;
}

.arrow-button {
  background: transparent;
  color: #0056b3;
  border: none;
  padding: 0;
  cursor: pointer;
  font-size: 2rem;
  transition: color 0.3s;
}

.arrow-button:hover {
    transform: translateY(-2px);
  color: #0056b3;
}

.arrow-button.left {
  margin-right: 1rem;
}

.arrow-button.right {
  margin-left: 1rem;
}

.scroll-wrapper {
  overflow-x: auto;
}

.activity-container {
  height: 60vh;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 2rem;
  padding: 2rem;
}
 
.activity-block {
    width: 10vw;
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: #f4f4f4;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  min-width: 300px;
  position: relative;
}
.activity-block:hover {
    transform: translateY(-10px);
}

.activity-info {
  font-size: 16px;
  color: #333;
}

.activity-image {
  width: 250px;
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.activity-image img {
  width: 250px; /* or your desired width */
  height: 200px; /* or your desired height */
  border-radius: 8px;
  object-fit: cover;
}
.search-box {
    position: fixed;
    width:20vw;
    left: 61vw;
    top: 15vh;
    color: #fff;
    /* color:black; */
    background-color: #f4f4f4;
    padding: 8px 16px;
    border-radius: 7px;
    display: inline-block;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent; 
    /* 初始无边框 */
  }
.overlay-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.4);
  z-index: 998;
}

.add-activity-form {
  position: fixed;
  top: 20vh;
  left: 50vw;
  transform: translateX(-50%);
  z-index: 999;
}
.delete-button {
  position: absolute;
  top: 8px;
  left: 8px;
  background: transparent;
  color: #888;
  border: none;
  font-size: 20px;
  font-weight: bold;
  cursor: pointer;
  z-index: 10;
}

.delete-button:hover {
  color: red;
}
  .new-activity-block input,
  .new-activity-block textarea {
    width: 100%;
    margin: 4px 0;
    padding: 6px;
    border-radius: 5px;
    border: 1px solid #ccc;
  }

  .new-activity-block button {
    margin-top: 10px;
    background-color: #007bff;
    color: white;
    border: none;
    padding: 6px 12px;
    border-radius: 5px;
    cursor: pointer;
  }

  .new-activity-block button:hover {
    background-color: #0056b3;
  }
  .upload-image {
    position: absolute;
    top: 8px;
    right: 8px;
    background: transparent;
    color: #888;
    border: none;
    font-size: 14px;
    cursor: pointer;
  }
  .add-box {
    position: fixed;
    left: 55vw;
    top: 15vh;
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
  .add-box:hover {
    transform: translateY(-2px);
    background-color: transparent; /* hover时背景透明 */
    border: 2px solid #fff; /* 显示白色边框 */
    color: #fff; /* 字体保持白色 */
  }
  </style>
