<template>

    <v-row justify="center">
      <v-col cols="12" sm="6" class="text-center">
        <p>moeder: {{horsename}}</p>
        <p>leeftijd: {{calculateAge(this.foal.birthDate)}}</p>
      </v-col>

      <v-col cols="12" sm="6" class="text-center">
        <v-textarea class="v-textarea text" rows="6" v-model="schedule"
                    variant="outlined" flat hide-details></v-textarea>
      </v-col>
    </v-row>

  <footer>
    <v-btn class="footer-left" @click="deleteFoal" v-if="isAdmin">
      Delete
    </v-btn>

    <v-btn class="footer-middle" @click="updateFoal" v-if="isAdmin">
      Edit
    </v-btn>

    <router-link class="footer-right" to="" @click="addFoal" v-if="this.isAdmin">
      <font-awesome-icon class="fa-2xl" color="white" :icon="['fas', 'circle-plus']" />
    </router-link>
  </footer>

</template>

<script>
import {storeLogin} from "@/helpFunctions/Cookies.js";
import {calculateAge} from "@/helpFunctions/Age.js";
import {useFoalStore} from "@/stores/foalStore.js";

export default {
  name: "foalDetailPage",
  props: {
    foal: Object,
    horsename: String
  },
  data() {
    const isAdmin = storeLogin();
    const foalStore = useFoalStore();
    return {
      isAdmin,
      calculateAge,
      foalStore,
      schedule: ""
    }
  },
  async beforeMount() {
    this.schedule = await this.foalStore.fetchScheduleById(this.$route.params.foalId);
    console.log(this.schedule);
  },
  methods: {
    deleteFoal(){
      this.$emit("deleteFoal");
    },
    addFoal(){
      this.$emit("addFoal");
    },
    updateFoal(){
      this.$emit("updateFoal");
    },

  }
}
</script>

<style scoped>

footer{
  position: absolute;
  display: flex;
  flex-flow: row;
  bottom: 25%;
  width: 100%
}

.footer-left {
  position: absolute;
  left: 10%;
}

.footer-right {
  position: absolute;
  right: 10%;
}

.footer-middle {
  margin: auto;
}
</style>