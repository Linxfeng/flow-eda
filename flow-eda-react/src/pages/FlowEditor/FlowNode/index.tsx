import React, { useEffect, useRef, useState } from 'react';
import './index.less';

const FlowNode: React.FC<{
  node: API.Node;
  changeLineState: (nodeId: string, show: boolean) => void;
  showNodeDetail: (node: API.Node) => void;
}> = (props) => {
  const node = props.node;
  const [mouseEnter, setMouseEnter] = useState<boolean>(false);
  const [active, setActive] = useState<boolean>(false);
  const [selected, setSelected] = useState<boolean>(false);
  const [clickedOutside, setClickedOutside] = useState(false);
  const className = active || selected ? 'node-item active' : 'node-item';
  const nodeRef = useRef<any>();

  const setActiveNode = (e: any) => {
    setClickedOutside(false);
    if (e.ctrlKey) {
      setSelected(!selected);
    } else {
      setActive(true);
      setSelected(false);
      setTimeout(() => {
        props.changeLineState(props.node.id, true);
        props.showNodeDetail(props.node);
      }, 0);
    }
    console.log(clickedOutside);
  };

  const setNotActive = (e: MouseEvent) => {
    if (!e.ctrlKey) {
      setSelected(false);
    }
    if (!active) {
      return;
    }
    props.changeLineState(props.node.id, false);
    setActive(false);
  };

  const handleClickOutside = (e: MouseEvent) => {
    if (nodeRef?.current && e.target && !nodeRef.current.contains(e.target)) {
      setClickedOutside(true);
      setNotActive(e);
    }
    console.log(clickedOutside);
  };

  useEffect(() => {
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  });

  return (
    <div
      ref={nodeRef}
      id="node"
      className={className}
      onClick={(e) => setActiveNode(e)}
      onMouseEnter={() => setMouseEnter(true)}
      onMouseLeave={() => setMouseEnter(false)}
      style={{ top: node.top, left: node.left, background: node.nodeType?.background }}
    >
      <div className="node-svg">
        <img src={node.nodeType?.svg} alt="" style={{ padding: '4px' }} />
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
      {mouseEnter && <div className="node-anchor anchor-top" />}
      {mouseEnter && <div className="node-anchor anchor-right" />}
      {mouseEnter && <div className="node-anchor anchor-bottom" />}
      {mouseEnter && <div className="node-anchor anchor-left" />}
    </div>
  );
};

export default FlowNode;
