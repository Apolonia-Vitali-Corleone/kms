<template>
  <el-dialog :title="title" :visible="visible" width="800px" @close="$emit('close')">
    <el-form :model="formData" ref="form" label-width="100px">
      <el-form-item label="分类" prop="categoryName"
                    :rules="[{ required: true, message: '请选择分类', trigger: 'change' }]">
        <el-select v-model="formData.categoryName" placeholder="请选择">
          <el-option
              v-for="item in flatCategories"
              :key="item.id"
              :label="item.name"
              :value="item.name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标题" prop="title" :rules="[{ required: true, message: '请输入标题', trigger: 'blur' }]">
        <el-input v-model="formData.title"></el-input>
      </el-form-item>
      <el-form-item label="标签" prop="tagName"
                    :rules="[{ required: true, message: '请选择标签', trigger: 'change' }]">
        <el-select v-model="formData.tagName" placeholder="请选择">
          <el-option
              v-for="item in tagOptions"
              :key="item.id"
              :label="item.name"
              :value="item.name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="可见性" prop="visibilityName"
                    :rules="[{ required: true, message: '请选择可见性', trigger: 'change' }]">
        <el-select v-model="formData.visibilityName" placeholder="请选择">
          <el-option
              v-for="item in visibilityOptions"
              :key="item.id"
              :label="item.name"
              :value="item.name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="关键词">
        <el-input v-model="formData.keywords" placeholder="逗号分隔"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="formData.status">
          <el-option label="启用" :value="1"></el-option>
          <el-option label="停用" :value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="问题序号">
        <el-input-number v-model="formData.questionNo" :min="1"></el-input-number>
      </el-form-item>
      <el-form-item label="创建人">
        <el-input v-model="formData.createdBy"></el-input>
      </el-form-item>
      <el-form-item label="摘要">
        <el-input type="textarea" v-model="formData.summary"></el-input>
      </el-form-item>
      <el-form-item label="内容">
        <el-input type="textarea" :rows="5" v-model="formData.content"></el-input>
      </el-form-item>
      <el-form-item label="附件">
        <el-upload
            action=""
            :file-list="formData.attachments"
            :http-request="uploadAttachment"
            :on-remove="removeAttachment"
            :on-preview="downloadAttachment">
          <el-button type="primary" size="small">上传</el-button>
        </el-upload>
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
  name: 'KnowledgeDialog',
  props: {
    visible: { type: Boolean, required: true },
    title: { type: String, default: '' },
    formData: {
      type: Object,
      default: () => ({
        id: null,
        categoryName: '',
        title: '',
        tagName: '',
        visibilityName: '',
        keywords: '',
        status: 1,
        questionNo: 1,
        createdBy: '',
        summary: '',
        content: '',
        attachments: []
      })
    },
    flatCategories: { type: Array, default: () => [] },
    tagOptions: { type: Array, default: () => [] },
    visibilityOptions: { type: Array, default: () => [] },
    uploadAttachment: Function,
    removeAttachment: Function,
    downloadAttachment: Function
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

