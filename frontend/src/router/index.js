import { createRouter, createWebHistory } from 'vue-router'
import {getTokenFromCookie} from "@/helpFunctions/Cookies.js";
import HomeView from '../views/HomeView.vue'
import HorseDetailPageView from '../views/HorseDetailPageView.vue'
import AddHorsePageView from '@/views/AddHorsePageView.vue'
import BirthdiaryView from '@/views/BirthdiaryView.vue'
import FilterPageView from '@/views/FilterPageView.vue'
import LoginView from '@/views/LoginView.vue'
import FoalPageView from "@/views/FoalPageView.vue";
import NotificationPageView from '@/views/NotificationPageView.vue'
import FoalDetailPageView from "@/views/FoalDetailPageView.vue";
import AddFoalPageView from "@/views/AddFoalPageView.vue";
import UpdateFoalPageView from "@/views/UpdateFoalPageView.vue";
import ChangeHorsePageView from "@/views/ChangeHorsePageView.vue";
import ForgotPasswordView from '@/views/ForgotPasswordView.vue'
import ResetPasswordView from '@/views/ResetPasswordView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'login',
      component: LoginView
    },
    {
      path: '/forgot-password',
      name: 'forgot-password',
      component: ForgotPasswordView
    },
    {
      path: '/reset-password',
      name: 'reset-password',
      component: ResetPasswordView
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      props: true
    },
    {
      path: '/filter',
      name: 'filter',
      component: FilterPageView
    },
    {
      path: '/horsedetail/:horseId',
      name: 'horse',
      component: HorseDetailPageView,
      props: true
    },
    {
      path: '/addhorse',
      name: 'addhorse',
      component: AddHorsePageView
    },
    {
      path: '/horsedetail/:horseId/diaries',
      name: 'BirthdiaryView',
      component: BirthdiaryView,
      props: true
    },
    {
      path: '/horsedetail/:horseId/foals',
      name: 'foalPageView',
      component: FoalPageView,
    },
    {
      path: '/horsedetail/:horseId/foals/:foalId',
      name: 'foalDetailPageView',
      component: FoalDetailPageView,
    },
    {
      path: '/horsedetail/:horseId/addFoal',
      name: 'addFoalView',
      component: AddFoalPageView,
    },
    {
      path: '/horsedetail/:horseId/foals/:foalId/update',
      name: 'UpdateFoalPageView',
      component: UpdateFoalPageView,
    },
    {
      path: '/notifications',
      name: 'NotificationView',
      component: NotificationPageView
    },
    {
      path: '/horsedetail/:horseId/update',
      name: 'UpdateView',
      component: ChangeHorsePageView,
      props: true,
    }
  ]
})

router.beforeEach((to, from, next) => {
  const token = getTokenFromCookie()

  if (to.name !== 'login' && to.name !== 'forgot-password' && to.name !== 'reset-password' && !token) {
    // Redirect to the login page if trying to access a protected route without a token
    next({ name: 'login' })
  } else if (to.name === 'login' && token) {
    // Redirect to the home page if already logged in and trying to access the login page
    next({ name: 'home' })
  } else {
    // Continue navigation
    next()
  }
})

export default router

