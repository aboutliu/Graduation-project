<template>
    <div class="Resource-viewer">
      <div class="sidebar-wrapper" @mouseover="showSidebar = true" @mouseleave="showSidebar = false">
        <div class="sidebar" v-show="showSidebar">
          <div class="sidebar-item" @click="goTo('/activity')">活动日历</div>
          <div class="sidebar-item" @click="goTo('/resource')">资源分享</div>
          <div class="sidebar-item" @click="goTo('/virtual')">虚拟空间</div>
          <div class="sidebar-item" @click="goTo('/help')">紧急求助</div>
          <div class="sidebar-item" @click="goTo('/success')">首页</div>
        </div>
      </div>
      <button  v-if="currentresources.length > 0" class="arrow-button left" @click="goToPrevResource">&#8592;</button>
    <input class="search-box" v-model="searchQuery" placeholder="🔍 搜索"/>
      <div class="Resource-container">
        <div class="Resource-block" v-for="(Resource, index) in currentresources" :key="index">
          <div class="Resource-info">
              <div class="Resource-image">
                  <img :src="'http://localhost:8080' + '/images/' + (Resource.image || 'Resource.png')" alt="活动图片" />
              </div>
            <p><strong>名称：</strong>{{ Resource.name }}</p>
            <p><strong>日期：</strong>{{ Resource.time }}</p>
            <p><strong>地点：</strong>{{ Resource.place }}</p>
            <p><strong>编辑人：</strong>{{ Resource.editor }}</p>
            <p><strong>说明：</strong>{{ Resource.introduction }}</p>
          </div>
        </div>
      </div>
  
      <button v-if="currentresources.length > 0" class="arrow-button right" @click="goToNextResource">&#8594;</button>
    </div>
  </template>
  
  <script setup>
  import { ref, computed, onMounted } from 'vue';
  import axios from 'axios';
  
  const resources = ref([]);
  const currentIndex = ref(0);
  const itemsPerPage = 3;
  
  const searchQuery = ref('');
  
  const currentresources = computed(() => {
    if (searchQuery.value.trim()) {
      return resources.value.filter(act =>
        act.name && act.name.includes(searchQuery.value)
      );
    }
    return resources.value.slice(currentIndex.value, currentIndex.value + itemsPerPage);
  });
  
  const goToPrevResource = () => {
    if (currentIndex.value > 0) {
      currentIndex.value -= itemsPerPage;
    }
  };
  
  const goToNextResource = () => {
    if (currentIndex.value + itemsPerPage < resources.value.length) {
      currentIndex.value += itemsPerPage;
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

  const showSidebar = ref(false);

  import { useRouter } from 'vue-router'

  const router = useRouter()
  const goTo = (path) => {
    router.push(path)
  }
  </script>
    
  <style scoped>
  .Resource-viewer {
    display: flex;
    align-items: center;
    /* justify-content: center; */
    gap: 1rem;
    min-height: 100vh;
    width: 86vw;
    padding: 2rem;
    font-family: Arial, sans-serif;

    background-image: url('images/resource.png');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;

    color: #fff; 
    backdrop-filter: brightness(0.9)
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
  
  .Resource-container {
    height: 60vh;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    gap: 2rem;
    padding: 2rem;
  }
   
  .Resource-block {
      width: 10vw;
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #f4f4f4;
    padding: 1.5rem;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    min-width: 300px;
  }
  .Resource-block:hover {
      transform: translateY(-10px);
  }
  
  .Resource-info {
    font-size: 16px;
    color: #333;
  }
  
  .Resource-image {
    width: 250px;
    display: flex;
    justify-content: center;
    margin-bottom: 1rem;
  }
  
  .Resource-image img {
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
      border: 2px solid transparent; /* 初始无边框 */
      outline: none;
    }
  .overlay-Resource {
    position: fixed;
    top: 10vh;
    left: 48vw;
    transform: translateX(-50%);
    z-index: 999;
    background: rgba(255, 255, 255, 0.95);
    padding: 1rem;
    border-radius: 12px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
  }
  .sidebar-wrapper {
  /* background-color: white; */
  position: fixed;
  top: 0vh;
  left: 0;
  width: 40px;
  height: 30vh;
  z-index: 999;
}

.sidebar {
  background-color: rgba(0, 0, 0, 0.8);
  /* background-color: white; */
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
  </style>