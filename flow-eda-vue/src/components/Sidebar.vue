<template>
  <div class="sidebar">
    <el-menu :collapse="collapse" :default-active="onRoutes" active-text-color="#20a0ff" background-color="#324157"
             class="sidebar-el-menu" router text-color="#bfcbd9" unique-opened>
      <template v-for="item in items">
        <el-menu-item :index="item.index" style="font-size: 16px">
          <span :class="item.icon" style="margin-right: 4px"></span>
          <template #title>{{ item.title }}</template>
        </el-menu-item>
      </template>
    </el-menu>
  </div>
</template>

<script>
import {computed} from "vue";
import {useStore} from "vuex";
import {useRoute} from "vue-router";

export default {
  setup() {
    const items = [
      {
        icon: "icon-lx-flow",
        index: "/flows",
        title: "流程管理"
      },
      {
        icon: "icon-lx-logs",
        index: "/logs",
        title: "日志管理"
      }
    ];

    const route = useRoute();
    const onRoutes = computed(() => {
      return route.path;
    });

    const store = useStore();
    const collapse = computed(() => store.state.collapse);

    return {
      items,
      onRoutes,
      collapse,
    };
  },
};
</script>

<style lang="less" scoped>
.sidebar {
  display: block;
  position: absolute;
  left: 0;
  top: 70px;
  bottom: 0;
  overflow-y: scroll;

  ::-webkit-scrollbar {
    width: 0;
  }

  > ul {
    height: 100%;
  }
}

.sidebar-el-menu:not(.el-menu--collapse) {
  width: 200px;
}
</style>
