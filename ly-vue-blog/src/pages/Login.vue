<!-- 登录注册 -->
<template>
  <div class="login">
    <el-tabs
      v-model="activeName"
      type="border-card"
      stretch
      class="login-form"
      @tab-click="resetField"
    >
      <el-tab-pane label="登录" name="1">
        <!-- status-icon，展示校验状态 -->
        <el-form :model="loginForm" ref="loginForm" :rules="loginRules" style="margin-top: 10px">
          <el-form-item prop="username" :error="errorLogin">
            <el-input
              type="text"
              v-model="loginForm.username"
              placeholder="账号或邮箱"
              ref="inputData"
              auto-complete="on"
              :clearable="true"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              type="password"
              v-model="loginForm.password"
              placeholder="密码"
              @keyup.enter.native="handleLogin"
              ref="password"
              auto-complete="off"
              :show-password="true"
              :clearable="true"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="captcha" v-if="captchaEnabled">
            <el-input
              type="text"
              v-model="loginForm.captcha"
              placeholder="验证码"
              :clearable="true"
              style="width:65%; float: left;"
              @keyup.enter.native="handleLogin"
            >
            </el-input>
            <div class="login-code">
              <img :src="captchaImg" @click="getCaptcha" class="login-code-img" title="看不清？换一张"/>
            </div>
          </el-form-item>
          <div style="display: flex;justify-content: space-between">
            <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>
            <span @click="open=true" class="toPath">忘记密码</span>
          </div>
          <!--<router-link class="link-type" :to="'/register'">立即注册</router-link>-->
          <el-form-item>
            <el-button
              :loading="loading"
              type="primary"
              @click="handleLogin"
              style="width: 100%"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      <el-tab-pane label="注册" name="2">
        <!-- status-icon，展示校验状态 -->
        <el-form :model="registerForm" ref="registerForm" :rules="registerRules" style="margin-top: 10px">
          <el-form-item prop="email" :error="emailError"
          >
            <el-input
              type="text"
              v-model="registerForm.email"
              placeholder="邮箱"
              @blur="checkEmail"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="username" :error="userNameError">
            <el-input
              type="text"
              v-model="registerForm.username"
              placeholder="账号"
              ref="inputData"
              @blur="checkUserName"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="nickName">
            <el-input
              type="text"
              v-model="registerForm.nickName"
              placeholder="昵称"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              type="password"
              v-model="registerForm.password"
              placeholder="密码"
              ref="password"
              auto-complete="off"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input
              type="password"
              v-model="registerForm.confirmPassword"
              placeholder="确认密码"
              ref="confirmPassword"
              auto-complete="off"
            >
            </el-input>
          </el-form-item>
          <el-form-item prop="captcha" :error="errorRCaptcha">
            <el-input
              type="text"
              v-model="registerForm.captcha"
              placeholder="验证码"
              :clearable="true"
              style="width:65%; float: left;"
              @keyup.enter.native="handleLogin"
            >
            </el-input>
            <el-button
              type="primary"
              @click="sendEmailCode('registerForm')"
              style="width:30%; float: right;"
            >
              <span v-if="!isCount" style="font-size: 10px">获取验证码</span>
              <span v-else style="font-size: 10px">{{ codeTime }}s</span>
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button
              :loading="loading"
              type="primary"
              @click="handleRegister"
              style="width: 100%"
            >
              <span v-if="!loading">注册</span>
              <span v-else>注 册 中...</span>
            </el-button>
            <span @click="activeName='1';isCount=false" class="toPath" style="float: right;">已有账号？去登录</span>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2020-2023 @Rêve All Rights Reserved.</span>
    </div>


    <el-dialog title="忘记密码" :visible.sync="open" width="30%" center>
      <el-form :model="lostForm" :rules="lostRules" label-width="80px" ref="lostForm">
        <el-form-item v-show="sendEmail" prop="email" :error="emailError"
                      label="邮箱"
        >
          <el-input
            type="text"
            v-model="lostForm.email"
            placeholder="邮箱"
            @blur="checkEmail"
            key="email-input"
          >
          </el-input>
        </el-form-item>
        <el-form-item v-show="!sendEmail" prop="password" label="密码">
          <el-input
            type="text"
            v-model="lostForm.password"
            placeholder="密码"
            key="pwd-input"
          >
          </el-input>
        </el-form-item>
        <el-form-item v-show="sendEmail" prop="captcha" :error="errorRCaptcha"
                      label="验证码"
        >
          <el-input
            type="text"
            v-model="lostForm.captcha"
            placeholder="验证码"
            :clearable="true"
            style="width:65%; float: left;"
            key="captcha-input"
          >
          </el-input>
          <el-button
            type="primary"
            @click="sendEmailCode('lostForm')"
            style="width:30%; float: right;"
          >
            <span v-if="!isCount" style="font-size: 10px">获取验证码</span>
            <span v-else style="font-size: 10px">{{ codeTime }}s</span>
          </el-button>
        </el-form-item>

        <el-form-item v-show="!sendEmail" prop="confirmPassword" label="确认密码">
          <el-input
            type="text"
            v-model="lostForm.confirmPassword"
            placeholder="确认密码"
            key="confirmPwd-input"
          >
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitLostForm" v-loading="lostLoading">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {getCaptcha, sendEmailCode, userRegister, userLogin, resetPwd, checkEmailCaptcha} from "@/api/user";
import Cookies from "js-cookie";
import {encrypt, decrypt} from '@/utils/jsencrypt'
import {setToken} from "../utils/auth";
import regexValue from "../utils/regexValue";
import {checkEmail, checkUserName} from "../api/user";

export default {
  name: "Login",
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.registerForm.password !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    const equalToPasswordLost = (rule, value, callback) => {
      if (this.lostForm.password !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      activeName: this.$route.query.login,
      loginForm: {
        username: "",
        password: "",
        uuid: "",
        captcha: "",
        rememberMe: 'false'
      },
      registerForm: {
        uuid: ''
      },
      lostForm: {
        password: '',
        confirmPassword: '',
        email: '',
        captcha: ''
      },
      open: false,
      sendEmail: true, //是否显示发送验证码对话框
      loginRules: {
        username: [
          {trigger: 'blur', required: true, message: '请输入用户名'}
        ],
        password: [
          {trigger: 'blur', required: true, message: '请输入密码'},
        ],
        captcha: [
          {require: true, trigger: 'blur', message: '请输入验证码'}
        ]
      },
      lostRules: {
        password: [
          {trigger: 'blur', required: true, message: '请输入密码'},
        ],
        confirmPassword: [
          {trigger: 'blur', required: true, message: '请再次输入您的密码'},
          {required: true, validator: equalToPasswordLost, trigger: "blur"}
        ],
        email: [
          {trigger: 'blur', required: true, message: '请输入您的邮箱'},
          {
            pattern: regexValue.email_regex.regex,
            message: regexValue.email_regex.message,
            trigger: "blur",
          },
        ],
        captcha: [
          {require: true, trigger: 'blur', message: '请输入验证码'}
        ]
      },
      registerRules: {
        username: [
          {trigger: 'blur', required: true, message: '请输入您的用户名'},
          {
            pattern: regexValue.userName_regex.regex,
            message: regexValue.userName_regex.message,
            trigger: "blur",
          },
        ],
        nickName: [
          {trigger: 'blur', required: true, message: '请输入您的昵称'}
        ],
        password: [
          {trigger: 'blur', required: true, message: '请输入您的密码'},
          /*     {
                 pattern: regexValue.pwd_regex.regex,
                 message: regexValue.pwd_regex.message,
                 trigger: "blur",
               },*/
        ],
        confirmPassword: [
          {trigger: 'blur', required: true, message: '请再次输入您的密码'},
          {required: true, validator: equalToPassword, trigger: "blur"}
        ],
        email: [
          {trigger: 'blur', required: true, message: '请输入您的邮箱'},
          {
            pattern: regexValue.email_regex.regex,
            message: regexValue.email_regex.message,
            trigger: "blur",
          },
        ],
        captcha: [
          {require: true, trigger: 'blur', message: '请输入验证码'}
        ]
      },
      captchaEnabled: false,
      captchaImg: '',
      loading: false,     //登录加载
      isCount: false,  //是否显示倒计时
      codeTime: 0,    //验证码时延
      emailError: "",  //邮箱错误消息
      userNameError: "", //用户名已存在
      errorLogin: "",   //登录错误
      errorRCaptcha: "",//验证码错误消息
      redirect: '',
      tempEmail:'',
      lostLoading:false
    }
  },
  watch: {
    $route: {
      //记住重定向前的位置
      handler: function (route) {
        this.redirect = route.query && route.query.redirect
      },
      immediate: true
    }
  },
  methods: {
    async getCaptcha() {
      try {
        const res = await getCaptcha()
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.captchaImg = res.captcha
          this.loginForm.uuid = res.uuid
        }
      } catch (err) {
        this.$message({message: err || "获取验证码出错"})
      }
    },
    getCookie() {
      const username = Cookies.get("blog_username");
      const password = Cookies.get("blog_password");
      const rememberMe = Cookies.get('blog_rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      this.$refs.loginForm.validate(async valid => {
        if (valid) {
          this.errorLogin = ''
          this.loading = true;
          if (this.loginForm.rememberMe) {
            Cookies.set("blog_username", this.loginForm.username, {expires: 30});
            Cookies.set("blog_password", encrypt(this.loginForm.password), {expires: 30});
            Cookies.set('blog_rememberMe', this.loginForm.rememberMe, {expires: 30});
          } else {
            Cookies.remove("blog_username");
            Cookies.remove("blog_password");
            Cookies.remove('blog_rememberMe');
          }
          try {
            let res = await userLogin(this.loginForm)
            if (res.code === 61) {
              this.errorLogin = res.msg
              this.loading = false
              return
            }
            setToken(res.token)
            // 存储用户信息
            localStorage.setItem("userInfo", JSON.stringify(res.userInfo))
            this.$router.push({path: this.redirect || "/"}).catch(() => {
            });
          } catch (err) {
            console.log(err)
            this.loading = false;
            await this.getCaptcha();
          }
        }
      })
    },
    checkEmail() {
      this.emailError=''
      this.$refs.registerForm.validateField('email', async vaild => {
        if (vaild === '') {
          let res = await checkEmail(this.registerForm.email)
          if (res.code !== 200) {
            this.emailError = res.msg
          }
        }
      })
    },
    checkUserName() {
      this.$refs.registerForm.validateField('username', async vaild => {
        if (vaild === '') {
          let res = await checkUserName(this.registerForm.username)
          if (res.code !== 200) {
            this.userNameError = res.msg
          }
        }
      })
    },
    handleRegister() {
      this.$refs.registerForm.validate(async valid => {
        if (valid) {
          this.loading = true;
          try {
            const res = await userRegister(this.registerForm)
            if (!this.isEmpty(res)) {
              if (res.code === 62) {
                this.errorRCaptcha = res.msg
                this.loading = false;
                return
              }
            }
            this.$message({message: "注册成功", type: "success"})
            this.$refs.registerForm.resetFields()
            this.loading = false;
            this.activeName = "1"
          } catch (err) {
            console.log(err)
            this.loading = false;
          }
        }
      })
    },
    submitLostForm() {
      if (this.sendEmail === true) {
        this.errorRCaptcha = ''
        this.$refs.lostForm.validateField('email', async vaild => {
          if (vaild === '') {
            this.lostLoading = true
            let res = await checkEmailCaptcha({email: this.lostForm.email, captcha: this.lostForm.captcha})
            if (res && res.code === 62) {
              this.errorRCaptcha = res.msg
              this.lostLoading = false
            } else {
              this.sendEmail = false
              this.lostLoading = false
              this.tempEmail=this.lostForm.email
              this.$refs.lostForm.resetFields()
            }
          }
        })
      } else if (this.sendEmail === false) {
        this.$refs.lostForm.validateField('confirmPassword', async vaild => {
          if (vaild === '') {
            this.lostLoading = true
            try {
              let res = await resetPwd({email: this.tempEmail, password: this.lostForm.password,confirmPassword:this.lostForm.confirmPassword})
              this.lostLoading = false
              this.$message({message: '重置密码成功，去登录吧', type: 'success'})
              this.cancel()
              this.$refs.lostForm.resetFields()
            }catch (err){
              console.log(err)
              this.lostLoading = false
              this.$message({message: '发送失败，请稍后重试', type:'error'})
          }
        }
      }
    )
    }
  },
  cancel() {
    this.open = false
    this.sendEmail = true
    this.emailError = ''
    this.errorRCaptcha = ''
    this.lostLoading=false
    this.lostForm = {
      password: '',
      confirmPassword: '',
      email: '',
      captcha: ''
    }
    this.$refs.lostForm.resetFields()
  },

  async sendEmailCode(val) {
    this.emailError = ""
    if (val === 'registerForm') {
      if (this.isEmpty(this.registerForm.email)) {
        this.emailError = "请输入正确的邮箱"
        return
      }
      if (this.registerForm.email && this.isCount === false) {
        try {
          sendEmailCode({email: this.registerForm.email})
          this.startCount()
        } catch (err) {
          this.$message({message: err || "发送验证码出错", type: "error"})
        }
      } else if (this.isCount) {
        this.$message({message: this.codeTime + "s后才可重发哦", type: "warning"})
      }
    } else {
      if (this.isEmpty(this.lostForm.email)) {
        this.emailError = "请输入正确的邮箱"
        return
      }
      if (this.lostForm.email && this.isCount === false) {
        try {
          sendEmailCode({email: this.lostForm.email})
          this.startCount()
        } catch (err) {
          this.$message({message: err || "发送验证码出错", type: "error"})
        }
      } else if (this.isCount) {
        this.$message({message: this.codeTime + "s后才可重发哦", type: "warning"})
      }
    }
  },
  startCount() {
    this.isCount = true
    this.codeTime = 60;
    let timer = setInterval(() => {
      this.codeTime--;
      if (this.codeTime == 0) {
        clearInterval(timer);
        this.isCount = false;
      }
    }, 1000);
  },
  //判断变量是否为空  空返回TRUE 否则返回 FALSE
  isEmpty(value) {
    if (!value && typeof (value) == "undefined") {
      return true;
    } else {
      return false;
    }
  },
  resetField(val) {
    if (val.name === "1") {
      this.$refs.registerForm.resetFields()
    }else{
      this.$refs.loginForm.resetFields()
      this.getCookie()
    }
  }
}
,
created()
{
  this.getCaptcha()
  this.getCookie();
}
}
</script>

<style lang="scss">
.el-tabs--border-card > .el-tabs__content {
  padding-bottom: 0px;
}


.login {
  display: flex;
  justify-content: right;
  align-items: center;
  height: 100vh;
  background: url("../../static/img/login_background.jpg") center no-repeat;
  background-size: cover;
}

.title {
  font-size: 24px;
  margin: 0 auto 30px auto;
  text-align: center;
  color: #707070;
}

/*内边距撑开白色框*/
.login-form {
  border-radius: 6px;
  background: #ffffff;
  width: 400px;
  padding: 25px 25px 5px 25px;
  margin-right: 80px;

  .el-input {
    height: 38px;
    border-radius: 5px;

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
