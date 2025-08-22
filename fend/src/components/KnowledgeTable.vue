<template>
  <div>
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
          <el-tag :type="scope.row.status===1?'success':'info'">
            {{ scope.row.status === 1 ? '启用' : '停用' }}
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
          <el-button type="text" size="small" @click="onView(scope.row)">查看</el-button>
          <el-button type="text" size="small" @click="onEdit(scope.row)">编辑</el-button>
          <el-button type="text" size="small" @click="onDelete(scope.row)">删除</el-button>
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
  </div>
</template>

<script>
import { getKnowledgeList } from '../api/knowledge'

export default {
  name: 'KnowledgeTable',
  props: {
    filters: {
      type: Object,
      default: () => ({})
    },
    onView: {
      type: Function,
      required: true
    },
    onEdit: {
      type: Function,
      required: true
    },
    onDelete: {
      type: Function,
      required: true
    }
  },
  data() {
    return {
      tableData: [],
      tableHeight: 400,
      pagination: {
        page: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  watch: {
    filters: {
      deep: true,
      handler() {
        this.pagination.page = 1
        this.loadKnowledgeList()
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.computeHeight()
      window.addEventListener('resize', this.computeHeight)
    })
    this.loadKnowledgeList()
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.computeHeight)
  },
  methods: {
    computeHeight() {
      const tableEl = this.$refs.table && this.$refs.table.$el
      if (!tableEl) return
      const top = tableEl.getBoundingClientRect().top
      const pagerH = this.$refs.pager ? this.$refs.pager.offsetHeight : 0
      const gap = 12
      const h = window.innerHeight - top - pagerH - gap
      this.tableHeight = Math.max(h, 240)
    },
    handleSelectionChange(val) {
      this.$emit('selection-change', val)
    },
    async loadKnowledgeList() {
      const params = {
        relatedCategoryIds: this.filters.categoryIds,
        title: this.filters.title,
        tagName: this.filters.tagName,
        status: this.filters.status,
        visibilityName: this.filters.visibilityName,
        questionNo: this.filters.questionNo,
        createdAt: this.filters.createdAt,
        page: this.pagination.page,
        pageSize: this.pagination.pageSize
      }
      try {
        const data = await getKnowledgeList(params)
        this.tableData = data.records
        this.pagination.total = data.total
        this.$emit('pagination-change', { ...this.pagination })
      } catch (e) {
        this.$message.error(e.message)
      }
    }
  }
}
</script>

<style scoped>
.pager {
  margin-top: 16px;
  text-align: right;
}
</style>

