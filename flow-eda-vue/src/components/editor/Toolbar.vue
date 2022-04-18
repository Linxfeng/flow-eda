<template>
  <div class="toolbar">
    <el-tooltip content="撤销" placement="bottom">
      <span class="command" @click="undo">
      <span class="icon-lx-undo"/>
      </span>
    </el-tooltip>
    <el-tooltip content="重做" placement="bottom">
      <span class="command" @click="redo">
      <span class="icon-lx-redo"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
    <el-tooltip content="保存" placement="bottom">
      <span class="command" @click="save">
      <span class="icon-lx-save"/>
      </span>
    </el-tooltip>
    <el-tooltip content="运行" placement="bottom">
      <span class="command" @click="run">
      <span class="icon-lx-run"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
    <el-tooltip content="复制" placement="bottom">
      <span class="command" @click="copy">
      <span class="icon-lx-copy"/>
      </span>
    </el-tooltip>
    <el-tooltip content="粘贴" placement="bottom">
      <span class="command" @click="paste">
      <span class="icon-lx-paste"/>
      </span>
    </el-tooltip>
    <el-tooltip content="删除" placement="bottom">
      <span class="command" @click="del">
      <span class="icon-lx-delete"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
    <el-tooltip content="放大" placement="bottom">
      <span class="command" @click="zoomIn">
      <span class="icon-lx-zoomIn"/>
      </span>
    </el-tooltip>
    <el-tooltip content="缩小" placement="bottom">
      <span class="command" @click="zoomOut">
      <span class="icon-lx-zoomOut"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
  </div>
</template>

<script>
import {ElMessageBox} from "element-plus";

export default {
  name: "Toolbar",
  setup(props, context) {

    const save = () => {
      context.emit("saveData");
    };

    const run = () => {
      ElMessageBox.confirm("确认运行本流程？这将会保存本流程并覆盖之前的数据", "提示", {
        type: "warning",
      }).then(() => {
        context.emit("executeFlow");
      }).catch(() => {
      });
    };

    const del = () => {
      context.emit("deleteNode");
    }

    return {
      save,
      run,
      del
    }
  }
};
</script>

<style lang="less" scoped>
.toolbar {
  text-align: right;
  width: 100%;
  padding: 8px 0;
  border-top: 1px solid #ccc;
  border-left: 1px solid #ccc;
  border-right: 1px solid #ccc;
  box-shadow: 0 8px 12px 0 rgba(0, 52, 107, 0.04);

  .command {
    display: inline-block;
    margin: 0 6px;
    line-height: 27px;
    border: 1px solid rgba(2, 2, 2, 0);
    border-radius: 2px;

    span {
      margin: 0 6px;
    }

    &:nth-of-type(1) {
      margin-left: 24px;
    }

    &:hover {
      border: 1px solid #E9E9E9;
      cursor: pointer;
    }
  }

  .separator {
    margin: 4px;
    border-left: 2px solid #e4e4e4;
  }

  .icon-select.disable {
    background: #EEE;
  }

  .disable {
    color: rgba(0, 0, 0, 0.25);

    &:hover {
      border: 1px solid rgba(2, 2, 2, 0);
      cursor: default;
    }
  }
}
</style>
