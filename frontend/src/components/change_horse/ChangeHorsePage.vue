<template>
  <v-form class="form-container" @submit.prevent="submitForm" method="post">

    <div class="form-elements">
      <div class="input-container">
        <span class="container-span-elements">NAAM:</span>
        <v-text-field v-model="newHorse.name" :rules="nameRules" variant="underlined" required>
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
        </div>
      </div>

      <div class="input-container">
        <span class="container-span-elements">STOKMAAT:</span>
        <v-text-field label="CM" v-model="newHorse.height"
                      :rules="[value => value > 0 || 'Stokhoogte kan niet 0 of lager zijn']" variant="underlined"
                      type="number"></v-text-field>
      </div>

      <div class="input-container" style="width: 90%;">
        <span class="container-span-elements">DRACHTIG:</span>
        <v-checkbox v-model="newHorse.isPregnant" hide-details>
        </v-checkbox>
        <div class="geboortedatum-datepicker" v-if="newHorse.isPregnant" style="margin-left: 1.5em">
          <v-btn id="button_start_date_dialog" @click="pregnantDatePickerActive = true">{{ pregnantDateButtonText }}
          </v-btn>
          <v-dialog v-model="pregnantDatePickerActive" width="auto">
            <v-date-picker
                v-model="selectedPregnantDate"
                @update:model-value="updatePregnantDateButton"
            ></v-date-picker>
          </v-dialog>
        </div>
      </div>

      <div class="input-container">
        <span class="container-span-elements">DEKHENGST:</span>
        <v-text-field v-model="newHorse.studHorse" variant="underlined">
        </v-text-field>
      </div>
    </div>
    <div class="button-container">
      <v-btn class="final-button" color="green-darken-3" type="submit">GEGEVENS WIJZIGEN</v-btn>
      <v-btn class="final-button" color="red-darken-3" @click="goToDetailPage()" style="margin-top: 1.5em">ANNULEREN</v-btn>
    </div>
  </v-form>
</template>



<script>
import { useHorseStore } from '@/stores/horseStore.js'
import router from "@/router/index.js";

export default {
  name: 'ChangeHorsePage',
  data() {
    const store = useHorseStore();
    const horseId = this.$route.params.horseId;
    const horse = null;
    const newHorse = {
      name: '',
      birthDate: '',
      height: 0,
      studHorse: '',
      isPregnant: false,
      datePregnant: null
    }
    const nameRules = [
      value => !!value || 'Gelieve een naam in te vullen.'
    ]
    return {
      store,
      horseId,
      horse,
      nameRules,
      newHorse,
      birthDatePickerActive: false,
      pregnantDatePickerActive: false,
      selectedBirthDate: [],
      selectedPregnantDate: [],
      birthDateIsoFormatted: '',
      pregnantDateIsoFormatted: '',
      birthDateButtonText: 'Kiezen',
      pregnantDateButtonText: 'Kiezen'
    }
  },
  async mounted() {
    this.horse = await this.store.fetchUpdatableHorseById(this.horseId);
    this.newHorse.name = this.horse.name;
    this.newHorse.studHorse = this.horse.studHorse;
    this.newHorse.birthDate = this.horse.birthDate;
    this.newHorse.datePregnant = this.horse.datePregnant;
    this.newHorse.height = this.horse.height;
    this.newHorse.isPregnant = this.horse.isPregnant;

  },
  methods: {
    submitForm() {
      console.log('HORSE TO CHECK:', this.newHorse)
      console.log(this.newHorse.datePregnant)
      console.log(this.horseId)
      let stringRequiresFields = ''
      if (this.newHorse.name === '') {
        stringRequiresFields += '- Naam\n'
      }
      console.log(this.selectedBirthDate)
      if (this.selectedBirthDate.length < 4 && this.newHorse.birthDate === null) {
        stringRequiresFields += '- Geboortedatum\n'
      }
      if (this.newHorse.height <= 0) {
        stringRequiresFields += '- Stokmaat (getal)'
      }
      if (this.checkPregnancyDate() === false && this.newHorse.datePregnant === null) {
        stringRequiresFields += '- Datum van drachtig\n'
      }

      if (stringRequiresFields.length > 0) {
        alert('Benodigde velden zijn niet ingevuld:\n' + stringRequiresFields)
        return
      }

      if (!this.newHorse.isPregnant) {
        this.newHorse.datePregnant = null;
      }

      console.log("Controleren van INFO voor: " + this.newHorse.name)
      console.log("Controleren van INFO voor: " + this.newHorse.isPregnant)
      console.log("Controleren van INFO voor: " + this.newHorse.datePregnant)

      this.store.updateHorseById(this.horseId, this.newHorse);
      setTimeout(() => {
        this.goToDetailPage()
      }, 300)
    },
    goToDetailPage() {
      router.push(`/horsedetail/${this.horseId}`)
    },
    checkPregnancyDate() {
      if (!(this.newHorse.isPregnant)) {
        this.newHorse.datePregnant = null
      } else if (this.newHorse.datePregnant == null) {
        return false
      }
      return true
    },
    updateBirthDateButton() {
      if (this.selectedBirthDate instanceof Date) {
        this.birthDateButtonText = ('00' + this.selectedBirthDate.getDate()).slice(-2) + '-' + ('00' + (this.selectedBirthDate.getMonth() + 1)).slice(-2) + '-' + this.selectedBirthDate.getFullYear()
        this.newHorse.birthDate = this.birthDateButtonText
      }
    },
    updatePregnantDateButton() {
      if (this.selectedPregnantDate instanceof Date) {
        this.pregnantDateIsoFormatted = this.selectedPregnantDate.getFullYear() + '-' + ('00' + (this.selectedPregnantDate.getMonth() + 1)).slice(-2) + '-' + ('00' + this.selectedPregnantDate.getDate()).slice(-2)
        this.pregnantDateButtonText = ('00' + this.selectedPregnantDate.getDate()).slice(-2) + '-' + ('00' + (this.selectedPregnantDate.getMonth() + 1)).slice(-2) + '-' + this.selectedPregnantDate.getFullYear()
        this.newHorse.datePregnant = this.pregnantDateButtonText
      }
    }
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
  flex: 0 1 200%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.final-button {
  width: 60%;
}
</style>