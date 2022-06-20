<template>
  <el-form :model="ruleForms" label-width="120px">
    <el-form-item label="用户名：" prop="username">
      <el-input v-model="ruleForms.username" autocomplete="off"/>
    </el-form-item>
    <el-form-item label="密码：" prop="password">
      <el-input type="password" v-model="ruleForms.password"/>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="handleLogin">Submit</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {reactive} from "vue";
import {getCode, userLogin} from "../api/oauth2";

export default {
  name: "Login",
  setup() {
    const ruleForms = reactive({username: "", password: ""});

    const handleLogin = () => {
      userLogin(ruleForms.username, ruleForms.password).then((res) => {
        if (res) {
          console.log(res);
          getCode();
        }
      });
    };

    return {
      ruleForms,
      handleLogin,
    };
  },
};
</script>

<style scoped></style>
