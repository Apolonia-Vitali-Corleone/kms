<template>
  <div class="filter-bar">
    <!-- 第一行：关联类目 -->
    <el-form :inline="true" :model="queryForm" label-width="0" class="filter-row-1">
      <el-form-item size="medium">
        <el-tag>关联类目</el-tag>
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
        <div class="tag-input full-tags" v-popover:catPop tabindex="0">
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

    <!-- 第二行：其余筛选项 + 按钮 -->
    <el-form :inline="true" :model="queryForm" label-width="0" class="filter-row-2">
      <el-form-item>
        <el-tag>标题</el-tag>
        <el-input v-model="queryForm.title" placeholder="请输入标题" style="width: 220px; margin-left:8px;" />
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
          <el-option label="启用" :value="1" />
          <el-option label="停用" :value="0" />
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
        <el-input v-model.number="queryForm.questionNo" placeholder="请输入问题序号" />
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
        <el-button type="primary" icon="el-icon-search" @click="onSearch">搜索</el-button>
        <el-button icon="el-icon-refresh" @click="onReset">重置</el-button>
        <el-button type="primary" icon="el-icon-plus" @click="$emit('add')">新增知识</el-button>
        <el-button type="danger" icon="el-icon-delete" @click="$emit('batch-delete')" :disabled="disableBatch">批量删除</el-button>
        <el-button type="success" icon="el-icon-download" @click="$emit('export')">导出Excel</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
export default {
  name: 'KnowledgeFilter',
  props: {
    categoryTree: {
      type: Array,
      default: () => []
    },
    id2name: {
      type: Object,
      default: () => ({})
    },
    tagOptions: {
      type: Array,
      default: () => []
    },
    visibilityOptions: {
      type: Array,
      default: () => []
    },
    disableBatch: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      queryForm: {
        categoryIds: [],
        title: '',
        tagName: '',
        status: undefined,
        visibilityName: '',
        questionNo: null,
        createdAt: ''
      }
    }
  },
  watch: {
    queryForm: {
      deep: true,
      handler(val) {
        this.$emit('change', { ...val })
      }
    }
  },
  methods: {
    syncTreeChecks() {
      this.$nextTick(() => {
        if (this.$refs.catTree) {
          this.$refs.catTree.setCheckedKeys(this.queryForm.categoryIds || [])
        }
      })
    },
    onCatCheckChange() {
      const tree = this.$refs.catTree
      this.queryForm.categoryIds = tree ? tree.getCheckedKeys() : []
    },
    removeCat(id) {
      const tree = this.$refs.catTree
      this.queryForm.categoryIds = (this.queryForm.categoryIds || []).filter(x => x !== id)
      if (tree) tree.setChecked(id, false, true)
    },
    onSearch() {
      this.$emit('search', { ...this.queryForm })
    },
    onReset() {
      this.queryForm = {
        categoryIds: [],
        title: '',
        tagName: '',
        status: undefined,
        visibilityName: '',
        questionNo: null,
        createdAt: ''
      }
      this.$emit('reset', { ...this.queryForm })
    }
  }
}
</script>

<style scoped>
.filter-bar {
  margin-bottom: 10px;
}
.full-tags {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
}
.tag-chip {
  margin-right: 4px;
  margin-top: 4px;
}
.placeholder {
  color: #ccc;
}
.filter-row-1 { margin-bottom: 8px; }
.filter-row-2 { margin-top: 0; }
.filter-row-1 .el-form-item__content {
  display: flex;
  align-items: center;
}
.tag-input {
  display: inline-flex;
  margin-left: 8px;
  vertical-align: middle;
}
</style>
