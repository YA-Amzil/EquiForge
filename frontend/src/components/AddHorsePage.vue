<template>
  <v-form class="form-container" @submit.prevent="submitForm" method="post">

    <div class="form-elements">
      <div class="input-container">
        <span class="container-span-elements">NAAM:</span>
        <v-text-field
            v-model="newHorse.name"
            :rules="nameRules"
            variant="underlined"
            required>
        </v-text-field>
      </div>

      <div class="input-container">
        <span class="container-span-elements">GEBOORTEDATUM:</span>
        <div class="geboortedatum-datepicker">
          <v-btn
              id="button_start_date_dialog"
              @click="birthDatePickerActive = true"
          >{{ birthDateButtonText }}
          </v-btn>
          <v-dialog v-model="birthDatePickerActive">
            <v-date-picker
                v-model="selectedBirthDate"
                @update:model-value="updateBirthDateButton"
                style="margin-inline: auto"
            ></v-date-picker>
          </v-dialog>
        </div>
      </div>

      <div class="input-container">
        <span class="container-span-elements">STOKMAAT:</span>
        <v-text-field
            label="CM"
            v-model="newHorse.height"
            :rules="[value => value > 0 || 'Stokhoogte kan niet 0 of lager zijn']"
            variant="underlined"
            type="number"></v-text-field>
      </div>

      <div class="input-container" style="width: 90%;">
        <span class="container-span-elements">DRACHTIG:</span>
        <v-checkbox
            v-model="newHorse.isPregnant"
            hide-details
        >
        </v-checkbox>
        <div class="geboortedatum-datepicker" v-if="newHorse.isPregnant" style="margin-left: 1.5em">
          <v-btn
              id="button_start_date_dialog"
              @click="pregnantDatePickerActive = true"
          >{{ pregnantDateButtonText }}
          </v-btn>
          <v-dialog v-model="pregnantDatePickerActive">
            <v-date-picker
                v-model="selectedPregnantDate"
                @update:model-value="updatePregnantDateButton"
                style="margin-inline: auto"
            ></v-date-picker>
          </v-dialog>
        </div>
      </div>

      <div class="input-container">
        <span class="container-span-elements">DEKHENGST:</span>
        <v-text-field
            v-model="newHorse.studHorse"
            variant="underlined">
        </v-text-field>
      </div>
    </div>
    <div class="button-container">
      <v-btn class="final-button" color="success" type="submit">OPSLAAN</v-btn>
      <v-btn class="final-button" color="error" @click="goToHomepage"
             style="margin-top: 1.5em">ANNULEREN
      </v-btn>
    </div>
  </v-form>
</template>


<script>
import { useHorseStore } from '@/stores/horseStore.js'

export default {
  name: 'AddHorsePage',
  data() {
    const store = useHorseStore()
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
  created() {
    this.store.fetchHorses()
  },
  methods: {
    submitForm() {
      console.log('horse:', this.newHorse)
      let stringRequiresFields = ''
      if (this.newHorse.name === '') {
        stringRequiresFields += '- Naam\n'
      }
      console.log(this.selectedBirthDate)
      if (this.selectedBirthDate.length < 4) {
        stringRequiresFields += '- Geboortedatum\n'
      }
      if (this.newHorse.height <= 0) {
        stringRequiresFields += '- Stokmaat (getal)'
      }
      if (this.checkPregnancyDate() === false) {
        stringRequiresFields += '- Datum van drachtig\n'
      }

      if (stringRequiresFields.length > 0) {
        alert('Benodigde velden zijn niet ingevuld:\n' + stringRequiresFields)
        return
      }
      this.store.addHorse(this.newHorse)
      setTimeout(() => {
        this.goToHomepage()
      }, 300)
    },
    goToHomepage() {
      this.$router.push('/')
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