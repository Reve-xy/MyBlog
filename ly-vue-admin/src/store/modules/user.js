import {login, logout, getInfo, getIndexData} from '@/api/login'
import {getToken, setToken, removeToken} from '@/utils/auth'
import router from "@/router";

const user = {
  state: {
    token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    permissions: [],
    indexData: {}
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, name) => {
      state.name = name
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      state.permissions = permissions
    },
    SET_INDEX_DATA: (state, data) => {
      state.indexData = data
    }
  },

  actions: {
    // 登录
    async login({commit}, userInfo) {
      const username = userInfo.username.trim()
      const password = userInfo.password
      const captcha = userInfo.captcha
      const uuid = userInfo.uuid
      const res = await login(username, password, captcha, uuid)
      if (res.token) {
        setToken(res.token)
        commit('SET_TOKEN', res.token)
      }
      return res
    },

    // 获取用户信息
    async getInfo({commit, state}) {
      const res = await getInfo()
      const user = res.user
      // const avatar = user.avatar === '' ? require('@/assets/images/avatar.gif') : user.avatar
      const avatar = user.avatar || require('@/assets/images/avatar.gif')
      if (res.roles && res.roles.length > 0) { // 验证返回的roles是否是一个非空数组
        commit('SET_ROLES', res.roles)
        commit('SET_PERMISSIONS', res.permissions)
      } else {
        commit('SET_ROLES', ['ROLE_DEFAULT'])
      }
      commit('SET_NAME', user.nickName)
      commit('SET_AVATAR', avatar)
    },

    // 退出系统
    async logout({commit, state}) {
      await logout(state.token)
      commit('SET_TOKEN', '')
      commit('SET_ROLES', [])
      commit('SET_PERMISSIONS', [])
      removeToken()
    },

    async getIndexData({commit}) {
      const res = await getIndexData()
      if (res) {
        commit('SET_INDEX_DATA', res)
      }
    },

    // 前端 登出
    FedLogOut({commit}) {
      return new Promise(resolve => {
        commit('SET_TOKEN', '')
        removeToken()
        router.push('/login')
        resolve()
      })
    }
  }
}

export default user
