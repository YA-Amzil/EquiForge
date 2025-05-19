<template>
  <div class="headerContainer">
    <v-img :src="image" alt="Logo Tinkerstal" class="mx-auto"></v-img>
  </div>
</template>

<script>
import image from "@/assets/LogoTinkerstal2.png"
export default {
  name: "HomePageHeader",
  data() {
    return {
      image: image
    };
  }
};
</script>

<style scoped>
.headerContainer {
  border-bottom: 10px solid #160F0B;
  background: #21150F;
  max-width: 100%;
  padding: 1em 0;
}
</style>