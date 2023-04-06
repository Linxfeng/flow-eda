<template>
  <div class="header">
    <div class="collapse-btn" @click="collapseChange">
      <i v-if="!collapse" class="el-icon-s-fold"></i>
      <i v-else class="el-icon-s-unfold"></i>
    </div>
    <div class="logo">流程管理系统</div>
    <div class="header-right">
      <div class="header-user-con">
        <div class="user-avatar">
          <img alt="" src="../assets/img/logo.png" />
        </div>
        <el-dropdown class="user-name" trigger="click" @command="handleCommand">
          <span class="el-dropdown-link">
            {{ username }}
            <i class="el-icon-caret-bottom"></i>
          </span>
          <template #dropdown>
            <el-dropdown-menu>
              <a href="https://github.com/Linxfeng/flow-eda" target="_blank">
                <el-dropdown-item>代码仓库</el-dropdown-item>
              </a>
              <el-dropdown-item divided command="logout"
                >退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
  </div>
</template>
<script>
import { ref, computed, onMounted } from "vue";
import { useStore } from "vuex";
import { useRouter } from "vue-router";
import { userInfo, userLogout } from "../api/oauth2";

export default {
  setup() {
    const router = useRouter();
    const store = useStore();
    const collapse = computed(() => store.state.collapse);
    const username = ref("");

    // 获取当前用户信息
    const getUserInfo = () => {
      userInfo().then((res) => {
        if (res && res.result) {
          username.value = res.result.username;
          localStorage.setItem("username", res.result.username);
        }
      });
    };
    getUserInfo();

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

    // 下拉选项命令
    const handleCommand = async (command) => {
      if (command === "logout") {
        // 退出登录
        await userLogout();
        store.state.tagsList = [];
        await router.push("/");
      }
    };

    return {
      username,
      collapse,
      collapseChange,
      handleCommand,
    };
  },
};
</script>
<style lang="less" scoped>
.header {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  color: #fff;
  font-size: 22px;
  background-color: #242f42;

  .logo {
    float: left;
    width: 250px;
    line-height: 70px;
  }
}

.collapse-btn {
  float: left;
  padding: 0 21px;
  line-height: 70px;
  cursor: pointer;

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
  align-items: center;
  height: 70px;
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

.header-user-con {
  display: flex;
  align-items: center;
  height: 70px;
}

.user-name {
  margin-left: 10px;
}

.el-dropdown-link {
  color: #fff;
  cursor: pointer;
}

.el-dropdown-menu__item {
  text-align: center;
}
</style>
