import axios from 'axios'
import { Message } from 'element-ui'

const service = axios.create({
  baseURL: 'http://192.168.233.129:19988/api/v1'
})

service.interceptors.response.use(resp => {
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
