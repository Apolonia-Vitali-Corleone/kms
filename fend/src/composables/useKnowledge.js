import Vue from 'vue'
import { getKnowledgeList } from '@/api/knowledge'

const state = Vue.observable({
  tableData: [],
  pagination: {
    page: 1,
    pageSize: 10,
    total: 0
  }
})

async function loadKnowledgeList(filters) {
  const params = {
    relatedCategoryIds: filters.categoryIds,
    title: filters.title,
    tagName: filters.tagName,
    status: filters.status,
    visibilityName: filters.visibilityName,
    questionNo: filters.questionNo,
    createdAt: filters.createdAt,
    page: state.pagination.page,
    pageSize: state.pagination.pageSize
  }
  const data = await getKnowledgeList(params)
  state.tableData = data.records
  state.pagination.total = data.total
}

export function useKnowledge() {
  return { state, loadKnowledgeList }
}
