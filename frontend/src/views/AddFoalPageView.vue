<template>
  <div class="page">
    <header>
      <v-img :src="image" :height="136" style="border-top: 15px solid transparent" class="mx-auto image" />
      <h1 class="title">Veulen toevoegen</h1>
    </header>

    <main>
      <AddFoalPage class="content" />
    </main>
  </div>

</template>

<script>
import image from '@/assets/LogoTinkerstal.png'
import AddFoalPage from '@/components/addFoal/AddFoalPage.vue'


export default {
  name: 'AddFoalPageView',
  data() {
    const foalId = this.$route.params.id;
    return {
      foalId,
      image,
    }
  },
  components: { AddFoalPage }
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