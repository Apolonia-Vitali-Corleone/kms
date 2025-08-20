<template>
  <div class="form-container">
    <el-form :model="form" ref="form" label-width="120px">
      <el-form-item label="Category Name" prop="name" :rules="[{ required: true, message: 'Please enter category name', trigger: 'blur' }]">
        <el-input v-model="form.name" placeholder="Please enter category name" />
      </el-form-item>
      <el-form-item label="Is Recommended" prop="recommend">
        <el-select v-model="form.recommend" placeholder="Is Recommended">
          <el-option label="Recommended" :value="1" />
          <el-option label="Not Recommended" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="Status" prop="status">
        <el-select v-model="form.status" placeholder="Status">
          <el-option label="Enabled" :value="1" />
          <el-option label="Disabled" :value="0" />
        </el-select>
      </el-form-item>
      <el-form-item label="Remark">
        <el-input v-model="form.remark" placeholder="Please enter remark" />
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
  name: 'AddCategory',
  data () {
    return {
      form: {
        name: '',
        recommend: 0,
        status: 1,
        remark: ''
      }
    }
  },
  methods: {
    submit () {
      this.$refs.form.validate(async valid => {
        if (!valid) return
        try {
          await http.post('/category/create', this.form)
          this.$message.success('Category created')
          this.$router.back()
        } catch (e) {
          this.$message.error(e.message)
        }
      })
    }
  }
}
</script>

<style scoped>
.form-container {
  max-width: 600px;
  margin: 20px auto;
  padding: 20px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
</style>

