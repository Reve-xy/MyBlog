import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  mode: 'history',
  scrollBehavior(to, from, savePosition) { // 在点击浏览器的“前进/后退”，或者切换导航的时候触发。
    if (savePosition) {
      return savePosition;
    } else {
      return {
        x: 0,
        y: 0
      }
    }
  },
  routes: [{
    path: '/',
    redirect: '/Home',
    meta: {
      auth: true
    },
    name: 'Home'
  }, //首页
    {
      path: '/Home',
      component: () => import('../pages/Home'),
      meta: {
        auth: true
      },
      name: 'Home'
    }, //首页
    {
      path: '/Share',
      component: () => import('../pages/Share'),
      meta: {
        auth: true
      },
      name: 'Share'
    }, //分类
    {
      path: '/DetailArticle',
      component: () => import('../pages/DetailArticle'),
      meta: {
        auth: true
      },
      name: 'DetailArticle'
    }, //分享详情
    {
      path: '/Reward',
      component: () => import('../pages/Reward'),
      meta: {
        auth: true
      },
      name: 'Reward'
    }, //赞赏
    {
      path: '/FriendsLink',
      component: () => import('../pages/FriendsLink'),
      meta: {
        auth: true
      },
      name: 'FriendsLink'
    }, //友链


    {
      path: '/Login',
      component: () => import('../pages/Login'),
      meta: {
        auth: false
      },
      name: 'Login'
    }, //注册登录
    {
      path: '/UserInfo',
      component: () => import('../pages/UserInfo'),
      meta: {
        auth: true
      },
      name: 'UserInfo'
    }, //用户个人中心
    {
      path: '*',
      component: () => import('../pages/NotFound'),
      name: "NotFound"
    }
  ]
})
