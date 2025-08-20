import Vue from 'vue'
import Router from 'vue-router'
import KmsKnowledge from '../views/KmsKnowledge.vue'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', component: KmsKnowledge },
    { path: '*', redirect: '/' }
  ]
})
