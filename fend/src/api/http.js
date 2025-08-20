import axios from 'axios'
import { Message } from 'element-ui'

// Create axios instance with base URL driven by environment configuration
const service = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL || ''
})

// Global response interceptor to unwrap backend responses
service.interceptors.response.use(resp => {
  // For file downloads, bypass the normal envelope handling
  if (resp.config && resp.config.responseType === 'blob') {
    return resp
  }

  const res = resp.data
  if (res.code !== 0) {
    Message.error(res.message || 'Error')
    return Promise.reject(res)
  }
  return res.data
}, error => {
  Message.error(error.message)
  return Promise.reject(error)
})

export default service
