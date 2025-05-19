import { defineStore } from 'pinia'
import {getTokenFromCookie} from "@/helpFunctions/Cookies.js";

const allHorsesUrl = 'http://localhost:8082/api/horses'
const addHorseAPI = 'http://localhost:8082/api/horses/create-new'

export const useHorseStore = defineStore('horse', {
  state: () => ({
    isAdmin: false,
    horses: [],
    isLoading: false,
    horse: {}
  }),
  actions: {
    async fetchHorses() {
      try {
        this.isLoading = true
        const token = getTokenFromCookie()
        const response = await fetch(allHorsesUrl + (this.$route?.query ? '?' + new URLSearchParams(this.$route?.query) : ''), {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        })

        if (!response.ok) {
          throw new Error('Failed to fetch horses')
        }

        const data = await response.json()
        console.log(data)
        this.horses = data
        return data
      } catch (error) {
        console.error('There seems to be a problem fetching the horses', error)
      } finally {
        this.isLoading = false
      }
    },

    async fetchHorsesById(horseId) {
      try {
        this.isLoading = true
        const token = getTokenFromCookie()
        const response = await fetch(`${allHorsesUrl}/${horseId}`, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        })

        if (!response.ok) {
          throw new Error('Failed to fetch horse with ID: ' + horseId)
        }

        const data = await response.json()
        console.log('Fetched horse:')
        console.log(data)
        this.horse = data
        return data
      } catch (error) {
        console.error('There seems to be a problem fetching a horse', error)
      } finally {
        this.isLoading = false
      }
    },
    async fetchUpdatableHorseById(horseId) {
      try {
        this.isLoading = true
        const token = getTokenFromCookie()
        const response = await fetch(`${allHorsesUrl}/${horseId}/updatehorse`, {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        })

        if (!response.ok) {
          throw new Error('Failed to fetch horse with ID: ' + horseId)
        }

        const data = await response.json()
        console.log('Fetched horse:')
        console.log(data)
        this.horse = data
        return data
      } catch (error) {
        console.error('There seems to be a problem fetching a horse', error)
      } finally {
        this.isLoading = false
      }
    },

    async addHorse(horse) {
      try {
        this.isLoading = true
        const token = getTokenFromCookie()
        const response = await fetch(addHorseAPI, {
          method: 'POST',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(horse)
        })
        if (!response.ok) {
          throw new Error('Fetching horses failed, there\'s en error: ' + response.status)
        }

        console.log('Horse has been succesfully added!')
      } catch (error) {
        console.log(error)
      }
    },

    async fetchCoverDatesByHorseId(horseId) {
      try {
        this.isLoading = true
        return await fetch(allHorsesUrl + `/${horseId}/plannedDates`)
          .then(response => {
            if (!response.ok) {
              throw new Error('Failed to fetch horse with ID: ' + horseId)
            }
            return response.json()
          })
          .catch(error => console.error(error))
      } finally {
        this.isLoading = false
      }
    },

    async newCoverDate(horseId, plannedDate) {

      const token = getTokenFromCookie()

      const parsedDate = new Date(plannedDate)
      const day = String(parsedDate.getDate()).padStart(2, '0')
      const month = String(parsedDate.getMonth() + 1).padStart(2, '0')
      const year = parsedDate.getFullYear()
      const formattedDate = `${day}/${month}/${year}`
      console.log('formatted date: ' + formattedDate)

      await fetch(allHorsesUrl + `/${horseId}/plannedDates`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formattedDate)
      }).then(async response => {
        if (!response.ok) {
          const errorMessage = await response.text()
          throw new Error(`HTTP Error ${response.status}: ${errorMessage}`)
        }
        console.log('Response: ' + response)
      })
        .catch(error => console.error(error))
    },


    async setPregnancyDate(horseId, pregnantDate) {
      const token = getTokenFromCookie();

      const [day, month, year] = pregnantDate.split('/').map(Number);
      const parsedDate = new Date(year, month - 1, day);

      const parsedDay = String(parsedDate.getDate()).padStart(2, '0')
      const parsedMonth = String(parsedDate.getMonth() + 1).padStart(2, '0')
      const parsedYear = parsedDate.getFullYear()
      const formattedDate = `${parsedDay}/${parsedMonth}/${parsedYear}`
      console.log('formatted date: ' + formattedDate);

      await fetch(allHorsesUrl + `/${horseId}/setPregnancy`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(formattedDate)
      }).then(async response => {
        if (!response.ok) {
          const errorMessage = await response.text()
          throw new Error(`HTTP Error ${response.status}: ${errorMessage}`)
        }
      })
        .catch(error => console.error(error))
    },

    async fetchPregnancyDate(horseId) {
      const token = getTokenFromCookie();
      return await fetch(allHorsesUrl + "/" + horseId + "/setPregnancy", {
        headers: {
          'Authorization' : `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      }).then(async response => {
        if (!response.ok) {
          const errorMessage = await response.text()
          throw new Error(`HTTP Error ${response.status}: ${errorMessage}`)
        }
        return response.json();
      }).catch(error => console.error(error))
    },

    async deleteCoverDate(horseId, date) {
      const token = getTokenFromCookie()

      console.log("Te verwijderen date: " + date);

      await fetch(allHorsesUrl + "/" + horseId + "/plannedDates", {
        method: 'DELETE',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(date)
      }).then(response => {
        if (!response.ok) {
          throw new Error('Deleting the date failed, there\'s an error: ' + response.status)
        }
        console.log('Date was succesfully deleted')
      }).catch(error => {
        console.log(error)
      })
    },

    async fetchHorseDiariesByHorseId(horseId) {
      try {
        this.isLoading = true
        const token = getTokenFromCookie()
        const response = await fetch(allHorsesUrl + '/' + horseId + '/diaries', {
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        })

        if (!response.ok) {
          throw new Error('Fetching diaries failed, there\'s an error: ' + response.status)
        }

        const data = await response.json()
        console.log('fetched diaries:', data)
        return data
      } finally {
        this.isLoading = false
      }
    },
    newHorseDiary(horseId, diaryName) {
      const token = getTokenFromCookie()
      fetch(allHorsesUrl + `/${horseId}/diaries`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          'name': diaryName
        })
      }).then(response => console.log(response))
    },
    updateHorseDiary(horseId, diaryName, diaryText) {
      const token = getTokenFromCookie()
      fetch(allHorsesUrl + `/${horseId}/diaries`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          'name': diaryName,
          'text': diaryText
        })
      }).then(response => console.log(response))
    },
    fetchHorsesByFilter(horse) {
      try {
        this.isLoading = true
        const token = getTokenFromCookie()
        fetch(allHorsesUrl, {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            'name': horse.name,
            'birthDate': horse.birthDate,
            'height': horse.height,
            'studHorse': horse.studHorse,
            'isPregnant': horse.isPregnant,
            'datePregnant': horse.datePregnant
          })
        }).then(response => {
          if (!response.ok) {
            throw new Error('Fetching horses failed, there\'s en error: ' + response.status)
          }
          console.log('Horse has been succesfully added!')
        }).catch(error => {
          console.log(error)
        })
      } finally {
        this.isLoading = false
      }
    },
    async fetchFoalsByHorseId(horseId) {
      try{
        const token = getTokenFromCookie()
        const response = await fetch (allHorsesUrl + "/" + horseId + "/foals", {
          method: 'GET',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
          });
        if(!response.ok){
          throw new Error("Could not fetch foals from horse, there's an error: " + response.status);
        }
        const data = await response.json();
        console.log('storedata: ', data);
        return data;
      }
      catch(error) {
        console.error("error fetching data: ", error);
      }
    },
    deleteHorseById(horseId) {
      try {
        this.isLoading = true
        const token = getTokenFromCookie()
        fetch(allHorsesUrl + `/${horseId}/delete`, {
          method: 'DELETE',
          headers: {
            'Authorization': `Bearer ${token}`,
            'Content-Type': 'application/json'
          }
        }).then(response => {
          if (!response.ok) {
            throw new Error('Deleting the horse failed, there\'s an error: ' + response.status)
          }
          console.log('Horse was succesfully deleted')
        }).catch(error => {
          console.log(error)
        })
      } finally {
        this.isLoading = false
      }
    },
    updateHorseById(horseId, horse) {
      const token = getTokenFromCookie()
      fetch(`${allHorsesUrl}/${horseId}/update`, {
        method: 'PUT',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({
          'name': horse.name,
          'birthDate': horse.birthDate,
          'height': horse.height,
          'studHorse': horse.studHorse,
          'isPregnant': horse.isPregnant,
          'datePregnant': horse.datePregnant
        })
      }).then(response => {
        if (!response.ok) {
          throw new Error('Updating horse failed ' + response.status)
        }
        console.log('Horse has been succesfully updated!')
      }).catch(error => {
        console.log(error)
      })
    },

    logoTinkerstal() {
      return '@/assets/LogoTinkerstal.png'
    },
    logoHorseOverview() {
      return '@/assets/HorseOverviewLogo.png'
    },
    logoHorseNavigation() {
      return '@/assets/NavHorseIcon.png'
    }
  }
})