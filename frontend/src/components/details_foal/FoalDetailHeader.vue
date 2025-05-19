<template>
  <v-row justify="center">

    <v-col cols="12" class="text-center">
      <header>
        <v-img :src="image" :height="150" class="mx-auto" />
        <h2>{{ name }}</h2>
      </header>
    </v-col>
  </v-row>
</template>

<script>
import image from "@/assets/LogoTinkerstal.png"

export default {
  name: "FoalDetailHeader",
  props: {
    name: String
  },
  data(){
    return{
      image: image
    }
  }
}
</script>

<style scoped>

</style>