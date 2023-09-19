const getters = {
  sidebar: state => state.app.sidebar,
  size: state => state.app.size,
  device: state => state.app.device,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  introduction: state => state.user.introduction,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  permission_routes: state => state.permission.routes,
  topbarRouters: state => state.permission.topbarRouters,
  defaultRoutes: state => state.permission.defaultRoutes,
  sidebarRouters: state => state.permission.sidebarRouters,
  indexData:state=>state.user.indexData,
  cacheViews:state=>state.tabs.cacheViews,
  activeMenu:state=>state.tabs.activeMenu,
  // tabs
  activeTabs: state => state.tabs.activeTabs,
  cacheTabs: state => state.tabs.cacheTabs
}
export default getters
