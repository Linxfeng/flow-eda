import {createApp} from 'vue'
import router from './router'
import App from './App.vue'
import store from './store'
import './assets/css/icon.css'
import 'element-plus/lib/theme-chalk/index.css'
import ElementPlus from 'element-plus'
import localeZH from 'element-plus/lib/locale/lang/zh-cn'

const app = createApp(App)
app
    .use(ElementPlus, {locale: localeZH})
    .use(store)
    .use(router)
    .mount('#app')