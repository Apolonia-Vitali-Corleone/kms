<template>
  <div>
    <el-form :model="form" label-width="120px">
      <el-form-item label="类目">
        <el-input v-model="form.categoryName"></el-input>
      </el-form-item>
      <el-form-item label="标题">
        <el-input v-model="form.title"></el-input>
      </el-form-item>
      <el-form-item label="标签">
        <el-select v-model="form.tagName" multiple placeholder="选择标签">
          <el-option v-for="t in tags" :key="t.id" :label="t.name" :value="t.name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="可见性">
        <el-select v-model="form.visibilityName" placeholder="选择">
          <el-option v-for="v in visibilities" :key="v.id" :label="v.name" :value="v.name"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="关键词">
        <el-input v-model="form.keywords"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-switch v-model="statusSwitch" active-value="1" inactive-value="0"></el-switch>
      </el-form-item>
      <el-form-item label="简介">
        <el-input type="textarea" v-model="form.summary"></el-input>
      </el-form-item>
      <el-form-item label="问题序号">
        <el-input type="number" v-model.number="form.questionNo"></el-input>
      </el-form-item>
      <el-form-item label="内容">
        <quill-editor v-model="form.content"></quill-editor>
      </el-form-item>
      <el-form-item label="附件">
        <el-upload ref="uploader" action="" :auto-upload="false" multiple :on-change="handleFileChange">
          <el-button slot="trigger">选择文件</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submit">提交</el-button>
        <el-button @click="$router.back()">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>
<script>
import { getAllTags } from '../api/tag'
import { getAllVisibilities } from '../api/visibility'
import { createKnowledge, getOne, updateKnowledge, uploadFiles } from '../api/knowledge'

export default {
  props: ['id'],
  data() {
    return {
      form: { categoryName:'', title:'', tagName:[], visibilityName:'', keywords:'', status:1, summary:'', questionNo:1, content:'', attachments:[] },
      tags: [],
      visibilities: [],
      files: [],
      statusSwitch: '1'
    }
  },
  created() {
    getAllTags().then(res => { this.tags = res })
    getAllVisibilities().then(res => { this.visibilities = res })
    if (this.id) {
      getOne(this.id).then(res => {
        this.form = res
        this.form.tagName = res.tagName ? res.tagName.split(',') : []
        this.statusSwitch = String(res.status)
      })
    }
  },
  methods: {
    handleFileChange(file, fileList) {
      this.files = fileList.map(f => f.raw)
    },
    submit() {
      this.form.status = parseInt(this.statusSwitch)
      this.form.tagName = this.form.tagName.join(',')
      if (this.files.length > 0) {
        uploadFiles(this.files).then(paths => {
          this.form.attachments = JSON.stringify(paths)
          this.save()
        })
      } else {
        this.form.attachments = JSON.stringify([])
        this.save()
      }
    },
    save() {
      if (this.id) {
        updateKnowledge(this.id, this.form).then(() => this.$router.push('/knowledge/list'))
      } else {
        createKnowledge(this.form).then(() => this.$router.push('/knowledge/list'))
      }
    }
  }
}
</script>
