<template>
  <div class="login-body">
    <div class="login-container">
      <div class="head">
        <div class="name">
          <div class="title">流程管理系统</div>
        </div>
      </div>
      <el-form
        label-position="top"
        :rules="rules"
        :model="ruleForms"
        ref="loginForm"
        class="login-form"
      >
        <el-form-item label="用户名：" prop="username">
          <el-input
            type="text"
            v-model.trim="ruleForms.username"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码：" prop="password">
          <el-input
            type="password"
            v-model.trim="ruleForms.password"
            autocomplete="off"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <div style="color: #333">登录表示您已同意<a>《服务条款》</a></div>
          <el-button style="width: 100%" type="primary" @click="handleLogin"
            >立即登录</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { reactive, computed } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { ElMessage } from "element-plus";
import { getToken } from "../api/oauth2";

export default {
  name: "Login",
  setup() {
    const router = useRouter();
    const store = useStore();

    const ruleForms = reactive({ username: "", password: "" });
    const rules = {
      username: [
        { required: "true", message: "账户不能为空", trigger: "blur" },
      ],
      password: [
        { required: "true", message: "密码不能为空", trigger: "blur" },
      ],
    };
    const tagsList = computed(() => store.state.tagsList);

    // 进入登录页，先刷新以关闭其他标签页
    if (tagsList.value.length > 1) {
      location.reload();
    }

    // 用户登录
    const handleLogin = () => {
      getToken(ruleForms.username, ruleForms.password).then((res) => {
        if (res) {
          localStorage.setItem("flow.user", JSON.stringify(res));
          const item = tagsList.value[0];
          // 登陆成功，跳转首页
          store.commit("delTagsItem", { item });
          router.push("/");
        } else {
          ElMessage.error("login failed");
        }
      });
    };

    return {
      rules,
      ruleForms,
      handleLogin,
    };
  },
};
</script>

<style scoped>
.login-body {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  background-color: #fff;
  /* background-image: linear-gradient(25deg, #077f7c, #3aa693, #5ecfaa, #7ffac2); */
}

.login-container {
  width: 420px;
  height: 500px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 21px 41px 0 rgba(0, 0, 0, 0.2);
}

.head {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px 0 20px 0;
}

.head img {
  width: 100px;
  height: 100px;
  margin-right: 20px;
}

.head .title {
  color: #1baeae;
  font-weight: bold;
  font-size: 28px;
}

.head {
  color: #999;
  font-size: 12px;
}

.login-form {
  width: 70%;
  margin: 0 auto;
}
</style>
<style>
.login-form {
  margin-bottom: 12px;
}
</style>
