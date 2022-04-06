<template>
  <div id="node" ref="node"
       v-click-outside="setNotActive"
       :class="[(data.isActive || data.isSelected) ? 'active' : '']"
       :style="{top: node.top, left: node.left, background: node.background}"
       class="node-item"
       @click="setActive"
       @mouseenter="showAnchor"
       @mouseleave="hideAnchor">
    <div class="log-wrap">
      <img :src="node.svg" alt="" style="padding: 4px">
    </div>
    <div class="nodeName">{{ node.nodeName }}</div>
    <!--节点选中时四周边框样式-->
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
      isSelected: false,
    });

    const showAnchor = () => {
      data.mouseEnter = true;
    };

    const hideAnchor = () => {
      data.mouseEnter = false
    };

    const setActive = (e) => {
      if (e.ctrlKey) {
        data.isSelected = !data.isSelected;
        return false;
      }
      data.isActive = true;
      data.isSelected = false;
      setTimeout(() => {
        context.emit("changeLineState", props.node.id, true);
        context.emit("showNodeDetail", props.node, true);
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
@labelColor: #409eff;
@nodeSize: 20px;
@viewSize: 10px;
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

  .log-wrap {
    width: 40px;
    height: 40px;
    border-right: 1px solid #b7b6b6;
  }

  .nodeName {
    font-size: 14px;
    flex-grow: 1;
    width: 0;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
    text-align: center;
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
  border: 1px dashed @labelColor;
  box-shadow: 0 5px 9px 0 rgba(0, 0, 0, 0.5);
}
</style>
