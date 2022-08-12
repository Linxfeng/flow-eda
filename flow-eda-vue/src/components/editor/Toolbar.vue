<template>
  <div class="toolbar">
    <div style="float: left; padding-left: 20px; position: relative">
      <span style="font-size: 13px">版本：</span>
      <el-select
        v-model="currentVersion"
        class="m-2"
        placeholder="请选择版本"
        size="small"
      >
        <el-option
          v-for="item in versions"
          :key="item"
          :label="item"
          :value="item"
        />
      </el-select>
    </div>
    <el-tooltip content="导入" placement="bottom">
      <span class="command" @click="handle('import')">
        <span class="icon-lx-import" />
      </span>
    </el-tooltip>
    <el-tooltip content="导出" placement="bottom">
      <span class="command" @click="handle('export')">
        <span class="icon-lx-export" />
      </span>
    </el-tooltip>
    <span class="separator" />
    <el-tooltip :content="logs" placement="bottom">
      <span class="command" @click="handle('logs')">
        <span class="icon-lx-logs" />
      </span>
    </el-tooltip>
    <el-tooltip content="存为版本" placement="bottom">
      <span class="command" @click="handle('version')">
        <span class="icon-lx-version" />
      </span>
    </el-tooltip>
    <span class="separator" />
    <el-tooltip content="保存" placement="bottom">
      <span class="command" @click="handle('save')">
        <span class="icon-lx-save" />
      </span>
    </el-tooltip>
    <el-tooltip v-if="status !== 'RUNNING'" content="运行" placement="bottom">
      <span class="command" @click="handle('run')">
        <span class="icon-lx-run" />
      </span>
    </el-tooltip>
    <el-tooltip v-if="status === 'RUNNING'" content="停止" placement="bottom">
      <span class="command" @click="handle('stop')">
        <span class="icon-lx-stop" />
      </span>
    </el-tooltip>
    <span class="separator" />
    <el-tooltip content="复制" placement="bottom">
      <span class="command" @click="handle('copy')">
        <span class="icon-lx-copy" />
      </span>
    </el-tooltip>
    <el-tooltip content="粘贴" placement="bottom">
      <span class="command" @click="handle('paste')">
        <span class="icon-lx-paste" />
      </span>
    </el-tooltip>
    <el-tooltip content="删除" placement="bottom">
      <span class="command" @click="handle('del')">
        <span class="icon-lx-delete" />
      </span>
    </el-tooltip>
    <span class="separator" />
    <el-tooltip content="放大" placement="bottom">
      <span class="command" @click="handle('zoom-in')">
        <span class="icon-lx-zoomIn" />
      </span>
    </el-tooltip>
    <el-tooltip content="缩小" placement="bottom">
      <span class="command" @click="handle('zoom-out')">
        <span class="icon-lx-zoomOut" />
      </span>
    </el-tooltip>
    <span class="separator" />
    <el-tooltip content="全屏" placement="bottom">
      <span class="command" @click="handle('zoom-full')">
        <span class="icon-lx-zoomFull" />
      </span>
    </el-tooltip>
    <el-tooltip content="重置" placement="bottom">
      <span class="command" @click="handle('zoom-reset')">
        <span class="icon-lx-zoomReset" />
      </span>
    </el-tooltip>
  </div>
</template>

<script>
import { ElMessage, ElMessageBox } from "element-plus";
import { ref } from "vue";
import Moment from "moment";

export default {
  name: "Toolbar",
  props: {
    status: String,
  },
  setup(props, context) {
    const versions = ["当前版本：最新版", "2022-08-10", "v1.0.1"];
    const currentVersion = ref(versions[0]);

    const run = () => {
      ElMessageBox.confirm(
        "确认运行本流程？这将会保存本流程并覆盖之前的数据",
        "提示",
        {
          type: "warning",
        }
      )
        .then(() => {
          context.emit("executeFlow");
        })
        .catch(() => {});
    };

    const stop = () => {
      ElMessageBox.confirm("确认停止运行？这将会立即停止本流程的运行", "提示", {
        type: "warning",
      })
        .then(() => {
          context.emit("stopFlow");
        })
        .catch(() => {});
    };

    let logs = ref("查看日志");
    let openLogs = false;

    const handle = (command) => {
      if (command.startsWith("zoom")) {
        context.emit("zoomNode", command.split("-")[1]);
      } else if (command === "import") {
        ElMessageBox.confirm(
          "确认导入流程？这将会从当前剪切板中读取内容生成新的流程图，完全覆盖本流程的数据",
          "提示",
          {
            type: "warning",
          }
        )
          .then(() => {
            context.emit("importFlow");
          })
          .catch(() => {});
      } else if (command === "export") {
        context.emit("exportFlow");
        ElMessage.success("导出成功！内容已复制到剪切板");
      } else if (command === "logs") {
        openLogs = !openLogs;
        if (openLogs) {
          context.emit("showLogs", true);
          logs.value = "关闭日志";
        } else {
          context.emit("showLogs", false);
          logs.value = "查看日志";
        }
      } else if (command === "version") {
        const date = Moment().format("YYYYMMDD-HH:mm:ss");
        ElMessageBox.prompt("请输入版本名称：", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          inputPattern: /^[\u4e00-\u9fa5_a-zA-Z0-9(.){\[\]}@:\-=—]+$/,
          inputErrorMessage: "版本名称不能包含特殊字符和空格",
          inputValue: date,
        })
          .then(({ value }) => {
            context.emit("saveData", value);
          })
          .catch(() => {});
      } else if (command === "save") {
        context.emit("saveData", null);
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
      logs,
      versions,
      currentVersion,
      handle,
    };
  },
};
</script>

<style lang="less" scoped>
.toolbar {
  width: 100%;
  padding: 8px 0;
  text-align: right;
  border-top: 1px solid #ccc;
  border-right: 1px solid #ccc;
  border-left: 1px solid #ccc;
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
      border: 1px solid #e9e9e9;
      cursor: pointer;
    }
  }

  .separator {
    margin: 4px;
    border-left: 2px solid #e4e4e4;
  }
}
</style>
