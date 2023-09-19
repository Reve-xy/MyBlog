import { constantRoutes } from '@/router'
import { getRouters } from '@/api/menu'
import Layout from '@/layout/index'
import ParentView from '@/components/ParentView'
import InnerLink from '@/layout/components/InnerLink'

const permission = {
  state: {
    routes: [],
    addRoutes: [],
    defaultRoutes: [],
    sidebarRouters: []
  },
  mutations: {
    SET_ROUTES: (state, routes) => {
      state.addRoutes = routes
      state.routes = constantRoutes.concat(routes)
    },
    SET_DEFAULT_ROUTES: (state, routes) => {
      state.defaultRoutes = constantRoutes.concat(routes)
    },
    SET_SIDEBAR_ROUTERS: (state, routes) => {
      state.sidebarRouters = routes
    }
  },
  actions: {
    // 生成路由
    async generateRoutes({ commit }) {
      const res = await getRouters()
      // 获取menus 自己组装routers
      const routers = buildRouterTree(res.menus)
      const sdata = JSON.parse(JSON.stringify(routers))
      const rdata = JSON.parse(JSON.stringify(routers))
      // component转换为组件对象，并对path进行处理
      const sidebarRoutes = filterAsyncRouter(sdata)
      const rewriteRoutes = filterAsyncRouter(rdata, false, true)
      rewriteRoutes.push({ path: '*', redirect: '/404', hidden: true })
      commit('SET_ROUTES', rewriteRoutes)
      // 动态路由的整合
      commit('SET_SIDEBAR_ROUTERS', constantRoutes.concat(sidebarRoutes))
      // 动态路由
      commit('SET_DEFAULT_ROUTES', sidebarRoutes)
      return rewriteRoutes
    }
  }
}

function buildRouterTree(menus) {
  // 遍历menus
  const routers = []
  for (let i = 0; i < menus.length; i++) {
    const menu = menus[i]
    const router = {}
    router.hidden = menu.visible === '1'
    router.name = getRouterName(menu)
    router.path = getRouterPath(menu)
    router.component = getComponent(menu)
    router.meta = getComponentMeta(menu)

    const cMenus = menu.children
    // 父路由是目录才可以设置子路由
    if (cMenus && cMenus.length > 0 && menu.menuType === 'D') {
      router.alwaysShow = true
      router.redirect = 'noRedirect'
      router.children = buildRouterTree(cMenus)
    }// 路由是一级菜单，配置到layout下
    else if (isMenuFrame(menu)) {
      router.meta = null
      const childrenRouterList = []
      const children = {}
      children.path = menu.path // 一级菜单的path应该不加/
      children.component = menu.component
      children.name = getRouterName(menu)
      children.meta = getComponentMeta(menu)
      childrenRouterList.push(children)
      router.children = childrenRouterList
    } // 处理错误路由
    else if (menu.parentId === '0') {
      router.meta = { 'title': menu.menuName, 'icon': menu.icon }
      router.path = '/inner'
      const childrenRouterList = []
      const children = {}
      const routerPath = menu.path.replace('http://',
        '')
      routerPath.replace('https://', '')
      children.path = routerPath
      children.component = 'InnerLink'
      children.name = getRouterName(menu)
      children.meta = getComponentMeta(menu)
      childrenRouterList.push(children)
      router.children = childrenRouterList
    }
    routers.push(router)
  }
  return routers
}

function getComponentMeta(menu) {
  const meta = {}
  meta.title = menu.menuName
  meta.icon = menu.icon
  meta.link = menu.link
  return meta
}

function getComponent(menu) {
  // 可点击跳转的项，并且不是一级菜单，二级菜单
  if (menu.component && !isMenuFrame(menu)) {
    return menu.component
  }
  // 通常来说，component为空、应该是M目录，parentId为0，但是特殊情况，比如后端返回了按钮或者错误数据，填充innerlink进路由
  if (!menu.component && menu.parentId !== '0') {
    return 'InnerLink'
  }
  return 'Layout'
}

function getRouterPath(menu) {
  // 一级目录
  if (menu.parentId === '0' && menu.menuType === 'D') {
    return '/' + menu.path
  }
  // 一级菜单
  if (isMenuFrame(menu)) {
    return '/'
  }
  return menu.path
}

// 判断是否为一级菜单路由
function isMenuFrame(menu) {
  return menu.parentId === '0' && menu.menuType === 'M'
}

function getRouterName(menu) {
  return firstToUpper(menu.path)
}

// 字符串首字母转大写，后面字母小写
/**
 * 方法一：js字符串切割
 * @param {*} str
 */
function firstToUpper(str) {
  return str.trim().toLowerCase().replace(str[0], str[0].toUpperCase())
}

// 遍历后台传来的路由字符串，转换为组件对象
function filterAsyncRouter(asyncRouterMap, lastRouter = false, type = false) {
  return asyncRouterMap.filter(route => {
    if (type && route.children) {
      route.children = filterChildren(route.children)
    }
    if (route.component) {
      // Layout ParentView 组件特殊处理
      if (route.component === 'Layout') {
        route.component = Layout
      } else if (route.component === 'ParentView') {
        route.component = ParentView
      } else if (route.component === 'InnerLink') {
        route.component = InnerLink
      } else {
        route.component = loadView(route.component)
      }
    }
    if (route.children != null && route.children && route.children.length) {
      route.children = filterAsyncRouter(route.children, route, type)
    } else {
      // 孩子路由没有值，删除数组
      delete route['children']
      delete route['redirect']
    }
    return true
  })
}

// 对三级菜单做处理
function filterChildren(childrenMap, lastRouter = false) {
  let children = []
  childrenMap.forEach((el, index) => {
    if (el.children && el.children.length) {
      if (el.component === 'ParentView' && !lastRouter) {
        el.children.forEach(c => {
          c.path = el.path + '/' + c.path
          if (c.children && c.children.length) {
            children = children.concat(filterChildren(c.children, c))
            return
          }
          children.push(c)
        })
        return
      }
    }
    if (lastRouter) {
      el.path = lastRouter.path + '/' + el.path
    }

    children = children.concat(el)
  })
  return children
}

export const loadView = (view) => {
  if (process.env.NODE_ENV === 'development') {
    return (resolve) => require([`@/views/${view}`], resolve)
  } else {
    // 使用 import 实现生产环境的路由懒加载
    // return () => import(`@/views/${view}`)
  }
}

export default permission
