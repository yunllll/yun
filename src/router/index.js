import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/',
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
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
