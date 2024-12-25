import { createRouter, createWebHashHistory } from 'vue-router'

const routes = [
  {
    path: '/element',
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
  {
    path: '/',
    name: 'feijiejie',
    component: () => import('../views/feijiejie.vue'),
    meta: { title: '肺结节检测' }
  },
  {
    path: '/shendanshu',
    name: 'tree',
    component: () => import('../views/tree.vue'),
    meta: { title: 'Merry Christmas!' }
  }
]

const router = createRouter({
  history: createWebHashHistory(),
  routes
})

export default router
