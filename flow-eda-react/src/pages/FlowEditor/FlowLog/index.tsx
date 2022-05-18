import React from 'react';
import CodeMirror from '@uiw/react-codemirror';
import { javascript } from '@codemirror/lang-javascript';
import './index.less';

const FlowLog: React.FC<{ logContent: string }> = (props) => {
  return (
    <div id="flow-log" className="flow-log">
      <div className="drag-box" />
      <CodeMirror
        value={props.logContent}
        theme="dark"
        height="314px"
        readOnly={true}
        extensions={[javascript({ jsx: true })]}
      />
    </div>
  );
};

export default FlowLog;
