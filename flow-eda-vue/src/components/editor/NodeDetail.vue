<template>
  <div v-click-outside="hideDetail" class="node-detail">
    <div class="detail-header">
      <div class="title">{{ node.typeName }}</div>
      <el-button class="button el-button--small" style="float: right" type="primary" @click="updateNode">保存</el-button>
    </div>
    <div class="detail-body">
      <div class="row">
        <div class="title">名称：</div>
        <el-input v-model="node.nodeName" class="input"/>
      </div>
      <div v-for="item in data.params" :id="item.key" :key="item.key" class="row">
        <div class="title">{{ item.key }}：</div>
        <el-input v-model="item.value" class="input"/>
      </div>
    </div>
  </div>
</template>

<script>
import vClickOutside from 'click-outside-vue3'
import {reactive} from "vue";

export default {
  name: "NodeDetail",
  props: {
    node: Object
  },
  directives: {
    clickOutside: vClickOutside.directive
  },
  setup(props, context) {
    // 节点参数
    const data = reactive({
      params: []
    });
    if (props.node.parameter) {
      const args = props.node.parameter.split(",");
      args.map(p => {
        data.params.push({key: p, value: ""});
      });
    }

    const hideDetail = () => {
      context.emit("showNodeDetail", props.node, false);
    };

    const updateNode = () => {
      console.log(data.params);
    };

    return {
      data,
      hideDetail,
      updateNode
    };
  }
};
</script>

<style lang="less">
.node-detail {
  height: 100%;
  overflow: auto;
  background: #ffffff;
  flex: 0 0 auto;
  float: left;
  width: 20%;
  border-right: 1px solid #E9E9E9;
  border-bottom: 1px solid #E9E9E9;

  .detail-header {
    padding: 12px 12px 10px 12px;
    min-height: 32px;
    border-bottom: 1px solid #DCE3E8;

    .title {
      float: left;
      font-size: 18px;
      text-align: center;
      line-height: 32px;
    }

    .button {
      font-size: 14px;
      padding: 7px 15px
    }
  }

  .detail-body {
    .row {
      text-align: left;
      font-size: 14px;
      padding: 8px 12px 8px 12px;

      .title {
        width: 98%;
        padding: 2px 2px 6px;
      }

      .input {
        width: 98%;
      }
    }
  }
}
</style>
