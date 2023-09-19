/**
 * @author Rêve
 * @date 2023/5/8 23:16
 * @description
 */
const tabs = {
  namespaced: true,
  state: {
    activeTabs: '',
    activeMenu:'',
    cacheTabs: [{
      path: '/index',
      fullPath:'/index',
      title: '首页',
      name: 'Index'
    }],
    cacheViews: [],
  },
  mutations: {
    ADD_TABS(state, tab) {
      // 判断是否在栈内
      const index = state.cacheTabs.findIndex(item => item.path === tab.path)
      if (index === -1) {
        // 添加到tabs中
        state.cacheTabs.push(tab)
      }else{
        state.cacheTabs[index].fullPath=tab.fullPath
      }
      // 当前激活的tab
      state.activeTabs = tab.fullPath
      state.activeMenu=tab.path
    },
    SET_ACTIVE_TABS(state, activeTabs) {
      state.activeTabs = activeTabs
    },
    DEL_TABS(state, tabPath) {
      if (tabPath === state.activeTabs) {
        state.cacheTabs.forEach((tab, index) => {
          if (tab.fullPath === tabPath) {
            const nextTab = state.cacheTabs[index + 1] || state.cacheTabs[index - 1]
            if (nextTab) {
              state.activeTabs = nextTab.fullPath
              state.activeMenu=nextTab.path
            }
          }
        })
      }
      const temp=state.cacheTabs.filter(tab=>tab.fullPath===tabPath)
      state.cacheViews=state.cacheViews.filter(tabName=>tabName===temp.name)
      state.cacheTabs = state.cacheTabs.filter(tab => tab.fullPath !== tabPath)
    },
    ADD_CACHE_VIEWS(state, tabName) {
      if(tabName==='Index')return
      // 判断是否在栈内
      const index = state.cacheViews.findIndex(tab => tab === tabName)
      if (index === -1) {
        // 添加到tabs中
        state.cacheViews.push(tabName)
      }
    },
    DEL_CACHE_VIEWS(state, tabName) {
      state.cacheViews = state.cacheViews.filter(tab => tab !== tabName)
    },
  },
  actions: {
    addTabs(context, tab) {
      context.commit('ADD_TABS', tab)
    },
    setActiveTabs(context, activeTabs) {
      context.commit('SET_ACTIVE_TABS', activeTabs)
    },
    delTabs(context, tabPath) {
      context.commit('DEL_TABS', tabPath)
    },
    addCacheViews(context, tabName) {
      context.commit('ADD_CACHE_VIEWS', tabName)
    },
    delCacheViews(context, tabName) {
      context.commit('DEL_CACHE_VIEWS', tabName)
    },
  }
}

export default tabs
