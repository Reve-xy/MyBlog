<template>
  <div class="app-container">
    <el-row :gutter="20">
      <el-col :span="24" :xs="24">
        <el-form
          v-show="showSearch"
          ref="queryForm"
          :model="queryParams"
          :inline="true"
          label-width="68px"
        >
          <el-form-item label="博客标题" prop="title">
            <el-input
              v-model="queryParams.title"
              placeholder="请输入关键词"
              clearable
              size="small"
              style="width: 240px"
              @keyup.enter.native="handleQuery"
            />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="queryParams.categoryId" placeholder="请选择分类" size="small">
              <el-option
                v-for="category in categoryList"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              icon="el-icon-search"
              size="mini"
              @click="handleQuery"
            >搜索
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button
              type="info"
              size="mini"
              icon="el-icon-refresh-left"
              @click="resetField"
            >重置</el-button
            >
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button
              type="primary"
              plain
              icon="el-icon-plus"
              size="mini"
              @click="handleAdd"
            >新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              @click="handleDelete"
            >删除
            </el-button>
          </el-col>

        </el-row>

        <el-table v-loading="loading" :data="articleList" style="width: 100%" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" />
          <el-table-column prop="id" label="博文ID" align="center" />
          <el-table-column prop="title" label="标题" align="center" />
          <el-table-column prop="thumbnail" label="缩略图" align="center">
            <template slot-scope="scope">
              <el-image
                style="width: 100px; height: 100px"
                :src="scope.row.thumbnail"
                fit="fill"
              />
            </template>
          </el-table-column>
          <el-table-column prop="status" label="前台是否显示" align="center">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                active-value="0"
                inactive-value="1"
                @change="handleStatusChange(scope.row)"
              />
            </template>
          </el-table-column>
          <el-table-column prop="createTime" label="创建时间" align="center" />
          <el-table-column prop="viewCount" label="访问量" align="center" />


          <el-table-column
            label="操作"
            align="center"
            width="160"
            class-name="small-padding fixed-width"
          >
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
              >删除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <el-pagination
      :page-size.sync="queryParams.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
      :page-sizes="[10, 20, 30, 40]"
      :current-page.sync="queryParams.pageNum"
      @current-change="getList"
      @size-change="getList"
    />

  </div>
</template>

<script>
// import { getToken } from '@/utils/auth'
import {
  listArticle,
  delArticle, updateArticle, changeArticleStatus
}
from '@/api/content/article'
import { listAllCategory } from '@/api/content/category'
import { changeUserStatus } from '@/api/system/user'

export default {
  name: 'Article',
  data() {
    return {
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title:undefined,
        status: undefined
      },
      // 是否显示弹出层
      open: false,
      // 总条数
      total: 0,
      articleList: [],
      showSearch: true,
      categoryList: [],
      categoryId: undefined,
      loading: false
    }
  },
  watch: {
    $route: {
      handler: function(route) {
        this.categoryId = route.query && route.query.category
      },
      immediate: true
    }
  },
  created() {
    this.getCategoryList()
    this.$nextTick(() => {
      if (this.categoryId) {
        this.queryParams.categoryId = this.categoryId
      }
      this.getList()
    })
  },
  methods: {
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    /** 查询用户列表 */
    getList() {
      this.loading = true
      listArticle(this.queryParams).then((response) => {
        this.articleList = response.rows
        this.total = response.total-0
        this.loading = false
      })
    },
    // 获取分类列表
    getCategoryList() {
      listAllCategory().then((response) => {
        this.categoryList = response
      })
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.$store.dispatch('tabs/delCacheViews','Write')
      this.$router.push('/redirect/write?id=' + row.id)
    },
    /** 新增用户 */
    handleAdd() {
      this.$router.push('/write')
    },
    /* 文章状态修改*/
    async handleStatusChange(row) {
      await changeArticleStatus(row.id, row.status)
      this.$modal.msgSuccess('操作成功')
      this.getList()
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除分类编号为"' + ids + '"的数据项？').then(function() {
        return delArticle(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    resetField(){
      this.queryParams= {
          pageNum: 1,
          pageSize: 10,
          title:undefined,
          status: undefined
      }
      this.getList()
    }
  }

}
</script>

