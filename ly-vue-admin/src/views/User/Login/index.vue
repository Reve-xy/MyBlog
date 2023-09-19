<!--
*@login
*@author Rêve
*@date 2023/4/18 22:38
*@description
-->
<template>
  <div class="login">
    <!-- status-icon，展示校验状态 -->
    <el-form ref="loginForm" :model="loginForm" :rules="rules" class="login-form">
      <h3 class="title">登 录</h3>
      <el-form-item prop="username" :error="errorLogin">
        <el-input
          ref="inputData"
          v-model="loginForm.username"
          type="text"
          placeholder="账号"
          maxlength="12"
          autocomplete="on"
        >
          <svg-icon slot="prefix" icon-class="user" />
        </el-input>
      </el-form-item>
      <el-form-item prop="password">
        <el-input
          ref="password"
          v-model="loginForm.password"
          :type="passwordType"
          placeholder="密码"
          autocomplete="off"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="password" />
          <svg-icon
            slot="suffix"
            :icon-class="passwordType === 'password' ? 'eye' : 'eye-open'"
            class="pointer-select"
            @click="showPwd"
          />
        </el-input>
      </el-form-item>
      <el-form-item v-if="captchaEnabled" prop="captcha" :error="errorCaptcha">
        <el-input
          v-model="loginForm.captcha"
          type="text"
          placeholder="验证码"
          :clearable="true"
          maxlength="8"
          style="width:65%; float: left;"
          @keyup.enter.native="handleLogin"
        >
          <svg-icon slot="prefix" icon-class="validCode" />
        </el-input>
        <div class="login-code">
          <img :src="captchaImg" class="login-code-img" @click="getCaptcha">
        </div>
      </el-form-item>
      <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
      <el-form-item>
        <el-button
          :loading="loading"
          type="primary"
          style="width: 100%"
          @click="handleLogin"
        >
          <span v-if="!loading">登 录</span>
          <span v-else>登 录 中...</span>
        </el-button>
        <!--<div style="float: right;">
          <router-link class="link-type" :to="'/register'">立即注册</router-link>
        </div>-->
      </el-form-item>
    </el-form>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2020-2023 @Rêve All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>

import { getCaptcha } from '@/api/login'
import Cookies from 'js-cookie'
import { encrypt, decrypt } from '@/utils/jsencrypt'
import { loginPassRule, loginNameRule } from '@/utils/formVaild'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: '',
        uuid: '',
        captcha: '',
        rememberMe: 'false'
      },
      rules: {
        username: [
          { trigger: 'blur', required: true, message: '请输入用户名' }
          // {pattern:'^[a-zA-Z0-9_-]{5,19}$', message: '用户名格式不正确，包含非法字符', trigger: 'blur'}
        ],
        password: [
          { trigger: 'blur', required: true, message: '请输入密码' }
        ],
        captcha: [
          { require: true, trigger: 'blur', message: '请输入验证码' }
        ]
      },
      captchaEnabled: false,
      captchaImg: '',
      loading: false, // 登录加载
      passwordType: 'password',
      redirect: undefined, // 重定向前的地址
      errorLogin: '',
      errorCaptcha: ''
    }
  },
  watch: {
    $route: {
      // 记住重定向前的位置
      handler: function(route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  created() {
    this.getCaptcha()
    this.getCookie()
  },
  methods: {
    async getCaptcha() {
      try {
        const res = await getCaptcha()
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled
        if (this.captchaEnabled) {
          this.captchaImg = res.captcha
          this.loginForm.uuid = res.uuid
        }
      } catch (err) {
        this.$message({ message: err || '获取验证码出错' })
      }
    },
    getCookie() {
      const username = Cookies.get('username')
      const password = Cookies.get('password')
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      }
    },
    handleLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (valid) {
          this.errorLogin = ''
          this.errorCaptcha = ''
          this.loading = true
          if (this.loginForm.rememberMe) {
            Cookies.set('username', this.loginForm.username, { expires: 30 })
            Cookies.set('password', encrypt(this.loginForm.password), { expires: 30 })
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 })
          } else {
            Cookies.remove('username')
            Cookies.remove('password')
            Cookies.remove('rememberMe')
          }
          try {
            const res = await this.$store.dispatch('login', this.loginForm)
            if (res.code === 61) {
              this.errorLogin = res.msg
              this.loading = false
              return
            } else if (res.code === 62) {
              this.errorCaptcha = res.msg
              this.loading = false
              await this.getCaptcha()
              return
            }
            this.$router.push({ path: this.redirect || '/' }).catch(() => {
            })
          } catch (err) {
            console.log(err)
            this.loading = false
            await this.getCaptcha()
          }
        }
      })
    },
    showPwd() {
      if (this.passwordType === 'password') {
        this.passwordType = ''
      } else {
        this.passwordType = 'password'
      }
      this.$nextTick(() => {
        this.$refs.password.focus()
      })
    },
    resetForm(form) {
      this.$refs[form].resetFields()
    }
  }
}
</script>

<style lang="scss">

$light_gray: #fff;
$cursor: #fff;
$dark_gray: #889aa4;
$light_gray: #eee;
.login {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  background: url("../../../assets/images/login-background.jpg");
  background-size: cover;
}

.title {
  text-align: center;
  color: #707070;
}

/*内边距撑开白色框*/
.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;

  .el-input {
    height: 38px;

    input {
      height: 38px;
    }
  }
}

.login-code {
  float: left;
  margin-left: 8px;
  border-radius: 4px;
  height: 38px;

  img {
    cursor: pointer;
    vertical-align: middle;
  }
}

//底部显示
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #fff;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}

.pointer-select {
  cursor: pointer;
  user-select: none;
}

.login-code-img {
  height: 38px;
}

</style>
