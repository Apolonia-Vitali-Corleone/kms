import Vue from 'vue'
import Router from 'vue-router'
import KnowledgePage from '../views/KnowledgePage.vue'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', component: KnowledgePage },
    { path: '*', redirect: '/' }
  ]
})
