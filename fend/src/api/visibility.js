import http from './http'

export function getAllVisibilities() {
  return http.get('/visibilities')
}
