<template>
  <div class="container-elements">

    <header>

      <div class="top-button-container">

        <div class="left-buttons-container">

          <router-link to="/filter">
            <font-awesome-icon class="fa-icon" icon="fas fa-filter" size="2xl" />
          </router-link>

          <router-link to="">
            <font-awesome-icon class="fa-icon" icon="fas fa-search" size="2xl" @click="showOrHideSearchBar" />
          </router-link>


        </div>

        <v-text-field hide-details placeholder="Search" v-model="searchTerm" v-if="searchBarVisible"
          v-on:update:model-value="filterHorsesByNameUsingSearchTerm" variant="underlined" style="margin-inline: 2em">
        </v-text-field>

        <div class="days-counter-container">
          <v-icon icon="mdi:mdi-arrow-up-bold-box-outline" size="29" />
          <v-icon icon="mdi:mdi-counter" size="32" />
          <v-icon icon="mdi:mdi-arrow-down-bold-box-outline" size="29" />
        </div>
      </div>

    </header>

    <main>

      <router-link class="d-flex mt-2" :to="'/horsedetail/' + horse.id" v-for="(horse) in horses" :key="horse.id">
        <div class="horse-container">
          <div class="horse-container-image">
            <v-img class="horsePicture" :src="overviewPicture"
                   :style="{ filter: horse.pregnant ? 'invert(80%) sepia(48%) saturate(3475%) hue-rotate(273deg) brightness(93%) contrast(95%)': 'invert(100%)'}"></v-img>
          </div>

          <div class="horse-container-name">
            {{ horse.name }}
          </div>


          <div class="pregnancyDataBox">
            <div v-if="horse.daysPregnant > - 1 || horse.expectedPregnancyDays > - 1"
                 class="horse-container-dayspregnant">
              {{ horse.daysPregnant }}
            </div>
            <div v-else class="horse-container-dayspregnant">

            </div>

            <div v-if="horse.daysPregnant > - 1 || horse.expectedPregnancyDays > - 1"
                 class="horse-container-expectedpregnancy">
              {{ horse.expectedPregnancyDays }}
            </div>
            <div v-else class="horse-container-expectedpregnancy">

            </div>
          </div>
        </div>
      </router-link>
    </main>

    <footer>
      <router-link to="/addhorse" v-if="this.isAdmin">
        <font-awesome-icon class="fa-2xl" color="white" :icon="['fas', 'circle-plus']" />
      </router-link>
    </footer>

  </div>

</template>

<script>

import { RouterLink } from 'vue-router'
import { useHorseStore } from '@/stores/horseStore.js'
import router from '@/router/index.js'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import overviewPicture from "@/assets/HorseOverviewLogo.png"
import {storeLogin} from "@/helpFunctions/Cookies.js";

export default {
  components: { FontAwesomeIcon, RouterLink },
  data() {
    const store = useHorseStore()
    let searchTerm = ""
    let searchBarVisible = false
    const isAdmin = storeLogin()
    return {
      horses: [],
      allHorses: [],
      store,
      isAdmin,
      searchTerm,
      searchBarVisible,
      overviewPicture: overviewPicture
    }
  },
  async mounted() {
    await this.fetchAndFilterHorses()
  },
  methods: {
    logItem(item, index) {
      console.log(this.store.fetchHorsesById(index + 1))
    },
    goToDetailPage(index) {
      router.push(`/horsedetail/${index}`)
    },
    goToAddHorsePage() {
      router.push('/addhorse')
    },
    filterList() {
      let filterlist = this.horses

      if (Object.hasOwn(this.$route?.query, 'pregnant')) {
        filterlist = filterlist.filter(horse => {
          return horse.pregnant === (this.$route?.query.pregnant === 'true')
        })
      }

      if (Object.hasOwn(this.$route.query, 'decked')) {
        if (this.$route.query.decked === 'true') {
          filterlist = filterlist.filter(horse => horse.studHorse !== '')
        } else {
          filterlist = filterlist.filter(horse => horse.studHorse === '')
        }
      }

      if (Object.hasOwn(this.$route.query, 'expected_pregnancy')) {
        filterlist = filterlist.filter(horse => this.compareDatesInRegion(this.$route.query.start_date, this.$route.query.end_date, horse.expectedPregnancyDate))
      }

      this.horses = filterlist
    },
    compareDatesInRegion(start_boundary, end_boundary, requested_date) {
      if (requested_date === '') {
        return false
      }

      let start_array = start_boundary.split('-')
      console.log('start_array', start_array)
      let end_array = end_boundary.split('-')
      console.log('end_array', end_array)
      let date_array = requested_date.split('-')
      console.log('date_array', date_array)

      // Check jaren
      if (start_array[2] > date_array[2] || date_array[2] > end_array[2]) {
        console.log('not the same year')
        return false
      }
      // Check maanden en dagen
      if (date_array[2] === start_array[2]) {
        if (date_array[1] < start_array[1] || (date_array[1] === start_array[1] && date_array[0] < start_array[0])) {
          // begin datum (inclusief)
          console.log('same year, too early')
          return false
        }
      } else if (date_array[2] === end_array[2]) {
        if (date_array[1] > end_array[1] || (date_array[1] === end_array[1] && date_array[0] > end_array[0])) {
          // eind datum (inclusief)
          console.log('same year, too late')
          return false
        }
      }
      console.log('within boundaries')
      return true
    },
    filterHorsesByNameUsingSearchTerm() {
      const tempThis = this;
      console.log("filtering by searchTerm");
      if (tempThis.searchTerm.length === 0 && this.horses.length !== this.allHorses.length) {
        console.log("reset horses")
        this.fullHorsesReset();
      }
      else {
        tempThis.removeHorsesWhichDontContainSearchTerm();
        tempThis.addHorsesWhichDoContainSearchTerm();
      }
    },
    showOrHideSearchBar() {
      this.searchBarVisible = !this.searchBarVisible;
    },
    async fetchAndFilterHorses(){
      this.allHorses = await this.store.fetchHorses();
      this.fullHorsesReset();
      this.filterList();
      console.log("start");
    },
    fullHorsesReset(){
      console.log("cloning horses");
      this.horses = this.allHorses.map(a => { return { ...a } });
      console.log("horses cloned");
    },
    addHorsesWhichDoContainSearchTerm() {
      console.log("adding horses back to array");
      this.allHorses.forEach(v => {
        if (v.name.toLowerCase().includes(this.searchTerm.toLowerCase())) {
          if (!this.checkIfHorsesContainsHorse(v)) {
            this.horses.push(v);
          }
        }
      }
      )
    },
    removeHorsesWhichDontContainSearchTerm() {
      console.log("removing horses from array");
      let tempThis = this;
      this.horses.reduceRight(function (acc, item, index, object) {
        if (!item.name.toLowerCase().includes(tempThis.searchTerm.toLowerCase())) {
          object.splice(index, 1);
        }
      }, []);
    },
    checkIfHorsesContainsHorse(horse) {
      let value = false;
      this.horses.forEach(v => {
        if (v.id === horse.id) {
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

.horsePicture {
  width: 50px;
  height: 50px;
  margin-left: 15%;
}

.horse-container {
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

.pregnancyDataBox {
  display: flex;
  margin-left: auto;
}

.horse-container-name {
  margin-left: 20%;
}

.horse-container-dayspregnant {
  margin-right: 30%;
}

.horse-container-expectedpregnancy {
  margin-right: 35px;
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

.left-buttons-container {
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

.days-counter-container {
  display: inline-flex;
  align-items: center;
  margin-right: 0.8em;
  padding-top: 20px;
}

.days-counter-container * {
  margin: 0 -4.6px;
}

header {
  flex: 0 1 auto;
}

main {
  height: 70%;
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