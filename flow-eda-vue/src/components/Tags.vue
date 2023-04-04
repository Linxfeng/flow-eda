<template>
  <div v-if="showTags" class="tags">
    <ul>
      <li v-for="(item,index) in tagsList" :key="index" :class="{'active': isActive(item.path)}" class="tags-li">
        <router-link :to="item.path" class="tags-li-title">{{ item.title }}</router-link>
        <span class="tags-li-icon" @click="closeTags(index)"><el-icon><Close /></el-icon></span>
      </li>
    </ul>
    <div class="tags-close-box">
      <el-dropdown @command="handleTags">
        <el-button size="small" type="primary">
          标签选项
          <el-icon><ArrowDown /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu size="small">
            <el-dropdown-item command="other">关闭其他</el-dropdown-item>
            <el-dropdown-item command="me">关闭当前</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import { computed } from "vue";
import { useStore } from "vuex";
import { onBeforeRouteUpdate, useRoute, useRouter } from "vue-router";
import { ElMessageBox } from "element-plus";

export default {
  setup() {
    const route = useRoute();
    const router = useRouter();

    const isActive = (path) => {
      return path === route.fullPath;
    };

    const store = useStore();
    const tagsList = computed(() => store.state.tagsList);
    const showTags = computed(() => tagsList.value.length > 0);

    // 关闭单个标签
    const closeTags = (index) => {
      const delItem = tagsList.value[index];
      // 关闭编辑器页面时，弹出提示
      if (delItem.name === "Editor") {
        ElMessageBox.confirm(
          "确定关闭编辑器页面？关闭后未保存的数据将丢失",
          "提示",
          {
            type: "warning",
          }
        )
          .then(() => closeTag(index, delItem))
          .catch((err) => {});
      } else {
        closeTag(index, delItem);
      }
    };

    const closeTag = (index, delItem) => {
      store.commit("delTagsItem", { index });
      const item = tagsList.value[index]
        ? tagsList.value[index]
        : tagsList.value[index - 1];
      if (item) {
        delItem.path === route.fullPath && router.push(item.path);
      } else {
        if (tagsList.value.length === 0) {
          tagsList.value[0] = delItem;
          router.push(delItem.path);
        }
      }
    };

    // 设置标签
    const setTags = (route) => {
      const isExist = tagsList.value.some((item) => {
        return item.path === route.fullPath;
      });
      if (!isExist) {
        if (tagsList.value.length >= 8) {
          store.commit("delTagsItem", { index: 0 });
        }
        store.commit("setTagsItem", {
          name: route.name,
          title: route.meta.title,
          path: route.fullPath,
        });
      }
    };
    setTags(route);

    onBeforeRouteUpdate((to) => {
      setTags(to);
    });

    // 关闭当前标签
    const closeMe = () => {
      const index = tagsList.value.findIndex((i) => i.path === route.fullPath);
      closeTags(index);
    };

    // 关闭其他标签
    const closeOther = () => {
      const curItem = tagsList.value.filter((item) => {
        return item.path === route.fullPath;
      });
      store.commit("closeTagsOther", curItem);
    };

    const handleTags = (command) => {
      command === "other" ? closeOther() : closeMe();
    };

    return {
      tagsList,
      showTags,
      isActive,
      closeTags,
      handleTags,
    };
  },
};
</script>

<style lang="less">
.tags {
  position: relative;
  height: 30px;
  padding-right: 120px;
  overflow: hidden;
  background: #fff;
  box-shadow: 0 5px 10px #ddd;

  ul {
    box-sizing: border-box;
    width: 100%;
    height: 100%;
  }
}

.tags-li {
  float: left;
  height: 23px;
  margin: 3px 5px 2px 3px;
  padding: 0 5px 0 12px;
  overflow: hidden;
  color: #666;
  font-size: 12px;
  line-height: 23px;
  vertical-align: middle;
  background: #fff;
  border: 1px solid #e9eaec;
  border-radius: 3px;
  cursor: pointer;
  transition: all 0.3s ease-in;
}

.tags-li-title {
  float: left;
  max-width: 80px;
  margin-right: 5px;
  overflow: hidden;
  color: #666;
  white-space: nowrap;
  text-overflow: ellipsis;
}

.tags-li.active .tags-li-title {
  color: #fff;
}

.tags-li.active {
  background-color: #409eff;
  border: 1px solid #409eff;
}

.tags-close-box {
  position: absolute;
  top: 0;
  right: 0;
  box-sizing: border-box;
  width: 110px;
  height: 30px;
  padding-top: 1px;
  text-align: center;
  background: #fff;
}
</style>
