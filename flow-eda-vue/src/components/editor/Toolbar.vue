<template>
  <div class="toolbar">
    <span class="separator"/>
    <el-tooltip content="保存" placement="bottom">
      <span class="command" @click="handle('save')">
      <span class="icon-lx-save"/>
      </span>
    </el-tooltip>
    <el-tooltip v-if="statusRef!=='RUNNING'" content="运行" placement="bottom">
      <span class="command" @click="handle('run')">
      <span class="icon-lx-run"/>
      </span>
    </el-tooltip>
    <el-tooltip v-if="statusRef==='RUNNING'" content="停止" placement="bottom">
      <span class="command" @click="handle('stop')">
      <span class="icon-lx-stop"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
    <el-tooltip content="复制" placement="bottom">
      <span class="command" @click="handle('copy')">
      <span class="icon-lx-copy"/>
      </span>
    </el-tooltip>
    <el-tooltip content="粘贴" placement="bottom">
      <span class="command" @click="handle('paste')">
      <span class="icon-lx-paste"/>
      </span>
    </el-tooltip>
    <el-tooltip content="删除" placement="bottom">
      <span class="command" @click="handle('del')">
      <span class="icon-lx-delete"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
    <el-tooltip content="放大" placement="bottom">
      <span class="command" @click="handle('zoom-in')">
      <span class="icon-lx-zoomIn"/>
      </span>
    </el-tooltip>
    <el-tooltip content="缩小" placement="bottom">
      <span class="command" @click="handle('zoom-out')">
      <span class="icon-lx-zoomOut"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
    <el-tooltip content="全屏" placement="bottom">
      <span class="command" @click="handle('zoom-full')">
      <span class="icon-lx-zoomFull"/>
      </span>
    </el-tooltip>
    <el-tooltip content="重置" placement="bottom">
      <span class="command" @click="handle('zoom-reset')">
      <span class="icon-lx-zoomReset"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
  </div>
</template>

<script>
import {ElMessage, ElMessageBox} from "element-plus";
import {ref, watch} from "vue";

export default {
  name: "Toolbar",
  props: {
    status: String
  },
  setup(props, context) {

    // 运行状态副本，通过watch监听状态变化
    let statusRef = ref("");
    watch(() => props.status,
        (n, o) => statusRef.value = n
    );

    const run = () => {
      ElMessageBox.confirm("确认运行本流程？这将会保存本流程并覆盖之前的数据", "提示", {
        type: "warning",
      }).then(() => {
        if (statusRef.value === "RUNNING") {
          ElMessage.warning("流程正在运行中，请稍候")
        } else {
          context.emit("executeFlow");
          statusRef.value = "RUNNING";
        }
      }).catch(() => {
      });
    };

    const stop = () => {
      ElMessageBox.confirm("确认停止运行？这将会立即停止本流程的运行", "提示", {
        type: "warning",
      }).then(() => {
        if (statusRef.value !== "RUNNING") {
          ElMessage.warning("流程已运行完成")
        } else {
          context.emit("stopFlow");
          statusRef.value = "FAILED";
        }
      }).catch(() => {
      });
    };

    const handle = (command) => {
      if (command.startsWith("zoom")) {
        context.emit("zoomNode", command.split('-')[1]);
      } else if (command === "save") {
        context.emit("saveData");
        ElMessage.success("保存成功");
      } else if (command === "run") {
        run();
      } else if (command === "stop") {
        stop();
      } else if (command === "copy") {
        context.emit("copyNode");
      } else if (command === "paste") {
        context.emit("pasteNode");
      } else if (command === "del") {
        context.emit("deleteNode");
      }
    };

    return {
      statusRef,
      handle
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
}
</style>
