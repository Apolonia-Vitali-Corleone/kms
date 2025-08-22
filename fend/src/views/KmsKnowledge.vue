<template>
  <div>
    <el-container class="kms-knowledge">
      <el-aside width="20%" class="kms-tree">
        <div class="tree-toolbar">
          <el-button type="primary" size="mini" @click="openCategoryDialog()">新增类目</el-button>
        </div>

        <!--这下面是左侧的那棵树-->
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
                @change="updateCategoryStatusByButton(data)"
                active-color="#13ce66"
                inactive-color="#909399">
            </el-switch>
          </span>
        </span>
        </el-tree>
      </el-aside>
      <!--
      这是他妈的搜索框
      数据是他妈的queryForm
      -->
      <el-main class="kms-main">
        <div class="filter-bar" ref="filterBar">
          <!-- 第一行：只有 关联类目 + 可点输入框 -->
          <el-form :inline="true" :model="queryForm" label-width="0" class="filter-row-1">
            <el-form-item size="medium">
              <el-tag>关联类目</el-tag>

              <!-- popover A -->
              <el-popover
                  ref="catPop"
                  placement="bottom-start"
                  trigger="click"
                  width="320"
              >
                <el-tree
                    ref="catTree"
                    :data="categoryTree"
                    node-key="id"
                    show-checkbox
                    :props="{ label: 'name', children: 'children' }"
                    :default-checked-keys="queryForm.categoryIds"
                    :check-strictly="true"
                    @check-change="onCatCheckChange"
                />
              </el-popover>

              <!-- popover B（保留你的同步策略） -->
              <el-popover
                  ref="catPop"
                  placement="bottom-start"
                  trigger="click"
                  width="320"
                  @show="syncTreeChecks"
              >
                <el-tree
                    ref="catTree"
                    :data="categoryTree"
                    node-key="id"
                    show-checkbox
                    :props="{ label: 'name', children: 'children' }"
                    :check-strictly="true"
                    @check-change="onCatCheckChange"
                />
              </el-popover>

              <!-- 可点输入框（展示所选标签） -->
              <div
                  class="tag-input full-tags"
                  v-popover:catPop
                  tabindex="0"
              >
                <span v-if="!queryForm.categoryIds.length" class="placeholder">请选择关联类目</span>
                <el-tag
                    v-for="id in queryForm.categoryIds"
                    :key="id"
                    closable
                    @close="removeCat(id)"
                    class="tag-chip"
                >
                  {{ id2name[id] || id }}
                </el-tag>
              </div>
            </el-form-item>
          </el-form>

          <!-- 第二行：其余筛选项 + 按钮（原逻辑不变） -->
          <el-form :inline="true" :model="queryForm" label-width="0" class="filter-row-2">
            <el-form-item>
              <el-tag>标题</el-tag>
              <el-input v-model="queryForm.title" placeholder="请输入标题" style="width: 220px; margin-left:8px;"/>
            </el-form-item>

            <el-form-item>
              <el-tag>标签</el-tag>
              <el-select v-model="queryForm.tagName" placeholder="请选择标签" clearable>
                <el-option
                    v-for="item in tagOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name"
                />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-tag>请选择状态</el-tag>
              <el-select v-model="queryForm.status" placeholder="状态" clearable>
                <el-option label="启用" :value="1"/>
                <el-option label="停用" :value="0"/>
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-tag>请选择可见度</el-tag>
              <el-select v-model="queryForm.visibilityName" placeholder="请选择标签" clearable>
                <el-option
                    v-for="item in visibilityOptions"
                    :key="item.id"
                    :label="item.name"
                    :value="item.name"
                />
              </el-select>
            </el-form-item>

            <el-form-item>
              <el-tag>问题序号</el-tag>
            </el-form-item>
            <el-form-item>
              <el-input v-model.number="queryForm.questionNo" placeholder="请输入问题序号"/>
            </el-form-item>

            <el-form-item>
              <el-tag>创建时间</el-tag>
            </el-form-item>
            <el-form-item>
              <el-date-picker
                  v-model="queryForm.createdAt"
                  type="date"
                  placeholder="请选择创建时间"
              />
            </el-form-item>

            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="loadKnowledgeList">搜索</el-button>
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

    <!-- Category Dialog -->
    <el-dialog :title="categoryDialogTitle" :visible.sync="categoryDialogVisible">
      <el-form :model="categoryForm" ref="categoryForm" label-width="80px">
        <el-form-item label="名称" prop="name" :rules="[{ required: true, message: '请输入名称', trigger: 'blur' }]">
          <el-input v-model="categoryForm.name"></el-input>
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

    <!-- Category Detail Dialog -->
    <el-dialog title="类目详情" :visible.sync="categoryDetailDialogVisible">
      <el-form label-width="80px">
        <el-form-item label="名称">{{ categoryDetail.name }}</el-form-item>
        <el-form-item label="备注">{{ categoryDetail.remark }}</el-form-item>
      </el-form>
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
import {getCategory, getRelatedCategories} from '@/api/category'
import {getAllTags} from '@/api/tag'
import {getAllVisibilities} from '@/api/visibility'
import {
  uploadAttachment as apiUploadAttachment,
  deleteAttachment as apiDeleteAttachment,
  downloadAttachment as apiDownloadAttachment
} from '../api/attachment'

export default {
  name: 'KmsKnowledge',
  data() {
    return {
      id2name: {},
      relatedCategory: [],
      tableHeight: 400, // 先声明为响应式，避免 warn
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
      // ...
      queryForm: {
        categoryIds: [],       // ✅ 存 id，发后端最稳
        title: '',
        tagName: '',
        status: undefined,
        visibilityName: '',
        questionNo: null,
        createdAt: '',
      },
      cascaderProps: {
        multiple: true,        // ✅ 多选
        checkStrictly: true,   // ✅ 父子不互相关联，想选就选
        emitPath: false,       // ✅ 只返回选中节点的 id，不返回整条路径
        value: 'id',
        label: 'name',
        children: 'children'
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
      categoryDetailDialogVisible: false,
      categoryDetail: {},
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
    syncTreeChecks() {
      this.$nextTick(() => {
        if (this.$refs.catTree) {
          this.$refs.catTree.setCheckedKeys(this.queryForm.categoryIds || [])
        }
      })
    },
    // 勾选变化时，收集所有勾选节点 id（也可限制为叶子：getCheckedKeys(true)）
    onCatCheckChange() {
      const tree = this.$refs.catTree
      this.queryForm.categoryIds = tree ? tree.getCheckedKeys() : []
    },

    // 点击标签上的 × 直接移除，同时反勾选树
    removeCat(id) {
      const tree = this.$refs.catTree
      this.queryForm.categoryIds = (this.queryForm.categoryIds || []).filter(x => x !== id)
      if (tree) tree.setChecked(id, false, true)
    },

    // onCatCheckChange() {
    //   const tree = this.$refs.catTree
    //   this.queryForm.categoryIds = tree.getCheckedKeys()
    //   // 如果你也想保持名称数组，可加：
    //   // this.queryForm.categoryNames = tree.getCheckedNodes().map(n => n.name)
    // },
    // loadKnowledgeList 同方案A，传 relatedCategoryIds: this.queryForm.categoryIds
    // 计算高度什么的，我不懂，也不需要动
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
          this.$nextTick(() => {
            this.$refs.categoryTree.setCurrentKey(this.currentCategoryId)
          })
        }
        await this.loadRelatedCategory()
      } catch (e) {
        this.$message.error(e.message)
      }
    },
    // 点击之后获取到当前的这个category的id值
    handleCategoryClick(data) {
      this.currentCategoryId = data.id
    },
    // 打开这个对话框，新增类目
    openCategoryDialog(parent) {
      this.categoryDialogTitle = '新增类目'
      // parent ? '新增类目' : '新建类目'
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
    // 修改这个category的名称
    renameCategory(data) {
      this.categoryDialogTitle = '重命名类目'
      this.categoryForm = {
        ...data,
        parentId: data.parentId || data.parent_id || null
      }
      delete this.categoryForm.parent_id
      this.categoryDialogVisible = true
    },
    // 这个是提交按钮，无论你是修改还是新增
    async submitCategory() {
      this.$refs.categoryForm.validate(async valid => {
        if (!valid) return
        try {
          const url = this.categoryForm.id ? '/category/update' : '/category/create'
          await http.post(url, this.categoryForm)
          this.$message.success('操作成功')
          this.categoryDialogVisible = false
          await this.loadCategoryTree()
          await this.loadRelatedCategory()
        } catch (e) {
          this.$message.error(e.message)
        }
      })
    },
    // 这个是category的删除按钮
    async deleteCategory(data) {
      this.$confirm('确定删除该类目吗？', '提示', {type: 'warning'}).then(async () => {
        try {
          await http.post('/category/delete', {id: data.id})
          this.$message.success('删除成功')
          await this.loadCategoryTree()
        } catch (e) {
          this.$message.error(e.message)
        }
      }).catch(() => {
      })
    },
    // 更新状态，就是那个按钮的操作，
    async updateCategoryStatusByButton(data) {
      try {
        await http.post('/category/status', {id: data.id, status: data.status})
        this.$message.success('操作成功')
      } catch (e) {
        this.$message.error(e.message)
        data.status = data.status === 1 ? 0 : 1
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
    resetQuery() {
      this.queryForm = {
        // 第1个参数
        categoryNames: [],      // ✅ 多选用数组
        // 第2个参数
        title: '',
        // 第3个参数
        tagName: '',
        // 第4个参数
        status: undefined,
        // 第5个参数
        visibilityName: '',
        // 第6个参数
        questionNo: null,
        // 第7个参数
        createdAt: '',
      }
      this.pagination.page = 1
      this.loadKnowledgeList()
    },
    //这它妈就是搜索的那个按钮
    async loadKnowledgeList() {
      const params = {
        relatedCategoryIds: this.queryForm.categoryIds,
        // relatedCategoryIds: this.queryForm.categoryIds,  // ✅
        title: this.queryForm.title,
        tagName: this.queryForm.tagName,
        status: this.queryForm.status,
        visibilityName: this.queryForm.visibilityName,
        questionNo: this.queryForm.questionNo,
        createdAt: this.queryForm.createdAt,
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
.tag-input {
  min-height: 40px;
  max-height: 140px; /* 选得特别多时，限制一下最高度 */
  overflow-y: auto; /* 超出滚动，避免把页面撑太长 */
  display: flex;
  align-items: center;
  flex-wrap: wrap; /* 允许换行 */
  gap: 6px;
  padding: 6px 10px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: text;
  box-sizing: border-box;
}

.tag-input:focus {
  outline: none;
  border-color: #409eff;
}

.tag-input .placeholder {
  color: #c0c4cc;
}

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

.kms-knowledge {
  /* 关键：让整个布局以视口为基准 */
  height: 100vh;
}

.kms-tree {
  border-right: 1px solid #ebeef5;
  overflow-y: auto;
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

.filter-bar {
  padding-bottom: 10px;
}

.pager {
  padding: 10px 0;
  text-align: right;
}

.filter-row-1 { margin-bottom: 8px; }
.filter-row-2 { margin-top: 0; }

/* 让“关联类目”这一行横向排布 */
.filter-row-1 .el-form-item__content {
  display: flex;
  align-items: center;
}

/* 让可点输入框和 el-tag 同行显示 */
.tag-input {
  display: inline-flex;   /* 原来是 flex -> 改成 inline-flex */
  margin-left: 8px;       /* 和“关联类目”留点间距 */
  vertical-align: middle;
}
</style>

