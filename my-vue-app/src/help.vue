<template>
    <div class="help-viewer">
        <!-- 地图 -->
        <div class="map-container" ref="map"></div>

        <!-- 定位器 -->
        <button class="locate-btn" @click="locateMe">定位我</button>

        <!-- 添加按钮和搜索框 -->
        <button class="add-btn" @click="startAddCard" v-if="!isAdding">添加</button>
        <input class="search-box" v-model="searchQuery" placeholder="🔍 搜索"/>

        <!-- 添加卡片表单 -->
        <div v-if="isAdding" class="add-card-form">
          <label>
            名称:
            <input v-model="newCard.name" type="text" />
          </label>
          <label>
            日期:
            <input v-model="newCard.time" type="text" />
          </label>
          <label>
            地点:
            <input v-model="newCard.place" type="text" />
          </label>
          <label>
            编辑人:
            <input v-model="newCard.phone" type="text" />
          </label>
          <label>
            说明:
            <input v-model="newCard.introduction" type="text" />
          </label>
          <label>
            图片:
            <input type="file" @change="handleImageUpload" />
          </label>
          <label>
            经度:
            <input v-model="newCard.longitude" type="text" />
          </label>
          <label>
            纬度:
            <input v-model="newCard.latitude" type="text" />
          </label>
          <button @click="confirmAddCard">确认</button>
          <button @click="cancelAddCard">取消</button>
        </div>

        <!-- 导航栏 -->
        <div class="sidebar-wrapper" @mouseover="showSidebar = true" @mouseleave="showSidebar = false">
            <div class="sidebar" v-show="showSidebar">
            <div class="sidebar-item" @click="goTo('/activity')">活动日历</div>
            <div class="sidebar-item" @click="goTo('/resource')">资源分享</div>
            <div class="sidebar-item" @click="goTo('/virtual')">社区榜样</div>
            <div class="sidebar-item" @click="goTo('/help')">紧急求助</div>
            <div class="sidebar-item" @click="goTo('/success')">首页</div>
            </div>
        </div>

        <!-- 展示卡片：轮播三张卡片，中间一张居中，两侧部分遮挡，可点击切换 -->
        <div 
          class="activity-carousel-wrapper" 
          v-if="helpCards.length > 0"
        >
          <!-- <div class="nav-zone left-zone" @click="prevCard">&#9664;</div> -->
          <div class="left" @click="prevCard">&#9664;</div>
          <div 
            v-for="(card, index) in threeCardSet" 
            :key="index" 
            :class="['activity-card', getCardClass(index)]"
            @click="handleCardClick(card, index)"
          >
            <div class="activity-image">
              <img :src="'http://localhost:8080/images/' + (card.image || 'help.png')" alt="活动图片" />
            </div>
            <p><strong>名称：</strong>{{ card.name }}</p>
            <p><strong>日期：</strong>{{ card.time }}</p>
            <p><strong>地点：</strong>{{ card.place }}</p>
            <p><strong>编辑人：</strong>{{ card.phone }}</p>
            <p><strong>说明：</strong>{{ card.introduction }}</p>
          </div>
          <div class="nav-zone right-zone" @click="nextCard">&#9654;</div>
        </div>

      <!-- more -->
    </div>
  </template>
  
<script>
import L from 'leaflet';
import 'leaflet/dist/leaflet.css';
import 'leaflet-routing-machine';
import 'leaflet-routing-machine/dist/leaflet-routing-machine.css';
import axios from 'axios';


export default {
  name: 'MapView',
  data() {
    return {
      map: null,
      userMarker: null,
      showSidebar: false,
      helpCards: [],
      currentIndex: 0,
      helpMarkers: [],
      routingControl: null,  // 新增路线控件引用
      userCoords: null,      // 用户当前坐标
      isAdding: false,
      newCard: {
        name: '',
        time: '',
        place: '',
        phone: '',
        introduction: '',
        longitude: '',
        latitude: '',
        image: null,
      },
      userId: localStorage.getItem('userId') || '未登录',
      searchQuery: '',
    };
  },
  computed: {
    filteredCards() {
      if (!this.searchQuery) return this.helpCards;
      return this.helpCards.filter(card =>
        card.name && card.name.includes(this.searchQuery)
      );
    },
    threeCardSet() {
      if (this.filteredCards.length === 0) return [];
      const prev = this.filteredCards[this.currentIndex - 1] || {};
      const current = this.filteredCards[this.currentIndex];
      const next = this.filteredCards[this.currentIndex + 1] || {};
      return [prev, current, next];
    },
  },
  mounted() {
    this.initMap();
    this.loadHelpCard();
  },
  methods: {
    goTo(path) {
      this.$router.push(path);
    },
    async loadHelpCard() {
      try {
        const response = await axios.get('http://localhost:8080/help');
        if (response.data && response.data.length > 0) {
          this.helpCards = response.data;
          this.addHelpMarkers();
        }
      } catch (error) {
        console.error('加载帮助卡片失败:', error);
      }
    },
    nextCard() {
      if (this.currentIndex < this.filteredCards.length - 1) {
        this.currentIndex++;
      }
    },
    prevCard() {
      if (this.currentIndex > 0) {
        this.currentIndex--;
      }
    },
    goToIndex(index) {
      if (index === 0 && this.currentIndex > 0) {
        this.currentIndex--;
      } else if (index === 2 && this.currentIndex < this.helpCards.length - 1) {
        this.currentIndex++;
      }
    },
    handleCardClick(card, index) {
      if (index === 1 && card.latitude && card.longitude && this.map) {
        const targetCoords = [card.latitude, card.longitude];
        this.map.setView(targetCoords, 15);

        // 移除之前的路线
        if (this.routingControl) {
          this.map.removeControl(this.routingControl);
        }

        if (!this.userCoords) {
          alert('无法获取你的当前位置，无法规划路径');
          return;
        }

        this.routingControl = L.Routing.control({
          waypoints: [
            L.latLng(this.userCoords[0], this.userCoords[1]),
            L.latLng(targetCoords[0], targetCoords[1])
          ],
          routeWhileDragging: false,
          showAlternatives: false,
          createMarker: (i, wp) => {
            return L.circleMarker(wp.latLng, {
              radius: 6,
              color: i === 0 ? 'blue' : 'red',
              fillColor: i === 0 ? 'blue' : 'red',
              fillOpacity: 0.8
            });
          }
        }).addTo(this.map);

      } else {
        this.goToIndex(index);
      }
    },
    getCardClass(index) {
      if (index === 0) return 'left-card';
      if (index === 1) return 'center-card';
      if (index === 2) return 'right-card';
      return '';
    },
    initMap() {
      const defaultCoords = [30.8201, 106.6089];
      this.map = L.map(this.$refs.map);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);

      if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(
          (pos) => {
            this.userCoords = [pos.coords.latitude, pos.coords.longitude];
            this.map.setView(this.userCoords, 15);

            if (this.userMarker) {
              this.userMarker.setLatLng(this.userCoords).openPopup();
            } else {
              this.userMarker = L.circleMarker(this.userCoords, {
                radius: 6,
                color: 'blue',
                fillColor: 'blue',
                fillOpacity: 0.8,
              }).addTo(this.map).bindPopup('我在这里').openPopup();
            }
          },
          () => {
            this.userCoords = defaultCoords;
            this.map.setView(defaultCoords, 15);
          }
        );
      } else {
        this.userCoords = defaultCoords;
        this.map.setView(defaultCoords, 15);
      }
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
    addHelpMarkers() {
      if (!this.map || !this.helpCards.length) return;
      this.helpCards.forEach(card => {
        if (card.latitude && card.longitude) {
          const marker = L.circleMarker([card.latitude, card.longitude], {
            radius: 6,
            color: 'red',
            fillColor: 'red',
            fillOpacity: 0.8
          }).addTo(this.map).bindPopup(card.name || '帮助标记');
          this.helpMarkers.push(marker);
        }
      });
    },
    confirmAddCard() {
      // 如果 longitude 和 latitude 为空，取当前用户坐标
      let longitude = this.newCard.longitude;
      let latitude = this.newCard.latitude;
      if (!longitude || !latitude) {
        if (this.userCoords) {
          longitude = this.userCoords[1];
          latitude = this.userCoords[0];
        } else {
          longitude = '';
          latitude = '';
        }
      }

      if (!this.newCard.introduction) {
        alert('名称不能为空');
        return;
      }

      if (!this.newCard.imageFile) {
        alert('请上传图片');
        return;
      }

      const file = this.newCard.imageFile;
      const extension = file.name.split('.').pop();
      const filename = this.userId + this.newCard.introduction + '.' + extension;

      const formData = new FormData();
      formData.append('file', file, filename);
      formData.append('filename', filename);  // 如果后端需要

      const cardData = {
        name: this.newCard.name,
        time: this.newCard.time,
        place: this.newCard.place,
        phone: this.newCard.phone,
        introduction: this.newCard.introduction,
        longitude,
        latitude,
        idname: this.userId + this.newCard.name,
        image: filename
      };

      axios.post('http://localhost:8080/help/uploadImage', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      })
      .then(() => {
        return axios.post('http://localhost:8080/help', cardData);
      })
      .then(() => {
        this.helpCards.splice(this.currentIndex + 1, 0, {
          ...this.newCard,
          longitude,
          latitude,
          idname: this.userId + this.newCard.name,
          image: filename
        });
        this.isAdding = false;
        this.newCard = {
          name: '',
          time: '',
          place: '',
          phone: '',
          introduction: '',
          longitude: '',
          latitude: '',
          imageFile: null
        };
      })
      .catch(err => {
        console.error('添加卡片失败', err);
        alert('添加失败，请稍后重试!!!');
      });
    },
    handleImageUpload(event) {
      const file = event.target.files[0];
      if (file) {
        this.newCard.imageFile = file;
      }
    },
    cancelAddCard() {
      this.newCard = {
        name: '',
        time: '',
        place: '',
        phone: '',
        introduction: '',
        longitude: '',
        latitude: '',
        imageFile: null
      };
      this.isAdding = false;
    },
    startAddCard() {
      this.isAdding = true;
      if (this.userCoords) {
        this.newCard.longitude = this.userCoords[1];
        this.newCard.latitude = this.userCoords[0];
      } else {
        this.newCard.longitude = '';
        this.newCard.latitude = '';
      }
    }
  },
};
</script>
  
  <style scoped>
  .help-viewer {
    /* border-radius: 1%; */
    /* width: 200%; */
    height: 778px;
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
    /* left: 69vw; */
    right: 130px;
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

.add-btn {
    position: fixed;
    height:40px;
    top: 116px;
    right: calc(130px + 20vw + 10px);
    background-color:white;
    color: grey;
    border: none;
    padding: 8px 16px;
    border-radius: 7px;
    font-weight: bold;
    cursor: pointer;
    transition: background-color 0.3s ease;
}

.add-btn:hover {
    /* background-color: #45a049; */
}

.add-card-form {
  position: fixed;
  top: 20vh;
  right: 130px;
  background: white;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0,0,0,0.3);
  z-index: 1300;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.add-card-form label {
  font-weight: bold;
  display: flex;
  flex-direction: column;
  font-size: 14px;
}

.add-card-form input {
  padding: 6px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-size: 14px;
}

.add-card-form button {
  padding: 8px 12px;
  border-radius: 5px;
  border: none;
  font-weight: bold;
  cursor: pointer;
}

.add-card-form button:first-of-type {
  background-color: #4CAF50;
  color: white;
  margin-right: 10px;
}

.add-card-form button:last-of-type {
  background-color: #f44336;
  color: white;
}

  .activity-block {
    /* z-index: 1200; */
    width:20vw;
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

  .activity-block-wrapper {
    /* 已被轮播卡片替换 */
    display: none;
  }

  .activity-carousel-wrapper {
    position: absolute;
    top: 200px;
    right: 80px;
    z-index: 1200;
    display: flex;
    gap: 20px;
    width: 40vw;
    justify-content: center;
    align-items: center;
    overflow: visible;
  }

  .activity-card {
    width: 300px;
    height: auto;
    background-color: #f4f4f4;
    padding: 1.2rem;
    border-radius: 12px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.3s ease;
    cursor: pointer;
    z-index: 1;
    opacity: 1;
    transform: scale(1);
  }

  .nav-zone {
    width: 20px;
    height: 100%;
    cursor: pointer;
    opacity: 0;
    background-color: rgba(0, 0, 0, 0.1);
    transition: background-color 0.2s ease, opacity 0.2s;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    color: white;
    font-weight: bold;
  }

  .nav-zone:hover {
    opacity: 1;
    background-color: rgba(0, 0, 0, 0.3);
  }

  .left-zone {
    /* position: absolute;
    left: -20px;
    top: 0; */
    position: fixed;
    right: 20vw;
  }

  .right-zone {
    position: absolute;
    right: -20px;
    top: 0;
  }
  .left {
    width: 20px;
    height: 100%;
    cursor: pointer;
    opacity: 0;
    background-color: rgba(0, 0, 0, 0.1);
    transition: background-color 0.2s ease, opacity 0.2s;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 20px;
    color: white;
    font-weight: bold;
    position: fixed;
    left:200vw;
    top:10vh;
  }

  .left-card {
    transform: translateX(340px);
    opacity: 0.9;
    z-index: 0;
  }

  .left-card:hover {
    transform: translateX(330px);
  }

  .center-card {
    transform: translateX(57px);
    opacity: 1;
    z-index: 1;
  }

  .right-card {
    transform: translateX(-227px);
    opacity: 0.9;
    z-index: 0;
    /* transform-origin: bottom right;  设置旋转中心点为右下角 */
    /* transform-origin: 40px 40px; 距离左边缘20px、上边缘40px处 */
  }
  .right-card:hover {
    transform: translateX(-221px);
    /* transform: rotate(30deg); 顺时针旋转30度 */
  }
  </style>