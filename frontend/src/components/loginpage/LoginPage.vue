<template>
  <v-container class="login-container">
    <v-img class="mx-auto my-6" :height="100" :src="image" alt="Tinkerstal logo"></v-img>
    <v-card style="background-color: transparent;" class="mx-auto pb-8 form-container" elevation="0" max-width="448"
      rounded="lg">
      <div class="text-subtitle-1 text-medium-emphasis">Account</div>

      <v-text-field v-model="usernameOrEmail" density="compact" placeholder="Gebruikersnaam of E-mailadres"
        prepend-inner-icon="mdi:mdi-account-outline" variant="outlined" required>
      </v-text-field>

      <div class="text-subtitle-1 text-medium-emphasis d-flex align-center justify-space-between">
        Wachtwoord
      </div>

      <v-text-field v-model="password" :append-inner-icon="visible ? 'mdi:mdi-eye-off' : 'mdi:mdi-eye'"
        :type="visible ? 'text' : 'password'" density="compact" placeholder="Wachtwoord"
        prepend-inner-icon="mdi:mdi-lock-outline" variant="outlined" required
        @click:append-inner="visible = !visible"></v-text-field>

      <v-card class="mb-12" color="surface-variant" variant="tonal">
        <!-- <v-card-text class="text-medium-emphasis text-caption">
            Warning: After 3 consecutive failed login attempts, your account will be temporarily locked for
            three hours. If you must log in now, you can also click "Forgot login password?" below to reset the
            login password.
        </v-card-text> -->
      </v-card>

      <v-btn @click="submitForm" class="mb-8" color="white" size="large" variant="tonal" block>
        Inloggen
      </v-btn>

      <router-link class="text-caption text-decoration-none text-white" to="/forgot-password">
        <p style="text-align: center;">Wachtwoord vergeten?</p>

      </router-link>
    </v-card>
  </v-container>
</template>

<script>

import image from '@/assets/LogoTinkerstal2.png'

export default {
  name: 'LoginPage',
  data() {
    return {
      image: image,
      usernameOrEmail: '',
      password: '',
      visible: false
    }
  },
  methods: {
    async submitForm() {
      const usernameOrEmail = this.usernameOrEmail
      const password = this.password

      if (!usernameOrEmail || !password) {
        console.error('Unauthorized access: Username or password cannot be empty')
        return
      }

      console.log('Logging in with:', usernameOrEmail, password)

      try {
        const basicAuth = 'Basic ' + btoa(usernameOrEmail + ':' + password)
        console.log(basicAuth)
        const response = await fetch('http://localhost:8082/api/users/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': basicAuth
          }
        })

        if (response.ok) {
          const data = await response.json()
          console.log('Success:', data)
          document.cookie = `jsonwebtoken=${data.token}; path=/;`
          this.$router.push({ name: 'home' }) // Redirect to home page
        } else if (response.status === 401) {
          console.error('Unauthorized access: Invalid username or password')
        } else {
          console.error('Error logging in:', response.statusText)
        }
      } catch (error) {
        console.error('Error logging in:', error)
      }
    }
  }
}

</script>