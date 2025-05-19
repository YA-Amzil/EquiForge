<template>
  <div class="bottom-navigation-button-container">

    <div>
      <router-link to="" @click="goBackToHorse">
        <font-awesome-icon class="fa-xl" icon="fas fa-backward" />
      </router-link>
    </div>

    <div>
      <router-link to="" @click="logout()">
        <font-awesome-icon class="fa-xl" :icon="['fas', 'right-from-bracket']" />
      </router-link>
    </div>

  </div>
</template>

<script>
import { useHorseStore } from "@/stores/horseStore.js";
import {FontAwesomeIcon} from "@fortawesome/vue-fontawesome";

export default {
  name: 'HomePageFooter',
  components: {FontAwesomeIcon},
  data() {
    const store = useHorseStore();
    return {
      store,
    }
  },
  methods: {
    goBackToHorse() {
      this.$router.push('/horsedetail/' + this.$route.params.horseId)
    },
    logout() {
      document.cookie = 'jsonwebtoken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
      this.$router.push({ name: 'login' });
    },
  }
}
</script>

<style scoped>

.bottom-navigation-button-container {
  background-color: #2B2B2B;
  padding: 2.5% 10%;
  display: flex;
  width: 100%;
  justify-content: space-between;
  align-items: center;
  font-variation-settings:
    'FILL' 1,
    'wght' 800,
    'GRAD' 20,
    'opsz' 24
}

.fa-xl {
  color: white;
}

</style>