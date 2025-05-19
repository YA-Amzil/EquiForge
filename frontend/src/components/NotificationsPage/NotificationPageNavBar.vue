<template>
  <div class="icon-container">
    <router-link to>
      <div @click="makeNotificationsSeen">
        <font-awesome-icon icon="fas fa-backward" size="xl" style="color: white"/>
      </div>
    </router-link>

    <router-link to>
      <div @click="addRemovableOptionToNotifications">
        <span class="material-symbols-outlined">delete</span>
      </div>
    </router-link>
  </div>


</template>

<script>
import { useNotificationStore} from "@/stores/notificationStore.js";

export default {
  name: "NotificationPageNavBar",
  data() {
    const store = useNotificationStore();
    const removableActivated = false;
    return {
      store,
      removableActivated
    }
  },
  methods: {
    makeNotificationsSeen() {
      this.store.updateAllTheNotificationsToRead();
      setTimeout(() => {
        this.$router.push("/");
      }, 300)
    },
    addRemovableOptionToNotifications() {
      this.removableActivated = true;
    }
  }
}
</script>

<style scoped>

.icon-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 6%;
  margin-left: 10%;
  margin-right: 10%;
}

</style>