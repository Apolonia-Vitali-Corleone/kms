<template>
  <el-dialog :title="title" :visible="visible" append-to-body @close="$emit('close')">
    <el-form :model="formData" ref="form" label-width="80px">
      <el-form-item
        label="名称"
        prop="name"
        :rules="[{ required: true, message: '请输入名称', trigger: 'blur' }]"
      >
        <el-input v-model="formData.name" />
      </el-form-item>
      <el-form-item label="推荐">
        <el-select v-model="formData.recommend">
          <el-option label="否" :value="0" />
          <el-option label="是" :value="1" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="formData.status">
          <el-option label="启用" :value="1" />
          <el-option label="停用" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="备注">
        <el-input v-model="formData.remark" />
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="$emit('close')">取消</el-button>
      <el-button type="primary" @click="handleSubmit">确定</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'CategoryDialog',
  props: {
    visible: { type: Boolean, required: true },
    title: { type: String, default: '' },
    formData: {
      type: Object,
      default: () => ({
        id: null,
        name: '',
        parentId: null,
        recommend: 0,
        status: 1,
        remark: ''
      })
    }
  },
  methods: {
    handleSubmit() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.$emit('submit', { ...this.formData })
        }
      })
    }
  }
}
</script>

