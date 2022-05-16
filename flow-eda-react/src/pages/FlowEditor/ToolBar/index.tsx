import React, { useState } from 'react';
import { message, Tooltip } from 'antd';
import ConfirmModal from '@/components/ConfirmModal';
import './index.less';

const ToolBar: React.FC<{
  status: string;
  copyNode: () => void;
  pasteNode: () => void;
  saveData: () => void;
  deleteNode: () => void;
  executeFlow: () => void;
  stopFlow: () => void;
  zoomNode: (command: string) => void;
  showLogs: (show: boolean) => void;
}> = (props) => {
  const [seeLog, setSeeLog] = useState<string>('查看日志');
  const [openLog, setOpenLog] = useState<boolean>(false);

  const handle = (command: string) => {
    if (command.startsWith('zoom')) {
      props.zoomNode(command.split('-')[1]);
    } else if (command === 'logs') {
      setOpenLog(!openLog);
      if (openLog) {
        props.showLogs(true);
        setSeeLog('关闭日志');
      } else {
        props.showLogs(false);
        setSeeLog('查看日志');
      }
    } else if (command === 'save') {
      props.saveData();
      message.success('保存成功');
    } else if (command === 'copy') {
      props.copyNode();
    } else if (command === 'paste') {
      props.pasteNode();
    } else if (command === 'del') {
      props.deleteNode();
    }
  };

  return (
    <div className="toolbar">
      <Tooltip title={seeLog} placement="bottom">
        <span className="command" onClick={() => handle('logs')}>
          <span className="icon-lx-logs" />
        </span>
      </Tooltip>
      <span className="separator" />
      <Tooltip title="保存" placement="bottom">
        <span className="command" onClick={() => handle('save')}>
          <span className="icon-lx-save" />
        </span>
      </Tooltip>
      <ConfirmModal
        key="run"
        title="确认运行本流程？这将会保存本流程并覆盖之前的数据"
        danger={false}
        onConfirm={async () => await props.executeFlow()}
      >
        <Tooltip v-if="status!=='RUNNING'" title="运行" placement="bottom">
          <span className="command">
            <span className="icon-lx-run" />
          </span>
        </Tooltip>
      </ConfirmModal>
      <ConfirmModal
        key="stop"
        title="确认停止运行？这将会立即停止本流程的运行"
        danger={false}
        onConfirm={async () => await props.stopFlow()}
      >
        <Tooltip v-if="status==='RUNNING'" title="停止" placement="bottom">
          <span className="command">
            <span className="icon-lx-stop" />
          </span>
        </Tooltip>
      </ConfirmModal>
      <span className="separator" />
      <Tooltip title="复制" placement="bottom">
        <span className="command" onClick={() => handle('copy')}>
          <span className="icon-lx-copy" />
        </span>
      </Tooltip>
      <Tooltip title="粘贴" placement="bottom">
        <span className="command" onClick={() => handle('paste')}>
          <span className="icon-lx-paste" />
        </span>
      </Tooltip>
      <Tooltip title="删除" placement="bottom">
        <span className="command" onClick={() => handle('del')}>
          <span className="icon-lx-delete" />
        </span>
      </Tooltip>
      <span className="separator" />
      <Tooltip title="放大" placement="bottom">
        <span className="command" onClick={() => handle('zoom-in')}>
          <span className="icon-lx-zoomIn" />
        </span>
      </Tooltip>
      <Tooltip title="缩小" placement="bottom">
        <span className="command" onClick={() => handle('zoom-out')}>
          <span className="icon-lx-zoomOut" />
        </span>
      </Tooltip>
      <span className="separator" />
      <Tooltip title="全屏" placement="bottom">
        <span className="command" onClick={() => handle('zoom-full')}>
          <span className="icon-lx-zoomFull" />
        </span>
      </Tooltip>
      <Tooltip title="重置" placement="bottom">
        <span className="command" onClick={() => handle('zoom-reset')}>
          <span className="icon-lx-zoomReset" />
        </span>
      </Tooltip>
      <span className="separator" />
    </div>
  );
};

export default ToolBar;
