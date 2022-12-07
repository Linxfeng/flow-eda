<template>
  <div id="flow-log" class="flow-log">
    <div v-dragging class="drag-box"></div>
    <Codemirror
        :value="logContent"
        :options="{mode: 'javascript', styleActiveLine: true, theme: 'dracula', readOnly: true}"
        style="font-size: 14px;"
        @change="onChange"
    />
  </div>
</template>

<script>
import Codemirror from "codemirror-editor-vue3";
import "codemirror/mode/javascript/javascript.js";
import "codemirror/theme/dracula.css";

export default {
  name: "FlowLog",
  props: {
    logContent: String
  },
  components: {
    Codemirror
  },
  directives: {
    // 自定义拖拽指令
    dragging(el) {
      let targetDiv = document.getElementById('flow-log')
      el.onmousedown = function () {
        document.onmousemove = function (e) {
          let th = document.body.clientHeight - e.clientY
          if (th < 100) {
            th = 100
          }
          targetDiv.style.height = th + "px";
        }
        document.onmouseup = function () {
          document.onmousemove = null;
          document.onmouseup = null;
        };
        return false;
      };
    }
  },
  setup() {

    // 自动滚动到底部
    const onChange = (v, cm) => {
      const nowScrollInfo = cm.getScrollInfo();
      cm.scrollTo(nowScrollInfo.left, nowScrollInfo.height);
    };

    return {
      onChange
    };
  }
};
</script>

<style>
.flow-log {
  height: 320px;
  width: 96%;
  position: fixed;
  bottom: 0;
}

.drag-box {
  height: 6px;
  cursor: n-resize;
}
</style>
