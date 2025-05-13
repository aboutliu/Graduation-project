<template>
    <div
      class="rounded-2xl shadow-lg p-6 cursor-pointer transition hover:shadow-2xl bg-white"
      @click="$emit('click')"
    >
      <div class="text-4xl mb-3">{{ icon }}</div>
      <h2 class="text-xl font-semibold mb-2">{{ title }}</h2>
      <p class="text-gray-600 text-sm">{{ description }}</p>
    </div>
  </template>
  
  <script setup>
  defineProps(['title', 'description', 'icon'])
  defineEmits(['click'])
  </script>