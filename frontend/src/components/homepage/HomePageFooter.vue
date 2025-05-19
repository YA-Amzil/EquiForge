<template>
  <div class="bottom-navigation-button-container">

    <div>
      <router-link to="/home">
        <v-img class="horseNavigationImage" :src="homePagePicture" />
      </router-link>
    </div>

    <div>
      <router-link to="" @click="goToNotificationsPage()">
        <span class="material-symbols-outlined" v-if="notificationStore.amountOfNotSeenNotifications > 0" style="color: green">notification_important</span>
        <span class="material-symbols-outlined" v-else>notifications</span>
      </router-link>
    </div>

    <div>
      <router-link to="" @click="logout()">
        <span class="material-symbols-outlined">logout</span>
      </router-link>
    </div>

  </div>
</template>

<script>
import { useHorseStore } from "@/stores/horseStore.js";
import { useNotificationStore} from "@/stores/notificationStore.js";
import homePagePicture from "@/assets/NavHorseIcon.png"


export default {
  name: 'HomePageFooter',
  data() {
    const store = useHorseStore();
    const notificationStore = useNotificationStore();
    return {
      store,
      notificationStore,
      homePagePicture: homePagePicture
    }
  },
  async mounted() {
    await this.notificationStore.fetchUnreadNotificationAmount();
  },
  methods: {
    switchToOverview() {
      console.log("Je gaat switchen naar Overview");
      this.$emit('showOverview');
    },
    switchToNotifications() {
      console.log("Je gaat switchen naar Meldingen");
      this.$emit('showNotifications');
    },
    logout() {
      document.cookie = 'jsonwebtoken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;';
      this.$router.push({ name: 'login' });
    },
    goToNotificationsPage() {
      this.$router.push({name: 'NotificationView'});
    }
  }
}
</script>

<style scoped>
.horseNavigationImage {
  width: 25px;
  height: 25px;
}

.bottom-navigation-button-container {
  background-color: #2B2B2B;
  padding-left: 10%;
  padding-right: 10%;
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

.bottom-navigation-button-container .material-symbols-outlined {
  color: white;
}

.fa-xl{
  color: white;
}
</style>