import './assets/main.css'

// Main
import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import router from './router'

// Vuetify
import 'vuetify/styles'
import { createVuetify } from 'vuetify'
import * as components from 'vuetify/components'
import * as directives from 'vuetify/directives'
import { en, nl } from 'vuetify/locale'

// FontAwesome
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { aliases, fa } from 'vuetify/iconsets/fa-svg'
import { library } from '@fortawesome/fontawesome-svg-core'
import { fas } from '@fortawesome/free-solid-svg-icons'
import { far } from '@fortawesome/free-regular-svg-icons'
library.add(far);
library.add(fas);

// MDI
import '@mdi/font/css/materialdesignicons.css'
import { mdi } from 'vuetify/iconsets/mdi'

// Vuetify App
const vuetify = createVuetify({
  components,
  directives,
  theme: {
    defaultTheme: 'dark'
  },
  locale: {
    locale: 'nl',
    messages: { nl, en }
  },
  icons: {
    defaultSet: 'fa',
    aliases,
    sets: {
      fa,
      mdi,
    },
  },
});

//Global Vue Error Exception Handler



//launch Vue App
const vueApp = createApp(App)
  .use(vuetify)
  .use(createPinia())
  .use(router)
  .component('font-awesome-icon', FontAwesomeIcon)

// Replace -> app.mount('#app')
router.isReady().then(() => {
  vueApp.mount('#app')
})