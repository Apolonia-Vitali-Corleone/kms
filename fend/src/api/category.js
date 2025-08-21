import http from './http'

export function getCategory(id) {
  return http.get(`/category/${id}`)
}

export function getRelatedCategories() {
  return http.get(`/category`)
}
