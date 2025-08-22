<template>
  <div>
    <el-container class="kms-knowledge">
      <CategoryTree
        :category-tree="categoryTree"
        :current-category-id="currentCategoryId"
        @select="currentCategoryId = $event"
        @refresh="loadCategoryTree"
      />
      <!--
      这是他妈的搜索框
      数据是他妈的queryForm
      -->
      <el-main class="kms-main">
        <KnowledgeFilter
          :category-tree="categoryTree"
          :id2name="id2name"
          :tag-options="tagOptions"
          :visibility-options="visibilityOptions"
          :disable-batch="!multipleSelection.length"
          @change="handleFilterChange"
          @search="handleFilterSearch"
          @reset="handleFilterReset"
          @add="openKnowledgeDialog()"
          @batch-delete="batchDelete"
          @export="exportExcel"
        />
        <KnowledgeTable
          ref="knowledgeTable"
          :filters="filters"
          :on-view="viewKnowledge"
          :on-edit="openKnowledgeDialog"
          :on-delete="deleteKnowledge"
          @selection-change="handleSelectionChange"
          @pagination-change="handlePaginationChange"
        />
      </el-main>
    </el-container>

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
        <el-form-item label="标签" prop="tagName"
                      :rules="[{ required: true, message: '请选择标签', trigger: 'change' }]">
          <el-select v-model="knowledgeForm.tagName" placeholder="请选择">
            <el-option
                v-for="item in tagOptions"
                :key="item.id"
                :label="item.name"
                :value="item.name"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="可见性" prop="visibilityName"
                      :rules="[{ required: true, message: '请选择可见性', trigger: 'change' }]">
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
        <el-form-item label="创建人">
          <el-input v-model="knowledgeForm.createdBy"></el-input>
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

      <!-- Knowledge Detail Dialog -->
      <el-dialog title="知识详情" :visible.sync="knowledgeDetailDialogVisible" width="60%">
      <h3>知识标题： {{ knowledgeDetail.title }}</h3>
      <h3>知识标签： {{ knowledgeDetail.tagName }}</h3>
      <h3>知识状态： {{ knowledgeDetail.status }}</h3>
      <h3>关键词： {{ knowledgeDetail.keywords }}</h3>
      <h3>知识分类： {{ knowledgeDetail.categoryName }}</h3>
      <h3>问题序号： {{ knowledgeDetail.questionNo }}</h3>
      <h3>创建人： {{ knowledgeDetail.createdBy }}</h3>
      <h3>创建时间： {{ knowledgeDetail.createdAt }}</h3>
      <div v-html="knowledgeDetail.content"></div>
    </el-dialog>
  </div>
</template>

<script>
import http from '../api/http'
import {
  getKnowledge,
  createKnowledge,
  updateKnowledge,
  deleteKnowledge as deleteKnowledgeApi,
  batchDeleteKnowledge
} from '../api/knowledge'
import {getRelatedCategories} from '@/api/category'
import {getAllTags} from '@/api/tag'
import {getAllVisibilities} from '@/api/visibility'
import {
  uploadAttachment as apiUploadAttachment,
  deleteAttachment as apiDeleteAttachment,
  downloadAttachment as apiDownloadAttachment
} from '../api/attachment'
import CategoryTree from '../components/CategoryTree.vue'
import KnowledgeFilter from '../components/KnowledgeFilter.vue'
import KnowledgeTable from '../components/KnowledgeTable.vue'

export default {
  name: 'KmsKnowledge',
  components: { CategoryTree, KnowledgeFilter, KnowledgeTable },
  data() {
    return {
      id2name: {},
      relatedCategory: [],
      categoryTree: [],
      flatCategories: [],
      currentCategoryId: null,
      filters: {
        categoryIds: [],
        title: '',
        tagName: '',
        status: undefined,
        visibilityName: '',
        questionNo: null,
        createdAt: '',
      },
      tagOptions: [],
      visibilityOptions: [],
      pagination: {
        page: 1,
        pageSize: 10,
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
        createdBy: '',
        summary: '',
        content: '',
        attachments: []
      },
      knowledgeDetailDialogVisible: false,
      knowledgeDetail: {}
    }
  },

  computed: {
    categoryNamesText() {
      const tree = this.$refs.catTree
      if (!tree) return ''
      return tree.getCheckedNodes().map(n => n.name).join('、')
    }
  },

  created() {
    // 先自动获取左侧的树
    this.loadCategoryTree()
    // 获取相关类目
    this.loadRelatedCategory()
    // 获取标签
    this.loadTag()
    // 获取可见度
    this.loadVisibility()
  },
  methods: {
    handleFilterChange(val) {
      this.filters = val
    },
    handleFilterSearch(val) {
      this.filters = val
    },
    handleFilterReset(val) {
      this.filters = val
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handlePaginationChange(p) {
      this.pagination = p
    },
    // 获取相关类目
    async loadRelatedCategory() {
      try {
        const data = await getRelatedCategories();
        this.relatedCategory = data
        console.log("from loadRelatedCategory")
        console.log(data)
      } catch (e) {
        this.$message.error(e.message)
      }
    },
    // 这是他妈的获取左侧的那棵树的
    // 覆盖你现有的 loadCategoryTree，把 id2name 填好
    async loadCategoryTree() {
      try {
        const data = await http.get('/category/tree')
        this.categoryTree = data || []
        this.flatCategories = []
        this.id2name = {}
        const walk = (nodes) => {
          nodes.forEach(n => {
            this.flatCategories.push({id: n.id, name: n.name})
            this.id2name[n.id] = n.name                // <== 建立映射
            if (n.children) walk(n.children)
          })
        }
        walk(this.categoryTree)
        // 其余保持你的原逻辑...
        if (this.categoryTree.length && !this.currentCategoryId) {
          this.currentCategoryId = this.categoryTree[0].id
        }
        await this.loadRelatedCategory()
      } catch (e) {
        this.$message.error(e.message)
      }
    },
    // ？？？？？
    async openKnowledgeDialog(row) {
      if (row && row.id) {
        this.knowledgeDialogTitle = '编辑知识'
        try {
          const data = await getKnowledge(row.id)

          this.knowledgeForm = Object.assign({}, data, {
            attachments: (data.attachments || []).map(a => ({
              id: a.id,
              name: a.fileName || a.name,
              url: a.url,
              size: a.size
            }))
          })
        } catch (e) {
          this.$message.error(e.message)
          return
        }
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
          questionNo: this.pagination.total + 1,
          createdBy: '',
          summary: '',
          content: '',
          attachments: []
        }
      }
      this.knowledgeDialogVisible = true
    },

    async viewKnowledge(row) {
      try {
        this.knowledgeDetail = await getKnowledge(row.id)
        console.log("你在查看什么？")
        console.log(this.knowledgeDetail)
        this.knowledgeDetailDialogVisible = true
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
              fileName: a.name || a.fileName,
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
          this.$refs.knowledgeTable.loadKnowledgeList()
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
          this.$refs.knowledgeTable.loadKnowledgeList()
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
           this.$refs.knowledgeTable.loadKnowledgeList()
        } catch (e) {
          this.$message.error(e.message)
        }
      }).catch(() => {
      })
    },

    exportExcel() {
      const params = {
        category_id: this.currentCategoryId,
        status: this.filters.status,
        keywords: this.filters.keywords,
        categoryName: this.filters.categoryName,
        tagName: this.filters.tagName,
        visibilityName: this.filters.visibilityName,
        questionNo: this.filters.questionNo,
        startDate: this.filters.createdAt,
        endDate: this.filters.createdAt
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

    async loadTag() {
      try {
        this.tagOptions = await getAllTags() || []
      } catch (e) {
        this.$message.error(e.message)
      }
    },

    async loadVisibility() {
      try {
        this.visibilityOptions = await getAllVisibilities() || []
      } catch (e) {
        this.$message.error(e.message)
      }
    },

    uploadAttachment(request) {
      if (!this.knowledgeForm.id) {
        this.$message.error('请先保存知识')
        request.onError()
        return
      }
      apiUploadAttachment(request.file, this.knowledgeForm.id).then(data => {
        request.onSuccess(data, request.file)
        this.knowledgeForm.attachments.push({
          id: data.id,
          name: data.name,
          url: data.url,
          size: data.size
        })
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
          name: f.name,
          url: f.url,
          size: f.size
        }))
      }).catch(e => {
        this.$message.error(e.message)
      })
    }
  },
}
</script>

<style scoped>
/* 标签输入样式：看起来像 el-input，支持自动增高换行 */



.tag-chip {
  margin: 2px 4px 2px 0;
}

/* 让选择框高度自适应、标签可换行并留点间距 */
.full-tags .el-input__inner {
  height: auto !important;
  min-height: 40px; /* 基础高度 */
  line-height: normal;
  padding-top: 6px;
  padding-bottom: 6px;
}

.full-tags .el-select__tags {
  max-width: 100%;
  white-space: normal; /* 允许换行 */
}

.full-tags .el-select__tags .el-tag {
  margin: 4px 6px 0 0; /* 标签间距更美观 */
}

.kms-knowledge {
  height: 100%;
}


.kms-main {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 10px;
  box-sizing: border-box;
}


.pager {
  padding: 10px 0;
  text-align: right;
}

.kms-knowledge {
  /* 关键：让整个布局以视口为基准 */
  height: 100vh;
}


/* 主区用列布局并允许子项伸展 */
.kms-main {
  display: flex;
  flex-direction: column;
  height: 100%;
  padding: 10px;
  box-sizing: border-box;
  /* 关键：允许内部子项在 flex 容器中正确计算高度 */
  min-height: 0;
}


.pager {
  padding: 10px 0;
  text-align: right;
}


/* 让“关联类目”这一行横向排布 */

/* 让可点输入框和 el-tag 同行显示 */

