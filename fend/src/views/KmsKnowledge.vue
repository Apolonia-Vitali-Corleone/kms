<template>
  <div>
  <el-container class="kms-knowledge">
    <el-aside width="240px" class="kms-tree">
      <div class="tree-toolbar">
        <el-button type="primary" size="mini" @click="openCategoryDialog()">新建类目</el-button>
      </div>
      <el-tree
        ref="categoryTree"
        :data="categoryTree"
        node-key="id"
        :props="{ label: 'name' }"
        highlight-current
        @node-click="handleCategoryClick"
        class="tree">
        <span class="custom-tree-node" slot-scope="{ node, data }">
          <span>{{ data.name }}</span>
          <span class="tree-actions">
            <el-tooltip content="新增" placement="top">
              <i class="el-icon-plus" @click.stop="openCategoryDialog(data)"></i>
            </el-tooltip>
            <el-tooltip content="重命名" placement="top">
              <i class="el-icon-edit" @click.stop="renameCategory(data)"></i>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <i class="el-icon-delete" @click.stop="deleteCategory(data)"></i>
            </el-tooltip>
            <el-tooltip content="上移" placement="top">
              <i class="el-icon-arrow-up" @click.stop="moveCategory(data, 'up')"></i>
            </el-tooltip>
            <el-tooltip content="下移" placement="top">
              <i class="el-icon-arrow-down" @click.stop="moveCategory(data, 'down')"></i>
            </el-tooltip>
            <el-switch
              v-model="data.status"
              :active-value="1"
              :inactive-value="0"
              @change="toggleCategoryStatus(data)"
              active-color="#13ce66"
              inactive-color="#909399">
            </el-switch>
          </span>
        </span>
      </el-tree>
    </el-aside>
    <el-main class="kms-main">
      <div class="filter-bar">
        <el-form :inline="true" :model="queryForm" label-width="0">
          <el-form-item>
            <el-input v-model="queryForm.keywords" placeholder="关键词"></el-input>
          </el-form-item>
          <el-form-item>
            <el-select v-model="queryForm.status" placeholder="状态" clearable>
              <el-option label="启用" :value="1"></el-option>
              <el-option label="停用" :value="0"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-date-picker
              v-model="queryForm.created"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              value-format="yyyy-MM-dd">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="fetchList">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
            <el-button type="primary" icon="el-icon-plus" @click="openKnowledgeDialog()">新增知识</el-button>
            <el-button type="danger" icon="el-icon-delete" @click="batchDelete" :disabled="!multipleSelection.length">批量删除</el-button>
            <el-button type="success" icon="el-icon-download" @click="exportExcel">导出Excel</el-button>
          </el-form-item>
        </el-form>
      </div>
      <el-table
        ref="table"
        :data="tableData"
        border
        height="calc(100% - 110px)"
        @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55"></el-table-column>
        <el-table-column prop="title" label="知识标题"></el-table-column>
        <el-table-column prop="status" label="知识状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="scope.row.status===1?'success':'info'">{{ scope.row.status===1?'启用':'停用' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="keywords" label="关键词"></el-table-column>
        <el-table-column prop="category_name" label="知识分类"></el-table-column>
        <el-table-column prop="question_no" label="问题序号" width="100"></el-table-column>
        <el-table-column prop="created_by" label="创建人" width="120"></el-table-column>
        <el-table-column prop="created_at" label="创建时间" width="160"></el-table-column>
        <el-table-column label="操作" width="240">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="viewKnowledge(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="openKnowledgeDialog(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="deleteKnowledge(scope.row)">删除</el-button>
            <el-button type="text" size="small" @click="openAttachmentDialog(scope.row)">附件</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pager">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :current-page.sync="pagination.page"
          :page-size.sync="pagination.page_size"
          :total="pagination.total"
          :page-sizes="[10,20,50,100]"
          @current-change="fetchList"
          @size-change="fetchList">
        </el-pagination>
      </div>
    </el-main>
  </el-container>

  <!-- Category Dialog -->
  <el-dialog :title="categoryDialogTitle" :visible.sync="categoryDialogVisible">
    <el-form :model="categoryForm" ref="categoryForm" label-width="80px">
      <el-form-item label="名称" prop="name" :rules="[{ required: true, message: '请输入名称', trigger: 'blur' }]">
        <el-input v-model="categoryForm.name"></el-input>
      </el-form-item>
      <el-form-item label="父级">
        <el-select v-model="categoryForm.parent_id" placeholder="请选择">
          <el-option :value="null" label="根类目"></el-option>
          <el-option
            v-for="item in flatCategories"
            :key="item.id"
            :label="item.name"
            :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      
      <el-form-item label="排序">
        <el-input-number v-model="categoryForm.sort" :min="0"></el-input-number>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="categoryForm.status">
          <el-option label="启用" :value="1"></el-option>
          <el-option label="停用" :value="0"></el-option>
        </el-select>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="categoryDialogVisible=false">取消</el-button>
      <el-button type="primary" @click="submitCategory">确定</el-button>
    </div>
  </el-dialog>

  <!-- Knowledge Dialog -->
  <el-dialog :title="knowledgeDialogTitle" :visible.sync="knowledgeDialogVisible" width="800px">
    <el-form :model="knowledgeForm" ref="knowledgeForm" label-width="100px">
      <el-form-item label="分类" prop="category_id" :rules="[{ required: true, message: '请选择分类', trigger: 'change' }]">
        <el-select v-model="knowledgeForm.category_id" placeholder="请选择">
          <el-option
            v-for="item in flatCategories"
            :key="item.id"
            :label="item.name"
            :value="item.id"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="标题" prop="title" :rules="[{ required: true, message: '请输入标题', trigger: 'blur' }]">
        <el-input v-model="knowledgeForm.title"></el-input>
      </el-form-item>
      <el-form-item label="标签">
        <el-input v-model="knowledgeForm.tags" placeholder="逗号分隔"></el-input>
      </el-form-item>
      <el-form-item label="关键词">
        <el-input v-model="knowledgeForm.keywords" placeholder="逗号分隔"></el-input>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="knowledgeForm.status">
          <el-option label="启用" :value="1"></el-option>
          <el-option label="停用" :value="0"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="类型">
        <el-select v-model="knowledgeForm.knowledge_type">
          <el-option label="问答" value="Q"></el-option>
          <el-option label="文档" value="D"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="问题序号">
        <el-input-number v-model="knowledgeForm.question_no" :min="1"></el-input-number>
      </el-form-item>
      <el-form-item label="摘要">
        <el-input type="textarea" v-model="knowledgeForm.summary"></el-input>
      </el-form-item>
      <el-form-item label="内容">
        <el-input type="textarea" :rows="5" v-model="knowledgeForm.content"></el-input>
      </el-form-item>
      <el-form-item label="附件">
        <el-upload
          action=""
          :file-list="knowledgeForm.attachments"
          :http-request="uploadAttachment"
          :on-remove="removeAttachment"
          :on-preview="downloadAttachment">
          <el-button type="primary" size="small">上传</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
    <div slot="footer">
      <el-button @click="knowledgeDialogVisible=false">取消</el-button>
      <el-button type="primary" @click="submitKnowledge">确定</el-button>
    </div>
  </el-dialog>
  </div>
</template>

<script>
import axios from 'axios'
const BASE_URL = 'http://localhost:19989'
const api = axios.create({ baseURL: BASE_URL })

function handleResponse (res) {
  if (res.data && res.data.code === 0) {
    return res.data
  } else {
    throw new Error(res.data ? res.data.message : '网络错误')
  }
}

export default {
  name: 'KmsKnowledge',
  data () {
    return {
      categoryTree: [],
      flatCategories: [],
      currentCategoryId: null,
      categoryDialogVisible: false,
      categoryDialogTitle: '',
      categoryForm: {
        id: null,
        name: '',
        parent_id: null,
        sort: 0,
        status: 1
      },
      queryForm: {
        keywords: '',
        status: undefined,
        created: []
      },
      tableData: [],
      pagination: {
        page: 1,
        page_size: 10,
        total: 0
      },
      multipleSelection: [],
      knowledgeDialogVisible: false,
      knowledgeDialogTitle: '',
      knowledgeForm: {
        id: null,
        category_id: null,
        title: '',
        tags: '',
        keywords: '',
        status: 1,
        knowledge_type: 'Q',
        question_no: 1,
        summary: '',
        content: '',
        attachments: []
      }
    }
  },
  mounted () {
    this.fetchCategoryTree()
  },
  methods: {
    async fetchCategoryTree () {
      try {
        const res = await api.get('/api/category/tree')
        const data = handleResponse(res)
        this.categoryTree = data.data || []
        this.flatCategories = []
        const walk = (nodes) => {
          nodes.forEach(n => {
            this.flatCategories.push({ id: n.id, name: n.name })
            if (n.children) walk(n.children)
          })
        }
        walk(this.categoryTree)
        if (this.categoryTree.length && !this.currentCategoryId) {
          this.currentCategoryId = this.categoryTree[0].id
          this.$nextTick(() => {
            this.$refs.categoryTree.setCurrentKey(this.currentCategoryId)
          })
          this.fetchList()
        }
      } catch (e) {
        this.$message.error(e.message)
      }
    },
    handleCategoryClick (data) {
      this.currentCategoryId = data.id
      this.pagination.page = 1
      this.fetchList()
    },
    openCategoryDialog (parent) {
      this.categoryDialogTitle = parent ? '新增类目' : '新建类目'
      this.categoryForm = {
        id: null,
        name: '',
        parent_id: parent ? parent.id : null,
        sort: 0,
        status: 1
      }
      this.categoryDialogVisible = true
    },
    renameCategory (data) {
      this.categoryDialogTitle = '重命名类目'
      this.categoryForm = Object.assign({}, data)
      this.categoryDialogVisible = true
    },
    async submitCategory () {
      this.$refs.categoryForm.validate(async valid => {
        if (!valid) return
        try {
          const url = this.categoryForm.id ? '/api/category/update' : '/api/category/create'
          const res = await api.post(url, this.categoryForm)
          handleResponse(res)
          this.$message.success('操作成功')
          this.categoryDialogVisible = false
          this.fetchCategoryTree()
        } catch (e) {
          this.$message.error(e.message)
        }
      })
    },
    async deleteCategory (data) {
      this.$confirm('确定删除该类目吗？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await api.post('/api/category/delete', { id: data.id })
          handleResponse(res)
          this.$message.success('删除成功')
          this.fetchCategoryTree()
        } catch (e) {
          this.$message.error(e.message)
        }
      }).catch(() => {})
    },
    async moveCategory (data, direction) {
      try {
        const res = await api.post('/api/category/sort', { id: data.id, direction })
        handleResponse(res)
        this.fetchCategoryTree()
      } catch (e) {
        this.$message.error(e.message)
      }
    },
    async toggleCategoryStatus (data) {
      try {
        const res = await api.post('/api/category/status', { id: data.id, status: data.status })
        handleResponse(res)
        this.$message.success('操作成功')
      } catch (e) {
        this.$message.error(e.message)
        data.status = data.status === 1 ? 0 : 1
      }
    },
    resetQuery () {
      this.queryForm = { keywords: '', status: undefined, created: [] }
      this.pagination.page = 1
      this.fetchList()
    },
    handleSelectionChange (val) {
      this.multipleSelection = val
    },
    async fetchList () {
      const params = {
        page: this.pagination.page,
        page_size: this.pagination.page_size,
        category_id: this.currentCategoryId,
        status: this.queryForm.status,
        keywords: this.queryForm.keywords,
        created_from: this.queryForm.created && this.queryForm.created[0],
        created_to: this.queryForm.created && this.queryForm.created[1]
      }
      try {
        const res = await api.post('/api/knowledge/page', params)
        const data = handleResponse(res).data
        this.tableData = data.records
        this.pagination.total = data.total
      } catch (e) {
        this.$message.error(e.message)
      }
    },
    openKnowledgeDialog (row) {
      if (row && row.id) {
        this.knowledgeDialogTitle = '编辑知识'
        this.knowledgeForm = Object.assign({}, row, { attachments: row.attachments || [] })
      } else {
        this.knowledgeDialogTitle = '新增知识'
        this.knowledgeForm = {
          id: null,
          category_id: this.currentCategoryId,
          title: '',
          tags: '',
          keywords: '',
          status: 1,
          knowledge_type: 'Q',
          question_no: this.tableData.length + 1,
          summary: '',
          content: '',
          attachments: []
        }
      }
      this.knowledgeDialogVisible = true
    },
    openAttachmentDialog (row) {
      this.openKnowledgeDialog(row)
    },
    viewKnowledge (row) {
      this.$alert(row.content, row.title)
    },
    async submitKnowledge () {
      this.$refs.knowledgeForm.validate(async valid => {
        if (!valid) return
        try {
          const url = this.knowledgeForm.id ? '/api/knowledge/update' : '/api/knowledge/create'
          const res = await api.post(url, this.knowledgeForm)
          handleResponse(res)
          this.$message.success('操作成功')
          this.knowledgeDialogVisible = false
          this.fetchList()
        } catch (e) {
          this.$message.error(e.message)
        }
      })
    },
    async deleteKnowledge (row) {
      this.$confirm('确定删除该知识吗？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await api.post('/api/knowledge/delete', { id: row.id })
          handleResponse(res)
          this.$message.success('删除成功')
          this.fetchList()
        } catch (e) {
          this.$message.error(e.message)
        }
      }).catch(() => {})
    },
    batchDelete () {
      if (!this.multipleSelection.length) return
      const ids = this.multipleSelection.map(i => i.id)
      this.$confirm(`确定删除选中的 ${ids.length} 条知识吗？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await api.post('/api/knowledge/batch_delete', { ids })
          handleResponse(res)
          this.$message.success('删除成功')
          this.fetchList()
        } catch (e) {
          this.$message.error(e.message)
        }
      }).catch(() => {})
    },
    exportExcel () {
      const params = {
        category_id: this.currentCategoryId,
        status: this.queryForm.status,
        keywords: this.queryForm.keywords,
        created_from: this.queryForm.created && this.queryForm.created[0],
        created_to: this.queryForm.created && this.queryForm.created[1]
      }
      api.post('/api/knowledge/export', params, { responseType: 'blob' }).then(res => {
        const disposition = res.headers['content-disposition'] || ''
        let filename = 'knowledge.xlsx'
        const match = disposition.match(/filename="?([^";]+)"?/)
        if (match) filename = decodeURIComponent(match[1])
        const blob = new Blob([res.data])
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = filename
        a.click()
        window.URL.revokeObjectURL(url)
      }).catch(e => {
        this.$message.error(e.message)
      })
    },
    uploadAttachment (request) {
      const formData = new FormData()
      formData.append('file', request.file)
      api.post('/api/attachment/upload', formData, {
        headers: { 'Content-Type': 'multipart/form-data' }
      }).then(res => {
        const data = handleResponse(res).data
        this.knowledgeForm.attachments.push({
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
      window.open(`${BASE_URL}/api/attachment/download?id=${file.file_id}`)
    },
    removeAttachment (file, fileList) {
      api.post('/api/attachment/delete', { id: file.file_id }).then(res => {
        handleResponse(res)
        this.$message.success('删除成功')
        this.knowledgeForm.attachments = fileList
      }).catch(e => {
        this.$message.error(e.message)
      })
    }
  }
}
</script>

<style scoped>
.kms-knowledge {
  height: 100%;
}
.kms-tree {
  border-right: 1px solid #ebeef5;
  overflow-y: auto;
}
.tree-toolbar {
  padding: 10px;
  text-align: center;
}
.tree {
  padding: 0 10px 10px;
}
.custom-tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}
.tree-actions i {
  font-size: 14px;
  margin-left: 5px;
  cursor: pointer;
  color: #409EFF;
}
.kms-main {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 10px;
  box-sizing: border-box;
}
.filter-bar {
  padding-bottom: 10px;
}
.pager {
  padding: 10px 0;
  text-align: right;
}
</style>

