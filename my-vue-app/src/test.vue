<template>
  <div class="TestContainer">
    <div class="Test" v-for="activity in activities" :key="activity.id">
      <div>
        <p><strong>名称:</strong> {{ activity.name }}</p>
        <p><strong>简介:</strong> {{ activity.introduction }}</p>
        <p><strong>时间:</strong> {{ activity.time }}</p>
        <p><strong>其他:</strong> {{ activity.others }}</p>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activities: []
    };
  },
  mounted() {
    fetch("http://localhost:8080/activities") // 确保这里的 API 地址正确
      .then(response => response.json())
      .then(data => {
        this.activities = data;
      })
      .catch(error => console.error("Error fetching activities:", error));
  }
};
</script>

<style>
body, html {
  margin: 0;
  padding: 0;
}

.TestContainer {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 50px;
  width: 85vw;
  position: fixed;
  left: 10vw;
  top: 20vh;
  height: calc(100vh - 20vh);
  overflow-y: auto;
  /* background-color: black; */
  scrollbar-width: none; /* Firefox */
  -ms-overflow-style: none; /* IE/Edge */
}

.TestContainer::-webkit-scrollbar {
  display: none; /* Chrome, Safari */
}

.Test {
    height: 20vh;
    width: 17vw;
    background-color: #C6E2FF;
    border-radius: 20px;
    box-shadow: 5px 5px 15px rgba(0, 0, 0, 0.3);
    display: flex;
    align-items: center;
    justify-content: center;
}

* {
  box-sizing: border-box;
}
</style>