<template>
    <div class="home-container">
        <!-- 提示框 -->
        <div v-if="notification.visible" class="notification-box">
            {{ notification.message }}
            <button class="close-btn" @click="notification.visible = false">×</button>
        </div>
        <!-- 导航栏 -->
        <div class="sidebar-wrapper" @mouseover="showSidebar = true" @mouseleave="showSidebar = false">
        <div class="sidebar" v-show="showSidebar">
          <div class="sidebar-item" @click="goTo('/activity')">活动日历</div>
          <div class="sidebar-item" @click="goTo('/resource')">资源分享</div>
          <div class="sidebar-item" @click="goTo('/virtual')">虚拟空间</div>
          <div class="sidebar-item" @click="goTo('/help')">紧急求助</div>
          <div class="sidebar-item" @click="goTo('/success')">首页</div>
        </div>
      </div>
  
      <div class="resource-box" >我的资源</div>
      <div class="share-box" @click="goTo('/personalactivity')">我的活动</div>
      <div class="help-box" @click="goTo('/personalhelp')">我的求助</div>
      <div class="talk-box" @click="goTo('/personalvirtual')">我的偶像</div>
      <div class="add-box" @click="showAddForm ? closeAddForm() : (showAddForm = true)">{{ showAddForm ? '关闭' : '添加' }}</div>
      <!-- <div class="share-box" @click="goTo('/resource')">资源分享</div>
      <div class="resource-box" @click="goTo('/resource')">活动日历</div> -->
      <div class="resource-viewer">
            <button v-if="currentresources.length > 0" class="arrow-button left" @click="goToPrevresource">&#8592;</button>
            <input class="search-box" v-model="searchQuery" placeholder="🔍 搜索"/>
            <div class="resource-container">
                <div class="resource-block" v-for="(resource, index) in currentresources" :key="index">
                    <button class="delete-button" @click="deleteresource(resource.id)">×</button>
                    <div class="resource-info">
                        <div class="resource-image">
                            <img :src="'http://localhost:8080' + '/images/' + (resource.image || 'resource.png')" alt="活动图片" />
                        </div>
                        <p><strong>名称：</strong>{{ resource.name }}</p>
                        <p><strong>日期：</strong>{{ resource.time }}</p>
                        <p><strong>类别：</strong>{{ resource.category }}</p>
                        <p><strong>编辑人：</strong>{{ resource.editor }}</p>
                        <p><strong>说明：</strong>{{ resource.introduction }}</p>
                    </div>
                </div>
            </div>
            <button v-if="currentresources.length > 0" class="arrow-button right" @click="goToNextresource">&#8594;</button>
      </div>

      <div v-if="showAddForm">
        <div v-if="showAddForm" class="resource-container" style="z-index: 999;">
  <div class="new-add-resource-block">
    <button class="upload-image" @click="imageInput.click()">上传图片</button>
    <input type="file" ref="imageInput" style="display:none" @change="handleImageUpload" />
    <div class="resource-info">
      <div class="resource-image">
        <img :src="'http://localhost:8080/images/' + (newresource.image || 'resource.png')" alt="上传图片" />
      </div>
      <input v-model="newresource.name" placeholder="名称" />
      <input v-model="newresource.time" placeholder="时间" />
      <input v-model="newresource.category" placeholder="类别" />
      <input v-model="newresource.editor" placeholder="编辑人" />
      <textarea v-model="newresource.introduction" placeholder="说明"></textarea>
      <button @click="addresource">添加</button>
    </div>
  </div>
</div>
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

// Notification ref and function
const notification = ref({ message: '', visible: false });

const showNotification = (msg) => {
  notification.value.message = msg;
  notification.value.visible = true;
  setTimeout(() => {
    notification.value.visible = false;
  }, 3000); // 自动关闭
};


const showSidebar = ref(false);
const showresources = ref(false);
const showAddForm = ref(false);

const resources = ref([]);
const currentIndex = ref(0);
const itemsPerPage = 3;

const searchQuery = ref('');
const userId = localStorage.getItem('userId') || '未登录';

const currentresources = computed(() => {
  const filtered = resources.value.filter(
    act => act.idname && act.idname.includes(userId)
  );

  const searched = searchQuery.value.trim()
  ? filtered.filter(act =>
      (act.name && act.name.toLowerCase().includes(searchQuery.value.toLowerCase())) ||
      (act.editor && act.editor.toLowerCase().includes(searchQuery.value.toLowerCase())) ||
      (act.introduction && act.introduction.toLowerCase().includes(searchQuery.value.toLowerCase()))
    )
  : filtered;

  return searched.slice(currentIndex.value, currentIndex.value + itemsPerPage);
});

const goToPrevresource = () => {
  if (currentIndex.value > 0) {
    currentIndex.value -= itemsPerPage;
  }
};

const goToNextresource = () => {
  const filtered = resources.value.filter(
    act => act.idname && act.idname.includes(userId)
  );
  if (currentIndex.value + itemsPerPage < filtered.length) {
    currentIndex.value += itemsPerPage;
  }
};

const deleteresource = async (id) => {
  try {
    // 找到对应活动
    const resource = resources.value.find(act => act.id === id);
    if (!resource) return;

    // 先删除活动
    await axios.delete(`http://localhost:8080/resources/${id}`);

    // 如果该活动有图片，发请求删除图片
    if (resource.image) {
      await axios.delete(`http://localhost:8080/deleteImage/${resource.image}`);
    }

    // 重新拉取活动列表，刷新视图
const response = await axios.get("http://localhost:8080/resources");
resources.value = response.data;

// 获取筛选后的活动总数
const filtered = resources.value.filter(
  act => act.idname && act.idname.includes(userId)
);

// 如果 currentIndex 太大（比如超出了页数），就回退一页
if (currentIndex.value >= filtered.length && currentIndex.value > 0) {
  currentIndex.value = Math.max(currentIndex.value - itemsPerPage, 0);
}
  } catch (error) {
    console.error("Failed to delete resource or image:", error);
  }
};

onMounted(async () => {
  try {
    const response = await axios.get("http://localhost:8080/resources");
    resources.value = response.data;
  } catch (error) {
    console.error("Failed to fetch resources:", error);
  }
});

import { useRouter } from 'vue-router'

const router = useRouter()
const goTo = (path) => {
  router.push(path)
}

// 新增活动相关
const newresource = ref({
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
//   alert('in');
  if (!file) return;

  const formData = new FormData();
  const filename = userId + newresource.value.name + '.' + file.name.split('.').pop();
//   alert(newresource.value.name);
  formData.append('file', file, filename);
  
  try {
    await axios.post('http://localhost:8080/resource/uploadImage', formData);
    // alert('try');
    uploadedImageName = filename;
    // alert('上传成功');
    showNotification('上传成功');
  } catch (error) {
    console.error('上传失败', error);
    // alert('上传失败');
    showNotification('上传失败');
  }
};

const addresource = async () => {
  const { name, time, category, editor, introduction } = newresource.value;
  if (!name || !time || !category || !editor || !introduction) {
    // alert('请填写所有必填字段');
    showNotification('请填写所有必填字段');
    return;
  }

  const resourceToAdd = {
    name,
    time,
    category,
    editor,
    introduction,
    image: uploadedImageName || '',
    idname: userId + name,
  };

  try {
    await axios.post('http://localhost:8080/resources', resourceToAdd);

    // 添加成功后重新拉取活动列表（这样才有 id）
    const response = await axios.get("http://localhost:8080/resources");
    resources.value = response.data;
    // alert('添加成功');
    showNotification('添加成功');
    Object.assign(newresource.value, {
      name: '', time: '', category: '', editor: '', introduction: '', image: ''
    });
    uploadedImageName = '';
  } catch (error) {
    Object.assign(newresource.value, {
      name: '', time: '', category: '', editor: '', introduction: '', image: ''
    });
    uploadedImageName = '';
    console.error('添加失败', error);
    // alert('添加失败');
    showNotification('添加失败');
  }
};

const closeAddForm = () => {
  showAddForm.value = false;
  Object.assign(newresource.value, {
    name: '', time: '', editor: '', category: '', introduction: '', image: ''
  });
  uploadedImageName = '';
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
  
  .resource-box {
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
  .resource-box:hover {
    transform: translateY(-2px);
    background-color: transparent; /* hover时背景透明 */
    border: 2px solid #fff; /* 显示白色边框 */
    color: #fff; /* 字体保持白色 */
  }
  .help-box {
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
  .help-box:hover {
    transform: translateY(-2px);
    background-color: transparent; /* hover时背景透明 */
    border: 2px solid #fff; /* 显示白色边框 */
    color: #fff; /* 字体保持白色 */
  }
  .share-box {
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
  .share-box:hover {
    transform: translateY(-2px);
    background-color: transparent; /* hover时背景透明 */
    border: 2px solid #fff; /* 显示白色边框 */
    color: #fff; /* 字体保持白色 */
  }
  .talk-box {
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
  .talk-box:hover {
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
.resource-viewer {
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

.resource-container {
  height: 60vh;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  gap: 2rem;
  padding: 2rem;
}
 
.resource-block {
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
.resource-block:hover {
    transform: translateY(-10px);
}

.resource-info {
  font-size: 16px;
  color: #333;
}

.resource-image {
  width: 250px;
  display: flex;
  justify-content: center;
  margin-bottom: 1rem;
}

.resource-image img {
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
    /* color: #fff; */
    color:black;
    background-color: #f4f4f4;
    padding: 8px 16px;
    border-radius: 7px;
    display: inline-block;
    font-size: 16px;
    font-weight: bold;
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent; 
    outline: none;
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

.add-resource-form {
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
  .new-resource-block input,
  .new-resource-block textarea {
    width: 100%;
    margin: 4px 0;
    padding: 6px;
    border-radius: 5px;
    border: 1px solid #ccc;
  }

  .new-resource-block button {
    margin-top: 10px;
    background-color: #007bff;
    color: white;
    border: none;
    padding: 6px 12px;
    border-radius: 5px;
    cursor: pointer;
  }

  .new-resource-block button:hover {
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
  .new-add-resource-block {

    width: 10vw;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #f4f4f4;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  min-width: 300px;
  position: fixed;
  left: 32vw;
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
  .sidebar-wrapper {
  position: fixed;
  top: 0vh;
  left: 0;
  width: 40px;
  height: 30vh;
  z-index: 999;
}

.sidebar {
  background-color: rgba(0, 0, 0, 0.8);
  color: white;
  width: 140px;
  height: 30vh;
  border-top-right-radius: 10px;
  border-bottom-right-radius: 10px;
  padding: 1rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  transition: all 0.3s ease;
}

.sidebar-item {
  padding: 8px;
  margin-bottom: 6px;
  background-color: #333;
  border-radius: 5px;
  text-align: center;
  cursor: pointer;
  font-weight: bold;
  font-size: 14px;
}

.sidebar-item:hover {
  background-color: #555;
}

.notification-box {
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background-color: #323232;
    color: #fff;
    padding: 12px 20px;
    border-radius: 6px;
    box-shadow: 0 2px 10px rgba(0,0,0,0.2);
    font-size: 14px;
    z-index: 1000;
  }

  .notification-box .close-btn {
    background: none;
    border: none;
    color: white;
    font-size: 16px;
    margin-left: 12px;
    cursor: pointer;
  }
  </style>
