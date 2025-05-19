
<template style="background: #181818">
  <body>

  <header>
    <component class="topHeader" :is="Header" :name="foal.name"/>
  </header>

  <main>
    <component class="contentComponent" @deleteFoal="deleteFoal" @addFoal="addFoal" @updateFoal="updateFoal" :is="Content" :foal="foal" :horsename="this.horse.name"/>
  </main>

  <footer>
    <component class="bottomNavBar" :is="Footer" :horseId="horseId" :routeBack="this.routeBack" />
  </footer>

  </body>

</template>

<script>
import FoalDetailPage from "@/components/details_foal/FoalDetailPage.vue";
import FoalDetailNavbar from "@/components/details_foal/FoalDetailNavbar.vue";
import FoalDetailHeader from "@/components/details_foal/FoalDetailHeader.vue";
import {useFoalStore} from "@/stores/foalStore.js";
import {useHorseStore} from "@/stores/horseStore.js";

export default {
  name: 'FoalDetailPageView',
  data() {
    const foalStore = useFoalStore();
    const horseStore = useHorseStore();
    const horseId = this.$route.params.horseId;
    const foalId = this.$route.params.foalId;
    let routeBack = '/horsedetail/' + horseId + '/foals/';
    return {
      Header: FoalDetailHeader,
      Footer: FoalDetailNavbar,
      Content: FoalDetailPage,
      foals: 0,
      foal: {},
      horse: {},
      routeBack,
      horseId,
      foalId,
      foalStore,
      horseStore
    }
  },
  async mounted() {
    this.foal = await this.foalStore.fetchFoal(this.foalId);
    this.horse = await this.horseStore.fetchHorsesById(this.horseId);
    this.foals = (await this.horseStore.fetchFoalsByHorseId(this.horseId)).length;
    if(this.foals === 1){
      this.routeBack = '/horsedetail/' + this.horseId;
    }
    console.log(this.foal.name);
  },
  methods: {
    deleteFoal(){
      this.foalStore.deleteFoalById(this.foalId);
      setTimeout(() => {
        this.$router.push(`/horsedetail/${this.horseId}/foals`);
      }, 500)
    },
    addFoal(){
      this.$router.push(`/horsedetail/${this.horseId}/addFoal`)
    },
    updateFoal(){
      this.$router.push(`/horsedetail/${this.horseId}/foals/${this.foalId}/update`)
    }
  }
}
</script>

<style scoped>
body {
  height: 100%;
  width: 100%;
  display: flex;
  flex-flow: column;
}
.topHeader {
  height: 20%;
  margin: 0;
}
.contentComponent {
  height: 70%;
}
.bottomNavBar {
  position: fixed;
  bottom: 0;
  height: 10%;
}
</style>