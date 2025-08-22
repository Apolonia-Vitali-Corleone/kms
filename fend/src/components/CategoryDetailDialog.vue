<template>
  <el-dialog title="类目详情" :visible="visible" append-to-body @close="$emit('close')">
    <el-form label-width="80px">
      <el-form-item label="名称">{{ detailData.name }}</el-form-item>
      <el-form-item label="备注">{{ detailData.remark }}</el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
export default {
  name: 'CategoryDetailDialog',
  props: {
    visible: { type: Boolean, required: true },
    detailData: {
      type: Object,
      default: () => ({})
    }
  }
}
</script>

