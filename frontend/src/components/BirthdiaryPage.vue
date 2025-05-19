<script>
import { useHorseStore } from '@/stores/horseStore.js'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { ref } from 'vue'
import {storeLogin} from "@/helpFunctions/Cookies.js";

export default {
  name: 'BirthdiaryPage',
  components: { FontAwesomeIcon },
  data() {
    const store = useHorseStore()
    const id = this.$route.params.horseId
    const diaries = ref([])
    const isAdmin = storeLogin()
    return {
      horse: null,
      horseId: id,
      diaries,
      isAdmin,
      activeDiary: '',
      diaryText: '',
      diaryMenuActive: false,
      store
    }
  },
  computed: {
    diaryKeys() {
      console.log('diaries', this.diaries)
      return Array.from(this.diaries.filter(d => d.diaryName))
    }
  },
  async beforeMount() {
    this.horse = await this.store.fetchHorsesById(this.horseId)
    let result = await this.store.fetchHorseDiariesByHorseId(this.horseId)
    console.log('result', result)
    Object.entries(result).forEach((entry) => {
      const [key, value] = entry
      console.log(`Adding key ${key} and value ${value}`)
      this.diaries.push(
        {
          diaryName: key,
          diaryText: value
        }
      )
    })
  },
  methods: {
    updateDiary() {
      let diary = this.diaries.find(d => d.diaryName === this.activeDiary)
      diary.diaryText = this.diaryText
      console.log('diary edited:', diary)
      this.store.updateHorseDiary(this.horseId, this.activeDiary, this.diaryText)
    },
    loadDiaryText() {
      console.log('Load diary text triggered')
      this.diaryMenuActive = !this.diaryMenuActive
      if (this.diaryMenuActive) {
        console.log('diary menu opened! will not load text further!')
        return
      }
      console.log('Diary selected, loading diary further...')
      console.log('active diary:')
      console.log(this.activeDiary)
      this.diaryText = this.activeDiary['diaryText']
    },
    saveDiary() {
      console.log('save Diary triggered')
      console.log(this.horse)
      if (this.diaries.find(d => d.diaryName === this.activeDiary)) {
        this.activeDiary = 'Al toegevoegd!'
        return
      }
      this.store.newHorseDiary(this.horseId, this.activeDiary)
      this.diaries.push({ diaryName: this.activeDiary, diaryText: '' })
    }
  }
}
</script>

<template>
  <div class="flex-center flex-column" v-if="horse">
    <br />
    <h3>Dagboek</h3>
    <div class="diarySelector flex-center">
      <v-combobox class="diaryPicker text" v-model="activeDiary" :items="diaryKeys" item-title="diaryName"
        v-on:update:menu="loadDiaryText(activeDiary)" density="comfortable" variant="outlined" hide-details
        return-object />
      <font-awesome-icon class="newDiary" icon="fas fa-plus" size="xl" v-on:click="saveDiary()" v-if="this.isAdmin" />
    </div>
    <v-textarea class="v-textarea text" rows="6" v-model="diaryText" v-on:change="this.updateDiary()"
      v-if="diaries.length > 0" variant="outlined" flat hide-details></v-textarea>
  </div>
  <h2 v-else class="flex-center flex-column">Laden...</h2>
</template>

<style scoped>
.flex-center {
  display: flex;
  align-items: center;
  justify-content: center;
}

.flex-column {
  display: flex;
  flex-direction: column;
}

.diarySelector {
  width: 50%;
  flex-direction: row;
  color: white;
}

.diaryPicker {
  width: 80%;
}

.v-textarea {
  width: 80%;
  margin: 2rem 0;
}

.text {
  background: #21150f;
  border-radius: 4px;
  color: white;
}

.newDiary {
  margin-left: 20px;
  vertical-align: middle;
}
</style>