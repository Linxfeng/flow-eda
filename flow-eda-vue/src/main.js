import vClickOutside from "click-outside-vue3";
import ElementPlus from "element-plus";
import localeZH from "element-plus/lib/locale/lang/zh-cn";
import "element-plus/theme-chalk/index.css";
import { createApp } from "vue";
import App from "./App.vue";
import "./assets/iconfont/iconfont.css";
import router from "./router";
import store from "./store";
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App);
app
  .use(ElementPlus, { locale: localeZH })
  .use(vClickOutside)
  .use(store)
  .use(router)
  .mount("#app");

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
window.$wsIp = "ws://localhost";
