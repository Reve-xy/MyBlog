<template>
  <div class="dashboard-container">
      <el-row :gutter="16" style="text-align: center">
        <el-col :span="4" :offset="0">
          <el-card shadow="never" :body-style="{ padding: '20px' }" class="border-1">
            <div class="head-card">
              <div class="mi"><el-icon class="el-icon-user round-icon"/></div>
              <div>
                <div class="title-gray">用户量</div>
                <div class="text-count">{{indexData.userCount}}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4" :offset="0">
          <el-card shadow="never" class="border-1">
            <!-- card body -->
            <div class="head-card">
              <div class="mi"><el-icon class="el-icon-document round-icon"/></div>
              <div>
                <div class="title-gray">文章</div>
                <div class="text-count">{{indexData.articleCount}}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4" :offset="0">
          <el-card shadow="never" :body-style="{ padding: '20px' }" class="border-1">
            <div class="head-card">
              <div class="mi"><el-icon class="el-icon-folder round-icon"/></div>
              <div>
                <div class="title-gray">分类</div>
                <div class="text-count">{{indexData.categoryCount}}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4" :offset="0">
          <el-card shadow="never" :body-style="{ padding: '20px' }" class="border-1">
            <div class="head-card">
              <div class="mi"><el-icon class="el-icon-price-tag round-icon"/></div>
              <div>
                <div class="title-gray">标签</div>
                <div class="text-count">{{indexData.tagCount}}</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="4" :offset="0">
          <el-card shadow="never" :body-style="{ padding: '20px' }" class="border-1">
            <div class="head-card">
              <div class="mi"><el-icon class="el-icon-view round-icon"/></div>
              <div>
                <div class="title-gray">总浏览量</div>
                <div class="text-count">{{indexData.viewCount}}</div>
              </div>
            </div>
          </el-card>
        </el-col>

      </el-row>
    <el-card style="margin-top: 20px">
    <div class="dashboard-text">
      <img src="../assets/images/avatar.gif" class="img-circle" style="width: 50px;height: 50px;vertical-align: middle;margin-right: 10px "/>
      <span>Rêve（刘易）博客后台管理系统</span>
    </div>
    <div style="font-size: 18px">
      <p>
        欢迎你&nbsp;{{name}}<br>
      </p>
      <p>
       座右铭：浮云吹作雪，世味煮成茶。<br>
      </p>
      <hr>
      <H1>
        {{helloText}}
      </H1>
    </div>
  </el-card>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import store from "@/store";

export default {
  name: 'Dashboard',
  data() {
    return {
      helloText: 0,
    }
  },
  computed: {
    ...mapGetters([
      'indexData','name'
    ])
  },
  mounted() {
    store.dispatch('getIndexData')
    const hours=new Date().getHours()
    if(hours>=23 && hours<6) {
      this.helloText="该休息了！"
    }
    else if(hours>6&&hours<12){
      this.helloText="早上好！"
    }
    else if(hours>12&&hours<18){
      this.helloText="下午好！"
    }
    else{
      this.helloText="晚上好！"
    }
  }
}
</script>

<style lang="scss" scoped>
.dashboard {
  &-container {
    margin: 30px;
  }

  &-text {
    font-size: 30px;
    line-height: 46px;
  }
}

.title-gray{
  color: #6d7280;
}
.text-count{
  font-weight: bold;
  font-size: 1.25rem;
  line-height: 1.75rem;
}
.head-card{
  display: flex;align-items: center
}
.mi{
  margin-right: 1rem
}
.round-icon{
  border-radius: 9999px;
  background: #f3f4f6;
  height: 2.5rem!important;
  width: 2.5rem!important;
  display: inline-flex;
  justify-content: center;
  align-items: center;
}
</style>
