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
          <el-table-column prop="visibilityName" label="可见度"></el-table-column>
          <el-table-column prop="questionNo" label="问题序号" width="100"></el-table-column>
          <el-table-column prop="createdBy" label="创建人" width="120"></el-table-column>
          <el-table-column prop="createdAt" label="创建时间" width="160"></el-table-column>
          <el-table-column label="操作" width="240">
            <template slot-scope="scope">
              <el-button type="text" size="small" @click="viewKnowledge(scope.row)">查看</el-button>
              <el-button type="text" size="small" @click="openKnowledgeDialog(scope.row)">编辑</el-button>
              <el-button type="text" size="small" @click="deleteKnowledge(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <div class="pager" ref="pager">
          <el-pagination
              background
              layout="total, sizes, prev, pager, next, jumper"
              :current-page.sync="pagination.page"
              :page-size.sync="pagination.pageSize"
              :total="pagination.total"
              :page-sizes="[10,20,50,100]"
              @current-change="loadKnowledgeList"
              @size-change="loadKnowledgeList">
          </el-pagination>
        </div>
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
  getKnowledgeList,
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

export default {
  name: 'KmsKnowledge',
  components: { CategoryTree, KnowledgeFilter },
  data() {
    return {
      id2name: {},
      relatedCategory: [],
      tableHeight: 400, // 先声明为响应式，避免 warn
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
      tableData: [],
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
    // 获取右下的这个知识列表
    this.loadKnowledgeList()
    // 获取标签
    this.loadTag()
    // 获取可见度
    this.loadVisibility()
  },
  mounted() {
    // 保留：mounted 只做高度计算与事件绑定
    this.$nextTick(() => {
      this.computeHeight()
      window.addEventListener('resize', this.computeHeight)
    })
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.computeHeight)
  },
  methods: {
    handleFilterChange(val) {
      this.filters = val
    },
    handleFilterSearch(val) {
      this.filters = val
      this.pagination.page = 1
      this.loadKnowledgeList()
    },
    handleFilterReset(val) {
      this.filters = val
      this.pagination.page = 1
      this.loadKnowledgeList()
    },
    computeHeight() {
      // 取表格真实顶部位置
      const tableEl = this.$refs.table && this.$refs.table.$el
      if (!tableEl) return
      const top = tableEl.getBoundingClientRect().top
      // 分页区高度（没有则为 0）
      const pagerH = this.$refs.pager ? this.$refs.pager.offsetHeight : 0
      // 底部留空隙，避免紧贴底边
      const gap = 12
      // 直接用视口高度计算可用空间
      const h = window.innerHeight - top - pagerH - gap
      // 最小高度兜底，避免极端情况下过小
      this.tableHeight = Math.max(h, 240)
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
      //
      //
      //
      //
    //
    // 接下来是他妈的右侧的这帮东西我操你妈的真难写
    //
    //
    //
    //
    //
    //
    // 处理选择？？不在乎这个，不用管
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 搜索框的重置按钮
      this.pagination.page = 1
      this.loadKnowledgeList()
    },
    //这它妈就是搜索的那个按钮
    async loadKnowledgeList() {
      const params = {
        relatedCategoryIds: this.filters.categoryIds,
        // relatedCategoryIds: this.filters.categoryIds,  // ✅
        title: this.filters.title,
        tagName: this.filters.tagName,
        status: this.filters.status,
        visibilityName: this.filters.visibilityName,
        questionNo: this.filters.questionNo,
        createdAt: this.filters.createdAt,
        page: this.pagination.page,
        pageSize: this.pagination.pageSize,
      }
      try {
        // 这里调用的不是search接口。
        // 按说应该是调用search接口的
        const data = await getKnowledgeList(params)
        // console.log("loadKnowledgeList----------------------")
        // console.log(data)
        // console.log(data.records)
        this.tableData = data.records
        this.pagination.total = data.total
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
          questionNo: this.tableData.length + 1,
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
          this.loadKnowledgeList()
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
          this.loadKnowledgeList()
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
          this.loadKnowledgeList()
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

