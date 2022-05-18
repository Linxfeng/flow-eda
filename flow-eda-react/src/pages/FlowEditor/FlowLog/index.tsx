import React from 'react';
import ReactCodeMirror from 'react-cmirror';
import 'codemirror/mode/javascript/javascript.js';
import 'codemirror/theme/dracula.css';
import './index.less';

const FlowLog: React.FC<{ logContent: string }> = (props) => {
  /** 内容更新时自动滚动到底部 */
  const autoScroll = (cm: any) => {
    const nowScrollInfo = cm.getScrollInfo();
    cm.scrollTo(nowScrollInfo.left, nowScrollInfo.height);
  };

  return (
    <div id="flow-log" className="flow-log">
      <div className="drag-box" />
      <ReactCodeMirror
        value={props.logContent}
        options={{
          mode: 'javascript',
          styleActiveLine: true,
          theme: 'dracula',
          readOnly: true,
          lineNumbers: true,
        }}
        onChange={autoScroll}
      />
    </div>
  );
};

export default FlowLog;
