<template style="background: #181818">
  <main style="height: 100dvh; width: 100dvw">
    <FoalPageHeader class="topHeader" @hide-picture="console.log('emit received')"/>
    <component class="contentComponent" :is="Content"/>
    <component class="bottomNavbar" :is="Footer"/>
  </main>
</template>

<script>

import FoalPage from "@/components/FoalPage/FoalPage.vue"
import FoalPageHeader from "@/components/FoalPage/FoalPageHeader.vue";
import FoalPageFooter from "@/components/FoalPage/FoalPageFooter.vue";


export default {
  name: 'FoalPageView',
  components: {
    FoalPageHeader
  },
  data() {
    return {
      Page: FoalPage,
      Content: FoalPage,
      Footer: FoalPageFooter
    }
  },
}
</script>

<style scoped>
.topHeader {
  height: 25%;
  top: 0;
}
.contentComponent {
  height: 58%;
}
.bottomNavbar {
  height: 15%;
  position: fixed;
  bottom: 0;
}
</style>