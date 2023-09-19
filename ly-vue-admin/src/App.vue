<template>
  <div id="app">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
  watch: {
    // 全局监视任何路由的切换，都将生成新tab
    $route(to, form) {
      if (to.path !== '/login' && to.path !== '/register' && to.name !== '404' && !to.path.includes('redirect')) {
        const obj = {
          path:to.path,
          fullPath: to.fullPath,
          title: to.meta.title,
          name: to.name
        }
        this.$store.dispatch('tabs/addTabs', obj)
        this.$store.dispatch("tabs/addCacheViews",to.name)
      }
    }
  }
}
</script>
