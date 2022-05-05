<template>
  <div id="log-detail" class="log-detail">
    <Codemirror v-model:value="logContent"
                :options="{mode: 'javascript', styleActiveLine: true, theme: 'dracula', readOnly: true}"
                style="font-size: 14px;"/>
  </div>
</template>

<script>
import Codemirror from "codemirror-editor-vue3";
import "codemirror/mode/javascript/javascript.js";
import "codemirror/theme/dracula.css";
import {onBeforeUnmount, ref, watch} from "vue";
import {onCloseLogDetail, onOpenLogDetail} from "../utils/websocket.js";

export default {
  name: "LogDetail",
  props: {
    path: String
  },
  components: {
    Codemirror
  },
  setup(props) {

    const logContent = ref("");

    // 监听参数变化，加载新数据，关闭旧连接
    watch(
        () => props.path,
        (n, o) => {
          getData(n);
          onCloseLogDetail(o);
        }
    );

    // 获取日志内容
    const getData = (path) => {
      logContent.value = "";
      onOpenLogDetail(path, (s) => {
        logContent.value = logContent.value.concat(s);
      });
    };

    // 初始加载
    getData(props.path);

    // 组件被销毁之前，关闭socket连接
    onBeforeUnmount(() => {
      onCloseLogDetail(props.path);
    });

    return {
      logContent
    };
  }
};
</script>

<style>
.log-detail {
  height: 100%;
  width: 100%;
}
</style>
