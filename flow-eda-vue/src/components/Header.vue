<template>
  <div class="header">
    <div class="collapse-btn" @click="collapseChange">
      <i v-if="!collapse" class="el-icon-s-fold"></i>
      <i v-else class="el-icon-s-unfold"></i>
    </div>
    <div class="logo">工作流管理系统</div>
    <div class="header-right">
      <div class="header-avatar">
        <div class="user-avatar">
          <img alt="" src="../assets/img/logo.png"/>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import {computed, onMounted} from "vue";
import {useStore} from "vuex";

export default {
  setup() {

    const store = useStore();
    const collapse = computed(() => store.state.collapse);

    // 侧边栏折叠
    const collapseChange = () => {
      store.commit("handleCollapse", !collapse.value);
    };

    //自动折叠侧边栏
    onMounted(() => {
      if (document.body.clientWidth < 1500) {
        collapseChange();
      }
    });

    return {
      collapse,
      collapseChange,
    };
  },
};
</script>
<style lang="less" scoped>
.header {
  background-color: #242f42;
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #fff;

  .logo {
    float: left;
    width: 250px;
    line-height: 70px;
  }
}

.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;

  &:hover {
    background: rgb(40, 52, 70);
  }
}

.header-right {
  float: right;
  padding-right: 50px;
}

.header-avatar {
  display: flex;
  height: 70px;
  align-items: center;
}

.user-name {
  margin-left: 10px;
}

.user-avatar {
  margin-left: 20px;

  img {
    display: block;
    width: 30px;
    height: 30px;
    border-radius: 50%;
  }
}
</style>
