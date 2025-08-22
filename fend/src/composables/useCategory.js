import Vue from 'vue'
import http from '@/api/http'
import { getRelatedCategories } from '@/api/category'

const state = Vue.observable({
  categoryTree: [],
  flatCategories: [],
  id2name: {},
  currentCategoryId: null,
  relatedCategory: []
})

async function loadCategoryTree() {
  const data = await http.get('/category/tree')
  state.categoryTree = data || []
  state.flatCategories = []
  state.id2name = {}
  const walk = nodes => {
    nodes.forEach(n => {
      state.flatCategories.push({ id: n.id, name: n.name })
      state.id2name[n.id] = n.name
      if (n.children) walk(n.children)
    })
  }
  walk(state.categoryTree)
  if (state.categoryTree.length && !state.currentCategoryId) {
    state.currentCategoryId = state.categoryTree[0].id
  }
  await loadRelatedCategory()
}

async function loadRelatedCategory() {
  const data = await getRelatedCategories()
  state.relatedCategory = data || []
}

export function useCategory() {
  return { state, loadCategoryTree, loadRelatedCategory }
}
