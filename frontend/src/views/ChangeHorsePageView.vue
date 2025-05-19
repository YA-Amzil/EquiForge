<template>
  <div class="page">
    <header>
      <v-img :src="image" :height="136" style="border-top: 15px solid transparent" class="mx-auto image" />
      <h1 class="title">Gegevens wijzigen</h1>
    </header>
    <main>
      <ChangeHorsePage />
    </main>
  </div>
</template>

<script>

import ChangeHorsePage from "@/components/change_horse/ChangeHorsePage.vue";
import image from '@/assets/LogoTinkerstal.png'

export default {
  name: "ChangeHorsePageView",
  components: {ChangeHorsePage},
  data() {
    return {
      image: image
    }
  },
}
</script>

<style scoped>
.page {
  display: flex;
  flex-flow: column;
  width: 100dvw;
  height: 100%;
}

header {
  background: #39251A;
  flex: 0 1 auto;
}

.title {
  text-align: center;
}

main {
  flex: 1 1 auto;
}
</style>
