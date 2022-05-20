import React, { useState } from 'react';
import { message, Tooltip } from 'antd';
import ConfirmModal from '@/components/ConfirmModal';
import IconFont from '@/components/IconFont';
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
  const iconStyle = { width: '18px', height: '18px' };

  const handle = (command: string) => {
    if (command.startsWith('zoom')) {
      props.zoomNode(command.split('-')[1]);
    } else if (command === 'logs') {
      if (openLog) {
        props.showLogs(false);
        setSeeLog('查看日志');
      } else {
        props.showLogs(true);
        setSeeLog('关闭日志');
      }
      setOpenLog(!openLog);
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
          <IconFont type="icon-lx-logs" style={iconStyle} />
        </span>
      </Tooltip>
      <span className="separator" />
      <Tooltip title="保存" placement="bottom">
        <span className="command" onClick={() => handle('save')}>
          <IconFont type="icon-lx-save" style={iconStyle} />
        </span>
      </Tooltip>
      {props.status !== 'RUNNING' && (
        <ConfirmModal
          key="run"
          title="确认运行本流程？这将会保存本流程并覆盖之前的数据"
          danger={false}
          onConfirm={async () => await props.executeFlow()}
        >
          <Tooltip title="运行" placement="bottom">
            <span className="command">
              <IconFont type="icon-lx-run" style={iconStyle} />
            </span>
          </Tooltip>
        </ConfirmModal>
      )}
      {props.status === 'RUNNING' && (
        <ConfirmModal
          key="stop"
          title="确认停止运行？这将会立即停止本流程的运行"
          danger={false}
          onConfirm={async () => await props.stopFlow()}
        >
          <Tooltip title="停止" placement="bottom">
            <span className="command">
              <IconFont type="icon-lx-stop" style={iconStyle} />
            </span>
          </Tooltip>
        </ConfirmModal>
      )}
      <span className="separator" />
      <Tooltip title="复制" placement="bottom">
        <span className="command" onClick={() => handle('copy')}>
          <IconFont type="icon-lx-copy" style={iconStyle} />
        </span>
      </Tooltip>
      <Tooltip title="粘贴" placement="bottom">
        <span className="command" onClick={() => handle('paste')}>
          <IconFont type="icon-lx-paste" style={iconStyle} />
        </span>
      </Tooltip>
      <Tooltip title="删除" placement="bottom">
        <span className="command" onClick={() => handle('del')}>
          <IconFont type="icon-lx-delete" style={iconStyle} />
        </span>
      </Tooltip>
      <span className="separator" />
      <Tooltip title="放大" placement="bottom">
        <span className="command" onClick={() => handle('zoom-in')}>
          <IconFont type="icon-lx-zoomIn" style={iconStyle} />
        </span>
      </Tooltip>
      <Tooltip title="缩小" placement="bottom">
        <span className="command" onClick={() => handle('zoom-out')}>
          <IconFont type="icon-lx-zoomOut" style={iconStyle} />
        </span>
      </Tooltip>
      <span className="separator" />
      <Tooltip title="全屏" placement="bottom">
        <span className="command" onClick={() => handle('zoom-full')}>
          <IconFont type="icon-lx-zoomFull" style={iconStyle} />
        </span>
      </Tooltip>
      <Tooltip title="重置" placement="bottom">
        <span className="command" onClick={() => handle('zoom-reset')}>
          <IconFont type="icon-lx-zoomReset" style={iconStyle} />
        </span>
      </Tooltip>
    </div>
  );
};

export default ToolBar;
