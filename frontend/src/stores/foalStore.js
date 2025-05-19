import { defineStore } from 'pinia'
import {getTokenFromCookie} from '@/helpFunctions/Cookies.js'

const allFoalsUrl = 'http://localhost:8082/api/foals'
const addFoalAPI = 'http://localhost:8082/api/foals/create-new'

export const useFoalStore = defineStore ("foal", {
    state: () => ({
        foals: [],
        foal: {}
    }),
    actions: {
        async addFoal(foal) {
            console.log(JSON.stringify(foal));
            try {
                const token = getTokenFromCookie();
                const response = await fetch(addFoalAPI, {
                    method: 'POST',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(foal)
                })
                if(!response.ok){
                    throw new Error("fetching foals failed, there's an error: " + response.status)
                }

                console.log('foal has been succesfully added!')
            } catch (error) {
                console.log(error)
            }
        },
        async fetchFoal(foalId){
            try {
                const token = getTokenFromCookie()
                const response = await fetch(`${allFoalsUrl}/${foalId}`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                })

                if (!response.ok) {
                    throw new Error('Failed to fetch foal with ID: ' + foalId)
                }

                const data = await response.json()
                console.log('Fetched foal:')
                console.log(data)
                this.foal = data
                return data
            } catch (error) {
                console.error('There seems to be a problem fetching a foal', error)
            }
        },
        async deleteFoalById(foalId){
            try {
                const token = getTokenFromCookie();
                const response = await fetch(`${allFoalsUrl}/${foalId}/delete`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                });

                if(!response.ok) {
                    throw new Error('failed to delete foal with ID: ' + foalId);
                }
                console.log("deleted foal");
            } catch (error) {
                console.error('There seems to be a problem deleting a foal', error)
            }
        },
        async updateFoal(foal, foalId){
            try {
                const token = getTokenFromCookie()
                const response = await fetch(`${allFoalsUrl}/${foalId}/update`, {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(foal)
                })

                if (!response.ok) {
                    throw new Error('Failed to update foal with ID: ' + foal.id)
                }

                const data = await response.json()
                console.log('Updated foal:')
                console.log(data)
                this.foal = data
                return data
            } catch (error) {
                console.error('There seems to be a problem updating the foal', error)
            }

        },
        async fetchScheduleById(foalId){
            try {
                const token = getTokenFromCookie()
                const response = await fetch(`${allFoalsUrl}/${foalId}/schedule`, {
                    method: 'GET',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                })

                if (!response.ok) {
                    throw new Error('Failed to fetch schedule for foal with ID: ' + foalId)
                }

                const data = JSON.parse(await response.text())
                console.log('Fetched schedule:')
                console.log(data)
                return data
            } catch (error) {
                console.error('There seems to be a problem fetching a schedule', error)
            }
        }
    }
})