<script>
export default {
  name: 'FoalDetailNavbar',
  props: {
    horseId: Number,
    routeBack: String
  },
}
</script>

<template>
  <div class="navbarContainer">

    <router-link :to="routeBack" >
      <font-awesome-icon icon="fas fa-backward" size="xl" />
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