<template>
  <div class="node-detail">
    <div class="detail-header">
      <div class="title">{{ node.typeName }}</div>
      <el-button class="button el-button--small" style="float: right" type="primary" @click="editNode">保存</el-button>
    </div>
    <div class="detail-body">
      <div class="row">
        <div class="title">ID：</div>
        <el-input class="input"/>
      </div>
      <div class="row">
        <div class="title">名称：</div>
        <el-input v-model="node.nodeName" class="input"/>
      </div>
      <div class="row">
        <div class="title">名称：</div>
        <el-input v-model="node.nodeName" class="input"/>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "NodeDetail",
  props: {
    node: Object
  },
  setup() {

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
