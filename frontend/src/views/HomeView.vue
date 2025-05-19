<template style="background: #181818">
  <main style="height: 100dvh; width: 100dvw">
    <component class="topHeader" :is="HomePageHeader"/>
    <component class="contentComponent" :is="currentComponent"/>
    <HomePageFooter class="bottomNavBar" @showNotifications="showNotifications" @showOverview = "showOverview"/>
  </main>
</template>

<script>

import HomePageFooter from "@/components/homepage/HomePageFooter.vue";
import HomePageHeader from "@/components/homepage/HomePageHeader.vue";
import HomePageOverview from "@/components/homepage/HomePageOverview.vue";
import HomePageNotifications from "@/components/homepage/HomePageNotifications.vue";

export default {
  name: 'HomeView',
  data() {
    return {
      HomePageOverview: HomePageOverview,
      HomePageHeader: HomePageHeader,
      HomePageFooter: HomePageFooter,
      HomePageNotifications: HomePageNotifications,
      currentComponent: HomePageOverview
    };
  },
  methods: {
    showNotifications() {
      this.currentComponent = HomePageNotifications
    },
    showOverview() {
      this.currentComponent = HomePageOverview
    }
  },
  components: {HomePageFooter}
}

</script>

<style scoped>
.topHeader {
  height: 20%;
  margin: 0;
}
.contentComponent {
  height: 70%;
}
.bottomNavBar {
  height: 10%;
}
</style>
