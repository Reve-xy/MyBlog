<!-- 头部公用 -->
<template>
  <div class="">
    <div class="headBack">

      <el-row class="container">
        <el-col :span="24">
          <!-- pc端导航 -->
          <div class="headBox" >
            <div class="contain">
              <input type="text" class="search short_long" placeholder="按下回车键搜索" ref="searchInput" v-model="input"
                     @blur="monitorInput"
                     @keyup.enter="searchChangeFun">
              <div class="btn" @click="openSearch"><img src="https://eveningwater.com/my-web-projects/js/62/images/search.svg" alt=""> </div>
            </div>

            <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal"
                     router active-text-color="#ffd04b">
              <el-menu-item index="/Home"><i class="fa fa-wa fa-home"></i> 首页</el-menu-item>
              <el-submenu index="/Share">
                <template slot="title" ><i class="fa fa-wa fa-archive"></i> 分类</template>
                <el-menu-item v-for="(item,index) in classListObj" :key="'class1'+index"
                              :index="'/Share?classId='+item.id">{{ item.name }}
                </el-menu-item>
              </el-submenu>
              <el-menu-item index="/Reward"><i class="fa fa-wa fa-cny"></i> 赞赏</el-menu-item>
              <el-menu-item index="/Friendslink"><i class="fa fa-wa fa-users"></i>友链</el-menu-item>


              <div class="userInfo">
                <div v-show="!haslogin" class="nologin">
                  <a href="javascript:void(0);" @click="logoinFun(1)">登录&nbsp;</a>|<a href="javascript:void(0);"
                                                                                      @click="logoinFun(2)">&nbsp;注册</a>
                </div>
                <div v-show="haslogin" class="haslogin">
                  <i class="fa fa-fw fa-user-circle userImg"></i>
                  <ul class="haslogin-info">
                    <li>
                      <a href="/UserInfo">个人中心</a>
                    </li>
                    <li>
                      <a href="javascript:void(0);" @click="userlogout">退出登录</a>
                    </li>
                  </ul>
                </div>
              </div>
            </el-menu>
          </div>
        </el-col>
      </el-row>
    </div>
    <div class="headImgBox">
         <!--:style="{backgroundImage:this.$store.state.themeObj.top_image?'url('+this.$store.state.themeObj.top_image+')':headBg}">-->
      <div class="scene">
        <div><span id="luke"></span></div>
      </div>
      <div class="h-information">
        <img
          :src="this.$store.state.themeObj.head_portrait?this.$store.state.themeObj.head_portrait:require('@/assets/img/avatar.jpg')"
          alt="">
        <h2 class="h-description">
          {{ this.$store.state.themeObj.autograph ? this.$store.state.themeObj.autograph : sign }}
        </h2>
      </div>
    </div>
  </div>
</template>
<script>
import {logout} from '../api/user'
import {removeToken} from '../utils/auth'
import {getCategoryList} from '../api/category'
import {
  Typeit
} from '../utils/plug.js'

export default {
  data() { //选项 / 数据
    return {
      userInfo: '', //用户信息
      haslogin: false, //是否已登录
      classListObj: '', //分类
      activeIndex: '/', //当前选择的路由模块
      state: '', //icon点击状态
      pMenu: true, //手机端菜单打开
      // path:'',//当前打开页面的路径
      input: '', //input输入内容
      headBg: 'url(../assets/img/headerBg.jpg)', //头部背景图
      headTou: '', //头像
      projectList: '', //项目列表
      sign:'浮云吹作雪，世味煮成茶。',
      openSearchFlag:false
    }
  },
  methods: { //事件处理器
    monitorInput(){ //blur事件
      if(this.input===''){
        this.$refs.searchInput.classList.add('short_long')
        this.$router.push({path: '/Home'});
      }else{
        this.$refs.searchInput.classList.remove('short_long')
      }
    },
    searchChangeFun() { //input change 事件
      if (this.input == '') {
        this.$store.state.keywords = '';
        this.$refs.searchInput.classList.add('short_long')
        this.$router.push({path: '/Home'});
      }else{
        this.$router.push({path:`/Home?search=${this.input}`})
      }
    },
    getCategoryList() {
      getCategoryList().then((response) => {
        this.classListObj = response
      })
    },
    logoinFun: function (msg) { //用户登录和注册跳转
      localStorage.setItem('logUrl', this.$route.fullPath);
      if (msg == 1) {
        this.$router.push({
          path: '/Login?login=1'
        });
      } else {
        this.$router.push({
          path: '/Login?login=2'
        });
      }
    },
    openSearch(){
      if(this.openSearchFlag){
        this.$refs.searchInput.classList.add('short_long')
      }else{
        this.$refs.searchInput.classList.remove('short_long')
      }
      this.openSearchFlag = !this.openSearchFlag;
    },
    // 用户退出登录
    userlogout() {
      var that = this;
      this.$confirm('是否确认退出?', '退出提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // console.log(that.$route.path);
        logout().then((response) => {
          removeToken()
          localStorage.removeItem('userInfo');
          that.haslogin = false;
          window.location.reload();
          that.$message({
            type: 'success',
            message: '退出成功!'
          });
          if (that.$route.path == '/UserInfo') {
            that.$router.push({
              path: '/'
            });
          }
        })
      }).catch(() => {
      });

    },
    routeChange() {
      var that = this;
      that.pMenu = true
      if(this.$route.query===''){
        this.activeIndex = this.$route.path == '/' ? '/Home' : this.$route.path;
      }else{
        // this.activeIndex=this.$route.path.substring(this.$route.path.lastIndexOf("?"))
        // console.log(this.$route.path)
        this.activeIndex=this.$route.fullPath
      }

      if (localStorage.getItem('userInfo')) { //存储用户信息
        // console.log(localStorage.getItem('userInfo'))
        that.haslogin = true;
        that.userInfo = JSON.parse(localStorage.getItem('userInfo'));
        // console.log(that.userInfo);
      } else {
        that.haslogin = false;
      }
      //获取分类
      this.getCategoryList()

     /* if ((this.$route.name == "Share" || this.$route.name == "Home") && this.$store.state.keywords) {
        this.input = this.$store.state.keywords;
      } else {
        this.input = '';
        this.$store.state.keywords = '';
      }*/
    }
  },
  watch: {
    // 如果路由有变化，会再次执行该方法
    '$route': 'routeChange'
  },
  created() { //生命周期函数
    //判断当前页面是否被隐藏
    var that = this;
    var hiddenProperty = 'hidden' in document ? 'hidden' :
      'webkitHidden' in document ? 'webkitHidden' :
        'mozHidden' in document ? 'mozHidden' :
          null;
    var visibilityChangeEvent = hiddenProperty.replace(/hidden/i, 'visibilitychange');
    var onVisibilityChange = function () {
      if (!document[hiddenProperty]) { //被隐藏
        if (that.$route.path != '/DetailShare') {
          if (localStorage.getItem('userInfo')) {
            that.haslogin = true;
          } else {
            that.haslogin = false;
          }
        }
      }
    }
    document.addEventListener(visibilityChangeEvent, onVisibilityChange);
    this.routeChange();
  },
  mounted() { //页面元素加载完成
    console.log("wdwd")
    let $self = this;
    // this.$refs.searchInput.classList.add('short_long')
    if(this.$route.query.search){
      console.log('123')
      this.input=this.$route.query.search
    }
    let timer = setTimeout(function () {
      Typeit($self.$store.state.themeObj.user_start, "#luke"); //打字机效果
      clearTimeout(timer);
    }, 500);
  }
}
</script>

<style>

.myClass {
  color:red
}

/*********头部导航栏********/

/*头部导航栏盒子*/

.headBack {
  width: 100%;
  background: rgba(40, 42, 44, 0.6);
  /*margin-bottom:30px;*/
  box-shadow: 0 2px 4px 0 rgba(0, 0, 0, .12), 0 0 6px 0 rgba(0, 0, 0, .04);
  position: fixed;
  left: 0;
  top: 0;
  right: 0;
  z-index: 100;
}

.headBox li.is-active {
  /*background: #48456C;*/
  background: rgba(73, 69, 107, 0.7);
}

/deep/.el-menu--horizontal > .el-submenu.is-active .el-submenu__title {
  border-bottom: none !important;
}

.headBox .el-menu {
  background: transparent;
  border-bottom: none !important;
}

.headBox .el-menu-demo li.el-menu-item,
.headBox .el-menu--horizontal .el-submenu .el-submenu__title {
  height: 38px;
  line-height: 38px;
  border-bottom: none !important;
  color: white;
}

.headBox .el-submenu__title i{
  color: inherit;
  opacity: 0.7;
}

.headBox .el-submenu li.el-menu-item {
  height: 38px;
  line-height: 38px;
}

.headBox li .fa-wa {
  vertical-align: baseline;
}

.headBox ul li.el-menu-item,
.headBox ul li.el-menu-item.is-active,
.headBox ul li.el-menu-item:hover,
.el-menu-item i
.headBox .el-submenu div.el-submenu__title,
.headBox .el-submenu__title i.el-submenu__icon-arrow {
  color: #fff;
}

/deep/ .headBox .el-submenu__title i.is-active{
  color :inherit;
}

.headBox .el-menu--horizontal .el-submenu > .el-menu {
  top: 38px;
  border: none;
  padding: 0;
}
 .headBox > ul li.el-menu-item:hover,
 .headBox > ul li.el-submenu:hover .el-submenu__title {
  background: #48456C !important;
  border-bottom: none;
}

.headBox > ul .el-submenu .el-menu,
.headBox > ul .el-submenu .el-menu-item {
  background: #48456C !important;
}

.headBox > ul .el-submenu .el-menu .el-menu-item {
  min-width: 0;
}

.headBox > ul .el-submenu .el-menu-item:hover {
  background: #000000 !important;
}

/*搜索框*/
.contain{
  width: max-content;
  overflow: hidden;
  border-radius: 8px;
  height: 30px;
  right:30%;
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  z-index: 8;
}
.contain::after{
  clear: both;
}
.search{
  height: 100%;
  width: 240px;/*240px;*/
  float: left;
  font-size: 14px;
  color: #545455;
  border: none;
  outline: none;
  padding-left: 15px;
  transition: all .3s ease;
  border-bottom: 2px solid #fff;
  border-radius: 8px;
}
.short_long{
  transform: translateX(238px);
}
.contain .btn{
  float: left;
  position: absolute;
  right: 0;
  top: 0;
  width: 50px;
  height: 100%;
  text-align: center;
  background-color: #fff;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
}
.contain .btn>img{
  width: 20px;
  height: 20px;
}
.contain .btn>img:hover{
  transform: scale(1.08);
}
/*搜索框结束*/

.headBox .userInfo {
  height: 100%;
  line-height: 38px;
  position: absolute;
  right: 30px;
  top: 0;
  color: #fff;
}

.headBox .userInfo a {
  color: #fff;
  font-size: 13px;
  transition: all 0.2s ease-out;
}

.headBox .userInfo a:hover {
  color: #48456C;
}

.headBox .nologin {
  text-align: right;
}

.headBox .haslogin {
  text-align: right;
  position: relative;
  min-width: 80px;
  cursor: pointer;
}

.headBox .haslogin:hover ul {
  visibility: visible;
  opacity: 1;
}

.headBox .haslogin ul {
  background: rgba(40, 42, 44, 0.6);
  padding: 5px 10px;
  position: absolute;
  right: 0;
  visibility: hidden;
  opacity: 0;
  transition: all 0.3s ease-out;
}

.headBox .haslogin ul li {
  border-bottom: 1px solid #48456C;
}

.headBox .haslogin ul li:last-child {
  border-bottom: 1px solid transparent;
}

/*******移动端*******/

.mobileBox {
  position: relative;
  height: 38px;
  line-height: 38px;
  color: #fff;
}

.hideMenu {
  position: relative;
  width: 100%;
  height: 100%;
  line-height: 38px;
}

.hideMenu ul.mlistmenu {
  width: 100%;
  position: absolute;
  left: 0;
  top: 100%;
  box-sizing: border-box;
  z-index: 999;
  box-shadow: 0 2px 6px 0 rgba(0, 0, 0, .12), 0 0 8px 0 rgba(0, 0, 0, .04);
  background: #48456C;
  color: #fff;
  border-right: none;
}


.hideMenu .el-submenu .el-menu {
  background: #64609E !important;
}

.hideMenu .el-menu-item,
.hideMenu .el-submenu__title {
  color: #fff;
}

.hideMenu > i {
  position: absolute;
  left: 10px;
  top: 12px;
  width: 30px;
  height: 30px;
  font-size: 16px;
  color: #fff;
  cursor: pointer;
}

.hideMenu .el-menu-item,
.el-submenu__title {
  height: 40px;
  line-height: 40px;
}

.mobileBox .searchBox {
  padding-left: 40px;
  width: 100%;
  box-sizing: border-box;
}

.mobileBox .searchBox .el-input__inner {
  display: block;
  border-radius: 2px;
  border: none;
  height: 25px;
}

.hideMenu ul.mlistmenu.pshow {
  display: block;
}

.hideMenu ul.mlistmenu .el-submenu__icon-arrow,
.mobileBox li.el-menu-item a {
  color: #fff;
}

.hideMenu > ul li.el-menu-item:hover,
.hideMenu > ul li.el-menu-item.is-active {
  background: #48576a;
}


/*头部背景图*/

.headImgBox {
  height: 650px;
  position: relative;
  width: 100%;
  background-size: cover;
 /* background-position: center 50%;
  background-repeat: no-repeat;*/
  margin-bottom: 90px;
  background:url(../assets/img/headerBg.jpg) no-repeat;
}

.h-information {
  text-align: center;
  width: 70%;
  margin: auto;
  position: relative;
  top: 480px;
  padding: 40px 0;
  font-size: 16px;
  opacity: 0.98;
  background: rgba(230, 244, 249, 0.8);
  border-radius: 5px;
  z-index: 1;
  animation: b 1s ease-out;
  -webkit-animation: b 1s ease-out;
}

@-webkit-keyframes b {
  0% {
    -webkit-transform: translateY(90px);
    transform: translateY(90px);
  }
  80% {
    -webkit-transform: translateY(5px);
    transform: translateY(5px)
  }
  90% {
    -webkit-transform: translateY(-5px);
    transform: translateY(-5px)
  }
  to {
    -webkit-transform: translateY(0);
    transform: translateY(0)
  }
}

@keyframes b {
  0% {
    -webkit-transform: translateY(90px);
    transform: translateY(90px);
  }
  80% {
    -webkit-transform: translateY(5px);
    transform: translateY(5px)
  }
  90% {
    -webkit-transform: translateY(-5px);
    transform: translateY(-5px)
  }
  to {
    -webkit-transform: translateY(0);
    transform: translateY(0)
  }
}

.h-information img {
  width: 100px;
  height: 100px;
  border-radius: 100%;
  transition: all .4s ease-in-out;
  -webkit-transition: all .4s ease-in-out;
  object-fit: cover;
}

.h-information img:hover {
  transform: rotate(360deg);
  -webkit-transform: rotate(360deg);
}

.h-information h2 {
  margin-top: 20px;
  font-size: 18px;
  font-weight: 700;
  /*font-family: 'Sigmar One';*/
}

.h-information h2 a {
  background: linear-gradient(to right, #DF2050, #48456D);
  -webkit-background-clip: text;
  color: transparent;
}

.headImgBox .scene {
  width: 100%;
  /*height:300px;*/
  text-align: center;
  font-size: 100px;
  font-weight: 200;
  color: #fff;
  position: absolute;
  left: 0;
  top: 160px;
  font-family: 'Sigmar One', Arial;
  text-shadow: 0 2px 2px #47456d;

}

.headImgBox .scene span {
  transform: matrix(1, 0, 0, 1, 0, 0);
  -webkit-transform: matrix(1, 0, 0, 1, 0, 0);
  text-shadow: 1px 1px 0 #ff3f1a, -1px -1px 0 #00a7e0;
}

.saying:after {
  content: "|";
  font-family: Arial, sans-serif;
  font-size: 1em;
  /*line-height: 0;*/
  display: inline-block;
  vertical-align: baseline;
  opacity: 1;
  text-shadow: 1px 1px 0 #ff3f1a, -1px -1px 0 #00a7e0;
  animation: caret 500ms infinite;
}



@keyframes caret {
  0%,
  100% {
    opacity: 1;
  }
  50% {
    opacity: 0;
  }
}
</style>
