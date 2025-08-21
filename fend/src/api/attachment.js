import http from './http'

export function uploadAttachment(file, knowledgeId) {
  const formData = new FormData()
  formData.append('file', file)
  formData.append('knowledgeId', knowledgeId)
  return http.post('/attachment/upload', formData, {
    // headers: {'Content-Type': 'multipart/form-data'}
  })
}

export function deleteAttachment(id) {
  return http.post('/attachment/delete', {id})
}

export function downloadAttachment(id) {
  return http.get('/attachment/download', {params: {id}, responseType: 'blob'})
}

