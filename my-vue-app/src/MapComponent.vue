<template>
    <div ref="map" style="height: 100%; width: 100%"></div>
  </template>
  
  <script>
  import L from 'leaflet';
  import 'leaflet/dist/leaflet.css';
  
  export default {
    mounted() {
      this.initMap();
    },
    methods: {
      initMap() {
        // 创建地图实例，设置中心点和缩放级别
        const map = L.map(this.$refs.map).setView([51.505, -0.09], 13);
  
        // 添加 OpenStreetMap 图层
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(map);
      },
    },
  };
  </script>
  
  <style scoped>
  /* 在这里设置地图容器的样式 */
  div {
    height: 400px; /* 可自定义高度 */
    width: 100%;   /* 宽度100% */
  }
  </style>