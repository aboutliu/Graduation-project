<template>
  <div class="map-container" ref="map"></div>
  <button class="locate-btn" @click="locateMe">定位我</button>
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
    };
  },
  mounted() {
    this.initMap();
  },
  methods: {
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
</style>