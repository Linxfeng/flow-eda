<template>
  <div id="node" v-click-outside="setNotActive"
       :class="[(data.isActive || data.isSelected) ? 'active' : '']"
       :style="{top: node.top, left: node.left, background: node.nodeType.background}"
       class="node-item"
       @click="setActive"
       @mouseenter="showAnchor"
       @mouseleave="hideAnchor">
    <div class="node-svg">
      <img :src="node.nodeType.svg" alt="" style="padding: 4px">
    </div>
    <div class="node-name">{{ node.nodeName }}</div>
    <div v-if="node.status === 'RUNNING'" class="node-status">
      <img alt="运行中" src="src/assets/svg/status/running.svg" title="运行中...">
    </div>
    <div v-if="node.status === 'FINISHED'" class="node-status">
      <img alt="运行完成" src="src/assets/svg/status/finished.svg" title="运行完成">
    </div>
    <div v-if="node.status === 'FAILED'" class="node-status">
      <img :title="node.error" alt="运行失败" src="src/assets/svg/status/failed.svg">
    </div>
    <div v-show="data.mouseEnter" class="node-anchor anchor-top"></div>
    <div v-show="data.mouseEnter" class="node-anchor anchor-right"></div>
    <div v-show="data.mouseEnter" class="node-anchor anchor-bottom"></div>
    <div v-show="data.mouseEnter" class="node-anchor anchor-left"></div>
  </div>
</template>

<script>
import vClickOutside from 'click-outside-vue3'
import {reactive} from "vue";

export default {
  name: "FlowNode",
  props: {
    node: Object
  },
  directives: {
    clickOutside: vClickOutside.directive
  },
  setup(props, context) {

    const data = reactive({
      mouseEnter: false,
      isActive: false,
      isSelected: false
    });

    const showAnchor = () => {
      data.mouseEnter = true;
    };

    const hideAnchor = () => {
      data.mouseEnter = false
    };

    // 选中节点
    const setActive = (e) => {
      // 默认聚焦一下编辑器面板，使键盘事件生效
      document.getElementById("flowWrap").focus();
      if (e.ctrlKey) {
        data.isSelected = !data.isSelected;
        return false;
      }
      data.isActive = true;
      data.isSelected = false;
      setTimeout(() => {
        context.emit("changeLineState", props.node.id, true);
        context.emit("showNodeDetail", props.node);
      }, 0);
    };

    const setNotActive = (e) => {
      if (!e.ctrlKey) {
        data.isSelected = false;
      }
      if (!data.isActive) {
        return;
      }
      context.emit("changeLineState", props.node.id, false);
      data.isActive = false;
    };

    return {
      data,
      setNotActive,
      setActive,
      showAnchor,
      hideAnchor
    };
  }
};
</script>

<style lang="less" scoped>
@nodeSize: 20px;
.node-item {
  position: absolute;
  display: flex;
  height: 40px;
  width: 120px;
  justify-content: center;
  align-items: center;
  border: 1px solid #b7b6b6;
  border-radius: 4px;
  cursor: move;
  box-sizing: content-box;
  z-index: 9995;

  &:hover {
    z-index: 9998;

    .delete-btn {
      display: block;
    }
  }

  .node-svg {
    width: 40px;
    height: 40px;
    border-right: 1px solid #b7b6b6;
  }

  .node-name {
    font-size: 14px;
    flex-grow: 1;
    width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    text-align: center;
  }

  .node-status {
    position: absolute;
    top: -6px;
    left: 110px;
  }

  .node-anchor {
    display: flex;
    position: absolute;
    width: @nodeSize;
    height: @nodeSize;
    align-items: center;
    justify-content: center;
    border-radius: 10px;
    cursor: crosshair;
    z-index: 9999;
    background: -webkit-radial-gradient(sandybrown 10%, white 30%, #9a54ff 60%);
  }

  .anchor-top {
    top: calc((@nodeSize / 2) * -1);
    left: 50%;
    margin-left: calc((@nodeSize / 2) * -1);
  }

  .anchor-right {
    top: 50%;
    right: calc((@nodeSize / 2) * -1);
    margin-top: calc((@nodeSize / 2) * -1);
  }

  .anchor-bottom {
    bottom: calc((@nodeSize / 2) * -1);
    left: 50%;
    margin-left: calc((@nodeSize / 2) * -1);
  }

  .anchor-left {
    top: 50%;
    left: calc((@nodeSize / 2) * -1);
    margin-top: calc((@nodeSize / 2) * -1);
  }
}

.active {
  border: 1px dashed #409eff;
  box-shadow: 0 5px 9px 0 rgba(0, 0, 0, 0.5);
}
</style>
