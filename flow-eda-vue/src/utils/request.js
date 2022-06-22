import axios from "axios";
import { ElMessage } from "element-plus";

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
    return config;
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
  (error) => {
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
);

export default service;
