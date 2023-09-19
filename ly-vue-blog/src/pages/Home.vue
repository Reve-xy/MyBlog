<!-- 首页 -->
<template>
    <div>
        <ly-header/>
        <div class="container">
            <el-row  :gutter="30">
                <el-col :sm="24" :md="16" style="transition:all .5s ease-out;margin-bottom:30px;">
                    <ly-articlelist/>
                </el-col>
                <el-col :sm="24"  :md="8" >
                    <ly-rightlist/>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
import header from '../components/header.vue'
import articlelist from '../components/articlelist.vue'
import rightlist from '../components/rightlist.vue'
    export default {
        name:'Home',
        data() { //选项 / 数据
            return {

            }
        },
        methods: { //事件处理器

        },
        components: { //定义组件
          'ly-header':header,
            'ly-articlelist':articlelist,
            'ly-rightlist':rightlist,
        },
        created() { //生命周期函数
        }
    }
</script>

<style>
.home{
  max-width: 80%;
  margin:0 auto;
  padding:0 10px;
}
</style>
