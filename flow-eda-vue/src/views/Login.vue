<template>
  <div
    class="login-body"
    v-loading="loading"
    element-loading-text="正在处理中"
    element-loading-spinner="el-icon-loading"
    element-loading-background="rgba(0, 0, 0, 0.6)"
  >
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
            @keyup.enter.native="handleLogin(loginFormRef)"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码：" prop="password">
          <el-input
            type="password"
            v-model="ruleForm.password"
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
            若需要永久保存用户下的数据，请先
            <router-link class="link" to="/register">注册账号</router-link>
          </p>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { userLogin } from "../api/oauth2";
import { ElMessage } from "element-plus";

export default {
  name: "Login",
  setup() {
    const router = useRouter();
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

    // 用户登录
    const handleLogin = async (formEl) => {
      formEl.validate(async (valid) => {
        if (valid) {
          loading.value = true;
          const success = await userLogin(ruleForm.username, ruleForm.password);
          if (success) {
            // 登录成功，跳转首页
            ElMessage.success("登录成功");
            await router.push("/flows");
          }
          loading.value = false;
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
  padding: 35px 0 20px 0;
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

.link {
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
