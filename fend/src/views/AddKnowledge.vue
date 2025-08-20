<template>
  <div class="form-container">
    <el-form :model="form" ref="form" label-width="150px">
      <el-form-item label="Knowledge Category" prop="category_id" :rules="[{ required: true, message: 'Please select knowledge category', trigger: 'change' }]">
        <el-select v-model="form.category_id" placeholder="Please select knowledge category">
          <el-option v-for="c in categories" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="Knowledge Title" prop="title" :rules="[{ required: true, message: 'Please enter title', trigger: 'blur' }]">
        <el-input v-model="form.title" placeholder="Please enter title" />
      </el-form-item>
      <el-form-item label="Knowledge Introduction">
        <el-input type="textarea" v-model="form.intro" placeholder="Please enter introduction" />
      </el-form-item>
      <el-form-item label="Knowledge Tags" prop="tags" :rules="[{ required: true, message: 'Please select tags', trigger: 'change' }]">
        <el-select v-model="form.tags" multiple placeholder="Please select tags">
          <el-option v-for="t in tagOptions" :key="t" :label="t" :value="t" />
        </el-select>
      </el-form-item>
      <el-form-item label="Knowledge Status" prop="status" :rules="[{ required: true, message: 'Please select status', trigger: 'change' }]">
        <el-select v-model="form.status" placeholder="Please select status">
          <el-option label="Enabled" :value="1" />
          <el-option label="Disabled" :value="0" />
          <el-option label="Draft" :value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="Keywords">
        <el-input v-model="form.keywords" placeholder="Please enter keywords" />
      </el-form-item>
      <el-form-item label="Knowledge Classification" prop="classification" :rules="[{ required: true, message: 'Please select classification', trigger: 'change' }]">
        <el-select v-model="form.classification" placeholder="Please select classification">
          <el-option v-for="opt in classificationOptions" :key="opt.value" :label="opt.label" :value="opt.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="Question Serial Number">
        <el-input-number v-model="form.question_no" :min="0" />
      </el-form-item>
      <el-form-item label="Knowledge Content" prop="content" :rules="[{ required: true, message: 'Please enter content', trigger: 'blur' }]">
        <quill-editor v-model="form.content" :options="editorOption" placeholder="Please enter content" />
      </el-form-item>
      <el-form-item label="Knowledge Attachments">
        <el-upload
          action=""
          :file-list="form.attachments"
          :http-request="uploadAttachment"
          :on-remove="removeAttachment"
          :on-preview="downloadAttachment"
        >
          <el-button type="primary" size="small">Upload</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit">Submit</el-button>
        <el-button @click="$router.back()">Cancel</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import http from '../api/http'
export default {
  name: 'AddKnowledge',
  data () {
    return {
      categories: [],
      tagOptions: ['General', 'FAQ', 'Guide'],
      classificationOptions: [
        { label: 'Question', value: 'Q' },
        { label: 'Document', value: 'D' }
      ],
      editorOption: {
        modules: {
          toolbar: [
            ['bold', 'italic', 'underline'],
            [{ 'header': 1 }, { 'header': 2 }],
            [{ 'list': 'ordered' }, { 'list': 'bullet' }],
            [{ 'align': [] }],
            ['blockquote'],
            [{ 'size': ['small', false, 'large', 'huge'] }],
            [{ 'color': [] }, { 'background': [] }],
            ['link', 'image']
          ]
        },
        theme: 'snow'
      },
      form: {
        category_id: null,
        title: '',
        intro: '',
        tags: [],
        status: null,
        keywords: '',
        classification: null,
        question_no: 0,
        content: '',
        attachments: []
      }
    }
  },
  mounted () {
    this.fetchCategories()
  },
  methods: {
    async fetchCategories () {
      try {
        const data = await http.get('/category/tree')
        this.categories = data || []
      } catch (e) {
        this.$message.error(e.message)
      }
    },
    submit () {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        try {
          const payload = { ...this.form, tags: this.form.tags.join(',') }
          await http.post('/knowledge/create', payload)
          this.$message.success('Knowledge created')
          this.$router.back()
        } catch (e) {
          this.$message.error(e.message)
        }
      })
    },
    uploadAttachment (request) {
      const formData = new FormData()
      formData.append('file', request.file)
      http.post('/attachment/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      }).then(data => {
        this.form.attachments.push({
          file_id: data.file_id,
          file_name: data.file_name,
          size: data.size,
          url: data.url
        })
        request.onSuccess()
      }).catch(err => {
        this.$message.error(err.message)
        request.onError()
      })
    },
    downloadAttachment (file) {
      window.open(`${http.defaults.baseURL}/attachment/download?id=${file.file_id}`)
    },
    removeAttachment (file, fileList) {
      http.post('/attachment/delete', { id: file.file_id }).then(() => {
        this.$message.success('Deleted')
        this.form.attachments = fileList
      }).catch(e => {
        this.$message.error(e.message)
      })
    }
  }
}
</script>

<style scoped>
.form-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
</style>

