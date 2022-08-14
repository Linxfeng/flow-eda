<template>
  <div style="height: 100%">
    <toolbar
      :versions="versions"
      :status="flowStatus"
      @copyNode="copyNode(data.selectedNode)"
      @deleteNode="deleteNode(data.selectedNode)"
      @executeFlow="executeFlow"
      @keyup="keyupNode($event, data.selectedNode)"
      @pasteNode="pasteNode"
      @saveData="saveData"
      @showLogs="showLogs"
      @stopFlow="stopFlow"
      @zoomNode="zoomNode"
      @importFlow="importFlow"
      @exportFlow="exportFlow"
      @switchVersion="switchVersion"
    />
    <div id="flow-content" class="flow-content">
      <div class="nodes-wrap">
        <el-collapse
          v-for="menu in Object.keys(data.nodeTypeList)"
          :model-value="menu"
        >
          <el-collapse-item :title="menu" :name="menu">
            <div
              v-for="item in data.nodeTypeList[menu]"
              :key="item.type"
              :style="{ background: item.background }"
              class="node"
              draggable="true"
              @dragstart="drag(item)"
              @mousemove="moveDes($event, item)"
              @mouseout="hideDes(item)"
            >
              <div class="svg">
                <img :src="item.svg" alt="" style="padding: 4px" />
              </div>
              <div class="name">{{ item.typeName }}</div>
              <div
                v-show="showDescription.show"
                :style="{
                  left: showDescription.left,
                  top: showDescription.top,
                }"
                class="description"
              >
                {{ showDescription.data }}
              </div>
            </div>
          </el-collapse-item>
        </el-collapse>
      </div>
      <div
        id="flowWrap"
        ref="flowWrap"
        class="flow-wrap"
        @dragover="allowDrop"
        @drop="drop"
        @keyup="keyupNode($event, data.selectedNode)"
      >
        <div id="flow" tabindex="1" @focusin="showNodeDetail(null)">
          <div
            v-show="auxiliaryLine.isShowXLine"
            :style="{
              width: auxiliaryLinePos.width,
              top: auxiliaryLinePos.y + 'px',
              left: auxiliaryLinePos.offsetX + 'px',
            }"
            class="auxiliary-line-x"
          ></div>
          <div
            v-show="auxiliaryLine.isShowYLine"
            :style="{
              height: auxiliaryLinePos.height,
              left: auxiliaryLinePos.x + 'px',
              top: auxiliaryLinePos.offsetY + 'px',
            }"
            class="auxiliary-line-y"
          ></div>
          <flowNode
            v-for="item in data.nodeList"
            :id="item.id"
            :key="item.id"
            :node="item"
            @changeLineState="changeLineState"
            @showNodeDetail="showNodeDetail"
          />
        </div>
      </div>
      <nodeDetail
        v-if="data.selectedNode"
        :node="data.selectedNode"
        @showNodeDetail="showNodeDetail"
        @updateNode="updateNode"
      />
      <flowLog v-if="logVisible" :log-content="logContent" />
    </div>
  </div>
</template>

<script>
import { nextTick, onBeforeUnmount, onMounted, reactive, ref } from "vue";
import { jsplumbSetting } from "../utils/jsplumbConfig.js";
import {
  jsplumbConnectOptions,
  jsplumbSourceOptions,
  jsplumbTargetOptions,
} from "../utils/jsplumbConfig";
import { ElMessage, ElMessageBox } from "element-plus";
import { jsPlumb } from "jsplumb";
import { generateUniqueID } from "../utils/util.js";
import { getNodeTypes } from "../api/nodeType.js";
import {
  executeNodeData,
  getNodeData,
  getVersion,
  saveVersion,
  setNodeData,
  stopNodeData,
} from "../api/nodeData.js";
import { onClose, onCloseLogs, onOpen, onOpenLogs } from "../api/ws.js";
import panzoom from "panzoom";
import toolbar from "../components/editor/Toolbar.vue";
import flowNode from "../components/editor/FlowNode.vue";
import nodeDetail from "../components/editor/NodeDetail.vue";
import flowLog from "../components/editor/FlowLog.vue";
import screenfull from "screenfull";

export default {
  name: "Editor",
  components: {
    flowNode,
    toolbar,
    nodeDetail,
    flowLog,
  },
  props: {
    flowId: String,
  },
  setup(props) {
    // jsPlumb实例
    const jsPlumbInstance = jsPlumb.getInstance();
    // 面板上的节点数据
    const data = reactive({
      nodeTypeList: {},
      nodeList: [],
      lineList: [],
      selectedNode: null,
    });
    // 对齐辅助线
    const auxiliaryLine = reactive({ isShowXLine: false, isShowYLine: false });
    const auxiliaryLinePos = reactive({
      width: "100%",
      height: "100%",
      offsetX: 0,
      offsetY: 0,
      x: 20,
      y: 20,
    });
    // 展示节点类型的描述
    let showDescription = reactive({
      show: false,
      data: "",
      left: "0px",
      top: "0px",
    });

    // 初始化节点类型
    const initNodeType = async () => {
      const res = await getNodeTypes();
      if (res) {
        data.nodeTypeList = res.result;
      }
    };

    // 初始化节点数据
    const initNode = async (version) => {
      const params = { flowId: props.flowId };
      if (version) {
        params.version = version;
      }
      const res = await getNodeData(params);
      if (res) {
        const node = [];
        const line = [];
        res.result.map((d) => {
          if (d.nodeName) {
            node.push(d);
          } else {
            line.push(d);
          }
        });
        data.nodeList = node;
        data.lineList = line;
      }
    };

    // 初始化画板
    const init = () => {
      jsPlumbInstance.ready(() => {
        // 导入默认配置
        jsPlumbInstance.importDefaults(jsplumbSetting);
        // 连线创建成功后，维护本地数据
        jsPlumbInstance.bind("connection", (evt) => {
          addLine(evt);
        });
        //连线双击删除事件
        jsPlumbInstance.bind("dblclick", (line) => {
          confirmDeleteLine(line);
        });
        //断开连线后，维护本地数据
        jsPlumbInstance.bind("connectionDetached", (evt) => {
          deleteLine(evt);
        });
        // 加载流程图
        loadEasyFlow();
        // 使整个jsPlumbInstance立即重绘。
        jsPlumbInstance.setSuspendDrawing(false, true);
      });
      // 面板缩放
      initPanZoom();
    };

    // 面板重绘，加载新的节点数据，重新绘制流程图
    const resetFlowPanel = () => {
      data.selectedNode = null;
      setTimeout(() => {
        jsPlumbInstance.cleanupListeners();
        jsPlumbInstance.deleteEveryConnection();
        jsPlumbInstance.deleteEveryEndpoint();
        jsPlumbInstance.reset();
        init();
      }, 0);
    };

    const addLine = (line) => {
      let from = line.source.id;
      let to = line.target.id;
      data.lineList.push({
        id: generateUniqueID(8),
        from: from,
        to: to,
      });
    };

    const confirmDeleteLine = (line) => {
      ElMessageBox.confirm("确认删除该连线？", "提示", {
        type: "warning",
      })
        .then(() => {
          jsPlumbInstance.deleteConnection(line);
        })
        .catch(() => {});
    };

    const deleteLine = (line) => {
      data.lineList.forEach((item, index) => {
        if (item.from === line.sourceId && item.to === line.targetId) {
          data.lineList.splice(index, 1);
        }
      });
    };

    // 加载流程图
    const loadEasyFlow = () => {
      // 初始化节点
      for (let i = 0; i < data.nodeList.length; i++) {
        let node = data.nodeList[i];
        // 设置源点，可以拖出线连接其他节点
        jsPlumbInstance.makeSource(node.id, jsplumbSourceOptions);
        // 设置目标点，其他源点拖出的线可以连接该节点
        jsPlumbInstance.makeTarget(node.id, jsplumbTargetOptions);
        // 注册节点拖动事件
        draggableNode(node.id);
      }
      //取消连接事件
      jsPlumbInstance.unbind("connection");
      // 初始化连线
      for (let i = 0; i < data.lineList.length; i++) {
        let line = data.lineList[i];
        jsPlumbInstance.connect(
          {
            source: line.from,
            target: line.to,
          },
          jsplumbConnectOptions
        );
      }
      //注册连接事件
      jsPlumbInstance.bind("connection", (evt) => {
        data.lineList.push({
          id: generateUniqueID(8),
          from: evt.source.id,
          to: evt.target.id,
        });
      });
    };

    // 拖动节点事件
    const draggableNode = (nodeId) => {
      jsPlumbInstance.draggable(nodeId, {
        //节点移动最小距离
        grid: [5, 5],
        drag: (params) => {
          alignForLine(nodeId, params.pos);
        },
        start: () => {},
        stop: (params) => {
          auxiliaryLine.isShowXLine = false;
          auxiliaryLine.isShowYLine = false;
          changeNodePosition(nodeId, params.pos);
        },
      });
    };

    const changeNodePosition = (nodeId, pos) => {
      data.nodeList.map((v) => {
        if (nodeId === v.id) {
          v.left = pos[0] + "px";
          v.top = pos[1] + "px";
          return v;
        }
      });
    };

    //移动节点时，动态显示对齐线
    const alignForLine = (nodeId, position) => {
      let showXLine = false;
      let showYLine = false;
      data.nodeList.some((el) => {
        if (el.id !== nodeId && el.left === position[0] + "px") {
          auxiliaryLinePos.x = position[0] + 60;
          showYLine = true;
        }
        if (el.id !== nodeId && el.top === position[1] + "px") {
          auxiliaryLinePos.y = position[1] + 20;
          showXLine = true;
        }
      });
      auxiliaryLine.isShowYLine = showYLine;
      auxiliaryLine.isShowXLine = showXLine;
    };

    // 设置面板缩放
    const initPanZoom = () => {
      const mainContainer = jsPlumbInstance.getContainer();
      const mainContainerWrap = mainContainer.parentNode;
      // 缩放时设置jsPlumbInstance的缩放比率
      const pan = panzoom(mainContainer, {
        smoothScroll: false,
        bounds: true,
        zoomDoubleClickSpeed: 1,
        minZoom: 0.5,
        maxZoom: 2,
      });
      jsPlumbInstance.mainContainerWrap = mainContainerWrap;
      jsPlumbInstance.pan = pan;
      pan.on("zoom", (e) => {
        const { x, y, scale } = e.getTransform();
        jsPlumbInstance.setZoom(scale);
        //根据缩放比例，缩放对齐辅助线长度和位置
        auxiliaryLinePos.width = (1 / scale) * 100 + "%";
        auxiliaryLinePos.height = (1 / scale) * 100 + "%";
        auxiliaryLinePos.offsetX = -(x / scale);
        auxiliaryLinePos.offsetY = -(y / scale);
      });
      pan.on("panend", (e) => {
        const { x, y, scale } = e.getTransform();
        auxiliaryLinePos.width = (1 / scale) * 100 + "%";
        auxiliaryLinePos.height = (1 / scale) * 100 + "%";
        auxiliaryLinePos.offsetX = -(x / scale);
        auxiliaryLinePos.offsetY = -(y / scale);
      });
      // 平移时设置鼠标样式
      mainContainerWrap.style.cursor = "grab";
      mainContainerWrap.addEventListener(
        "mousedown",
        function wrapMousedown(style) {
          style.cursor = "grabbing";
          mainContainerWrap.addEventListener(
            "mouseout",
            function wrapMouseout(style) {
              style.cursor = "grab";
            }
          );
        }
      );
      mainContainerWrap.addEventListener(
        "mouseup",
        function wrapMouseup(style) {
          style.cursor = "grab";
        }
      );
    };

    let currentItem = null;
    const drag = (item) => {
      currentItem = item;
    };

    const drop = (event) => {
      const containerRect = jsPlumbInstance
        .getContainer()
        .getBoundingClientRect();
      const scale = jsPlumbInstance.getZoom();
      let left = (event.pageX - containerRect.left - 60) / scale;
      let top = (event.pageY - containerRect.top - 20) / scale;
      let temp = {
        id: generateUniqueID(8),
        nodeName: currentItem.typeName,
        top: Math.round(top / 20) * 20 + "px",
        left: Math.round(left / 20) * 20 + "px",
        nodeType: currentItem,
      };
      addNode(temp);
    };

    // dragover取消默认事件后，才会触发drag事件
    const allowDrop = (event) => {
      event.preventDefault();
    };

    const addNode = (temp) => {
      data.nodeList.push(temp);
      nextTick(() => {
        jsPlumbInstance.makeSource(temp.id, jsplumbSourceOptions);
        jsPlumbInstance.makeTarget(temp.id, jsplumbTargetOptions);
        draggableNode(temp.id);
      });
    };

    // 剪切板
    let clipboard;

    // 复制节点
    const copyNode = (node) => {
      clipboard = node;
    };

    // 粘贴节点
    const pasteNode = () => {
      if (clipboard) {
        // 从剪切板复制一份临时节点对象
        const temp = JSON.parse(JSON.stringify(clipboard));
        temp.id = generateUniqueID(8);
        // 粘贴的节点向右下方向各移动15px,30px
        temp.left =
          parseFloat(temp.left.slice(0, temp.left.length - 2)) + 30 + "px";
        temp.top =
          parseFloat(temp.top.slice(0, temp.top.length - 2)) + 15 + "px";
        addNode(temp);
        clipboard = temp;
      }
    };

    // 删除当前节点
    const deleteNode = (node) => {
      if (node) {
        ElMessageBox.confirm("确认删除当前节点？", "提示", {
          type: "warning",
        })
          .then(() => {
            data.nodeList.map((v, index) => {
              if (v.id === node.id) {
                data.nodeList.splice(index, 1);
                jsPlumbInstance.remove(v.id);
                return v;
              }
            });
            data.selectedNode = undefined;
          })
          .catch(() => {});
      }
    };

    // 键盘事件操作节点
    const keyupNode = (e, node) => {
      if (e.ctrlKey === false && e.key === "Delete") {
        deleteNode(node);
      } else if (e.ctrlKey && e.key === "c") {
        copyNode(node);
      } else if (e.ctrlKey && e.key === "v") {
        pasteNode(node);
      }
    };

    // 界面缩放，以绘制面板原点为基准，每次缩放25%
    const zoomNode = (e) => {
      const scale = jsPlumbInstance.getZoom();
      const max = jsPlumbInstance.pan.getMaxZoom();
      const min = jsPlumbInstance.pan.getMinZoom();
      let temp;
      if (e === "in") {
        if (scale < max) {
          temp = scale + scale * 0.25;
        }
      } else if (e === "out") {
        if (scale > min) {
          temp = scale - scale * 0.25;
        }
      } else if (e === "full") {
        if (!screenfull.isEnabled) {
          ElMessage.warning("您的浏览器不支持全屏");
          return false;
        }
        screenfull.request(document.getElementById("flow-content"));
      } else if (e === "reset") {
        temp = 1;
      }
      if (!temp) {
        return;
      }
      // 限制缩放范围
      if (temp > max) {
        temp = max;
      } else if (temp < min) {
        temp = min;
      }
      jsPlumbInstance.setZoom(temp);
      document.getElementById("flow").style.transform = "scale(" + temp + ")";
    };

    //更改连线状态
    const changeLineState = (nodeId, val) => {
      let lines = jsPlumbInstance.getAllConnections();
      lines.forEach((line) => {
        if (line.targetId === nodeId || line.sourceId === nodeId) {
          if (val) {
            line.canvas.classList.add("active");
          } else {
            line.canvas.classList.remove("active");
          }
        }
      });
    };

    // 展示左侧节点类型的描述
    const moveDes = (e, type) => {
      showDescription.show = true;
      showDescription.data = type.description;
      showDescription.left = e.pageX - 165 + "px";
      showDescription.top = e.pageY - 114 + "px";
    };
    const hideDes = () => {
      showDescription.show = false;
      showDescription.data = "";
    };

    // 右侧栏展示节点详情
    const showNodeDetail = (node) => {
      data.selectedNode = null;
      if (node) {
        setTimeout(() => {
          data.selectedNode = node;
        }, 0);
      }
    };

    // 更新节点属性信息
    const updateNode = (node, params) => {
      data.selectedNode = node;
      data.nodeList.map((v) => {
        if (v.id === node.id) {
          v.nodeName = node.nodeName;
          v.remark = node.remark;
          v.params = params;
          return v;
        }
      });
    };

    // 导入流程
    const importFlow = async () => {
      try {
        const text = await navigator.clipboard.readText();
        const flow = JSON.parse(text);
        if (flow && flow.nodeList && flow.lineList) {
          // 流程数据重新赋值
          const tempId = {};
          flow.nodeList.forEach((n) => {
            tempId[n.id] = generateUniqueID(8);
            n.id = tempId[n.id];
            n.flowId = props.flowId;
            // 清除节点状态信息
            n.status = undefined;
            n.error = undefined;
            n.output = undefined;
          });
          flow.lineList.forEach((n) => {
            n.id = generateUniqueID(8);
            n.from = tempId[n.from];
            n.to = tempId[n.to];
            n.flowId = props.flowId;
          });
          data.nodeList = flow.nodeList;
          data.lineList = flow.lineList;
          // 清除绘板实例，重新初始化
          resetFlowPanel();
          ElMessage.success("导入成功！");
        } else {
          ElMessage.error("导入失败！剪切板内容不正确");
        }
      } catch (e) {
        ElMessage.error("导入失败！剪切板内容不正确");
        console.log(e);
      }
    };

    // 导出流程
    const exportFlow = () => {
      const flow = {
        nodeList: data.nodeList,
        lineList: data.lineList,
      };
      navigator.clipboard.writeText(JSON.stringify(flow));
    };

    // 切换版本
    const switchVersion = async (version) => {
      await initNode(version);
      // 清除绘板实例，重新初始化
      resetFlowPanel();
      ElMessage.success("加载成功！");
    };

    // 展示/关闭流程实时运行日志
    const logVisible = ref(false);
    let logContent = ref("");
    const showLogs = (show) => {
      if (show) {
        logVisible.value = true;
        onOpenLogs(props.flowId, (s) => {
          logContent.value = logContent.value.concat(s);
        });
      } else {
        logVisible.value = false;
      }
    };

    // 建立websocket连接，获取节点状态信息
    const flowStatus = ref("");
    const getNodeStatus = () => {
      onOpen(props.flowId, (s) => {
        const res = JSON.parse(s);
        if (res.flowStatus) {
          flowStatus.value = res.flowStatus;
        }
        if (res.nodeId) {
          data.nodeList.forEach((node) => {
            if (node.id === res.nodeId) {
              node.status = res.status;
              if (res.output) {
                node.output = res.output;
              }
              if (res.error) {
                node.error = res.error;
                ElMessage.error(res.error);
              }
            }
          });
        }
      });
    };

    // 获取版本列表
    const versions = ref(["当前最新版本"]);
    const getVersions = async () => {
      getVersion({ flowId: props.flowId }).then((res) => {
        const v = res.result;
        if (v.length > 0) {
          versions.value = ["当前最新版本", ...v];
        }
      });
    };

    // 重新生成节点id，保证各版本的节点id不冲突
    const generateNodeId = (nodes, lines) => {
      const tempId = {};
      nodes.forEach((n) => {
        tempId[n.id] = generateUniqueID(8);
        n.id = tempId[n.id];
      });
      lines.forEach((n) => {
        n.id = generateUniqueID(8);
        n.from = tempId[n.from];
        n.to = tempId[n.to];
      });
      return [...nodes, ...lines];
    };

    // 保存流程图所有节点数据
    const saveData = async (version) => {
      if (data.nodeList.length === 0) {
        ElMessage.error("请先绘制流程图");
        return false;
      }
      // 封装节点数据参数
      const nodes = [];
      const lines = [];
      // 重新生成节点id，并更新当前节点数据
      const tempId = {};
      data.nodeList.forEach((d) => {
        tempId[d.id] = generateUniqueID(8);
        d.id = tempId[d.id];
        const node = {
          id: d.id,
          nodeName: d.nodeName,
          flowId: props.flowId,
          typeId: d.nodeType.id,
          top: d.top,
          left: d.left,
          remark: d.remark,
          params: d.params,
          payload: d.payload,
        };
        nodes.push(node);
      });
      data.lineList.forEach((l) => {
        l.id = generateUniqueID(8);
        l.from = tempId[l.from];
        l.to = tempId[l.to];
        const line = {
          id: l.id,
          flowId: props.flowId,
          from: l.from,
          to: l.to,
        };
        lines.push(line);
      });
      // 更新当前最新数据
      await setNodeData([...nodes, ...lines]);
      if (version != null) {
        // 保存版本数据
        await saveVersion(version, generateNodeId(nodes, lines));
        ElMessage.success("保存成功");
        // 产生了新的版本，需要重新加载版本列表
        await getVersions();
      }
      return true;
    };

    // 运行本流程
    const executeFlow = async () => {
      data.nodeList.forEach((v) => {
        v.status = undefined;
        v.error = undefined;
        v.output = undefined;
      });
      // 先保存流程
      const save = await saveData();
      if (save) {
        // 运行流程
        const res = await executeNodeData(props.flowId);
        if (res) {
          ElMessage.success("操作成功");
        }
      }
    };

    // 停止流程
    const stopFlow = () => {
      stopNodeData(props.flowId).then((res) => {
        if (res) {
          ElMessage.success("操作成功");
        }
      });
    };

    // 初始化页面数据，渲染流程图
    onMounted(async () => {
      await initNodeType();
      await initNode(null);
      await nextTick(() => {
        init();
      });
      // 建立websocket连接
      getNodeStatus();
      // 获取版本列表
      await getVersions();
    });

    // 组件被销毁之前，关闭socket连接
    onBeforeUnmount(() => {
      onCloseLogs(props.flowId);
      onClose(props.flowId);
      jsPlumbInstance.reset();
    });

    return {
      data,
      flowStatus,
      versions,
      auxiliaryLine,
      auxiliaryLinePos,
      showDescription,
      drag,
      drop,
      allowDrop,
      copyNode,
      pasteNode,
      deleteNode,
      zoomNode,
      keyupNode,
      changeLineState,
      showNodeDetail,
      updateNode,
      moveDes,
      hideDes,
      saveData,
      executeFlow,
      stopFlow,
      logVisible,
      logContent,
      showLogs,
      importFlow,
      exportFlow,
      switchVersion,
    };
  },
};
</script>

<style lang="less" scoped>
.flow-content {
  display: flex;
  width: 100%;
  height: 94%;
  border: 1px solid #ccc;

  .nodes-wrap {
    width: 160px;
    height: 100%;
    overflow-y: auto;
    border-right: 1px solid #ccc;

    .node {
      display: flex;
      width: 120px;
      height: 40px;
      margin: 5px auto;
      line-height: 40px;
      border: 1px solid #ccc;

      &:hover {
        cursor: grab;
      }

      &:active {
        cursor: grabbing;
      }

      .svg {
        width: 40px;
        height: 40px;
      }

      .name {
        flex-grow: 1;
        width: 0;
        padding-right: 6px;
        font-size: 14px;
        text-align: center;
      }

      .description {
        position: absolute;
        z-index: 9999;
        height: 32px;
        padding-right: 10px;
        padding-left: 10px;
        font-size: 12px;
        line-height: 32px;
        text-align: left;
        background-color: #dcdfe6;
        border-radius: 10px;
      }
    }
  }

  .flow-wrap {
    position: relative;
    flex-grow: 1;
    height: 100%;
    overflow: hidden;
    background-image: url("../assets/img/point.png");
    outline: none !important;

    #flow {
      position: relative;
      width: 100%;
      height: 100%;

      .auxiliary-line-x {
        position: absolute;
        z-index: 9999;
        border: 0.5px dashed #2ab1e8;
      }

      .auxiliary-line-y {
        position: absolute;
        z-index: 9999;
        border: 0.5px dashed #2ab1e8;
      }
    }
  }
}
</style>

<style lang="less">
.el-collapse-item__wrap .el-collapse-item__content {
  padding-top: 5px;
  padding-bottom: 5px;
  background: #f0f0f0;
}

.el-collapse-item .el-collapse-item__header {
  padding-left: 40px;
  font-size: 14px;
}

.jtk-connector.active {
  z-index: 9999;

  path {
    animation: ring;
    animation-duration: 3s;
    animation-timing-function: linear;
    animation-iteration-count: infinite;
    stroke: #150042;
    stroke-width: 1.5;
    stroke-dasharray: 5;
  }
}

@keyframes ring {
  from {
    stroke-dashoffset: 50;
  }
  to {
    stroke-dashoffset: 0;
  }
}

:not(:root):fullscreen::backdrop {
  background: white;
}
</style>
