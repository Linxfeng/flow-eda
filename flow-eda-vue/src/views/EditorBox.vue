<template>
  <div style="height: 100%;">
    <editor v-for="item in editorList" v-show="item === flowId" :key="item" :flowId="item"></editor>
  </div>
</template>

<script>
import {computed} from "vue";
import {useStore} from "vuex";
import {useRoute} from "vue-router";
import Editor from "./Editor.vue";

export default {
  name: "EditorBox",
  components: {
    Editor
  },
  setup() {
    const store = useStore();
    const route = useRoute();

    const flowId = Number.parseInt(route.query.flowId);
    const editorList = computed(() => store.state.editorList).value;

    return {
      flowId,
      editorList
    };
  },
};
</script>
