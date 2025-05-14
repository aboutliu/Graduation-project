<template>
    <div class="help-viewer">
        <div class="map-container" ref="map"></div>
        <button class="locate-btn" @click="locateMe">定位我</button>
        <input class="search-box" v-model="searchQuery" placeholder="🔍 搜索"/>
        <div class="sidebar-wrapper" @mouseover="showSidebar = true" @mouseleave="showSidebar = false">
            <div class="sidebar" v-show="showSidebar">
            <div class="sidebar-item" @click="goTo('/activity')">活动日历</div>
            <div class="sidebar-item" @click="goTo('/resource')">资源分享</div>
            <div class="sidebar-item" @click="goTo('/virtual')">虚拟空间</div>
            <div class="sidebar-item" @click="goTo('/help')">紧急求助</div>
            <div class="sidebar-item" @click="goTo('/success')">首页</div>
            </div>
        </div>
    </div>
  </template>
  
  <script>
  import L from 'leaflet';
  import 'leaflet/dist/leaflet.css';

  export default {
    name: 'MapView',
    data() {
      return {
        map: null,
        userMarker: null,
        showSidebar: false,
      };
    },
    mounted() {
      this.initMap();
    },
    methods: {
      goTo(path) {
        this.$router.push(path);
      },
      initMap() {
        const defaultCoords = [30.8201, 106.6089]; // fallback: school location
        this.map = L.map(this.$refs.map);
  
        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(
            (position) => {
              const lat = position.coords.latitude;
              const lng = position.coords.longitude;
              this.map.setView([lat, lng], 18);
              if (this.userMarker) {
                this.userMarker.setLatLng([lat, lng]).openPopup();
              } else {
                this.userMarker = L.circleMarker([lat, lng], {
                  radius: 6,
                  color: 'blue',
                  fillColor: 'blue',
                  fillOpacity: 0.8
                }).addTo(this.map).bindPopup('我在这里').openPopup();
              }
            },
            (error) => {
              console.warn('Geolocation failed, using default.', error);
              this.map.setView(defaultCoords, 18);
              if (this.userMarker) {
                this.userMarker.setLatLng(defaultCoords).openPopup();
              } else {
                this.userMarker = L.circleMarker(defaultCoords, {
                  radius: 6,
                  color: 'blue',
                  fillColor: 'blue',
                  fillOpacity: 0.8
                }).addTo(this.map).bindPopup('我在这里').openPopup();
              }
            }
          );
        } else {
          console.warn('Geolocation not supported, using default.');
          this.map.setView(defaultCoords, 18);
          if (this.userMarker) {
            this.userMarker.setLatLng(defaultCoords).openPopup();
          } else {
            this.userMarker = L.circleMarker(defaultCoords, {
              radius: 6,
              color: 'blue',
              fillColor: 'blue',
              fillOpacity: 0.8
            }).addTo(this.map).bindPopup('我在这里').openPopup();
          }
        }
  
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
      },
      locateMe() {
        if (this.map && navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(
            (position) => {
              const lat = position.coords.latitude;
              const lng = position.coords.longitude;
              this.map.setView([lat, lng], 18);
              if (this.userMarker) {
                this.userMarker.setLatLng([lat, lng]).openPopup();
              } else {
                this.userMarker = L.circleMarker([lat, lng], {
                  radius: 6,
                  color: 'blue',
                  fillColor: 'blue',
                  fillOpacity: 0.8
                }).addTo(this.map).bindPopup('我在这里').openPopup();
              }
            },
            (error) => {
              console.warn('Geolocation failed:', error);
            }
          );
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .help-viewer {
    /* border-radius: 1%; */
    /* width: 200%; */
    height: 100%;
    width: 1200px;
    background-image: url('images/help.png');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
    /* height: 778px;
    width: 837px; */
  }
  .map-container {
    height: 778px;
    width: 837px;
  }
  
  .locate-btn {
    position: absolute;
    top: 10px;
    left: 160px;
    z-index: 1000;
    padding: 8px 12px;
    background-color: white;
    border: 1px solid #ccc;
    border-radius: 4px;
    cursor: pointer;
  }
  
  html, body {
    margin: 0;
    padding: 0;
    height: 100%;
  }

  .leaflet-control-zoom {
    position: fixed;
    left: 100vw;
    top: auto !important;
    bottom: 100px !important;
    right: 10px !important;
    left: auto !important;
    display: flex;
    flex-direction: column;
    gap: 6px;
  }
  .sidebar-wrapper {
  /* background-color: white; */
  position: fixed;
  top: 10vh;
  left: 112px;
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

.search-box {
    position: fixed;
    width:20vw;
    left: 68vw;
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
  </style>