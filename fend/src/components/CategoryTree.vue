<template>
  <el-aside width="20%" class="kms-tree">
    <div class="tree-toolbar">
      <el-button type="primary" size="mini" @click="openCategoryDialog()">新增类目</el-button>
    </div>
    <el-tree
      ref="categoryTree"
      :data="categoryTree"
      node-key="id"
      :props="{ label: 'name' }"
      highlight-current
      @node-click="handleCategoryClick"
      class="tree"
    >
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
            inactive-color="#909399"
          />
        </span>
      </span>
    </el-tree>

    <CategoryDialog
      :visible="categoryDialogVisible"
      :title="categoryDialogTitle"
      :formData="categoryForm"
      @submit="submitCategory"
      @close="categoryDialogVisible = false"
    />
    <CategoryDetailDialog
      :visible="categoryDetailDialogVisible"
      :detail-data="categoryDetail"
      @close="categoryDetailDialogVisible = false"
    />
  </el-aside>
</template>

<script>
import http from '../api/http'
import { getCategory } from '@/api/category'
import CategoryDialog from './CategoryDialog.vue'
import CategoryDetailDialog from './CategoryDetailDialog.vue'

export default {
  name: 'CategoryTree',
  components: { CategoryDialog, CategoryDetailDialog },
  props: {
    categoryTree: {
      type: Array,
      default: () => []
    },
    currentCategoryId: {
      type: [Number, String],
      default: null
    }
  },
  data() {
    return {
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
      categoryDetailDialogVisible: false,
      categoryDetail: {}
    }
  },
  watch: {
    categoryTree() {
      this.$nextTick(this.setCurrent)
    },
    currentCategoryId() {
      this.$nextTick(this.setCurrent)
    }
  },
  methods: {
    setCurrent() {
      if (this.$refs.categoryTree) {
        this.$refs.categoryTree.setCurrentKey(this.currentCategoryId)
      }
    },
    handleCategoryClick(data) {
      this.$emit('select', data.id)
    },
    openCategoryDialog(parent) {
      this.categoryDialogTitle = '新增类目'
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
      this.categoryDialogVisible = true
    },
    submitCategory(form) {
      const url = form.id ? '/category/update' : '/category/create'
      http.post(url, form).then(() => {
        this.$message.success('操作成功')
        this.categoryDialogVisible = false
        this.$emit('refresh')
      }).catch(e => {
        this.$message.error(e.message)
      })
    },
    async deleteCategory(data) {
      this.$confirm('确定删除该类目吗？', '提示', { type: 'warning' })
        .then(async () => {
          try {
            await http.post('/category/delete', { id: data.id })
            this.$message.success('删除成功')
            this.$emit('refresh')
          } catch (e) {
            this.$message.error(e.message)
          }
        })
        .catch(() => {})
    },
    async updateCategoryStatusByButton(data) {
      try {
        await http.post('/category/status', { id: data.id, status: data.status })
        this.$message.success('操作成功')
      } catch (e) {
        this.$message.error(e.message)
        data.status = data.status === 1 ? 0 : 1
      }
    },
    async viewCategory(data) {
      this.categoryDetail = await getCategory(data.id)
      this.categoryDetailDialogVisible = true
    }
  }
}
</script>

<style scoped>
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
</style>
