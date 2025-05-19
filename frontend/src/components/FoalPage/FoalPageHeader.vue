<template>
  <div class="headerContainer">
    <v-img :src="image" alt="Logo Tinkerstal" class="mx-auto"></v-img>
    <h3>{{this.horseName}}</h3>
  </div>
</template>

<script>
import image from "@/assets/LogoTinkerstal.png"
import {useHorseStore} from "@/stores/horseStore.js";
export default {
  name: "HomePageHeader",
  data() {
    const store = useHorseStore()
    let hidePicture = false
    return {
      image: image,
      store,
      horseName: "",
      hidePicture
    };
  },
  async mounted(){
    this.horseName = (await this.store.fetchHorsesById(this.$route.params.horseId)).name;
  },
  computed: {
    isHidden(){
      return this.hidePicture
    }
  },
  methods: {
    hideThePicture(){
      this.hidePicture = true;
    },
    showThePicture(){
      this.hidePicture = false;
    }
  }
};
</script>

<style scoped>
.headerContainer {
  max-width: 100%;
  padding: 2em 0;
  border-bottom: 10px solid #160F0B;
  background: #21150F;
  text-align: center;
}
</style>