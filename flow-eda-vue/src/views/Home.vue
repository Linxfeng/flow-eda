<template>
  <div class="about">
    <v-header />
    <v-sidebar />
    <div :class="{ 'content-collapse': collapse }" class="content-box">
      <v-tags />
      <div class="content">
        <router-view v-slot="{ Component }">
          <transition mode="out-in" name="move">
            <keep-alive :include="tagsList">
              <component :is="Component" />
            </keep-alive>
          </transition>
        </router-view>
      </div>
    </div>
  </div>
</template>
<script>
import { computed } from "vue";
import { useStore } from "vuex";
import vHeader from "../components/Header.vue";
import vSidebar from "../components/Sidebar.vue";
import vTags from "../components/Tags.vue";

export default {
  components: {
    vHeader,
    vSidebar,
    vTags,
  },
  setup() {
    const store = useStore();
    const tagsList = computed(() =>
      store.state.tagsList.map((item) => item.name)
    );
    const collapse = computed(() => store.state.collapse);

    return {
      tagsList,
      collapse,
    };
  },
};
</script>
