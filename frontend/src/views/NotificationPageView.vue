<template>
  <main style="height: 100dvh; width: 100dvw">
    <component class="topHeader" :is="HomePageHeader"/>
    <component class="contentComponent" :is="NotificationPage"/>
  </main>
</template>

<script>

import NotificationPage from '@/components/NotificationsPage/NotificationPage.vue';
import HomePageHeader from "@/components/homepage/HomePageHeader.vue";

export default {
  name: "NotificationPageView",
  data() {
    return {
      HomePageHeader: HomePageHeader,
      NotificationPage: NotificationPage
    };
  },
};
</script>

<style scoped>
  .topHeader {
  height: 20%;
  margin: 0;
}
  .contentComponent {
  height: 70%;
}
</style>