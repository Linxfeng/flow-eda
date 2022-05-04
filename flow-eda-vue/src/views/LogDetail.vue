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
import {onBeforeUnmount, ref} from "vue";
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

    // 获取日志内容
    const logContent = ref("");
    const getData = () => {
      onOpenLogDetail(props.path, (s) => {
        logContent.value = logContent.value.concat(s);
      });
    };
    getData();

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
