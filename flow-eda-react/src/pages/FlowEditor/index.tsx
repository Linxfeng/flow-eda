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

  /** ????????????????????????????????????????????? */
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

  /** ???????????? */
  const addLine = (evt: any) => {
    const from = evt.source.id;
    const to = evt.target.id;
    setLineList((lines) => {
      // ??????????????????
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

  /** ?????????????????????????????? */
  const reloadNode = (node: API.Node) => {
    const nodeId = node.id;
    jsPlumbInstance.makeSource(nodeId, makeOptions);
    jsPlumbInstance.makeTarget(nodeId, makeOptions);
    // ??????????????????????????????
    jsPlumbInstance.draggable(nodeId, {
      // @ts-ignore // ???????????????????????????5px
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

  /** ??????????????????????????? */
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

  /** ????????????????????? */
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
        // ????????????????????????
        setNodeList(nodes);
        nodes.forEach((node) => {
          jsPlumbInstance.makeSource(node.id, makeOptions);
          jsPlumbInstance.makeTarget(node.id, makeOptions);
        });
        // ???????????????????????????
        setLineList(lines);
      }
    });
  };

  /** ????????????????????? */
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

  /** ?????????????????? */
  const init = () => {
    // ?????????????????????
    getNodeTypes().then((res) => setNodeTypes(res));
    // ????????????????????????
    jsPlumbInstance.ready(() => {
      // ??????????????????
      jsPlumbInstance.importDefaults(defaultSetting);
      // ??????????????????????????????????????????
      jsPlumbInstance.bind('connection', (evt) => {
        addLine(evt);
      });
      //????????????????????????
      jsPlumbInstance.bind('dblclick', (line) => {
        confirmDeleteLine(line);
      });
      //????????????????????????????????????
      jsPlumbInstance.bind('connectionDetached', (evt) => {
        lineList.forEach((item, index) => {
          if (item.from === evt.sourceId && item.to === evt.targetId) {
            lineList.splice(index, 1);
          }
        });
      });
      // ??????????????????
      loadFlowData();
      // ????????????
      jsPlumbInstance.setSuspendDrawing(false, true);
    });
    // ??????????????????
    setPanZoom(jsPlumbInstance, (style: any) => setAuxiliaryLineStyle(style));
  };

  /** ???????????? */
  const copyNode = () => {
    setClipboard(selectedNode);
  };
  /** ???????????? */
  const pasteNode = () => {
    if (clipboard) {
      // ??????????????????????????????????????????
      const temp = JSON.parse(JSON.stringify(clipboard));
      temp.id = generateUniqueID(8);
      // ???????????????????????????????????????15px,30px
      temp.left = parseFloat(temp.left.slice(0, temp.left.length - 2)) + 30 + 'px';
      temp.top = parseFloat(temp.top.slice(0, temp.top.length - 2)) + 15 + 'px';
      setSelectedNode(temp);
      setNodeList([...nodeList, temp]);
    }
  };

  /** ?????????????????? */
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

  /** ???????????????????????? */
  const drag = (item: API.NodeType) => {
    setCurrentItem(item);
  };
  /** ???????????????????????? */
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
    // ????????????
    setNodeList([...nodeList, temp]);
  };

  /** ??????????????????????????????????????? */
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

  /** ??????????????????????????? */
  const showNodeDetail = (node: API.Node | undefined) => {
    setSelectedNode(undefined);
    if (node) {
      setTimeout(() => {
        setSelectedNode(node);
      }, 0);
    }
  };

  /** ???????????????????????? */
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

  /** ???????????????????????? */
  const keyupNode = (e: any) => {
    if (!e.ctrlKey && e.key === 'Delete') {
      deleteNode();
    } else if (e.ctrlKey && e.key === 'c') {
      copyNode();
    } else if (e.ctrlKey && e.key === 'v') {
      pasteNode();
    }
  };

  /** ???????????? */
  const importFlow = async () => {
    try {
      const text = await navigator.clipboard.readText();
      const flow = JSON.parse(text);
      if (flow && flow.nodeList && flow.lineList) {
        // ????????????????????????
        const tempId = {};
        flow.nodeList.forEach((n: API.Node) => {
          tempId[n.id] = generateUniqueID(8);
          n.id = tempId[n.id];
          n.flowId = id;
          // ????????????????????????
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
        // ??????????????????????????????????????????
        setTimeout(() => {
          jsPlumbInstance.cleanupListeners();
          jsPlumbInstance.deleteEveryConnection();
          jsPlumbInstance.deleteEveryEndpoint();
          jsPlumbInstance.reset();
          // ????????????????????????
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

  /** ???????????? */
  const exportFlow = async () => {
    const flow = {
      nodeList: nodeList,
      lineList: lineList,
    };
    await navigator.clipboard.writeText(JSON.stringify(flow));
    message.success(formatMsg('pages.flowList.editor.exportFlow.success'));
  };

  /** ?????????????????? */
  const showLogs = (show: boolean) => {
    if (show) {
      setLogVisible(true);
      onOpenLogs(id, (s) => {
        // ??????????????????
        setLogContent((arr) => {
          return [...arr, s];
        });
      });
    } else {
      setLogVisible(false);
    }
  };

  /** ????????????????????????????????? */
  const saveData = async (): Promise<boolean> => {
    if (nodeList.length === 0) {
      message.error(formatMsg('pages.flowList.editor.checkFlow'));
      return false;
    }
    // ????????????????????????
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
    // ??????/????????????????????????
    await setFlowData(body);
    return true;
  };

  /** ??????????????? */
  const executeFlow = async () => {
    nodeList.forEach((n) => {
      n.status = undefined;
      n.error = undefined;
      n.output = undefined;
    });
    // ????????????????????????
    const save = await saveData();
    if (save) {
      // ???????????????
      runFlow(id).then((res) => {
        if (res) {
          message.success(formatMsg('component.message.success'));
        }
      });
    }
  };

  /** ???????????? */
  const stopFlowData = async () => {
    stopFlow(id).then((res) => {
      if (res) {
        message.success(formatMsg('component.message.success'));
      }
    });
  };

  useEffect(() => {
    // ?????????
    init();
    // ??????ws??????????????????????????????????????????????????????
    onOpenNode(id, (s) => setWsMessage(s));

    // ?????????????????????ws??????
    return () => {
      onCloseNode(id);
      onCloseLogs(id);
    };
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  /** ????????????????????????????????????????????????????????? */
  useEffect(() => {
    nodeList.forEach((node) => reloadNode(node));
  }, [nodeList]); // eslint-disable-line react-hooks/exhaustive-deps

  /** ????????????????????????????????????????????????????????? */
  useEffect(() => {
    connectLines(lineList);
  }, [lineList]); // eslint-disable-line react-hooks/exhaustive-deps

  /** ??????ws?????????????????????????????????????????? */
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
              // ?????????????????????????????????????????????????????????
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
