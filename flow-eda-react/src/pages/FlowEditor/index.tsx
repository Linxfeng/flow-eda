import React, { useState, useEffect } from 'react';
import { useParams } from 'umi';
import { jsPlumb } from 'jsplumb';
import panzoom from 'panzoom';
import { getFlowData, getNodeTypes } from '@/services/api';
import {
  jsplumbConnectOptions,
  jsplumbSetting,
  jsplumbSourceOptions,
  jsplumbTargetOptions,
} from '@/pages/FlowEditor/jsplumb/jsplumbConfig';
import { generateUniqueID } from '@/utils/util';
import { PageContainer } from '@ant-design/pro-layout';
import { Card, message, Modal } from 'antd';
import './index.less';
import ToolBar from '@/pages/FlowEditor/ToolBar/index';
import FlowNode from '@/pages/FlowEditor/FlowNode/index';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { useFormatMessage } from '@/hooks';

const FlowEditor: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { formatMsg } = useFormatMessage();
  const [jsPlumbInstance] = useState(jsPlumb.getInstance());
  const [data] = useState<{ nodeList: API.Node[]; lineList: API.Node[] }>({
    nodeList: [],
    lineList: [],
  });
  const [auxiliaryLine, setAuxiliaryLine] = useState<{ showXLine: boolean; showYLine: boolean }>({
    showXLine: false,
    showYLine: false,
  });
  const [auxiliaryLinePos, setAuxiliaryLinePos] = useState<{ x: number; y: number }>({
    x: 20,
    y: 20,
  });
  const [auxiliaryLineStyle, setAuxiliaryLineStyle] = useState<{
    width: string;
    height: string;
    offsetX: number;
    offsetY: number;
  }>({ width: '100%', height: '100%', offsetX: 0, offsetY: 0 });
  const [showDesc, setShowDesc] = useState<{
    show: boolean;
    data: string | undefined;
    left: string;
    top: string;
  }>({ show: false, data: '', left: '0px', top: '0px' });
  const [nodeTypes, setNodeTypes] = useState<API.NodeType[]>([]);
  const [flowStatus, setFlowStatus] = useState<string>('');
  const [selectedNode, setSelectedNode] = useState<API.Node>();
  const [clipboard, setClipboard] = useState<API.Node>();
  const [currentItem, setCurrentItem] = useState<API.NodeType>();

  /** 初始化节点数据 */
  const initFlowData = async () => {
    const res = await getFlowData(id);
    res?.result?.map((d: API.Node) => {
      if (d.nodeName) {
        data.nodeList.push(d);
      } else {
        data.lineList.push(d);
      }
      setFlowStatus('FINISHED');
    });
  };

  /** 初始化节点类型 */
  const initNodeType = async () => {
    const res = await getNodeTypes();
    if (res) {
      setNodeTypes(res);
    }
  };

  /** 移动节点时，动态显示对齐线 */
  const alignForLine = (nodeId: string, position: number[]) => {
    let showXLine = false;
    let showYLine = false;
    let xPos = auxiliaryLinePos.x;
    let yPos = auxiliaryLinePos.y;
    data.nodeList.some((el) => {
      if (el.id !== nodeId && el.left === position[0] + 'px') {
        xPos = position[0] + 60;
        showYLine = true;
      }
      if (el.id !== nodeId && el.top === position[1] + 'px') {
        yPos = position[1] + 20;
        showXLine = true;
      }
    });
    setAuxiliaryLinePos({ x: xPos, y: yPos });
    setAuxiliaryLine({ showXLine: showXLine, showYLine: showYLine });
  };

  /** 注册节点拖动节点事件 */
  const draggableNode = (nodeId: string) => {
    console.log('draggableNode:' + nodeId);
    jsPlumbInstance.draggable(nodeId, {
      drag: (params) => {
        alignForLine(nodeId, params.pos);
      },
      start: () => {},
      stop: (params) => {
        setAuxiliaryLine({ showXLine: false, showYLine: false });
        data.nodeList.forEach((v) => {
          if (nodeId === v.id) {
            v.left = params.pos[0] + 'px';
            v.top = params.pos[1] + 'px';
          }
        });
      },
    });
  };

  /** 加载流程图 */
  const loadEasyFlow = () => {
    console.log('loadEasyFlow');
    for (let i = 0; i < data.nodeList.length; i++) {
      const node = data.nodeList[i];
      console.log(node);
      // 设置源点，可以拖出线连接其他节点
      jsPlumbInstance.makeSource(node.id, jsplumbSourceOptions);
      // 设置目标点，其他源点拖出的线可以连接该节点
      jsPlumbInstance.makeTarget(node.id, jsplumbTargetOptions);
      // 注册节点拖动事件
      draggableNode(node.id);
    }
    //取消连接事件
    jsPlumbInstance.unbind('connection');
    for (let i = 0; i < data.lineList.length; i++) {
      const line = data.lineList[i];
      jsPlumbInstance.connect(
        {
          source: line.from,
          target: line.to,
        },
        jsplumbConnectOptions,
      );
    }
    //注册连接事件
    jsPlumbInstance.bind('connection', (evt) => {
      console.log('connection');
      data.lineList.push({
        id: generateUniqueID(8),
        flowId: id,
        from: evt.source.id,
        to: evt.target.id,
      });
    });
  };

  /** 设置面板缩放 */
  const initPanZoom = () => {
    console.log('initPanZoom');
    const mainContainer = jsPlumbInstance.getContainer();
    const mainContainerWrap = mainContainer.parentNode;
    // @ts-ignore
    const pan = panzoom(mainContainer, {
      smoothScroll: false,
      bounds: true,
      zoomDoubleClickSpeed: 1,
      minZoom: 0.5,
      maxZoom: 2,
    });
    // @ts-ignore
    jsPlumbInstance.mainContainerWrap = mainContainerWrap;
    // @ts-ignore
    jsPlumbInstance.pan = pan;
    pan.on('zoom', (e: any) => {
      const { x, y, scale } = e.getTransform();
      jsPlumbInstance.setZoom(scale);
      //根据缩放比例，缩放对齐辅助线长度和位置
      setAuxiliaryLineStyle({
        width: (1 / scale) * 100 + '%',
        height: (1 / scale) * 100 + '%',
        offsetX: -(x / scale),
        offsetY: -(y / scale),
      });
    });
    pan.on('panend', (e: any) => {
      const { x, y, scale } = e.getTransform();
      setAuxiliaryLineStyle({
        width: (1 / scale) * 100 + '%',
        height: (1 / scale) * 100 + '%',
        offsetX: -(x / scale),
        offsetY: -(y / scale),
      });
    });
    // 平移时设置鼠标样式
    // @ts-ignore
    mainContainerWrap.style.cursor = 'grab';
    mainContainerWrap?.addEventListener('mousedown', function wrapMousedown(style) {
      // @ts-ignore
      style.cursor = 'grabbing';
      mainContainerWrap?.addEventListener('mouseout', function wrapMouseout(e) {
        // @ts-ignore
        e.cursor = 'grab';
      });
    });
    mainContainerWrap?.addEventListener('mouseup', function wrapMouseup(style) {
      // @ts-ignore
      style.cursor = 'grab';
    });
  };

  /** 初始化编辑器面板 */
  const initPanel = () => {
    console.log('ready');
    jsPlumbInstance.ready(() => {
      // @ts-ignore
      jsPlumbInstance.setContainer(document.getElementById('flow'));
      // 导入默认配置
      jsPlumbInstance.importDefaults(jsplumbSetting);
      // 连线创建成功后，维护本地数据
      jsPlumbInstance.bind('connection', (evt) => {
        const line: API.Node = {
          id: generateUniqueID(8),
          flowId: id,
          from: evt.source.id,
          to: evt.target.id,
        };
        if (data.lineList) {
          data.lineList.push(line);
        }
      });
      //连线双击删除事件
      jsPlumbInstance.bind('dblclick', (line) => {
        console.log(line);
        // confirmDeleteLine(line);
      });
      //断开连线后，维护本地数据
      jsPlumbInstance.bind('connectionDetached', (evt) => {
        if (data.lineList) {
          data.lineList.forEach((item, index) => {
            if (item.from === evt.sourceId && item.to === evt.targetId) {
              data.lineList.splice(index, 1);
            }
          });
        }
      });
      // 加载流程图
      loadEasyFlow();
      // 面板重绘
      jsPlumbInstance.setSuspendDrawing(false, true);
    });
    // 面板缩放
    initPanZoom();
  };

  /** 界面缩放，以绘制面板原点为基准，每次缩放25% */
  const zoomNode = (e: string) => {
    console.log(e);
    // const scale = jsPlumbInstance.getZoom();
    // const max = jsPlumbInstance.pan.getMaxZoom();
    // const min = jsPlumbInstance.pan.getMinZoom();
    // let temp;
    // if (e === 'in') {
    //   if (scale < max) {
    //     temp = scale + (scale * 0.25);
    //   }
    // } else if (e === 'out') {
    //   if (scale > min) {
    //     temp = scale - (scale * 0.25);
    //   }
    // } else if (e === 'full') {
    //   if (!screenfull.isEnabled) {
    //     ElMessage.warning("您的浏览器不支持全屏");
    //     return false;
    //   }
    //   screenfull.request(document.getElementById("flow-content"));
    // } else if (e === 'reset') {
    //   temp = 1;
    // }
    // if (!temp) {
    //   return;
    // }
    // // 限制缩放范围
    // if (temp > max) {
    //   temp = max;
    // } else if (temp < min) {
    //   temp = min;
    // }
    // jsPlumbInstance.setZoom(temp);
    // document.getElementById("flow")?.style?.transform = "scale(" + temp + ")";
  };

  /** 添加节点 */
  const addNode = (temp: API.Node) => {
    console.log('addNode: ');
    console.log(temp);
    data.nodeList.push(temp);
    jsPlumbInstance.makeSource(temp.id, jsplumbSourceOptions);
    jsPlumbInstance.makeTarget(temp.id, jsplumbTargetOptions);
    draggableNode(temp.id);
  };

  /** 复制节点 */
  const copyNode = () => {
    setClipboard(selectedNode);
  };
  /** 粘贴节点 */
  const pasteNode = () => {
    if (clipboard) {
      // 从剪切板复制一份临时节点对象
      const temp = JSON.parse(JSON.stringify(clipboard));
      temp.id = generateUniqueID(8);
      // 粘贴的节点向右下方向各移动15px,30px
      temp.left = parseFloat(temp.left.slice(0, temp.left.length - 2)) + 30 + 'px';
      temp.top = parseFloat(temp.top.slice(0, temp.top.length - 2)) + 15 + 'px';
      addNode(temp);
      setSelectedNode(temp);
    }
  };

  /** 删除当前节点 */
  const deleteNode = () => {
    if (selectedNode) {
      Modal.confirm({
        title: '确认删除当前节点？',
        icon: <ExclamationCircleOutlined />,
        okType: 'danger',
        okText: formatMsg('component.modalForm.confirm'),
        cancelText: formatMsg('component.modalForm.cancel'),
        onOk() {
          data.nodeList.map((v: API.Node, index: number) => {
            if (v.id === selectedNode.id) {
              data.nodeList.splice(index, 1);
              jsPlumbInstance.remove(v.id);
            }
          });
          setSelectedNode(undefined);
        },
      });
    }
  };

  const drag = (item: API.NodeType) => {
    setCurrentItem(item);
  };

  const drop = (event: any) => {
    const containerRect = jsPlumbInstance.getContainer().getBoundingClientRect();
    const scale = jsPlumbInstance.getZoom();
    const left = (event.pageX - containerRect.left - 60) / scale;
    const top = (event.pageY - containerRect.top - 20) / scale;
    const temp = {
      id: generateUniqueID(8),
      flowId: id,
      nodeName: currentItem?.typeName,
      top: Math.round(top / 20) * 20 + 'px',
      left: Math.round(left / 20) * 20 + 'px',
      nodeType: currentItem,
    };
    addNode(temp);
  };

  /** 展示左侧节点类型的描述 */
  const moveDes = (e: any, type: API.NodeType) => {
    setShowDesc({
      show: true,
      data: type.description,
      left: e.pageX - 246 + 'px',
      top: e.pageY - 114 + 'px',
    });
  };
  const hideDes = () => {
    setShowDesc({
      show: false,
      data: '',
      left: '0px',
      top: '0px',
    });
  };

  /** 右侧栏展示节点详情 */
  const showNodeDetail = (node: API.Node | undefined) => {
    setSelectedNode(undefined);
    if (node) {
      setTimeout(() => {
        setSelectedNode(node);
      }, 0);
    }
  };

  /** 更改连线状态 */
  const changeLineState = (nodeId: string, show: boolean) => {
    const lines = jsPlumbInstance.getAllConnections();
    lines.forEach((line) => {
      if (line.targetId === nodeId || line.sourceId === nodeId) {
        // @ts-ignore
        const canvas = line.canvas;
        if (canvas) {
          if (show) {
            canvas.classList?.add('active');
          } else {
            canvas.classList?.remove('active');
          }
        }
      }
    });
  };

  /** 键盘事件操作节点 */
  const keyupNode = (e: any) => {
    if (!e.ctrlKey && e.key === 'Delete') {
      deleteNode();
    } else if (e.ctrlKey && e.key === 'c') {
      copyNode();
    } else if (e.ctrlKey && e.key === 'v') {
      pasteNode();
    }
  };

  /** 展示运行日志 */
  const showLogs = (show: boolean) => {
    console.log(show);
    // if (show) {
    //   logVisible.value = true;
    //   onOpenLogs(props.flowId, (s) => {
    //     logContent.value = logContent.value.concat(s);
    //   });
    // } else {
    //   logVisible.value = false;
    // }
  };

  /** 保存流程图所有节点数据 */
  const saveData = async () => {
    if (data.nodeList.length === 0) {
      message.error('请先绘制流程图');
      return;
    }
    // 封装节点数据参数
    // let body = [];
    // data.nodeList.forEach(d => {
    //   const node = {
    //     id: d.id,
    //     nodeName: d.nodeName,
    //     flowId: id,
    //     typeId: d.nodeType.id,
    //     top: d.top,
    //     left: d.left,
    //     remark: d.remark,
    //     params: d.params,
    //     payload: d.payload
    //   };
    //   body.push(node);
    // });
    // data.lineList.forEach(l => {
    //   const line = {
    //     id: l.id,
    //     flowId: id,
    //     from: l.from,
    //     to: l.to
    //   };
    //   body.push(line);
    // });
    // // 保存流程数据
    // await setNodeData(body);
  };

  /** 运行本流程 */
  const executeFlow = async () => {
    // data.nodeList.forEach(v => {
    //   v.status = undefined;
    //   v.error = undefined;
    //   v.output = undefined;
    // });
    // await saveData();
    // // 建立websocket连接
    // await getNodeStatus();
    // // 运行
    // const res = await executeNodeData(id);
    // if (res) {
    //   message.success("操作成功");
    // }
  };

  /** 停止流程 */
  const stopFlow = async () => {
    // stopNodeData(id).then(res => {
    //   if (res) {
    //     message.success("操作成功");
    //   }
    // });
  };

  useEffect(() => {
    const res1 = initNodeType();
    const res2 = initFlowData();
    Promise.all([res1, res2]).then(() => {
      initPanel();
    });
  }, []);

  return (
    <PageContainer>
      <Card>
        <ToolBar
          status={flowStatus}
          copyNode={copyNode}
          pasteNode={pasteNode}
          saveData={saveData}
          deleteNode={deleteNode}
          executeFlow={executeFlow}
          stopFlow={stopFlow}
          zoomNode={zoomNode}
          showLogs={showLogs}
        />
        <div id="flow-content" className="flow-content">
          <div className="nodes-wrap">
            {nodeTypes.map((t) => {
              return (
                <div
                  key={t.type}
                  style={{ background: t.background }}
                  className="node"
                  draggable="true"
                  onDragStart={() => drag(t)}
                  onMouseMove={(e) => moveDes(e, t)}
                  onMouseOut={hideDes}
                >
                  <div className="svg">
                    <img src={t.svg} alt="" style={{ padding: '4px' }} />
                  </div>
                  <div className="name">{t.typeName}</div>
                  {showDesc.show && (
                    <div style={{ left: showDesc.left, top: showDesc.top }} className="description">
                      {showDesc.data}
                    </div>
                  )}
                </div>
              );
            })}
          </div>

          <div
            id="flowWrap"
            className="flow-wrap"
            onDragOver={(e) => e.preventDefault()}
            onDrop={(e) => drop(e)}
            onKeyUp={(e) => keyupNode(e)}
          >
            <div id="flow" onFocus={() => showNodeDetail(undefined)}>
              {auxiliaryLine.showXLine && (
                <div
                  style={{
                    width: auxiliaryLineStyle.width,
                    top: auxiliaryLinePos.y + 'px',
                    left: auxiliaryLineStyle.offsetX + 'px',
                  }}
                  className="auxiliary-line-x"
                />
              )}

              {auxiliaryLine.showYLine && (
                <div
                  style={{
                    height: auxiliaryLineStyle.height,
                    left: auxiliaryLinePos.x + 'px',
                    top: auxiliaryLineStyle.offsetY + 'px',
                  }}
                  className="auxiliary-line-y"
                />
              )}
              {data.nodeList.map((n) => {
                return (
                  <FlowNode
                    key={n.id}
                    node={n}
                    changeLineState={changeLineState}
                    showNodeDetail={showNodeDetail}
                  />
                );
              })}
            </div>
          </div>
        </div>
      </Card>
    </PageContainer>
  );
};

export default FlowEditor;
