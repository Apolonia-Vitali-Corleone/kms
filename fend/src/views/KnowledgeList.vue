<template>
  <div>
    <el-form inline @submit.native.prevent="fetchData">
      <el-form-item label="标题">
        <el-input v-model="query.title"></el-input>
      </el-form-item>
      <el-form-item label="关键词">
        <el-input v-model="query.keywords"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" placeholder="全部" style="width:100px">
          <el-option label="全部" :value="''"></el-option>
          <el-option label="启用" :value="1"></el-option>
          <el-option label="停用" :value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-button type="primary" @click="fetchData">搜索</el-button>
      <el-button type="success" @click="$router.push('/knowledge/create')">新增</el-button>
    </el-form>
    <el-table :data="list" style="width:100%" border>
      <el-table-column prop="title" label="标题"></el-table-column>
      <el-table-column prop="tagName" label="标签"></el-table-column>
      <el-table-column prop="categoryName" label="分类"></el-table-column>
      <el-table-column prop="status" label="状态"></el-table-column>
      <el-table-column prop="questionNo" label="问题序号"></el-table-column>
      <el-table-column prop="createdAt" label="创建时间"></el-table-column>
      <el-table-column label="操作">
        <template slot-scope="scope">
          <el-button size="mini" @click="toDetail(scope.row.id)">详情</el-button>
          <el-button size="mini" type="primary" @click="toEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" type="danger" @click="remove(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      layout="prev, pager, next"
      :total="total"
      :page-size="query.pageSize"
      @current-change="pageChange"
      style="margin-top:20px;text-align:right;">
    </el-pagination>
  </div>
</template>
<script>
import { pageKnowledge, deleteKnowledge } from '../api/knowledge'

export default {
  data() {
    return {
      query: { page: 1, pageSize: 10, title: '', keywords: '', status: '' },
      list: [],
      total: 0
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      pageKnowledge(this.query).then(res => {
        this.list = res.records
        this.total = res.total
      })
    },
    pageChange(p) {
      this.query.page = p
      this.fetchData()
    },
    toDetail(id) {
      this.$router.push(`/knowledge/detail/${id}`)
    },
    toEdit(id) {
      this.$router.push(`/knowledge/edit/${id}`)
    },
    remove(id) {
      this.$confirm('确定删除?').then(() => {
        deleteKnowledge(id).then(() => this.fetchData())
      })
    }
  }
}
</script>
