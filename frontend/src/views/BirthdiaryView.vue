<template>
  <div class="diaryPage">
    <header>
      <v-img :src="image" :height="150" class="mx-auto logo" />
      <h1 v-if="horse">{{ this.horse.name }}</h1>
    </header>
    <main>
      <BirthdiaryPage />
    </main>
    <footer>
      <HorseDetailNavbar :horse-id="this.horseId" />
    </footer>
  </div>
</template>

<script>
import { useHorseStore } from '@/stores/horseStore.js'
import BirthdiaryPage from '@/components/BirthdiaryPage.vue'
import image from '@/assets/LogoTinkerstal.png'
import HorseDetailNavbar from '@/components/details_horse/HorseDetailNavbar.vue'

export default {
  name: 'BirthdiaryView',
  components: { HorseDetailNavbar, BirthdiaryPage },
  data() {
    const store = useHorseStore()
    const id = this.$route.params.horseId;
    return {
      image: image,
      horse: null,
      horseId: id,
      store
    }
  },
  async beforeMount() {
    this.horse = await this.store.fetchHorsesById(this.horseId);
  },
  computed: {
    BirthdiaryPage() {
      return BirthdiaryPage
    }
  }
}
</script>

<style scoped>
.diaryPage {
  display: flex;
  flex-flow: column;
  text-align: center;
  height: 100%;
}

header {
  flex: 0 1 auto;
  padding-bottom: 3vh;
}

.logo {
  width: 50%;
  height: auto;
}

main {
  flex: 1 1 auto;
  overflow: auto;
}

footer {
  flex: 0 1 10%;
  background: #2B2B2B;
}
</style>
