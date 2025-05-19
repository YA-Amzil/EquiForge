import { defineStore } from 'pinia'

const notificationsURL = 'http://localhost:8082/api/notifications'
const amountOfUnreadNotificationsURL = 'http://localhost:8082/api/notifications/unreadnotifications'
const updateAllNotificationsToReadURL = 'http://localhost:8082/api/notifications/updateallnotifications'

export const useNotificationStore = defineStore('notification', {
    state: () => ({
        readNotifications: [],
        unreadNotifications: [],
        amountOfNotSeenNotifications: 0,
        amountOfSeenNotifications: 0,
        isLoading: false,  // Optional: Add a loading state if needed
    }),
    actions: {
        async fetchNotifications() {
            this.isLoading = true;
            const token = getTokenFromTheCookie()
            try {
                const response = await fetch(notificationsURL, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                })
                
                if (!response.ok) {
                    throw new Error('Failed to fetch notifications.');
                }
                const data = await response.json();
                this.readNotifications = data.readNotificationsList;
                this.unreadNotifications = data.unreadNotificationList;
                this.amountOfNotSeenNotifications = data.amountOfUnreadNotifications;
                this.amountOfSeenNotifications = data.amountOfReadNotifications;
            } catch (error) {
                console.error(error);
            } finally {
                this.isLoading = false;
            }
        },
        async fetchUnreadNotificationAmount() {
            this.isLoading = true;
            const token = getTokenFromTheCookie()
            try {
                const response = await fetch(amountOfUnreadNotificationsURL, {
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                })

                if (!response.ok) {
                    throw new Error('Failed to fetch the amount of notifications.');
                }
                const data = await response.json();
                this.amountOfNotSeenNotifications = data;
            } catch (error) {
                console.error(error);
            } finally {
                this.isLoading = false;
            }
        },
        async updateAllTheNotificationsToRead() {

            const token = getTokenFromTheCookie()

            await fetch(updateAllNotificationsToReadURL, {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
            }).then(async response => {
                if (!response.ok) {
                    const errorMessage = await response.text()
                    throw new Error(`HTTP Error ${response.status}: ${errorMessage}`)
                }
                console.log('Response: ' + response)
            })
                .catch(error => console.error(error))
        },
        deleteNotificationById(animalId) {
            try {
                this.isLoading = true;
                const token = getTokenFromTheCookie();
                fetch(notificationsURL + `/${animalId}/delete`, {
                    method: 'DELETE',
                    headers: {
                        'Authorization': `Bearer ${token}`,
                        'Content-Type': 'application/json'
                    }
                }).then(response => {
                    if (!response.ok) {
                        throw new Error('Deleting the notification failed, there\'s an error: ' + response.status)
                    }

                    const unreadIndex = this.unreadNotifications.findIndex(notification => notification.animalId === animalId);
                    const readIndex = this.readNotifications.findIndex(notification => notification.animalId === animalId);

                    console.log("Before removing " + this.amountOfSeenNotifications + " READ");
                    console.log("Before removing " + this.amountOfNotSeenNotifications + " UNREAD");

                    if (unreadIndex !== -1) {
                        this.unreadNotifications = this.unreadNotifications.filter(notification => notification.animalId !== animalId);
                        this.amountOfNotSeenNotifications--;
                    } else if (readIndex !== -1) {
                        this.readNotifications = this.readNotifications.filter(notification => notification.animalId !== animalId);
                        this.amountOfSeenNotifications--;
                    }

                    console.log("After removing " + this.amountOfSeenNotifications + " READ");
                    console.log("After removing " + this.amountOfNotSeenNotifications + " UNREAD");

                    console.log('Notification was succesfully deleted')
                }).catch(error => {
                    console.log(error)
                })
            } finally {
                this.isLoading = false;
            }
        },
    }
})

function getTokenFromTheCookie() {
    const cookies = document.cookie.split(';')
    for (const element of cookies) {
        const cookie = element.trim()
        if (cookie.startsWith('jsonwebtoken=')) {
            const token = cookie.substring('jsonwebtoken='.length);
            console.log("Token from cookie: ", token);
            return token;
        }
    }
    return null;
}