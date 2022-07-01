import { useFormatMessage } from '@/hooks';
import FlowLog from '@/pages/FlowEditor/FlowLog';
import FlowNode from '@/pages/FlowEditor/FlowNode/index';
import { changeLineState, setPanZoom, zoomPan } from '@/pages/FlowEditor/js/editor';
import { connectOptions, defaultSetting, makeOptions } from '@/pages/FlowEditor/js/jsplumbConfig';
import FlowDetail from '@/pages/FlowEditor/NodeDetail';
import ToolBar from '@/pages/FlowEditor/ToolBar/index';
import { getFlowData, getNodeTypes, runFlow, setFlowData, stopFlow } from '@/services/api';
import { onCloseLogs, onCloseNode, onOpenLogs, onOpenNode } from '@/services/ws';
import { generateUniqueID } from '@/utils/util';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-layout';
import { Card, Collapse, message, Modal } from 'antd';
import { jsPlumb } from 'jsplumb';
import React, { useEffect, useState } from 'react';
import { useParams } from 'umi';
import './index.less';
const { Panel } = Collapse;

const FlowEditor: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const { formatMsg } = useFormatMessage();
  const [jsPlumbInstance] = useState(jsPlumb.getInstance());
  const [nodeList, setNodeList] = useState<API.Node[]>([]);
  const [lineList, setLineList] = useState<API.Node[]>([]);
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
  }>({ show: false, data: undefined, left: '0px', top: '0px' });
  const [nodeTypes, setNodeTypes] = useState<object>({});
  const [flowStatus, setFlowStatus] = useState<string>('');
  const [selectedNode, setSelectedNode] = useState<API.Node>();
  const [clipboard, setClipboard] = useState<API.Node>();
  const [currentItem, setCurrentItem] = useState<API.NodeType>();
  const [logVisible, setLogVisible] = useState<boolean>(false);
  const [logContent, setLogContent] = useState<string[]>([]);
  const [wsMessage, setWsMessage] = useState<string>();

  /** 移动节点时，动态显示对齐辅助线 */
  const alignForLine = (nodeId: string, position: number[]) => {
    let showXLine = false;
    let showYLine = false;
    let xPos = auxiliaryLinePos.x;
    let yPos = auxiliaryLinePos.y;
    nodeList.forEach((n) => {
      if (n.id !== nodeId) {
        if (n.left === position[0] + 'px') {
          xPos = position[0] + 60;
          showYLine = true;
        }
        if (n.top === position[1] + 'px') {
          yPos = position[1] + 20;
          showXLine = true;
        }
      }
    });
    setAuxiliaryLinePos({ x: xPos, y: yPos });
    setAuxiliaryLine({ showXLine: showXLine, showYLine: showYLine });
  };

  /** 添加连线 */
  const addLine = (evt: any) => {
    const from = evt.source.id;
    const to = evt.target.id;
    setLineList((lines) => {
      // 防止重复连线
      const has = lines.some((l) => {
        return l.from === from && l.to === to;
      });
      if (has) {
        return lines;
      } else {
        const line = {
          id: generateUniqueID(8),
          flowId: id,
          from: evt.source.id,
          to: evt.target.id,
        };
        return [...lines, line];
      }
    });
  };

  /** 动态注册节点到面板上 */
  const reloadNode = (node: API.Node) => {
    const nodeId = node.id;
    jsPlumbInstance.makeSource(nodeId, makeOptions);
    jsPlumbInstance.makeTarget(nodeId, makeOptions);
    // 注册节点拖动节点事件
    jsPlumbInstance.draggable(nodeId, {
      // @ts-ignore // 设置拖动最小栅格为5px
      grid: [5, 5],
      drag: (params) => {
        alignForLine(nodeId, params.pos);
      },
      stop: (params) => {
        setAuxiliaryLine({ showXLine: false, showYLine: false });
        nodeList.forEach((v) => {
          if (nodeId === v.id) {
            v.left = params.pos[0] + 'px';
            v.top = params.pos[1] + 'px';
            return;
          }
        });
      },
    });
  };

  /** 给面板上的节点连线 */
  const connectLines = (lines: API.Node[]) => {
    lines.forEach((line) => {
      jsPlumbInstance.connect(
        {
          source: line.from,
          target: line.to,
        },
        connectOptions,
      );
    });
  };

  /** 初始化节点数据 */
  const loadFlowData = () => {
    getFlowData(id).then((res) => {
      if (res?.result) {
        const nodes: API.Node[] = [];
        const lines: API.Node[] = [];
        res.result.forEach((d: API.Node) => {
          if (d.nodeName) {
            nodes.push(d);
          } else {
            lines.push(d);
          }
        });
        // 加载流程数据节点
        setNodeList(nodes);
        nodes.forEach((node) => {
          jsPlumbInstance.makeSource(node.id, makeOptions);
          jsPlumbInstance.makeTarget(node.id, makeOptions);
        });
        // 连接节点之间的连线
        setLineList(lines);
      }
    });
  };

  /** 删除节点间连线 */
  const confirmDeleteLine = (line: any) => {
    Modal.confirm({
      title: formatMsg('pages.flowList.editor.delLine'),
      icon: <ExclamationCircleOutlined />,
      okType: 'primary',
      okText: formatMsg('component.modalForm.confirm'),
      cancelText: formatMsg('component.modalForm.cancel'),
      onOk() {
        jsPlumbInstance.deleteConnection(line);
      },
    });
  };

  /** 初始化编辑器 */
  const init = () => {
    // 初始化节点类型
    getNodeTypes().then((res) => setNodeTypes(res));
    // 初始化编辑器面板
    jsPlumbInstance.ready(() => {
      // 导入默认配置
      jsPlumbInstance.importDefaults(defaultSetting);
      // 连线创建成功后，维护本地数据
      jsPlumbInstance.bind('connection', (evt) => {
        addLine(evt);
      });
      //连线双击删除事件
      jsPlumbInstance.bind('dblclick', (line) => {
        confirmDeleteLine(line);
      });
      //断开连线后，维护本地数据
      jsPlumbInstance.bind('connectionDetached', (evt) => {
        lineList.forEach((item, index) => {
          if (item.from === evt.sourceId && item.to === evt.targetId) {
            lineList.splice(index, 1);
          }
        });
      });
      // 加载流程数据
      loadFlowData();
      // 面板重绘
      jsPlumbInstance.setSuspendDrawing(false, true);
    });
    // 设置面板缩放
    setPanZoom(jsPlumbInstance, (style: any) => setAuxiliaryLineStyle(style));
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
      setSelectedNode(temp);
      setNodeList([...nodeList, temp]);
    }
  };

  /** 删除当前节点 */
  const deleteNode = () => {
    if (selectedNode) {
      Modal.confirm({
        title: formatMsg('pages.flowList.editor.delNode'),
        icon: <ExclamationCircleOutlined />,
        okType: 'primary',
        okText: formatMsg('component.modalForm.confirm'),
        cancelText: formatMsg('component.modalForm.cancel'),
        onOk() {
          nodeList.map((v: API.Node, index: number) => {
            if (v.id === selectedNode.id) {
              nodeList.splice(index, 1);
              jsPlumbInstance.remove(v.id);
            }
          });
          setSelectedNode(undefined);
        },
      });
    }
  };

  /** 拖拽左侧功能节点 */
  const drag = (item: API.NodeType) => {
    setCurrentItem(item);
  };
  /** 释放左侧功能节点 */
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
    // 添加节点
    setNodeList([...nodeList, temp]);
  };

  /** 展示左侧功能节点类型的描述 */
  const moveDes = (e: any, type: API.NodeType) => {
    setShowDesc({
      show: true,
      data: type.description,
      left: e.pageX - 168 + 'px',
      top: e.pageY - 168 + 'px',
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

  /** 更新节点属性信息 */
  const updateNode = async (node: API.Node, params: object) => {
    setSelectedNode(node);
    nodeList.forEach((n) => {
      if (n.id === node.id) {
        n.nodeName = node.nodeName;
        n.remark = node.remark;
        n.payload = node.payload;
        n.params = params;
        return;
      }
    });
    message.success(formatMsg('component.message.success'));
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

  /** 导入流程 */
  const importFlow = async () => {
    try {
      const text = await navigator.clipboard.readText();
      const flow = JSON.parse(text);
      if (flow && flow.nodeList && flow.lineList) {
        // 流程数据重新赋值
        const tempId = {};
        flow.nodeList.forEach((n: API.Node) => {
          tempId[n.id] = generateUniqueID(8);
          n.id = tempId[n.id];
          n.flowId = id;
          // 清除节点状态信息
          n.status = undefined;
          n.error = undefined;
          n.output = undefined;
        });
        flow.lineList.forEach((n: API.Node) => {
          n.id = generateUniqueID(8);
          if (n.from && tempId[n.from]) {
            n.from = tempId[n.from];
          }
          if (n.to && tempId[n.to]) {
            n.to = tempId[n.to];
          }
          n.flowId = id;
        });
        // 清除绘板实例，重新绘制流程图
        setTimeout(() => {
          jsPlumbInstance.cleanupListeners();
          jsPlumbInstance.deleteEveryConnection();
          jsPlumbInstance.deleteEveryEndpoint();
          jsPlumbInstance.reset();
          // 更新新的流程数据
          setNodeList(flow.nodeList);
          setLineList(flow.lineList);
        }, 0);
        message.success(formatMsg('pages.flowList.editor.importFlow.success'));
      } else {
        message.error(formatMsg('pages.flowList.editor.importFlow.failed'));
      }
    } catch (e) {
      message.error(formatMsg('pages.flowList.editor.importFlow.failed'));
    }
  };

  /** 导出流程 */
  const exportFlow = async () => {
    const flow = {
      nodeList: nodeList,
      lineList: lineList,
    };
    await navigator.clipboard.writeText(JSON.stringify(flow));
    message.success(formatMsg('pages.flowList.editor.exportFlow.success'));
  };

  /** 展示运行日志 */
  const showLogs = (show: boolean) => {
    if (show) {
      setLogVisible(true);
      onOpenLogs(id, (s) => {
        // 拼接日志内容
        setLogContent((arr) => {
          return [...arr, s];
        });
      });
    } else {
      setLogVisible(false);
    }
  };

  /** 保存流程图所有节点数据 */
  const saveData = async (): Promise<boolean> => {
    if (nodeList.length === 0) {
      message.error(formatMsg('pages.flowList.editor.checkFlow'));
      return false;
    }
    // 流程节点数据参数
    const body: API.Node[] = [];
    nodeList.forEach((d) => {
      const node: API.Node = {
        id: d.id,
        nodeName: d.nodeName,
        flowId: id,
        typeId: d.nodeType?.id,
        top: d.top,
        left: d.left,
        remark: d.remark,
        params: d.params,
        payload: d.payload,
      };
      body.push(node);
    });
    lineList.forEach((l) => {
      const line: API.Node = {
        id: l.id,
        flowId: id,
        from: l.from,
        to: l.to,
      };
      body.push(line);
    });
    // 保存/更新流程节点数据
    await setFlowData(body);
    return true;
  };

  /** 运行本流程 */
  const executeFlow = async () => {
    nodeList.forEach((n) => {
      n.status = undefined;
      n.error = undefined;
      n.output = undefined;
    });
    // 保存当前流程数据
    const save = await saveData();
    if (save) {
      // 运行本流程
      runFlow(id).then((res) => {
        if (res) {
          message.success(formatMsg('component.message.success'));
        }
      });
    }
  };

  /** 停止流程 */
  const stopFlowData = async () => {
    stopFlow(id).then((res) => {
      if (res) {
        message.success(formatMsg('component.message.success'));
      }
    });
  };

  useEffect(() => {
    // 初始化
    init();
    // 建立ws连接，实时获取流程状态和节点状态信息
    onOpenNode(id, (s) => setWsMessage(s));

    // 销毁组件时关闭ws连接
    return () => {
      onCloseNode(id);
      onCloseLogs(id);
    };
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  /** 当面板上的节点变化时，重新加载绘制节点 */
  useEffect(() => {
    nodeList.forEach((node) => reloadNode(node));
  }, [nodeList]); // eslint-disable-line react-hooks/exhaustive-deps

  /** 当面板上的连线发生变化时，重新绘制连线 */
  useEffect(() => {
    connectLines(lineList);
  }, [lineList]); // eslint-disable-line react-hooks/exhaustive-deps

  /** 监听ws消息，更新节点状态和输出信息 */
  useEffect(() => {
    if (wsMessage) {
      const res = JSON.parse(wsMessage);
      if (res.flowStatus) {
        setFlowStatus(res.flowStatus);
      }
      if (res.nodeId) {
        nodeList.forEach((node) => {
          if (node.id === res.nodeId) {
            node.status = res.status;
            if (res.output) {
              node.output = JSON.parse(JSON.stringify(res.output));
            }
            if (res.error) {
              node.error = res.error;
              message.error(res.error);
            }
            return;
          }
        });
      }
    }
  }, [wsMessage]); // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <PageContainer title={false}>
      <Card>
        <ToolBar
          status={flowStatus}
          copyNode={copyNode}
          pasteNode={pasteNode}
          saveData={saveData}
          deleteNode={deleteNode}
          executeFlow={executeFlow}
          stopFlow={stopFlowData}
          zoomNode={async (command) => {
            await zoomPan(command, jsPlumbInstance);
          }}
          showLogs={showLogs}
          exportFlow={exportFlow}
          importFlow={importFlow}
        />
        <div id="flow-content" className="flow-content">
          <div className="nodes-wrap">
            <Collapse
              expandIconPosition="right"
              bordered={false}
              activeKey={Object.keys(nodeTypes)}
            >
              {Object.keys(nodeTypes).map((k) => {
                return (
                  <Panel header={k} key={k}>
                    {nodeTypes[k].map((t: any) => {
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
                            <div
                              style={{ left: showDesc.left, top: showDesc.top }}
                              className="description"
                            >
                              {showDesc.data}
                            </div>
                          )}
                        </div>
                      );
                    })}
                  </Panel>
                );
              })}
            </Collapse>
          </div>

          <div
            id="flowWrap"
            className="flow-wrap"
            onClick={() => {
              // 点击事件冒泡，聚焦一下面板使快捷键生效
              document.getElementById('flowWrap')?.focus();
            }}
            onDragOver={(e) => e.preventDefault()}
            onDrop={(e) => drop(e)}
            onKeyUp={(e) => keyupNode(e)}
          >
            <div id="flow" onMouseDown={() => showNodeDetail(undefined)}>
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
              {nodeList.map((n) => {
                return (
                  <FlowNode
                    key={n.id}
                    node={n}
                    changeLineState={(nodeId, show) =>
                      changeLineState(nodeId, show, jsPlumbInstance)
                    }
                    showNodeDetail={showNodeDetail}
                  />
                );
              })}
            </div>
          </div>
          {selectedNode && <FlowDetail node={selectedNode} updateNode={updateNode} />}
          {logVisible && <FlowLog logContent={logContent.join('')} />}
        </div>
      </Card>
    </PageContainer>
  );
};

export default FlowEditor;
