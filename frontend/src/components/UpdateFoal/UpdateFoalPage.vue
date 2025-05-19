<template>
  <v-form v-model="valid" class="form-container" validate-on="input" @submit.prevent="update">
    <div class="form-elements">
      <div class="input-container">
        <span class="container-span-elements">NAAM:</span>
        <v-text-field v-model="newFoal.name" :rules="rules.nameRules" variant="underlined" required>

        </v-text-field>
      </div>

      <div class="input-container">
        <span class="container-span-elements">GEBOORTEDATUM:</span>
        <div class="geboortedatum-datepicker">
          <v-btn id="button_start_date_dialog" @click="birthDatePickerActive = true">{{ birthDateButtonText }}
          </v-btn>
          <v-dialog v-model="birthDatePickerActive">
            <v-date-picker v-model="selectedBirthDate" @update:model-value="updateBirthDateButton"
                           style="margin-inline: auto"></v-date-picker>
          </v-dialog>
          <div class="container-span-elements" v-if="this.newFoal.birthDate.length < 1 && submitClicked">don't forget to pick a date!</div>
        </div>
      </div>
    </div>

    <div class="button-container">
      <v-btn class="final-button" color="green" type="submit">update Veulen</v-btn>
      <v-btn class="final-button" color="red-darken-3" @click="backToFoals">ANNULEREN</v-btn>
    </div>

  </v-form>



</template>


<script>
import { useHorseStore } from '@/stores/horseStore.js'
import {useFoalStore} from "@/stores/foalStore.js";

export default {
  name: 'AddFoalPage',
  data() {
    const horseStore = useHorseStore()
    const foalStore = useFoalStore()
    let submitClicked = false
    const newFoal = {
      name: '',
      horseId: this.$route.params.horseId,
      birthDate: ''
    }
    // let valid = null
    return {
      horseStore,
      foalStore,
      newFoal,
      submitClicked,
      oldFoal: {},
      valid: null,
      birthDatePickerActive: false,
      selectedBirthDate: [],
      birthDateIsoFormatted: '',
      birthDateButtonText: 'Kiezen',
      rules: {
        nameRules: [value => !!value || 'Gelieve een naam in te vullen.'],
        dateRules: [value => !!value || 'Gelieve een geboortedatum te kiezen']
      }
    }
  },
  async mounted() {
    this.oldFoal = await this.foalStore.fetchFoal(this.$route.params.foalId);
    this.newFoal.name = this.oldFoal.name;
    this.selectedBirthDate = new Date(this.oldFoal.birthDate);
    this.updateBirthDateButton();
  },
  methods: {
    update() {
      if(this.valid && this.newFoal.birthDate.length > 1){
        this.foalStore.updateFoal(this.newFoal, this.$route.params.foalId);
        setTimeout(() => {
          this.backToFoals();
        }, 500)
      }
      else{
        this.submitClicked = true;
      }
    },
    backToFoals() {
      this.$router.push(`/horsedetail/${this.$route.params.horseId}/foals`)
    },
    updateBirthDateButton() {
      if (this.selectedBirthDate instanceof Date) {
        this.birthDateButtonText = ('00' + this.selectedBirthDate.getDate()).slice(-2) + '-' + ('00' + (this.selectedBirthDate.getMonth() + 1)).slice(-2) + '-' + this.selectedBirthDate.getFullYear()
        this.newFoal.birthDate = this.birthDateButtonText
      }
    },
  }
}
</script>


<style scoped>
.form-container {
  display: flex;
  flex-flow: column;
  height: 100%;
}

.form-elements {
  flex: 1 1 auto;
}

.input-container {
  margin-left: 5%;
  margin-right: 5%;
  display: flex;
  align-items: center;
}

.container-span-elements {
  padding-right: 2em;
}

.button-container {
  flex: 0 1 30%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding-inline: 2rem;
}

.final-button {
  margin: 1em 0;
  width: 80%;
}
</style>