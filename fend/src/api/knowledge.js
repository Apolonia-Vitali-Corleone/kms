import http from './http'

export function pageKnowledge(params) {
  return http.get('/knowledge', { params })
}

export function getOne(id) {
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

export function uploadFiles(files) {
  const form = new FormData()
  files.forEach(f => form.append('files', f))
  return http.post('/files/upload', form)
}
