import http from './http'

export function getCategory(id) {
  return http.get(`/category/${id}`)
}
