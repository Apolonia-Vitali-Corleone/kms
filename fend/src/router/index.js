import Vue from 'vue'
import Router from 'vue-router'
import KnowledgePage from '../views/KnowledgePage.vue'
import AddCategory from '../views/AddCategory.vue'
import AddKnowledge from '../views/AddKnowledge.vue'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/', component: KnowledgePage },
    { path: '/category/add', component: AddCategory },
    { path: '/knowledge/add', component: AddKnowledge },
    { path: '*', redirect: '/' }
  ]
})
