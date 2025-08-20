<template>
  <el-container class="knowledge-page">
    <el-aside class="category-panel">
      <el-tree :data="categories" node-key="id" default-expand-all>
        <span class="tree-node" slot-scope="{ node, data }">
          <span>{{ data.name }}</span>
          <span class="actions">
            <el-button type="text" size="mini" @click.stop="viewCategory(data)">详情</el-button>
            <el-button type="text" size="mini" @click.stop="editCategory(data)">修改</el-button>
            <el-button type="text" size="mini" @click.stop="removeCategory(data)">删除</el-button>
          </span>
        </span>
      </el-tree>
    </el-aside>
    <el-main class="main-panel">
      <el-form :inline="true" :model="query" class="search-form">
        <el-form-item label="关联类目">
          <el-select v-model="query.category" placeholder="请选择" clearable>
            <el-option
              v-for="c in categories"
              :key="c.id"
              :label="c.name"
              :value="c.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="query.title" placeholder="标题" />
        </el-form-item>
        <el-form-item label="标签">
          <el-input v-model="query.tags" placeholder="标签" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="query.status" placeholder="请选择" clearable>
            <el-option label="启用" :value="1" />
            <el-option label="停用" :value="0" />
          </el-select>
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="query.visibility" placeholder="请选择" clearable>
            <el-option
              v-for="opt in visibilityOptions"
              :key="opt.value"
              :label="opt.label"
              :value="opt.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="问题序号">
          <el-input v-model="query.question_no" placeholder="序号" />
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="query.created_at"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="search">搜索</el-button>
          <el-button @click="reset">重置</el-button>
        </el-form-item>
      </el-form>
      <el-table :data="tableData" border style="width:100%;margin-top:10px;">
        <el-table-column prop="title" label="标题" />
        <el-table-column prop="tags" label="标签" />
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'info'">
              {{ scope.row.status === 1 ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="visibility" label="分类" />
        <el-table-column prop="question_no" label="问题序号" width="100" />
        <el-table-column prop="created_at" label="创建时间" width="160" />
        <el-table-column label="操作" width="180">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewKnowledge(scope.row)">详情</el-button>
            <el-button type="text" size="small" @click="editKnowledge(scope.row)">修改</el-button>
            <el-button type="text" size="small" @click="deleteKnowledge(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
  </el-container>
</template>

<script>
import http from '../api/http'
export default {
  name: 'KnowledgePage',
  data () {
    return {
      categories: [],
      query: {
        category: null,
        title: '',
        tags: '',
        status: null,
        visibility: null,
        question_no: '',
        created_at: null
      },
      visibilityOptions: [
        { label: '公开', value: 'public' },
        { label: '私有', value: 'private' }
      ],
      tableData: [
        {
          id: 1,
          title: '示例知识',
          tags: '示例',
          status: 1,
          visibility: '公开',
          question_no: '001',
          created_at: '2023-01-01'
        }
      ]
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
    viewCategory (row) {
      this.$message.info(`查看 ${row.name}`)
    },
    editCategory (row) {
      this.$message.info(`修改 ${row.name}`)
    },
    removeCategory (row) {
      this.$message.info(`删除 ${row.name}`)
    },
    search () {
      this.$message.success('搜索')
    },
    reset () {
      this.query = {
        category: null,
        title: '',
        tags: '',
        status: null,
        visibility: null,
        question_no: '',
        created_at: null
      }
    },
    viewKnowledge (row) {
      this.$message.info(`查看 ${row.title}`)
    },
    editKnowledge (row) {
      this.$message.info(`修改 ${row.title}`)
    },
    deleteKnowledge (row) {
      this.$message.info(`删除 ${row.title}`)
    }
  }
}
</script>

<style scoped>
.knowledge-page {
  height: 100%;
}
.category-panel {
  width: 40%;
  border-right: 1px solid #ebeef5;
  overflow-y: auto;
}
.main-panel {
  flex: 1;
  padding: 10px;
  box-sizing: border-box;
}
.tree-node {
  display: flex;
  justify-content: space-between;
  width: 100%;
}
.actions {
  display: flex;
  gap: 4px;
}
</style>
