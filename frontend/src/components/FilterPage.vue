<template>
  <header>
    <v-img :src="store.logoTinkerstal()" height="125" />
  </header>
  <main>
    <h1 style="font-size: 300%">FILTERS</h1>

    <div class="checkbox-and-input-container">
      <div class="input-container">
        <span class="input-span">DRACHTIG:</span>
        <v-radio-group
          class="input-choice align-center"
          v-model="filterOption.pregnant"
          inline hide-details>
          <v-radio label="Ja"
                   color="#0A0"
                   value="true"></v-radio>
          <v-radio label="Nee"
                   color="#A00"
                   value="false"></v-radio>
          <v-radio label="Uit"
                   color="#000"
                   value=""></v-radio>
        </v-radio-group>
      </div>

      <div class="input-container">
        <span class="input-span">GEDEKT:</span>
        <v-radio-group
          class="input-choice align-center"
          v-model="filterOption.decked"
          inline hide-details>
          <v-radio label="Ja"
                   color="#0A0"
                   value="true"></v-radio>
          <v-radio label="Nee"
                   color="#A00"
                   value="false"></v-radio>
          <v-radio label="Uit"
                   color="#000"
                   value=""></v-radio>
        </v-radio-group>
      </div>

      <div class="input-container">
        <span>VERWACHTE BEVALLING</span>
        <v-checkbox label="Selecteer" color="white"
                    v-model="filterOption.expected_pregnancy"
                    style="margin-left: 8%"
                    hide-details></v-checkbox>
      </div>

      <div class="periode-container">
        <v-btn
          class="v-btn"
          id="button_start_date_dialog"
          @click="this.datepickers.start_date_active = true"
          :disabled="!filterOption.expected_pregnancy"
          :text="this.datepickers.start_date_text"
        ></v-btn>
          <v-dialog v-model="this.datepickers.start_date_active" width="auto">
            <v-date-picker
              v-model="this.datepickers.selected_start_date"
              @update:model-value="updateTextDatePicker"
            ></v-date-picker>
        </v-dialog>

        <span>TUSSEN</span>

        <v-btn
          id="button_end_date_dialog"
          @click="this.datepickers.end_date_active = true"
          :disabled="!filterOption.expected_pregnancy"
          :text="this.datepickers.end_date_text"
        ></v-btn>
        <v-dialog v-model="this.datepickers.end_date_active" width="auto">
          <v-date-picker
            v-model="this.datepickers.selected_end_date"
            @update:model-value="updateTextDatePicker"
          ></v-date-picker>
        </v-dialog>
      </div>
    </div>
  </main>
  <footer>
    <v-btn color="green-darken-3" @click="returnToHomePage(true)">
      ZOEK
    </v-btn>
    <v-btn color="red-darken-3" @click="returnToHomePage(false)">
      ANNULEER
    </v-btn>
  </footer>
</template>


<script>
import { useHorseStore } from '@/stores/horseStore.js'

export default {
  name: 'FilterPage',
  data() {
    const store = useHorseStore()
    const filterOption = {
      pregnant: '',
      decked: '',
      expected_pregnancy: '',
      start_date: '',
      end_date: ''
    }
    const datepickers = {
      start_date_active: false,
      end_date_active: false,
      selected_start_date: [],
      selected_end_date: [],
      start_date_text: 'Startdatum...',
      end_date_text: 'Einddatum...'
    }
    return { store, filterOption, datepickers }
  },
  methods: {
    updateTextDatePicker() {
      let date
      if (this.datepickers.start_date_active) {
        date = this.datepickers.selected_start_date
        this.datepickers.start_date_text = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear()
        this.filterOption.start_date = this.datepickers.start_date_text
      }
      if (this.datepickers.end_date_active) {
        date = this.datepickers.selected_end_date
        this.datepickers.end_date_text = ('0' + date.getDate()).slice(-2) + '-' + ('0' + (date.getMonth() + 1)).slice(-2) + '-' + date.getFullYear()
        this.filterOption.end_date = this.datepickers.end_date_text
      }
    },
    returnToHomePage(includeQuery) {
      if (includeQuery) {
        if (this.filterOption.expected_pregnancy && (this.filterOption.start_date === '' || this.filterOption.end_date === '')) {
          alert('Kies a.u.b. een start- en eind-datum voor de verwachte bevalling')
          return
          }

        const queryParams = {};
        for (const key in this.filterOption) {
          if (this.filterOption[key] !== '') {
            queryParams[key] = this.filterOption[key];
          }
        }

          this.$router.push({ path: '/home', query: queryParams })
      } else {
        this.$router.push({ path: '/home', query: false })
      }
    }
  }
}

</script>


<style scoped>
header {
  flex: 0 1 auto;
}

main {
  flex: 1 1 60%;
  flex-direction: column;

  padding-inline: 1.5rem;
}

footer {
  display: flex;
  flex-direction: column;
  flex: 0 1 15%;

  padding-inline: 3rem;
}

footer > * {
  margin-bottom: 1em;
}

.checkbox-and-input-container {
  padding-left: 1.5em;
  padding-right: 1.5em;
  font-size: 90%;
}

.input-container {
  display: flex;
  flex-direction: column;
}

.input-choice {
  display: flex;
  justify-content: space-evenly;
  padding-bottom: 10px;
}

.periode-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding-top: 0.5em;
}

.periode-container > .v-btn {
  width: 80%;
}

.periode-container > span {
  display: block;
  margin: 0.75em 0;
}

</style>