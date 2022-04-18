<template>
  <div style="height: 100%">
    <toolbar @deleteNode="deleteNode(data.selectedNode)" @executeFlow="executeFlow" @saveData="saveData"/>
    <div class="flow_region">
      <div class="nodes-wrap">
        <div v-for="item in data.nodeTypeList" :key="item.type" :style="{background: item.background}" class="node"
             draggable="true" @dragstart="drag(item)" @mousemove="moveDes($event,item)" @mouseout="hideDes(item)">
          <div class="log">
            <img :src="item.svg" alt="" style="padding: 4px">
          </div>
          <div class="name">{{ item.typeName }}</div>
          <div v-show="showDescription.show" :style="{left: showDescription.left,top: showDescription.top}"
               class="description">
            {{ showDescription.data }}
          </div>
        </div>
      </div>
      <div id="flowWrap" ref="flowWrap" class="flow-wrap" @dragover="allowDrop" @drop="drop"
           @keyup.delete="deleteNode(data.selectedNode)">
        <div id="flow">
          <div v-show="auxiliaryLine.isShowXLine"
               :style="{width: auxiliaryLinePos.width, top:auxiliaryLinePos.y + 'px', left: auxiliaryLinePos.offsetX + 'px'}"
               class="auxiliary-line-x"></div>
          <div v-show="auxiliaryLine.isShowYLine"
               :style="{height: auxiliaryLinePos.height, left:auxiliaryLinePos.x + 'px', top: auxiliaryLinePos.offsetY + 'px'}"
               class="auxiliary-line-y"></div>
          <flowNode v-for="item in data.nodeList" :id="item.id" :key="item.id" :node="item"
                    @changeLineState="changeLineState" @showNodeDetail="showNodeDetail"/>
        </div>
      </div>
      <nodeDetail v-if="data.selectedNode" :node="data.selectedNode" @showNodeDetail="showNodeDetail"
                  @updateNode="updateNode"/>
    </div>
  </div>
</template>

<script>
import {nextTick, reactive, onMounted, onBeforeUnmount} from 'vue';
import {jsplumbSetting} from '../components/editor/jsplumbConfig.js';
import {jsplumbConnectOptions, jsplumbSourceOptions, jsplumbTargetOptions} from "../components/editor/jsplumbConfig";
import {generateUniqueID} from "../utils/util.js";
import {getNodeTypes} from "../api/nodeType.js";
import {getNodeData, setNodeData, executeNodeData} from "../api/nodeData.js";
import {ElMessage, ElMessageBox} from "element-plus";
import jsplumb from "jsplumb";
import panzoom from "panzoom";
import toolbar from '../components/editor/Toolbar.vue';
import flowNode from "../components/editor/FlowNode.vue";
import nodeDetail from "../components/editor/NodeDetail.vue";
import {onOpen, onClose} from "../utils/websocket.js";

export default {
  name: "Editor",
  components: {
    flowNode,
    toolbar,
    nodeDetail
  },
  props: {
    flowId: String
  },
  setup(props) {
    // 面板上的节点数据
    const data = reactive({
      nodeTypeList: [],
      nodeList: [],
      lineList: [],
      selectedNode: null
    });
    let jsPlumb;
    // 对齐辅助线
    const auxiliaryLine = reactive({isShowXLine: false, isShowYLine: false});
    const auxiliaryLinePos = reactive({width: '100%', height: '100%', offsetX: 0, offsetY: 0, x: 20, y: 20});
    // 展示节点类型的描述
    let showDescription = reactive({show: false, data: '', left: '0px', top: '0px'});

    // 初始化节点类型
    const initNodeType = async () => {
      const res = await getNodeTypes({limit: 1000});
      if (res.message !== undefined) {
        ElMessage.error(res.message);
      } else {
        data.nodeTypeList = res.result;
      }
    };

    // 初始化节点数据
    const initNode = async () => {
      const res = await getNodeData({flowId: props.flowId});
      if (res.message !== undefined) {
        ElMessage.error(res.message);
      } else {
        res.result.map(d => {
          if (d.nodeName) {
            data.nodeList.push(d);
          } else {
            data.lineList.push(d);
          }
        });
      }
    };

    // 初始化画板
    const init = () => {
      jsPlumb.ready(() => {
        // 导入默认配置
        jsPlumb.importDefaults(jsplumbSetting);
        // 连线创建成功后，维护本地数据
        jsPlumb.bind("connection", evt => {
          addLine(evt);
        });
        //连线双击删除事件
        jsPlumb.bind("dblclick", line => {
          confirmDeleteLine(line);
        });
        //断开连线后，维护本地数据
        jsPlumb.bind("connectionDetached", evt => {
          deleteLine(evt);
        });
        // 加载流程图
        loadEasyFlow();
        // 使整个jsPlumb立即重绘。
        jsPlumb.setSuspendDrawing(false, true);
      });
      // 面板缩放
      initPanZoom();
    };

    const addLine = (line) => {
      let from = line.source.id;
      let to = line.target.id;
      data.lineList.push({
        id: generateUniqueID(8),
        from: from,
        to: to
      });
    };

    const confirmDeleteLine = (line) => {
      ElMessageBox.confirm("确认删除该连线？", "提示", {
        type: "warning",
      }).then(() => {
        jsPlumb.deleteConnection(line);
      }).catch(() => {
      });
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
        jsPlumb.makeSource(node.id, jsplumbSourceOptions);
        // 设置目标点，其他源点拖出的线可以连接该节点
        jsPlumb.makeTarget(node.id, jsplumbTargetOptions);
        // 注册节点拖动事件
        draggableNode(node.id);
      }
      //取消连接事件
      jsPlumb.unbind("connection");
      // 初始化连线
      for (let i = 0; i < data.lineList.length; i++) {
        let line = data.lineList[i];
        jsPlumb.connect(
            {
              source: line.from,
              target: line.to
            },
            jsplumbConnectOptions
        );
      }
      //注册连接事件
      jsPlumb.bind("connection", evt => {
        data.lineList.push({
          id: generateUniqueID(8),
          from: evt.source.id,
          to: evt.target.id
        });
      });
    };

    // 拖动节点事件
    const draggableNode = (nodeId) => {
      jsPlumb.draggable(nodeId, {
        //节点移动最小距离
        grid: [5, 5],
        drag: (params) => {
          alignForLine(nodeId, params.pos);
        },
        start: () => {
        },
        stop: (params) => {
          auxiliaryLine.isShowXLine = false;
          auxiliaryLine.isShowYLine = false;
          changeNodePosition(nodeId, params.pos);
        }
      });
    };

    const changeNodePosition = (nodeId, pos) => {
      data.nodeList.map(v => {
        if (nodeId === v.id) {
          v.left = pos[0] + 'px';
          v.top = pos[1] + 'px';
          return v;
        }
      });
    };

    //移动节点时，动态显示对齐线
    const alignForLine = (nodeId, position) => {
      let showXLine = false;
      let showYLine = false;
      data.nodeList.some(el => {
        if (el.id !== nodeId && el.left === position[0] + 'px') {
          auxiliaryLinePos.x = position[0] + 60;
          showYLine = true;
        }
        if (el.id !== nodeId && el.top === position[1] + 'px') {
          auxiliaryLinePos.y = position[1] + 20;
          showXLine = true;
        }
      })
      auxiliaryLine.isShowYLine = showYLine;
      auxiliaryLine.isShowXLine = showXLine;
    };

    // 设置面板缩放
    const initPanZoom = () => {
      const mainContainer = jsPlumb.getContainer();
      const mainContainerWrap = mainContainer.parentNode;
      const pan = panzoom(mainContainer, {
        smoothScroll: false,
        bounds: true,
        zoomDoubleClickSpeed: 1,
        minZoom: 0.5,
        maxZoom: 2,
        //设置滚动缩放的组合键，默认不需要组合键
        beforeWheel: () => {
        },
        beforeMouseDown: function (e) {
          return e.ctrlKey;
        }
      });
      jsPlumb.mainContainerWrap = mainContainerWrap;
      jsPlumb.pan = pan;
      // 缩放时设置jsPlumb的缩放比率
      pan.on("zoom", e => {
        const {x, y, scale} = e.getTransform();
        jsPlumb.setZoom(scale);
        //根据缩放比例，缩放对齐辅助线长度和位置
        auxiliaryLinePos.width = (1 / scale) * 100 + '%';
        auxiliaryLinePos.height = (1 / scale) * 100 + '%';
        auxiliaryLinePos.offsetX = -(x / scale);
        auxiliaryLinePos.offsetY = -(y / scale);
      });
      pan.on("panend", (e) => {
        const {x, y, scale} = e.getTransform();
        auxiliaryLinePos.width = (1 / scale) * 100 + '%';
        auxiliaryLinePos.height = (1 / scale) * 100 + '%';
        auxiliaryLinePos.offsetX = -(x / scale);
        auxiliaryLinePos.offsetY = -(y / scale);
      });
      // 平移时设置鼠标样式
      mainContainerWrap.style.cursor = "grab";
      mainContainerWrap.addEventListener("mousedown", function wrapMousedown(style) {
        style.cursor = "grabbing";
        mainContainerWrap.addEventListener("mouseout", function wrapMouseout(style) {
          style.cursor = "grab";
        });
      });
      mainContainerWrap.addEventListener("mouseup", function wrapMouseup(style) {
        style.cursor = "grab";
      });
    };

    let currentItem = null;
    const drag = (item) => {
      currentItem = item;
    };

    const drop = (event) => {
      const containerRect = jsPlumb.getContainer().getBoundingClientRect();
      const scale = getScale();
      let left = (event.pageX - containerRect.left - 60) / scale;
      let top = (event.pageY - containerRect.top - 20) / scale;
      let temp = {
        id: generateUniqueID(8),
        nodeName: currentItem.typeName,
        top: (Math.round(top / 20)) * 20 + "px",
        left: (Math.round(left / 20)) * 20 + "px",
        nodeType: currentItem
      };
      addNode(temp);
    };

    // dragover取消默认事件后，才会触发drag事件
    const allowDrop = (event) => {
      event.preventDefault();
    };

    const getScale = () => {
      let scale1;
      if (jsPlumb.pan) {
        const {scale} = jsPlumb.pan.getTransform();
        scale1 = scale;
      } else {
        const matrix = window.getComputedStyle(jsPlumb.getContainer()).transform;
        scale1 = matrix.split(", ")[3] * 1;
      }
      jsPlumb.setZoom(scale1);
      return scale1;
    };

    const addNode = (temp) => {
      data.nodeList.push(temp);
      nextTick(() => {
        jsPlumb.makeSource(temp.id, jsplumbSourceOptions);
        jsPlumb.makeTarget(temp.id, jsplumbTargetOptions);
        draggableNode(temp.id);
      });
    };

    // 删除当前节点
    const deleteNode = (node) => {
      if (node) {
        ElMessageBox.confirm("确认删除当前节点？", "提示", {
          type: "warning",
        }).then(() => {
          data.nodeList.map((v, index) => {
            if (v.id === node.id) {
              data.nodeList.splice(index, 1);
              jsPlumb.remove(v.id);
              return v;
            }
          });
        }).catch(() => {
        });
      }
    };

    //更改连线状态
    const changeLineState = (nodeId, val) => {
      let lines = jsPlumb.getAllConnections();
      lines.forEach(line => {
        if (line.targetId === nodeId || line.sourceId === nodeId) {
          if (val) {
            line.canvas.classList.add('active');
          } else {
            line.canvas.classList.remove('active');
          }
        }
      });
    };

    // 展示左侧节点类型的描述
    const moveDes = (e, type) => {
      showDescription.show = true;
      showDescription.data = type.description;
      showDescription.left = (e.pageX - 246) + 'px';
      showDescription.top = (e.pageY - 114) + 'px';
    };
    const hideDes = () => {
      showDescription.show = false;
      showDescription.data = '';
    };

    // 右侧栏展示节点详情
    const showNodeDetail = (node, show) => {
      if (show) {
        data.selectedNode = node;
      } else {
        data.selectedNode = null;
      }
    };

    // 更新节点属性信息
    const updateNode = (node, params) => {
      data.selectedNode = node;
      data.nodeList.map(v => {
        if (v.id === node.id) {
          v.nodeName = node.nodeName;
          v.remark = node.remark;
          v.params = params;
          return v;
        }
      });
    };

    // 保存流程图所有节点数据
    const saveData = async () => {
      if (data.nodeList.length === 0) {
        ElMessage.error("请先绘制流程图");
        return;
      }
      // 封装节点数据参数
      let body = [];
      data.nodeList.forEach(d => {
        const node = {
          id: d.id,
          nodeName: d.nodeName,
          flowId: props.flowId,
          typeId: d.nodeType.id,
          top: d.top,
          left: d.left,
          remark: d.remark,
          params: d.params,
          payload: d.payload
        };
        body.push(node);
      });
      data.lineList.forEach(l => {
        const line = {
          id: l.id,
          flowId: props.flowId,
          from: l.from,
          to: l.to
        };
        body.push(line);
      });
      // 保存流程数据
      const res = await setNodeData(body);
      if (res.message !== undefined) {
        ElMessage.error(res.message);
      } else {
        ElMessage.success("保存成功");
      }
    };

    // 运行本流程
    const executeFlow = async () => {
      await saveData();
      // 流程中如果有输出节点，则需建立websocket连接
      const outs = data.nodeList.filter(d => "output" === d.nodeType.type);
      outs.forEach(d => {
        onOpen(d.id, (s) => d.output = JSON.parse(s));
      });
      // 运行流
      const res = await executeNodeData(props.flowId);
      if (res.message !== undefined) {
        ElMessage.error(res.message);
      } else {
        ElMessage.success("运行成功");
      }
    };

    // 初始化页面数据，渲染流程图
    onMounted(async () => {
      jsPlumb = jsplumb.jsPlumb.getInstance();
      await initNodeType();
      await initNode();
      await nextTick(() => {
        init();
      });
    });

    // 组件被销毁之前，关闭socket连接
    onBeforeUnmount(() => onClose());

    return {
      data,
      auxiliaryLine,
      auxiliaryLinePos,
      showDescription,
      drag,
      drop,
      allowDrop,
      deleteNode,
      changeLineState,
      showNodeDetail,
      updateNode,
      moveDes,
      hideDes,
      saveData,
      executeFlow
    };
  }
};
</script>

<style lang="less" scoped>
.flow_region {
  display: flex;
  width: 100%;
  height: 94%;
  border: 1px solid #ccc;

  .nodes-wrap {
    width: 150px;
    height: 100%;
    border-right: 1px solid #ccc;

    .node {
      display: flex;
      height: 40px;
      width: 80%;
      margin: 5px auto;
      border: 1px solid #ccc;
      line-height: 40px;

      &:hover {
        cursor: grab;
      }

      &:active {
        cursor: grabbing;
      }

      .log {
        width: 40px;
        height: 40px;
      }

      .name {
        font-size: 14px;
        width: 0;
        flex-grow: 1;
        text-align: center;
        padding-right: 6px;
      }

      .description {
        font-size: 12px;
        position: absolute;
        line-height: 32px;
        height: 32px;
        background-color: #dcdfe6;
        text-align: left;
        padding-left: 10px;
        padding-right: 10px;
        border-radius: 10px;
        z-index: 9999;
      }
    }
  }

  .flow-wrap {
    height: 100%;
    position: relative;
    overflow: hidden;
    outline: none !important;
    flex-grow: 1;
    background-image: url("../assets/img/point.png");

    #flow {
      position: relative;
      width: 100%;
      height: 100%;

      .auxiliary-line-x {
        position: absolute;
        border: .5px dashed #2ab1e8;
        z-index: 9999;
      }

      .auxiliary-line-y {
        position: absolute;
        border: .5px dashed #2ab1e8;
        z-index: 9999;
      }
    }
  }
}
</style>

<style lang="less">
.jtk-connector.active {
  z-index: 9999;

  path {
    stroke: #150042;
    stroke-width: 1.5;
    animation: ring;
    animation-duration: 3s;
    animation-timing-function: linear;
    animation-iteration-count: infinite;
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
</style>
