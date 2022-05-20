import React, { useState } from 'react';
import ClickOutside from 'react-click-outsider';
import './index.less';
import { useFormatMessage } from '@/hooks';

const FlowNode: React.FC<{
  node: API.Node;
  changeLineState: (nodeId: string, show: boolean) => void;
  showNodeDetail: (node: API.Node) => void;
}> = (props) => {
  const node = props.node;
  const { formatMsg } = useFormatMessage();
  const [mouseEnter, setMouseEnter] = useState<boolean>(false);
  const [active, setActive] = useState<boolean>(false);
  const [selected, setSelected] = useState<boolean>(false);
  const className = active || selected ? 'node-item active' : 'node-item';

  const setActiveNode = (e: any) => {
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
  };

  const setNotActive = (e: any) => {
    if (!e.ctrlKey && selected) {
      setSelected(false);
    }
    if (active) {
      props.changeLineState(props.node.id, false);
      setActive(false);
    }
  };

  return (
    <ClickOutside onClickOutside={(e: any) => setNotActive(e)}>
      <div
        id={props.node.id}
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
            <img
              alt={formatMsg('component.flows.status.running')}
              src="/svg/status/running.svg"
              title={formatMsg('component.flows.status.running') + '...'}
            />
          </div>
        )}
        {node.status === 'FINISHED' && (
          <div className="node-status">
            <img
              alt={formatMsg('component.flows.status.finished')}
              src="/svg/status/finished.svg"
              title={formatMsg('component.flows.status.finished')}
            />
          </div>
        )}
        {node.status === 'FAILED' && (
          <div className="node-status">
            <img
              alt={formatMsg('component.flows.status.finished')}
              src="/svg/status/failed.svg"
              title={node.error}
            />
          </div>
        )}
        {mouseEnter && <div className="node-anchor anchor-top" />}
        {mouseEnter && <div className="node-anchor anchor-right" />}
        {mouseEnter && <div className="node-anchor anchor-bottom" />}
        {mouseEnter && <div className="node-anchor anchor-left" />}
      </div>
    </ClickOutside>
  );
};

export default FlowNode;
