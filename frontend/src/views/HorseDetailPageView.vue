<template>
  <main>
    <HorseDetailPage class="content" />
  </main>
  <footer>
    <HorseDetailNavbar class="bottomNavBar" :horse-id="this.$route.params.id" />
  </footer>
</template>

<script>
import HorseDetailPage from '@/components/details_horse/HorseDetailPage.vue'
import HorseDetailNavbar from '@/components/details_horse/HorseDetailNavbar.vue'
import image from '@/assets/LogoTinkerstal.png'

export default {
  name: 'HorseDetailPageView',
  data() {
    const horseId = this.$route.params.id;
    return {
      horseId: horseId,
      image: image,
    }
  },
  components: { HorseDetailNavbar, HorseDetailPage }
}
</script>

<style scoped>
.content {
  height: 90%;
}

.bottomNavBar {
  height: 10%;
}

header {
  flex: 0 1 auto;
}

main {
  flex: 1 1 auto;
}

footer {
  flex: 0 1 10%;
}
</style>