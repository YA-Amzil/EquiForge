<template>
  <div style="color: red">{{this.id}}</div>
  <v-container v-if="horse" fluid class="detail-container">
    <!-- Header en naam van het paard sectie -->
    <v-row justify="center">

      <v-col cols="12" class="text-center">
        <header>
          <v-img :src="image" :height="150" class="mx-auto" />
          <h2>{{ horse.name }}</h2>
        </header>
      </v-col>
    </v-row>

      <!-- Details sectie -->
      <v-row justify="center">
        <!-- Linkerkant van de pagina -->
        <v-col cols="12" sm="6" class="text-center">
          <p class="tes"><b>Leeftijd:</b> {{ calculateAge(horse.birthDate) }} jaar</p>
          <p><b>Stokmaat:</b> {{ horse.height }} cm</p>
          <p><b>Status:</b> {{ horse.isPregnant ? 'Drachtig' : 'Niet drachtig' }}</p>
          <br/>
          <p>Dekdata:</p>
          <div class="scrollable-list">
            <ul>
              <li v-for="(dekdatum, index) in this.dekdataDisplay" :key="dekdatum" class="listItems">
                <input type="radio"
                       :id="'item-' + index"
                       :name="'Dekdatumgroup'"
                       :value="dekdatum"
                       :checked="tickedCoverDate === dekdatum"
                       @change="tickCoverDate(dekdatum)">
                <label :for="'item-' + index">{{ dekdatum }}</label>
                <button class="deleteDate" @click="deleteCoverDate(dekdatum, index)" v-if="isAdmin">Delete</button>
              </li>
            </ul>
          </div>
          <div class="geboortedatum-datepicker">
            <v-btn
              id="button_start_date_dialog"
              @click="coverDatePickerActive = true"
              v-if="isAdmin"
            >{{ coverDateButtonText }}
            </v-btn>
            <v-dialog v-model="coverDatePickerActive">
              <v-date-picker
                v-model="selectedCoverDate"
                @update:model-value="updateCoverDateButton"
                style="margin-inline: auto"
              ></v-date-picker>
            </v-dialog>
            <v-btn @click="newCoverDate(this.coverDateTest)" v-if="this.isAdmin">Log</v-btn>
          </div>
          <div>
            <p id="dekstatus"></p>
          </div>
        </v-col>

        <!-- Rechterkant van de pagina -->
        <v-col cols="12" sm="6" class="text-center">
          <p><b>Huidig geselecteerde dekdatum:</b> {{this.currentSelectedPregnancyDate}}</p>
          <p><b>Verwachte geboorte:</b> {{this.endOfPregnancyDate}}</p>
        </v-col>
      </v-row>

    <v-btn @click="deleteHorse()" v-if="this.isAdmin">
      Delete
    </v-btn>

    <router-link :to="`/horsedetail/${horseId}/update`" v-if="this.isAdmin">
      <div>
        <span class="material-symbols-outlined">edit</span>
      </div>
    </router-link>

  </v-container>
</template>

<script>

import { useHorseStore } from "@/stores/horseStore.js";
import image from '@/assets/LogoTinkerstal.png'
import {storeLogin} from "@/helpFunctions/Cookies.js";

export default {
  name: "HorseDetailPage",
  data() {
    const store = useHorseStore();
    const id = this.$route.params.horseId;
    const isAdmin = storeLogin()
    return {
      store,
      horse: null,
      horseId: id,
      isAdmin,
      dekdata: [],
      dekdataDisplay: [],
      tussenarray: [],
      image: image,
      todayDate: null,
      coverDatePickerActive: false,
      selectedCoverDate: [],
      coverDateIsoFormatted: '',
      coverDateButtonText: 'Kiezen',
      coverDateTest: [],
      tickedCoverDate: null,
      pregnancyDate: null,
      endOfPregnancyDate: null,
      currentSelectedPregnancyDate: null
    };
  },
  async beforeMount() {
    const horseId = this.$route.params.horseId;
    this.horse = await this.store.fetchHorsesById(horseId);
    this.tussenarray = await this.store.fetchCoverDatesByHorseId(horseId);
    this.tussenarray.sort();
    console.log("tussen: " + this.tussenarray)
    for (let i = 0; i < this.tussenarray.length; i++) {
      let datum = new Date(this.tussenarray[i]);
      this.dekdata.push(datum);
      this.dekdataDisplay.push(`${datum.getDate()}/${datum.getMonth() + 1}/${datum.getFullYear()}`);
    }
    this.tussenarray = [];
    await this.logCoverDateList();

    if (this.horse.isPregnant === false) {
      this.pregnancyDate = null;
    } else {
      this.pregnancyDate = await this.store.fetchPregnancyDate(horseId);
      let tussendate = new Date(this.pregnancyDate);
      this.currentSelectedPregnancyDate = `${tussendate.getDate()}/${tussendate.getMonth() + 1}/${tussendate.getFullYear()}`;
      tussendate.setDate(tussendate.getDate() + 320);
      this.endOfPregnancyDate = `${tussendate.getDate()}/${tussendate.getMonth() + 1}/${tussendate.getFullYear()}`;
    }

  },
  methods: {
    calculateAge(datum) {
      const birthDate = new Date(datum);

      const today = new Date();

      let age = today.getFullYear() - birthDate.getFullYear();
      const monthDifference = today.getMonth() - birthDate.getMonth();

      if (monthDifference < 0 || (monthDifference === 0 && today.getDate() < birthDate.getDate())) {
        age--;
      }

      return age;
    },
    async logCoverDateList() {
      let coverDates = await this.store.fetchCoverDatesByHorseId(this.horseId);
      console.log("Fetched dekkingsdata: " + coverDates);
      console.log("Local dekdata: " + this.dekdata);
      console.log("Displaydata: " + this.dekdataDisplay);
    },

    async newCoverDate(newDate) {
      let newCoverDate = new Date(newDate);

      const isAlreadyAdded = this.dekdata.some(date => {
        return date.getFullYear() === newCoverDate.getFullYear() &&
          date.getMonth() === newCoverDate.getMonth() &&
          date.getDate() === newCoverDate.getDate();
      });

      if (isAlreadyAdded) {
        console.log(`${this.horse.name} is vandaag al gedekt`);
        document.getElementById('dekstatus').textContent = `${this.horse.name} is vandaag al gedekt`;
      } else {
        document.getElementById('dekstatus').textContent = "";

        await this.store.newCoverDate(this.horseId, newCoverDate);
        this.tussenarray = await this.store.fetchCoverDatesByHorseId(this.horseId);
        console.log("tussenarray: " + this.tussenarray);
        const newDate = this.tussenarray[0];
        const dekdatum = new Date(newDate);
        console.log("Datum: " + dekdatum)
        this.dekdata.push(dekdatum);
        this.dekdataDisplay.push(`${dekdatum.getDate()}/${dekdatum.getMonth() + 1}/${dekdatum.getFullYear()}`);
        console.log("display: " + this.dekdataDisplay);

        await this.logCoverDateList();
      }
    },
    deleteHorse(){
      console.log("Horse with Id: " + this.horseId + "being deleted");
      this.store.deleteHorseById(this.horseId);
      setTimeout(() => {
        this.$router.push("/");
      }, 300)
    },
    goToHorseUpdatePage() {
      this.$router.push(`/horsedetail/${this.horseId}/update`)
    },
    updateCoverDateButton() {
      if (this.selectedCoverDate instanceof Date) {
        this.coverDateIsoFormatted = this.selectedCoverDate.getFullYear() + '-' + ('00' + (this.selectedCoverDate.getMonth() + 1)).slice(-2) + '-' + ('00' + this.selectedCoverDate.getDate()).slice(-2);
        this.coverDateButtonText = ('00' + this.selectedCoverDate.getDate()).slice(-2) + '-' + ('00' + (this.selectedCoverDate.getMonth() + 1)).slice(-2) + '-' + this.selectedCoverDate.getFullYear();
        this.coverDateTest = this.coverDateIsoFormatted;
        console.log(this.coverDateTest);
      }
    },
    async tickCoverDate(dekdatum) {
      this.tickedCoverDate = new Date(dekdatum);
      console.log("tickedDate: " + dekdatum);
      await this.store.setPregnancyDate(this.horseId, dekdatum);
      this.pregnancyDate = await this.store.fetchPregnancyDate(this.horseId);
      let tussendate = new Date(this.pregnancyDate);
      this.currentSelectedPregnancyDate = `${tussendate.getDate()}/${tussendate.getMonth() + 1}/${tussendate.getFullYear()}`;
      tussendate.setDate(tussendate.getDate() + 320);
      this.endOfPregnancyDate = `${tussendate.getDate()}/${tussendate.getMonth() + 1}/${tussendate.getFullYear()}`;
    },
    async deleteCoverDate(dekdatum, index) {
      try {
        await this.store.deleteCoverDate(this.horseId, dekdatum);
        this.dekdata.splice(index, 1);
        this.dekdataDisplay.splice(index, 1);
      } catch (error) {
        console.error(`Failed to delete date ${dekdatum}:`, error);
      }
    },
  },
}
</script>

<style>
.detail-container {
  margin: auto;
  color: white;
  padding: 20px;
}

.text-center {
  text-align: center;
}

.scrollable-list {
  max-height: 100px;
  overflow-y: auto;
  margin-bottom: 10px;
}

.listItems {
  margin-top: 10px;
  margin-bottom: 10px;
}

.deleteDate {
  margin-left: 25px;
  padding-left: 10px;
  padding-right: 10px;
  border: white 1px solid;
  border-radius: 20px;
  background: darkred;
}

/* .logo-image {
  text-align: center;
} */

</style>

