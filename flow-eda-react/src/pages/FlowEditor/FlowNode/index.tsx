import React from 'react';
import './index.less';

const FlowNode: React.FC<{ node: any }> = (props) => {
  const imgStyle = { padding: '4px' };
  const node = props.node;

  return (
    <div id="node" className="node-item" style={{ background: node.background }}>
      <div className="node-svg">
        <img src={node.nodeType.svg} alt="" style={imgStyle} />
      </div>
      <div className="node-name">{node.nodeName}</div>
      {node.status === 'RUNNING' && (
        <div className="node-status">
          <img alt="运行中" src="/svg/status/running.svg" title="运行中..." />
        </div>
      )}
      {node.status === 'FINISHED' && (
        <div className="node-status">
          <img alt="运行完成" src="/svg/status/finished.svg" title="运行完成" />
        </div>
      )}
      {node.status === 'FAILED' && (
        <div className="node-status">
          <img alt="运行失败" src="/svg/status/failed.svg" title={node.error} />
        </div>
      )}
      <div className="node-anchor anchor-top" />
      <div className="node-anchor anchor-right" />
      <div className="node-anchor anchor-bottom" />
      <div className="node-anchor anchor-left" />
    </div>
  );
};

export default FlowNode;
