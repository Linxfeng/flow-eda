import axios from "axios";
import { ElMessage } from "element-plus";
import { refreshToken } from "../api/oauth2";

const service = axios.create({
  timeout: 8000,
  headers: {
    "Content-Type": "application/json;charset=utf-8",
    "Access-Control-Allow-Origin": "*",
  },
  withCredentials: true,
});

service.interceptors.request.use(
  (config) => {
    if (config.url === "/oauth/token") {
      return config;
    }
    // 请求头添加token
    const headers = { ...config.headers };
    const token = localStorage.getItem("access_token");
    if (token) {
      headers.Authorization = "Bearer " + token;
    }
    return { ...config, headers };
  },
  (error) => {
    console.log(error);
    return Promise.reject();
  }
);

service.interceptors.response.use(
  (response) => {
    const res = response.data;
    if (res.message) {
      ElMessage.error(res.message);
    } else {
      return res;
    }
  },
  async (error) => {
    // 未授权
    if (error.response.status === 401) {
      // 刷新token
      const pass = await refreshToken();
      if (pass) {
        // 刷新token成功，继续请求
        return service.request(error.response.config);
      }
      // 登录过期，跳转登陆页
      ElMessage.error("登录过期");
      location.href = "/";
    } else {
      // 请求出错，弹出错误信息
      if (error.response) {
        const res = error.response.data;
        if (res.message) {
          ElMessage.error(res.message);
        } else if (res.error) {
          ElMessage.error(res.error);
        }
      } else {
        ElMessage.error("request timeout");
      }
    }
  }
);

export default service;
