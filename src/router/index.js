import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '',
    name: 'element',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '../views/element.vue')
  },
  {
    path: '/dataui',
    name: 'dataui',
    component: () => import('../views/dataui.vue')
  },
  {
    path: '/vanTa',
    name: 'vanTa',
    component: () => import('../views/vanTa.vue')
  },
  {
    path: '/xiTong',
    name: 'xiTong',
    component: () => import('../views/xiTong.vue')
  },
  {
    path: '/pose',
    name: 'pose',
    component: () => import('../views/pose.vue')
  },
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/login.vue'),
    meta: { title: '登录' }
  },
  {
    path: '/validCode',
    name: 'validCode',
    component: () => import('../components/validCode.vue')
  },
  {
    path: '/phoneVaild',
    name: 'phoneVaild',
    component: () => import('../components/phoneVaild.vue')
  },
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
