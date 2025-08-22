import http from './http'

export function searchKnowledge(params) {
  return http.post('/knowledge/search', params)
}

export function getKnowledge(id) {
  return http.get(`/knowledge/${id}`)
}

export function createKnowledge(data) {
  return http.post('/knowledge', data)
}

export function updateKnowledge(id, data) {
  return http.put(`/knowledge/${id}`, data)
}

export function deleteKnowledge(id) {
  return http.delete(`/knowledge/${id}`)
}

export function batchDeleteKnowledge(ids) {
  return http.delete('/knowledge', { data: { ids } })
}
