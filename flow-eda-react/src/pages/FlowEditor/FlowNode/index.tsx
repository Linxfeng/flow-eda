import React from 'react';
import './index.less';
import svg from '/public/svg/start.svg';

const FlowNode: React.FC = () => {
  const imgStyle = { padding: '4px' };

  return (
    <div id="node" className="node-item" style={{ background: 'rgba(0, 128, 0, 0.2)' }}>
      <div className="node-svg">
        <img src={svg} alt="" style={imgStyle} />
      </div>
      <div className="node-name">节点名称</div>
      <div className="node-anchor anchor-top" />
      <div className="node-anchor anchor-right" />
      <div className="node-anchor anchor-bottom" />
      <div className="node-anchor anchor-left" />
    </div>
  );
};

export default FlowNode;
