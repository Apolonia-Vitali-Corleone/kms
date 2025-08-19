<template>
  <div v-if="data">
    <h2>{{ data.title }}</h2>
    <p>类目: {{ data.categoryName }}</p>
    <p>标签: {{ data.tagName }}</p>
    <p>可见性: {{ data.visibilityName }}</p>
    <p>关键词: {{ data.keywords }}</p>
    <p>状态: {{ data.status }}</p>
    <p>问题序号: {{ data.questionNo }}</p>
    <p>简介: {{ data.summary }}</p>
    <div v-html="data.content"></div>
    <div v-if="attachments.length">
      <h3>附件</h3>
      <ul>
        <li v-for="a in attachments" :key="a"><a :href="'/api/v1/files/' + a" target="_blank">{{ a }}</a></li>
      </ul>
    </div>
    <p>创建时间: {{ data.createdAt }}</p>
  </div>
</template>
<script>
import { getOne } from '../api/knowledge'
export default {
  props: ['id'],
  data() {
    return { data: null, attachments: [] }
  },
  created() {
    getOne(this.id).then(res => {
      this.data = res
      this.attachments = res.attachments ? JSON.parse(res.attachments) : []
    })
  }
}
</script>
