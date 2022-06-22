<template>
  <div class="login-body" v-loading="loading">
    <div class="login-container">
      <div class="head">
        <div class="name">
          <div class="title">流程管理系统</div>
        </div>
      </div>
      <el-form
        label-position="top"
        :rules="rules"
        :model="ruleForm"
        ref="loginFormRef"
        class="login-form"
      >
        <el-form-item label="账号：" prop="username">
          <el-input
            type="text"
            v-model="ruleForm.username"
            autocomplete="off"
            @keyup.enter.native="handleLogin(loginFormRef)"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码：" prop="password">
          <el-input
            type="password"
            v-model="ruleForm.password"
            autocomplete="off"
            @keyup.enter.native="handleLogin(loginFormRef)"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            style="width: 100%; margin-bottom: 10px"
            type="primary"
            @click="handleLogin(loginFormRef)"
            >立即登录
          </el-button>
          <p class="tips">
            提示：默认使用体验账号 test 登录，体验账号的数据会每日重置。
          </p>
          <p class="tips">
            若需要永久保存用户所有数据，请先<a class="href" href="/"
              >注册账号</a
            >
          </p>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed } from "vue";
import { useRouter } from "vue-router";
import { useStore } from "vuex";
import { getToken } from "../api/oauth2";

export default {
  name: "Login",
  setup() {
    const router = useRouter();
    const store = useStore();

    const loginFormRef = ref(null);
    const loading = ref(false);
    const ruleForm = reactive({ username: "test", password: "test" });
    const rules = {
      username: [
        { required: "true", message: "账号不能为空", trigger: "blur" },
      ],
      password: [
        { required: "true", message: "密码不能为空", trigger: "blur" },
      ],
    };
    const tagsList = computed(() => store.state.tagsList);

    // 用户登录
    const handleLogin = (formEl) => {
      formEl.validate((valid) => {
        if (valid) {
          loading.value = true;
          getToken(ruleForm.username, ruleForm.password).then((res) => {
            if (res) {
              if (res.access_token) {
                localStorage.setItem("access_token", res.access_token);
              }
              if (res.refresh_token) {
                localStorage.setItem("refresh_token", res.refresh_token);
              }
              // 登陆成功，跳转首页
              const item = tagsList.value[0];
              store.commit("delTagsItem", { item });
              router.push("/");
            }
            loading.value = false;
          });
        }
      });
    };

    return {
      loading,
      rules,
      ruleForm,
      loginFormRef,
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
  height: 100%;
  background-color: #fff;
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
  color: #409eff;
  font-weight: bold;
  font-size: 26px;
}

.head {
  color: #999;
  font-size: 12px;
}

.login-form {
  width: 70%;
  margin: 0 auto;
}

.tips {
  margin-top: 12px;
  line-height: 24px;
}

.href {
  color: #409eff;
  font-weight: bold;
  font-size: 16px;
  text-decoration: underline;
}
</style>

<style>
.login-form {
  margin-bottom: 12px;
  color: #606266;
}
</style>
