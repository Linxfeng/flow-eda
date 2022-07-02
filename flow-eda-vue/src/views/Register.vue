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
        ref="registerFormRef"
        class="login-form"
      >
        <el-form-item label="账号：" prop="username">
          <el-input
            type="text"
            v-model="ruleForm.username"
            @keyup.enter.native="handleRegister(registerFormRef)"
          ></el-input>
        </el-form-item>
        <el-form-item label="密码：" prop="password">
          <el-input
            type="password"
            v-model="ruleForm.password"
            @keyup.enter.native="handleRegister(registerFormRef)"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button
            style="width: 100%; margin-bottom: 10px"
            type="primary"
            @click="handleRegister(registerFormRef)"
            >注册账号
          </el-button>
          <p class="tips">提示：账号注册成功后会自动登录，请牢记账号和密码</p>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import { ref, reactive } from "vue";
import { useRouter } from "vue-router";
import { userRegister, userLogin } from "../api/oauth2";
import { ElMessage } from "element-plus";

export default {
  name: "Register",
  setup() {
    const router = useRouter();
    const registerFormRef = ref(null);
    const loading = ref(false);
    const ruleForm = reactive({ username: "", password: "" });
    const rules = {
      username: [
        { required: "true", message: "账号不能为空", trigger: "blur" },
      ],
      password: [
        { required: "true", message: "密码不能为空", trigger: "blur" },
      ],
    };

    // 用户注册
    const handleRegister = async (formEl) => {
      formEl.validate(async (valid) => {
        if (valid) {
          loading.value = true;
          const r = await userRegister(ruleForm);
          if (r) {
            ElMessage.success("注册成功");
            // 自动登录
            const success = await userLogin(
              ruleForm.username,
              ruleForm.password
            );
            if (success) {
              // 登录成功，跳转首页
              await router.push("/flows");
            }
          }
          loading.value = false;
        }
      });
    };

    return {
      loading,
      rules,
      ruleForm,
      registerFormRef,
      handleRegister,
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
</style>

<style>
.login-form {
  margin-bottom: 12px;
  color: #606266;
}
</style>
