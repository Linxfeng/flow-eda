<template>
  <div class="flow-log">
    <Codemirror
        v-model:value="logContent"
        :KeepCursorInEnd="true"
        :options="{mode: 'javascript', styleActiveLine: true, theme: 'dracula', readOnly: true}"
        style="font-size: 14px;"
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
  setup() {

    return {};
  }
};
</script>

<style>
.flow-log {
  height: 400px;
  width: 96%;
  position: fixed;
  bottom: 0;
}
</style>
