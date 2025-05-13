<template>
  <div class="activity-viewer">
    <button class="arrow-button left" @click="goToPrevActivity">&#8592;</button>
  <input class="search-box" v-model="searchQuery" placeholder="🔍 搜索"/>
    <div class="activity-container">
      <div class="activity-block" v-for="(activity, index) in currentActivities" :key="index">
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

    <button class="arrow-button right" @click="goToNextActivity">&#8594;</button>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';

const activities = ref([]);
const currentIndex = ref(0);
const itemsPerPage = 3;

const searchQuery = ref('');

const currentActivities = computed(() => {
  if (searchQuery.value.trim()) {
    return activities.value.filter(act =>
      act.name && act.name.includes(searchQuery.value)
    );
  }
  return activities.value.slice(currentIndex.value, currentIndex.value + itemsPerPage);
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

onMounted(async () => {
  try {
    const response = await axios.get("http://localhost:8080/activities");
    activities.value = response.data;
  } catch (error) {
    console.error("Failed to fetch activities:", error);
  }
});
</script>
  
<style scoped>
.activity-viewer {
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
    border: 2px solid transparent; /* 初始无边框 */
  }
.overlay-activity {
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
</style>