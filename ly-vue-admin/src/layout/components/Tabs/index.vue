<!--
*@index
*@author Rêve
*@date 2023/3/26 18:41
-->
<template>
  <el-tabs :value="activeTabs" type="card" closable @tab-remove="removeTab" @tab-click="clickTab">
    <el-tab-pane
      v-for="item in cacheTabs"
      :key="item.path"
      :label="item.title"
      :name="item.fullPath"
    >
    </el-tab-pane>
  </el-tabs>
</template>

<script>
import { mapGetters } from 'vuex'

export default {
  name: 'Index',
  components: {},
  data() {
    return {
    }
  },
  computed: {
    ...mapGetters(['activeTabs', 'cacheTabs'])
  },

  mounted() {

  },
  methods: {
    // 传入tabpane的name，name绑定为地址,方便菜单使用router
    removeTab(targetNamePath) {
      const tabs = this.cacheTabs
      let activeTabsPath = this.activeTabs
      // 主页不可删除
      if (targetNamePath === '/index') {
        this.$modal.msgWarning('主页不可删除哦')
        return
      }
      // 删除的是当前激活的标签页
      if (activeTabsPath === targetNamePath) {
        // 在缓存的标签页中找到删除标签页的具体位置，并根据前后标签状况进行移动
        tabs.forEach((tab, index) => {
          if (tab.fullPath === targetNamePath) {
            const nextTab = tabs[index + 1] || tabs[index - 1]
            if (nextTab) {
              activeTabsPath = nextTab.fullPath
            }
          }
        })
      }
      this.$store.dispatch('tabs/setActiveTabs', activeTabsPath)
      // 过滤掉删除的标签页，并存入缓存中
      this.$store.dispatch('tabs/delTabs', targetNamePath)
      console.log(activeTabsPath)
      this.$router.push({ path: activeTabsPath })
    },
    // 回调参数为选中标签的tab实例--->activeTabs
    clickTab(target) {
      // console.log(target)
      this.$router.push({ path: target.name })
    },
  }
}
</script>

<style scoped>
::v-deep .el-tabs__header{
  padding:5px 0;
}
</style>
