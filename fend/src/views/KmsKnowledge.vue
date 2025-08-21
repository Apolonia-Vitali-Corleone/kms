<template>
  <div>
    <el-container class="kms-knowledge">
      <el-aside width="20%" class="kms-tree">
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
            <el-tooltip content="查看" placement="top">
              <i class="el-icon-view" @click.stop="viewCategory(data)"></i>
            </el-tooltip>
            <el-tooltip content="新增" placement="top">
              <i class="el-icon-plus" @click.stop="openCategoryDialog(data)"></i>
            </el-tooltip>
            <el-tooltip content="重命名" placement="top">
              <i class="el-icon-edit" @click.stop="renameCategory(data)"></i>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <i class="el-icon-delete" @click.stop="deleteCategory(data)"></i>
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
        <div class="filter-bar" ref="filterBar">

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
              <el-input v-model="queryForm.categoryName" placeholder="分类名称"></el-input>
            </el-form-item>
            <el-form-item>
              <el-select v-model="queryForm.tagName" placeholder="标签" clearable>
                <el-option
                    v-for="item in tagOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-select v-model="queryForm.visibilityName" placeholder="可见性" clearable>
                <el-option
                    v-for="item in visibilityOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-input v-model.number="queryForm.questionNo" placeholder="问题序号"></el-input>
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
              <el-button type="danger" icon="el-icon-delete" @click="batchDelete" :disabled="!multipleSelection.length">
                批量删除
              </el-button>
              <el-button type="success" icon="el-icon-download" @click="exportExcel">导出Excel</el-button>
            </el-form-item>
          </el-form>
        </div>

        <el-table
            ref="table"
            :data="tableData"
            border
            :height="tableHeight"
            @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55"></el-table-column>
          <el-table-column prop="title" label="知识标题"></el-table-column>
          <el-table-column prop="tagName" label="知识标签"></el-table-column>
          <el-table-column prop="status" label="知识状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status===1?'success':'info'">{{
                  scope.row.status === 1 ? '启用' : '停用'
                }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="keywords" label="关键词"></el-table-column>
          <el-table-column prop="categoryName" label="知识分类"></el-table-column>
          <el-table-column prop="questionNo" label="问题序号" width="100"></el-table-column>
          <el-table-column prop="createdBy" label="创建人" width="120"></el-table-column>
          <el-table-column prop="createdAt" label="创建时间" width="160"></el-table-column>
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
        <el-form-item label="父级" v-if="showParentSelect">
          <el-select v-model="categoryForm.parentId" placeholder="请选择">
            <el-option :value="null" label="根类目"></el-option>
            <el-option
                v-for="item in flatCategories"
                :key="item.id"
                :label="item.name"
                :value="item.id"></el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="推荐">
          <el-select v-model="categoryForm.recommend">
            <el-option label="否" :value="0"></el-option>
            <el-option label="是" :value="1"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="categoryForm.status">
            <el-option label="启用" :value="1"></el-option>
            <el-option label="停用" :value="0"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="categoryForm.remark"></el-input>
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
        <el-form-item label="分类" prop="categoryName"
                      :rules="[{ required: true, message: '请选择分类', trigger: 'change' }]">
          <el-select v-model="knowledgeForm.categoryName" placeholder="请选择">
            <el-option
                v-for="item in flatCategories"
                :key="item.id"
                :label="item.name"
                :value="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="标题" prop="title" :rules="[{ required: true, message: '请输入标题', trigger: 'blur' }]">
          <el-input v-model="knowledgeForm.title"></el-input>
        </el-form-item>
        <el-form-item label="标签" prop="tagName" :rules="[{ required: true, message: '请选择标签', trigger: 'change' }]">
          <el-select v-model="knowledgeForm.tagName" placeholder="请选择">
            <el-option
                v-for="item in tagOptions"
                :key="item.id"
                :label="item.name"
                :value="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="可见性" prop="visibilityName" :rules="[{ required: true, message: '请选择可见性', trigger: 'change' }]">
          <el-select v-model="knowledgeForm.visibilityName" placeholder="请选择">
            <el-option
                v-for="item in visibilityOptions"
                :key="item.id"
                :label="item.name"
                :value="item.name"></el-option>
          </el-select>
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
        <el-form-item label="问题序号">
          <el-input-number v-model="knowledgeForm.questionNo" :min="1"></el-input-number>
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

    <!-- Category Detail Dialog -->
    <el-dialog title="类目详情" :visible.sync="categoryDetailDialogVisible">
      <el-form label-width="80px">
        <el-form-item label="名称">{{ categoryDetail.name }}</el-form-item>
        <el-form-item label="备注">{{ categoryDetail.remark }}</el-form-item>
      </el-form>
    </el-dialog>

    <!-- Knowledge Detail Dialog -->
    <el-dialog title="知识详情" :visible.sync="knowledgeDetailDialogVisible" width="60%">
      <h3>{{ knowledgeDetail.title }}</h3>
      <div v-html="knowledgeDetail.content"></div>
    </el-dialog>
  </div>
</template>

<script>
import http from '../api/http'
import {
  getKnowledgeList,
  getKnowledge,
  createKnowledge,
  updateKnowledge,
  deleteKnowledge as deleteKnowledgeApi,
  batchDeleteKnowledge
} from '../api/knowledge'
import {getCategory} from '../api/category'
import {getAllTags} from '../api/tag'
import {getAllVisibilities} from '../api/visibility'
import {
  uploadAttachment as apiUploadAttachment,
  deleteAttachment as apiDeleteAttachment,
  downloadAttachment as apiDownloadAttachment
} from '../api/attachment'

export default {
  name: 'KmsKnowledge',
  data() {
    return {
      categoryTree: [],
      flatCategories: [],
      currentCategoryId: null,
      categoryDialogVisible: false,
      categoryDialogTitle: '',
      categoryForm: {
        id: null,
        name: '',
        parentId: null,
        recommend: 0,
        status: 1,
        remark: ''
      },
      showParentSelect: false,
      queryForm: {
        keywords: '',
        status: undefined,
        categoryName: '',
        tagName: '',
        visibilityName: '',
        questionNo: null,
        created: []
      },
      tagOptions: [],
      visibilityOptions: [],
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
        categoryName: '',
        title: '',
        tagName: '',
        visibilityName: '',
        keywords: '',
        status: 1,
        questionNo: 1,
        summary: '',
        content: '',
        attachments: []
      },
      categoryDetailDialogVisible: false,
      categoryDetail: {},
      knowledgeDetailDialogVisible: false,
      knowledgeDetail: {}
    }
  },
  mounted() {
    this.fetchCategoryTree()
    this.loadTags()
    this.loadVisibilities()
  },
  methods: {
    async fetchCategoryTree() {
      try {
        const data = await http.get('/category/tree')
        this.categoryTree = data || []
        this.flatCategories = []
        const walk = (nodes) => {
          nodes.forEach(n => {
            this.flatCategories.push({id: n.id, name: n.name})
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
    handleCategoryClick(data) {
      this.currentCategoryId = data.id
      this.pagination.page = 1
      this.fetchList()
    },
    openCategoryDialog(parent) {
      this.categoryDialogTitle = parent ? '新增类目' : '新建类目'
      this.showParentSelect = false
      this.categoryForm = {
        id: null,
        name: '',
        parentId: parent ? parent.id : null,
        recommend: 0,
        status: 1,
        remark: ''
      }
      this.categoryDialogVisible = true
    },
    renameCategory(data) {
      this.categoryDialogTitle = '重命名类目'
      this.categoryForm = {
        ...data,
        parentId: data.parentId || data.parent_id || null
      }
      delete this.categoryForm.parent_id
      this.showParentSelect = true
      this.categoryDialogVisible = true
    },
    async submitCategory() {
      this.$refs.categoryForm.validate(async valid => {
        if (!valid) return
        try {
          const url = this.categoryForm.id ? '/category/update' : '/category/create'
          await http.post(url, this.categoryForm)
          this.$message.success('操作成功')
          this.categoryDialogVisible = false
          this.fetchCategoryTree()
        } catch (e) {
          this.$message.error(e.message)
        }
      })
    },
    async deleteCategory(data) {
      this.$confirm('确定删除该类目吗？', '提示', {type: 'warning'}).then(async () => {
        try {
          await http.post('/category/delete', {id: data.id})
          this.$message.success('删除成功')
          this.fetchCategoryTree()
        } catch (e) {
          this.$message.error(e.message)
        }
      }).catch(() => {
      })
    },
    async toggleCategoryStatus(data) {
      try {
        await http.post('/category/status', {id: data.id, status: data.status})
        this.$message.success('操作成功')
      } catch (e) {
        this.$message.error(e.message)
        data.status = data.status === 1 ? 0 : 1
      }
    },
    resetQuery() {
      this.queryForm = {
        keywords: '',
        status: undefined,
        categoryName: '',
        tagName: '',
        visibilityName: '',
        questionNo: null,
        created: []
      }
      this.pagination.page = 1
      this.fetchList()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },

    async fetchList() {
      const params = {
        page: this.pagination.page,
        page_size: this.pagination.page_size,
        category_id: this.currentCategoryId,
        status: this.queryForm.status,
        keywords: this.queryForm.keywords,
        categoryName: this.queryForm.categoryName,
        tagName: this.queryForm.tagName,
        visibilityName: this.queryForm.visibilityName,
        questionNo: this.queryForm.questionNo,
        startDate: this.queryForm.created && this.queryForm.created[0],
        endDate: this.queryForm.created && this.queryForm.created[1]
      }
      try {
        const data = await getKnowledgeList(params)
        this.tableData = data.records
        console.log(this.tableData)
        this.pagination.total = data.total
      } catch (e) {
        this.$message.error(e.message)
      }
    },

    openKnowledgeDialog(row) {
      if (row && row.id) {
        this.knowledgeDialogTitle = '编辑知识'
        this.knowledgeForm = Object.assign({}, row, {
          attachments: (row.attachments || []).map(a => ({
            ...a,
            name: a.fileName
          }))
        })
      } else {
        this.knowledgeDialogTitle = '新增知识'
        const current = this.flatCategories.find(c => c.id === this.currentCategoryId)
        this.knowledgeForm = {
          id: null,
          categoryName: current ? current.name : '',
          title: '',
          tagName: '',
          visibilityName: '',
          keywords: '',
          status: 1,
          questionNo: this.tableData.length + 1,
          summary: '',
          content: '',
          attachments: []
        }
      }
      this.knowledgeDialogVisible = true
    },

    openAttachmentDialog(row) {
      this.openKnowledgeDialog(row)
    },

    async viewKnowledge(row) {
      try {
        this.knowledgeDetail = await getKnowledge(row.id)
        this.knowledgeDetailDialogVisible = true
      } catch (e) {
        this.$message.error(e.message)
      }
    },

    async viewCategory(data) {
      try {
        this.categoryDetail = await getCategory(data.id)
        this.categoryDetailDialogVisible = true
      } catch (e) {
        this.$message.error(e.message)
      }
    },

    async submitKnowledge() {
      this.$refs.knowledgeForm.validate(async valid => {
        if (!valid) return
        try {
          const payload = {
            ...this.knowledgeForm,
            attachments: (this.knowledgeForm.attachments || []).map(a => ({
              id: a.id,
              fileName: a.fileName || a.name,
              url: a.url
            }))
          }
          if (this.knowledgeForm.id) {
            await updateKnowledge(this.knowledgeForm.id, payload)
          } else {
            await createKnowledge(payload)
          }
          this.$message.success('操作成功')
          this.knowledgeDialogVisible = false
          this.fetchList()
        } catch (e) {
          this.$message.error(e.message)
        }
      })
    },

    async deleteKnowledge(row) {
      this.$confirm('确定删除该知识吗？', '提示', {type: 'warning'}).then(async () => {
        try {
          await deleteKnowledgeApi(row.id)
          this.$message.success('删除成功')
          this.fetchList()
        } catch (e) {
          this.$message.error(e.message)
        }
      }).catch(() => {
      })
    },

    batchDelete() {
      if (!this.multipleSelection.length) return
      const ids = this.multipleSelection.map(i => i.id)
      this.$confirm(`确定删除选中的 ${ids.length} 条知识吗？`, '提示', {type: 'warning'}).then(async () => {
        try {
          await batchDeleteKnowledge(ids)
          this.$message.success('删除成功')
          this.fetchList()
        } catch (e) {
          this.$message.error(e.message)
        }
      }).catch(() => {
      })
    },

    exportExcel() {
      const params = {
        category_id: this.currentCategoryId,
        status: this.queryForm.status,
        keywords: this.queryForm.keywords,
        categoryName: this.queryForm.categoryName,
        tagName: this.queryForm.tagName,
        visibilityName: this.queryForm.visibilityName,
        questionNo: this.queryForm.questionNo,
        startDate: this.queryForm.created && this.queryForm.created[0],
        endDate: this.queryForm.created && this.queryForm.created[1]
      }
      http.post('/knowledge/export', params, {responseType: 'blob'}).then(res => {
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

    async loadTags() {
      try {
        this.tagOptions = await getAllTags() || []
      } catch (e) {
        this.$message.error(e.message)
      }
    },

    async loadVisibilities() {
      try {
        this.visibilityOptions = await getAllVisibilities() || []
      } catch (e) {
        this.$message.error(e.message)
      }
    },

    uploadAttachment(request) {
      apiUploadAttachment(request.file).then(data => {
        this.knowledgeForm.attachments.push({
          id: data.id,
          fileName: data.fileName,
          url: data.url,
          name: data.fileName
        })
        request.onSuccess()
      }).catch(err => {
        this.$message.error(err.message)
        request.onError()
      })
    },

    downloadAttachment(file) {
      apiDownloadAttachment(file.id).then(res => {
        const blob = new Blob([res.data])
        const url = window.URL.createObjectURL(blob)
        const a = document.createElement('a')
        a.href = url
        a.download = file.fileName || file.name
        a.click()
        window.URL.revokeObjectURL(url)
      })
    },

    removeAttachment(file, fileList) {
      apiDeleteAttachment(file.id).then(() => {
        this.$message.success('删除成功')
        this.knowledgeForm.attachments = fileList.map(f => ({
          id: f.id,
          fileName: f.fileName || f.name,
          url: f.url,
          name: f.name
        }))
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

