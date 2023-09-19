<!--
* @UserInfo
* @author Rêve
* @date 2023/5/6 22:48
* @description
-->
<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="用户昵称" prop="nickName">
      <el-input v-model="user.nickName" maxlength="30" />
    </el-form-item>
    <el-form-item label="手机号码" prop="phoneNumber">
      <el-input v-model="user.phoneNumber" maxlength="11" />
    </el-form-item>
    <el-form-item label="邮箱" prop="email">
      <el-input v-model="user.email" maxlength="50" />
    </el-form-item>
    <el-form-item label="性别">
      <el-radio-group v-model="user.sex">
        <el-radio label="0">男</el-radio>
        <el-radio label="1">女</el-radio>
        <el-radio label="2">未知</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button v-loading="loading" type="primary" size="medium" @click="submit">保存</el-button>
      <el-button type="danger" size="medium" @click="close">关闭</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { updateLoginUser } from '@/api/system/user'

export default {
  name: 'UserInfo',
  components: {},
  props: ['user'],
  data() {
    return {
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: '用户昵称不能为空', trigger: 'blur' }
        ],
        email: [
          { required: true, message: '邮箱地址不能为空', trigger: 'blur' },
          {
            type: 'email',
            message: '请输入正确的邮箱地址',
            trigger: ['blur', 'change']
          }
        ],
        phoneNumber: [
          { required: true, message: '手机号码不能为空', trigger: 'blur' },
          {
            pattern: /^(?:(?:\+|00)86)?1(?:(?:3[\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\d])|(?:9[1|8|9]))\d{8}$/,
            message: '请输入正确的手机号码',
            trigger: 'blur'
          }
        ]
      },
      loading: false
    }
  },
  methods: {
    submit() {
      this.$refs['form'].validate(async valid => {
        if (valid) {
          this.loading = true
          try {
            const res = await updateLoginUser(this.user)
            this.$modal.msgSuccess('修改成功')
            this.loading = false
          } catch (err) {
            console.log(err)
            this.loading = false
          }
        }
      })
    },
    close() {
      this.$store.dispatch('tabs/delTabs', '/user/profile')
      this.$router.push({ path: this.$store.getters.activeTabs })
    }
  }
}
</script>

<style scoped>

</style>
