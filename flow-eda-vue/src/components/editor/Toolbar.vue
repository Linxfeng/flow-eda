<template>
  <div class="toolbar">
    <el-tooltip content="撤销" placement="bottom">
      <span class="command" data-command="undo">
      <span class="iconfont icon-undo"/>
      </span>
    </el-tooltip>
    <el-tooltip content="重做" placement="bottom">
      <span class="command" data-command="redo">
      <span class="iconfont icon-redo"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
    <el-tooltip content="复制" placement="bottom">
      <span class="command" data-command="copy">
      <span class="iconfont icon-copy-o"/>
      </span>
    </el-tooltip>
    <el-tooltip content="粘贴" placement="bottom">
      <span class="command" data-command="paste">
      <span class="iconfont icon-paster-o"/>
      </span>
    </el-tooltip>
    <el-tooltip content="删除" placement="bottom">
      <span class="command" data-command="delete">
      <span class="iconfont icon-delete-o"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
    <el-tooltip content="放大" placement="bottom">
      <span class="command" data-command="zoomIn">
      <span class="iconfont icon-zoom-in-o"/>
      </span>
    </el-tooltip>
    <el-tooltip content="缩小" placement="bottom">
      <span class="command" data-command="zoomOut">
      <span class="iconfont icon-zoom-out-o"/>
      </span>
    </el-tooltip>
    <el-tooltip content="实际大小" placement="bottom">
      <span class="command" data-command="resetZoom">
      <span class="iconfont icon-actual-size-o"/>
      </span>
    </el-tooltip>
    <el-tooltip content="适应屏幕" placement="bottom">
      <span class="command" data-command="autoFit">
      <span class="iconfont icon-fit"/>
      </span>
    </el-tooltip>
    <span class="separator"/>
  </div>
</template>

<script>
export default {
  name: "Toolbar",
  setup() {

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

.iconfont {
  font-family: "fb-iconfont", serif !important;
  font-size: 16px;
  font-style: normal;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

.icon-copy-o:before {
  content: "\e8a2";
}

.icon-undo:before {
  content: "\e8a5";
}

.icon-zoom-in-o:before {
  content: "\e8a6";
}

.icon-actual-size-o:before {
  content: "\e7cb";
}

.icon-redo:before {
  content: "\e7cc";
}

.icon-fit:before {
  content: "\e7cd";
}

.icon-delete-o:before {
  content: "\e7ce";
}

.icon-paster-o:before {
  content: "\e7d1";
}

.icon-zoom-out-o:before {
  content: "\e7d2";
}
</style>
