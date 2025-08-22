import Vue from 'vue'
import { searchKnowledge } from '@/api/knowledge'

const state = Vue.observable({
  tableData: [],
  pagination: {
    page: 1,
    pageSize: 10,
    total: 0
  }
})

async function search(filters = {}) {
  const payload = {
    relatedCategories: filters.categoryIds,
    title: filters.title,
    tagName: filters.tagName,
    status: filters.status,
    visibilityName: filters.visibilityName,
    questionNo: filters.questionNo,
    startDate: filters.createdAt && filters.createdAt.length ? filters.createdAt[0] : '',
    endDate: filters.createdAt && filters.createdAt.length ? filters.createdAt[1] : '',
    page: state.pagination.page,
    pageSize: state.pagination.pageSize
  }
  const data = await searchKnowledge(payload)
  state.tableData = data.records
  state.pagination.total = data.total
}

export function useKnowledge() {
  return { state, search }
}
