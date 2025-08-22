<template>
  <el-dialog title="知识详情" :visible="visible" width="60%" @close="$emit('close')">
    <h3>知识标题： {{ detailData.title }}</h3>
    <h3>知识标签： {{ detailData.tagName }}</h3>
    <h3>知识状态： {{ detailData.status }}</h3>
    <h3>关键词： {{ detailData.keywords }}</h3>
    <h3>知识分类： {{ detailData.categoryName }}</h3>
    <h3>问题序号： {{ detailData.questionNo }}</h3>
    <h3>创建人： {{ detailData.createdBy }}</h3>
    <h3>创建时间： {{ detailData.createdAt }}</h3>
    <div v-html="detailData.content"></div>
  </el-dialog>
</template>

<script>
export default {
  name: 'KnowledgeDetailDialog',
  props: {
    visible: { type: Boolean, required: true },
    detailData: {
      type: Object,
      default: () => ({})
    }
  }
}
</script>

