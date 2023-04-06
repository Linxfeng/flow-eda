import vClickOutside from "click-outside-vue3";
import ElementPlus from "element-plus";
import localeZH from "element-plus/lib/locale/lang/zh-cn";
import "element-plus/lib/theme-chalk/index.css";
import { createApp } from "vue";
import App from "./App.vue";
import "./assets/iconfont/iconfont.css";
import router from "./router";
import store from "./store";

const app = createApp(App);
app
  .use(ElementPlus, { locale: localeZH })
  .use(vClickOutside)
  .use(store)
  .use(router)
  .mount("#app");

window.$wsIp = "ws://localhost";
