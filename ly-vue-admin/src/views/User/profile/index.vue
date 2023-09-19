<!--
* @index
* @author Rêve
* @date 2023/5/5 15:59
* @description
-->
<template>
  <div style="padding: 20px">
    <el-row :gutter="20">
      <el-col :span="6" :xs="24">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>个人信息</span>
          </div>
          <div>
            <div style="text-align: center;">
              <userAvatar :user="user" />
            </div>
            <ul class="list-group list-group-striped">
              <li class="list-group-item">
                <svg-icon icon-class="user" /><span class="icon-name">用户名称</span>
                <div class="pull-right">{{ user.userName }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="phone" /><span class="icon-name">手机号码</span>
                <div class="pull-right">{{ user.phoneNumber }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="email" /><span class="icon-name">用户邮箱</span>
                <div class="pull-right">{{ user.email }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="peoples" /><span class="icon-name">所属角色</span>
                <div class="pull-right">{{ user.roleGroups }}</div>
              </li>
              <li class="list-group-item">
                <svg-icon icon-class="date" /><span class="icon-name">创建日期</span>
                <div class="pull-right">{{ user.createTime }}</div>
              </li>
            </ul>
          </div>
        </el-card>
      </el-col>
      <el-col :span="18" :xs="24">
        <el-card>
          <div slot="header" class="clearfix">
            <span>基本资料</span>
          </div>
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="userinfo">
              <userInfo :user="user" />
            </el-tab-pane>
            <el-tab-pane label="修改密码" name="resetPwd">
              <resetPwd />
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import userAvatar from './UserAvatar'
import userInfo from './UserInfo'
import resetPwd from './ResetPwd'
import { getProfile } from '@/api/system/user'

export default {
  name: 'Profile',
  components: { userAvatar, userInfo, resetPwd },
  data() {
    return {
      user: {},
      activeTab: 'userinfo'
    }
  },
  created() {
    this.getUser()
  },
  methods: {
    getUser() {
      getProfile().then(response => {
        this.user = response
      })
    }

  }
}
</script>

<style lang="scss">
.list-group-striped > .list-group-item {
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}

.list-group {
  padding-left: 0px;
  list-style: none;
}

.list-group-item {
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0px;
  font-size: 13px;
}
.icon-name{
  margin-left: 5px;
}
.pull-right{
  float: right!important
}
</style>
