<template>
  <main>
    <div class="container">
      <!-- Unread Notifications -->
      <div v-if="store.amountOfNotSeenNotifications > 0">
        <div class="notification-info">
          <div style="color: green">
            {{ store.amountOfNotSeenNotifications }} nieuwe melding(en)!
          </div>
        </div>
        <ul>
          <li v-for="notification in store.unreadNotifications" :key="notification.animalId">
            <router-link to="" class="d-flex mt-2">
              <div class="notification-container">
                <span class="material-symbols-outlined">notifications</span>
                <div class="notification-container-name">
                  {{ notification.description }}
                </div>
                <div v-if="removableActivated" class="removeContainer"
                  @click="removeNotification(notification.animalId)">
                  <span class="material-symbols-outlined">remove</span>
                </div>
                <div v-else></div>
              </div>
            </router-link>
          </li>
        </ul>
      </div>

      <h2 class="container" style="margin-top: 5%">
        Meldingen
      </h2>
      <!-- Read Notifications -->
      <div v-if="store.amountOfSeenNotifications > 0">
        <ul>
          <li v-for="notification in store.readNotifications" :key="notification.animalId">
            <router-link to="" class="d-flex mt-2">
              <div class="notification-container">
                <span class="material-symbols-outlined">notifications</span>
                <div class="notification-container-name">
                  {{ notification.description }}
                </div>
                <div v-if="removableActivated" class="removeContainer"
                  @click="removeNotification(notification.animalId)">
                  <span class="material-symbols-outlined">remove</span>
                </div>
                <div v-else></div>
              </div>
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </main>

  <footer>
    <div class="icon-container">
      <router-link to="/">
        <div @click="makeNotificationsSeen">
          <font-awesome-icon icon="fas fa-backward" size="xl" style="color: white" />
        </div>
      </router-link>

      <router-link to="">
        <div @click="addRemovableOptionToNotifications">
          <span v-if="!removableActivated" class="material-symbols-outlined" style="color: green">delete</span>
          <span v-else class="material-symbols-outlined" style="color: red">delete</span>
        </div>
      </router-link>
    </div>
  </footer>
</template>

<script>
import { useNotificationStore } from "@/stores/notificationStore.js";

export default {
  data() {
    const store = useNotificationStore();
    return {
      store,
      removableActivated: false
    };
  },
  async mounted() {
    await this.store.fetchNotifications();
  },
  methods: {
    makeNotificationsSeen() {
      this.store.updateAllTheNotificationsToRead();
      this.$router.push("/");
    },
    addRemovableOptionToNotifications() {
      if (this.store.amountOfNotSeenNotifications + this.store.amountOfSeenNotifications > 0) {
        this.removableActivated = !this.removableActivated;
      }
    },
    async removeNotification(animalId) {
      await this.store.deleteNotificationById(animalId);
      if (this.store.amountOfNotSeenNotifications + this.store.amountOfSeenNotifications === 0) {
        this.removableActivated = false;
      }
    }
  }
};
</script>

<style scoped>
.notification-container {
  display: flex;
  align-items: center;
  padding: 4px;
  width: 90%;
  margin: 0 auto;
  justify-content: space-evenly;

  color: white;
  background: #21150f;
  border-style: dashed;
  border-radius: 10px;
  border-width: thin;
  border-color: #9F6713;
}

.container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.notification-info {
  text-align: center;
}

main {
  height: 73%;
  overflow-y: auto;
  flex: 1 1 auto;
}

.icon-container {
  display: flex;
  justify-content: space-between;
  margin-left: 10%;
  margin-right: 10%;
}

.removeContainer {
  color: red;
}

.notification-container-name {
  margin-left: 8%;
}

ul {
  list-style: none;
}
</style>
