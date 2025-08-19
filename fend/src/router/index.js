import Vue from 'vue'
import Router from 'vue-router'
import KnowledgeList from '../views/KnowledgeList.vue'
import KnowledgeForm from '../views/KnowledgeForm.vue'
import KnowledgeDetail from '../views/KnowledgeDetail.vue'

Vue.use(Router)

export default new Router({
  routes: [
    { path: '/knowledge/list', component: KnowledgeList },
    { path: '/knowledge/create', component: KnowledgeForm },
    { path: '/knowledge/edit/:id', component: KnowledgeForm, props: true },
    { path: '/knowledge/detail/:id', component: KnowledgeDetail, props: true },
    { path: '*', redirect: '/knowledge/list' }
  ]
})
