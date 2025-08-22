import Vue from 'vue'
import { getAllTags } from '@/api/tag'

const state = Vue.observable({
  tagOptions: []
})

async function loadTag() {
  const data = await getAllTags()
  state.tagOptions = data || []
}

export function useTag() {
  return { state, loadTag }
}
