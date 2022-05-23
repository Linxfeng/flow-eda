// @ts-ignore
import ReactCodeMirror from 'react-cmirror';
import React, { useRef } from 'react';
import 'codemirror/mode/javascript/javascript.js';
import 'codemirror/theme/dracula.css';
import './index.less';

const FlowLog: React.FC<{ logContent: string }> = (props) => {
  const flowLogRef = useRef(null);

  /** 内容更新时自动滚动到底部 */
  const autoScroll = (cm: any) => {
    const nowScrollInfo = cm.getScrollInfo();
    cm.scrollTo(nowScrollInfo.left, nowScrollInfo.height);
  };

  return (
    <div id="flow-log" ref={flowLogRef} className="flow-log">
      <div
        className="drag-box"
        onMouseDown={() => {
          document.onmousemove = function (e) {
            let th = document.body.clientHeight - e.clientY;
            if (th < 100) {
              th = 100;
            }
            // @ts-ignore
            const style = flowLogRef.current?.style;
            style.height = th + 'px';
          };
          document.onmouseup = function () {
            document.onmousemove = null;
            document.onmouseup = null;
          };
        }}
      />
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
