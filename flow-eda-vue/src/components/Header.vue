<template>
  <div class="header">
    <!-- 折叠按钮 -->
    <div class="collapse-btn" @click="collapseChange">
      <i v-if="!collapse" class="el-icon-s-fold"></i>
      <i v-else class="el-icon-s-unfold"></i>
    </div>
    <div class="logo">工作流管理系统</div>
    <div class="header-right">
      <div class="header-user-con">
        <!-- 用户头像 -->
        <div class="user-avatar">
          <img src="../assets/img/logo.png" alt=""/>
        </div>
        <!-- 用户名下拉菜单 -->
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
                    <span class="el-dropdown-link">
                        {{ username }}
                        <i class="el-icon-caret-bottom"></i>
                    </span>
          <template #dropdown>
            <el-dropdown-menu>
              <a href="https://gitee.com/Linxff/flow-eda" target="_blank">
                <el-dropdown-item>项目仓库</el-dropdown-item>
              </a>
              <el-dropdown-item command="user">个人中心</el-dropdown-item>
              <el-dropdown-item divided command="login-out">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>
<script>
import {computed, onMounted} from "vue";
import {useStore} from "vuex";
import {useRouter} from "vue-router";

export default {
  setup() {
    const username = localStorage.getItem("ms_username");
    const message = 2;
    const store = useStore();
    const collapse = computed(() => store.state.collapse);
    // 侧边栏折叠
    const collapseChange = () => {
      store.commit("handleCollapse", !collapse.value);
    };
    onMounted(() => {
      if (document.body.clientWidth < 1500) {
        collapseChange();
      }
    });
    // 用户名下拉菜单选择事件
    const router = useRouter();
    const handleCommand = (command) => {
      if (command === "login-out") {
        localStorage.removeItem("ms_username");
        router.push("/login");
      } else if (command === "user") {
        router.push("/user");
      }
    };
    return {
      username,
      message,
      collapse,
      collapseChange,
      handleCommand,
    };
  },
};
</script>
<style scoped>
.header {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  color: #fff;
}

.collapse-btn {
  float: left;
  padding: 0 21px;
  cursor: pointer;
  line-height: 70px;
}

.header .logo {
  float: left;
  width: 250px;
  line-height: 70px;
}

.header-right {
  float: right;
  padding-right: 50px;
}

.header-user-con {
  display: flex;
  height: 70px;
  align-items: center;
}

.btn-bell .el-icon-bell {
  color: #fff;
}

.user-name {
  margin-left: 10px;
}

.user-avatar {
  margin-left: 20px;
}

.user-avatar img {
  display: block;
  width: 30px;
  height: 30px;
  border-radius: 50%;
}

.el-dropdown-link {
  color: #fff;
  cursor: pointer;
}
</style>