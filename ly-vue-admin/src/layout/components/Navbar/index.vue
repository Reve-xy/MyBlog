<template>
  <div class="navbar">
    <hamburger
      :is-active="sidebar.opened"
      class="hamburger-container"
      @toggleClick="toggleSideBar"
    />

    <breadcrumb class="breadcrumb-container" />

    <div class="right-menu">
    <el-tooltip class="box-item" effect="dark" content="跳转博客前台" placement="bottom">
      <el-icon class="el-icon-house icon-redirect" @click.native="redirectFront">
      </el-icon>
    </el-tooltip>
    <!-- 刷新 -->
    <el-tooltip class="box-item" effect="dark" content="刷新" placement="bottom">
      <el-icon class="el-icon-refresh icon-redirect" @click.native="refresh"/>
    </el-tooltip>
    <el-dropdown class="avatar-container right-menu box-item" trigger="click">
      <div class="avatar-wrapper">
        <img :src="avatar" class="user-avatar">
        <i class="el-icon-caret-bottom" />
      </div>
      <el-dropdown-menu slot="dropdown">
        <router-link to="/">
          <el-dropdown-item> 首页 </el-dropdown-item>
        </router-link>
        <router-link to="/user/profile">
          <el-dropdown-item> 个人中心 </el-dropdown-item>
        </router-link>
        <el-dropdown-item divided @click.native="logout">
          <span style="display: block">退出登录</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </div>
  </div>
  <!--</div>-->
</template>

<script>
import { mapGetters } from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters(['sidebar', 'avatar'])
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm('确定注销并退出系统吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$store.dispatch('logout').then(() => {
            location.href = '/index'
          })
        })
        .catch(() => {})
    },
    refresh(){
        const { path, query } = this.$router.currentRoute;
        this.$store.dispatch('tabs/delCacheViews',this.$route.name)
        this.$router.replace({
          path: '/redirect' + path,
          query: query
      })
    },
    redirectFront(){
      location.href("http://127.0.0.1:8080")
    }
  }
}
</script>

<style lang="scss" scoped>

.box-item{
  margin:0 13px;
}
.icon-redirect{
  font-size: 18px;
  &:hover{
    cursor: pointer;
    color: #6d7280;
  }
}
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }
    }
  }
  .avatar-container {
    margin-right: 30px;

    .avatar-wrapper {
      margin-top: 5px;
      position: relative;

      .user-avatar {
        cursor: pointer;
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }

      .el-icon-caret-bottom {
        cursor: pointer;
        position: absolute;
        right: -20px;
        top: 25px;
        font-size: 12px;
      }
    }
  }
}
</style>
