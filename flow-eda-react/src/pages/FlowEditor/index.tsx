import React, { useState, useEffect } from 'react';
import { useParams } from 'umi';
import jsplumb from 'jsplumb';
import { getFlowData, getNodeTypes } from '@/services/api';
import {
  jsplumbConnectOptions,
  jsplumbSetting,
  jsplumbSourceOptions,
  jsplumbTargetOptions,
} from '@/pages/FlowEditor/jsplumb/jsplumbConfig';
import { generateUniqueID } from '@/utils/util';
import { PageContainer } from '@ant-design/pro-layout';
import { Card } from 'antd';
import ToolBar from '@/pages/FlowEditor/ToolBar';
import FlowNode from '@/pages/FlowEditor/FlowNode/index';

const FlowEditor: React.FC = () => {
  const { id } = useParams<{ id: string }>();
  const [jsPlumbInstance] = useState(jsplumb.jsPlumb.getInstance());
  const [data, setDate] = useState<{ nodeList: API.Node[]; lineList: API.Node[] }>({
    nodeList: [],
    lineList: [],
  });
  const [nodeTypes, setNodeTypes] = useState<API.NodeType[]>([]);
  const [auxiliaryLine, setAuxiliaryLine] = useState<{ showXLine: boolean; showYLine: boolean }>({
    showXLine: false,
    showYLine: false,
  });
  const [auxiliaryLinePos, setAuxiliaryLinePos] = useState<{ x: number; y: number }>({
    x: 20,
    y: 20,
  });

  /** 初始化节点数据 */
  const initFlowData = async () => {
    const res = await getFlowData(id);
    const node: API.Node[] = [];
    const line: API.Node[] = [];
    if (res?.result) {
      res.result.map((d) => {
        if (d.nodeName) {
          node.push(d);
        } else {
          line.push(d);
        }
      });
    }
    setDate({ nodeList: node, lineList: line });
  };

  // 初始化节点类型
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
    const nodes = data.nodeList;
    if (nodes && nodes.length > 0) {
      nodes.forEach((node) => {
        // 设置源点，可以拖出线连接其他节点
        jsPlumbInstance.makeSource(node.id, jsplumbSourceOptions);
        // 设置目标点，其他源点拖出的线可以连接该节点
        jsPlumbInstance.makeTarget(node.id, jsplumbTargetOptions);
        // 注册节点拖动事件
        draggableNode(node.id);
      });
    }
    //取消连接事件
    jsPlumbInstance.unbind('connection');
    const lines = data.lineList;
    if (lines && lines.length > 0) {
      // 初始化连线
      lines.forEach((line) => {
        jsPlumbInstance.connect(
          {
            source: line.from,
            target: line.to,
          },
          jsplumbConnectOptions,
        );
      });
    }
    //注册连接事件
    jsPlumbInstance.bind('connection', (evt) => {
      const line: API.Node = {
        id: generateUniqueID(8),
        flowId: id,
        from: evt.source.id,
        to: evt.target.id,
      };
      if (lines) {
        lines.push(line);
      }
    });
  };

  const initPanel = () => {
    jsPlumbInstance.ready(() => {
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
        console.log(auxiliaryLine);
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
    // initPanZoom();
  };

  // initNodeType();
  // initFlowData();
  // initPanel();

  // useEffect(() => {
  //   initNodeType();
  //   initFlowData();
  //   initPanel();
  // 加载流程节点数据
  // initFlowData().then(() => {
  //   // 初始化编辑器画板
  //   initPanel();
  // });
  //
  // return () => {
  //   if (jsPlumbInstance) {
  //     jsPlumbInstance.reset();
  //   }
  // };
  // });

  return (
    <PageContainer>
      <Card>
        <ToolBar />
        <div id="flow-content" className="flow-content">
          <div className="nodes-wrap">
            {nodeTypes.map((t) => {
              return (
                <div
                  key={t.type}
                  style={{ background: t.background }}
                  className="node"
                  draggable="true"
                >
                  <div className="svg">
                    <img src={t.svg} alt="" style={{ padding: '4px' }} />
                  </div>
                  <div className="name">{t.typeName}</div>
                </div>
              );
            })}
          </div>

          <div id="flowWrap" className="flow-wrap">
            <div id="flow">
              {data.nodeList.map((n) => {
                // <FlowNode id={n.id} key={n.id} node={n} />;
                return <FlowNode key={n.id} />;
              })}
            </div>
          </div>
        </div>
      </Card>
    </PageContainer>
  );
};

export default FlowEditor;
