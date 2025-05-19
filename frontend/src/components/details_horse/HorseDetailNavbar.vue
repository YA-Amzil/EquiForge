<script>
export default {
  name: 'HorseDetailNavbar',
  props: {
    horseId: { required: true, type: Number }
  },
  methods: {
    backToHome() {
      console.log('Current path:', this.$route.params.horseId)
    }
  }
}
</script>

<template>
  <div class="navbarContainer">
    <router-link to="/">
      <font-awesome-icon icon="fas fa-backward" size="xl" />
    </router-link>

    <router-link :to="{path: '/horsedetail/' + this.$route.params.horseId}">
      <font-awesome-icon icon="fas fa-info" size="xl" />
    </router-link>

    <router-link :to="{path: '/horsedetail/' + this.$route.params.horseId + '/diaries'}">
      <font-awesome-icon icon="fas fa-sticky-note" size="xl" />
    </router-link>

    <router-link :to="{path: '/horsedetail/' + this.$route.params.horseId + '/foals'}">
      <font-awesome-icon icon="fas fa-horse-head" size="xl" />
    </router-link>
  </div>
</template>

<style scoped>
.navbarContainer {
  background: #2B2B2B;
  width: 100%;
  display: flex;
  justify-content: center;
}

.navbarContainer * {
  position: relative;
  margin: 5px 8%;
  color: white;
}
</style>