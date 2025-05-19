<template>
  <div class="container-elements">

    <header>

      <div class="top-button-container">

        <div class="button-container">
          <router-link to="">
            <font-awesome-icon class="fa-icon" icon="fas fa-search" size="2xl" @click="showOrHideSearchBar" />
          </router-link>

        </div>

        <v-text-field hide-details placeholder="Search" v-model="searchTerm" v-if="searchBarVisible"
                      v-on:update:model-value="filterFoalsByNameUsingSearchTerm" variant="underlined" style="margin-inline: 2em">
        </v-text-field>

      </div>

    </header>

    <main>

      <router-link class="d-flex mt-2" :to="'/horsedetail/' + this.horseId + '/foals/' + foal.id" v-for="(foal) in foals" :key="foal.id">
        <div class="foal-container">

          <div class="foal-container-image">
            <v-img class="foalPicture" :src="overviewPicture"></v-img>
          </div>

          <div class="foal-container-name">
            {{ foal.name }}
          </div>

          <div class="foal-container-age">
            {{calculateAge(foal.birthDate)}}
          </div>

        </div>
      </router-link>
    </main>

    <footer>
      <router-link to="" @click="showAddFoalPage" v-if="this.isAdmin">
        <font-awesome-icon class="fa-2xl" color="white" :icon="['fas', 'circle-plus']" />
      </router-link>
    </footer>

  </div>

</template>

<script>

import { RouterLink } from 'vue-router'
import { useHorseStore } from '@/stores/horseStore.js'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {useFoalStore} from "@/stores/foalStore.js";
import overviewPicture from "@/assets/HorseOverviewLogo.png"
import {storeLogin} from "@/helpFunctions/Cookies.js"
import {calculateAge} from "@/helpFunctions/Age.js";

export default {
  name: "foalPage",
  components: {FontAwesomeIcon, RouterLink},
  data() {
    const horseId = this.$route.params.horseId;
    const horseStore = useHorseStore()
    const foalStore = useFoalStore()
    const isAdmin = storeLogin()
    const horsePicture = horseStore.logoHorseOverview();
    let searchTerm = ""
    let searchBarVisible = false
    return {
      allFoals: [],
      foals: [],
      horseId,
      horsePicture,
      horseStore,
      isAdmin,
      foalStore,
      searchTerm,
      searchBarVisible,
      overviewPicture: overviewPicture,
      calculateAge
    }
  },
  async mounted() {
    await this.fetchFoals()
    console.log("length", this.foals.length);
    if(this.foals.length === 1){
      this.showDetailPage(this.foals.at(0).id);
    }
  },
  methods: {
    filterFoalsByNameUsingSearchTerm() {
      const tempThis = this;
      console.log("filtering by searchTerm");
      if (tempThis.searchTerm.length === 0 && this.foals.length !== this.allFoals.length) {
        console.log("reset foals")
        this.fullFoalsReset();
      } else {
        tempThis.removeFoalsWhichDontContainSearchTerm();
        tempThis.addFoalsWhichDoContainSearchTerm();
      }
    },
    showDetailPage(id) {
      this.$router.push(`/horsedetail/${this.horseId}/foals/${id}`);
    },
    showAddFoalPage() {
      this.$router.push(`/horsedetail/${this.horseId}/addFoal`);
    },
    showOrHideSearchBar() {
      this.searchBarVisible = !this.searchBarVisible;
    },
    async fetchFoals() {
      this.allFoals = await this.horseStore.fetchFoalsByHorseId(this.horseId);
      console.log(this.allFoals)
      this.fullFoalsReset();
      console.log("start");
    },
    fullFoalsReset() {
      console.log("cloning foals");
      this.foals = this.allFoals.map(a => {
        return {...a}
      });
      console.log("foals cloned: ", this.foals);
    },
    addFoalsWhichDoContainSearchTerm() {
      console.log("adding foals back to array");
      this.allFoals.forEach(v => {
            if (v.name.toLowerCase().includes(this.searchTerm.toLowerCase())) {
              if (!this.checkIfFoalsContainsfoal(v)) {
                this.foals.push(v);
              }
            }
          }
      )
    },
    removeFoalsWhichDontContainSearchTerm() {
      console.log("removing foals from array");
      let tempThis = this;
      this.foals.reduceRight(function (acc, item, index, object) {
        if (!item.name.toLowerCase().includes(tempThis.searchTerm.toLowerCase())) {
          object.splice(index, 1);
        }
      }, []);
    },
    checkIfFoalsContainsfoal(foal) {
      let value = false;
      this.foals.forEach(v => {
        if (v.id === foal.id) {
          value = true;
        }
      })
      return value;
    }
  }
}
</script>

<style scoped>
.container-elements {
  display: flex;
  flex-flow: column;
  height: 100%;
}

.foalPicture {
  width: 50px;
  height: 50px;
  margin-left: 15%;
  filter: invert(100%);
}

.foal-container {
  display: flex;
  align-items: center;
  padding: 4px;
  width: 90%;
  margin: 0 auto;

  color: white;
  background: #21150f;
  border-style: dashed;
  border-radius: 10px;
  border-width: thin;
  border-color: #9F6713;
}

.foal-container-name {
  margin-left: 20%;
}

.foal-container-age {
  margin-right: 35px;
  margin-left: auto;
}

.top-button-container {
  color: white;
  display: flex;
  flex-flow: row;
  align-items: center;
  justify-content: space-between;
  width: 95%;
  height: auto;
}

.button-container {
  display: flex;
  flex-flow: row;
  padding-top: 20px;
}

.fa-icon {
  color: black;
  background: white;
  border-radius: 50%;
  padding: 15%;
  margin-left: 20px;
}

header {
  flex: 0 1 auto;
}

main {
  height: 80%;
  overflow-y: auto;
  flex: 1 1 auto;
}

footer {
  position: absolute;
  right: 10%;
  bottom: 15%;
  flex: 0 1 10%;
}
</style>