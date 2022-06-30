import ConfirmModal from '@/components/ConfirmModal';
import IconFont from '@/components/IconFont';
import { useFormatMessage } from '@/hooks';
import { message, Tooltip } from 'antd';
import React, { useState } from 'react';
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
  importFlow: () => void;
  exportFlow: () => void;
}> = (props) => {
  const { formatMsg } = useFormatMessage();
  const [seeLog, setSeeLog] = useState<string>(
    formatMsg('pages.flowList.editor.showLog', '查看日志'),
  );
  const [openLog, setOpenLog] = useState<boolean>(false);
  const iconStyle = { width: '18px', height: '18px' };

  const handle = async (command: string) => {
    if (command.startsWith('zoom')) {
      props.zoomNode(command.split('-')[1]);
    } else if (command === 'import') {
      props.importFlow();
    } else if (command === 'export') {
      props.exportFlow();
    } else if (command === 'logs') {
      if (openLog) {
        props.showLogs(false);
        setSeeLog(formatMsg('pages.flowList.editor.showLog', '查看日志'));
      } else {
        props.showLogs(true);
        setSeeLog(formatMsg('pages.flowList.editor.closeLog', '关闭日志'));
      }
      setOpenLog(!openLog);
    } else if (command === 'save') {
      props.saveData();
      message.success(formatMsg('component.message.success', '操作成功'));
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
      <Tooltip title={formatMsg('pages.flowList.editor.import', '导入')} placement="bottom">
        <span className="command" onClick={() => handle('import')}>
          <IconFont type="icon-lx-import" style={iconStyle} />
        </span>
      </Tooltip>
      <Tooltip title={formatMsg('pages.flowList.editor.export', '导出')} placement="bottom">
        <span className="command" onClick={() => handle('export')}>
          <IconFont type="icon-lx-export" style={iconStyle} />
        </span>
      </Tooltip>
      <span className="separator" />
      <Tooltip title={seeLog} placement="bottom">
        <span className="command" onClick={() => handle('logs')}>
          <IconFont type="icon-lx-logs" style={iconStyle} />
        </span>
      </Tooltip>
      <span className="separator" />
      <Tooltip title={formatMsg('component.option.save', '保存')} placement="bottom">
        <span className="command" onClick={() => handle('save')}>
          <IconFont type="icon-lx-save" style={iconStyle} />
        </span>
      </Tooltip>
      {props.status !== 'RUNNING' && (
        <ConfirmModal
          key="run"
          title={formatMsg('pages.flowList.editor.runConfirm')}
          danger={false}
          onConfirm={async () => await props.executeFlow()}
        >
          <Tooltip title={formatMsg('pages.flowList.editor.run', '运行')} placement="bottom">
            <span className="command">
              <IconFont type="icon-lx-run" style={iconStyle} />
            </span>
          </Tooltip>
        </ConfirmModal>
      )}
      {props.status === 'RUNNING' && (
        <ConfirmModal
          key="stop"
          title={formatMsg('pages.flowList.editor.stopConfirm')}
          danger={false}
          onConfirm={async () => await props.stopFlow()}
        >
          <Tooltip title={formatMsg('pages.flowList.editor.stop', '停止')} placement="bottom">
            <span className="command">
              <IconFont type="icon-lx-stop" style={iconStyle} />
            </span>
          </Tooltip>
        </ConfirmModal>
      )}
      <span className="separator" />
      <Tooltip title={formatMsg('pages.flowList.editor.copy', '复制')} placement="bottom">
        <span className="command" onClick={() => handle('copy')}>
          <IconFont type="icon-lx-copy" style={iconStyle} />
        </span>
      </Tooltip>
      <Tooltip title={formatMsg('pages.flowList.editor.paste', '粘贴')} placement="bottom">
        <span className="command" onClick={() => handle('paste')}>
          <IconFont type="icon-lx-paste" style={iconStyle} />
        </span>
      </Tooltip>
      <Tooltip title={formatMsg('pages.flowList.editor.del', '删除')} placement="bottom">
        <span className="command" onClick={() => handle('del')}>
          <IconFont type="icon-lx-delete" style={iconStyle} />
        </span>
      </Tooltip>
      <span className="separator" />
      <Tooltip title={formatMsg('pages.flowList.editor.zoomIn', '放大')} placement="bottom">
        <span className="command" onClick={() => handle('zoom-in')}>
          <IconFont type="icon-lx-zoomIn" style={iconStyle} />
        </span>
      </Tooltip>
      <Tooltip title={formatMsg('pages.flowList.editor.zoomOut', '缩小')} placement="bottom">
        <span className="command" onClick={() => handle('zoom-out')}>
          <IconFont type="icon-lx-zoomOut" style={iconStyle} />
        </span>
      </Tooltip>
      <span className="separator" />
      <Tooltip title={formatMsg('pages.flowList.editor.zoomOut', '全屏')} placement="bottom">
        <span className="command" onClick={() => handle('zoom-full')}>
          <IconFont type="icon-lx-zoomFull" style={iconStyle} />
        </span>
      </Tooltip>
      <Tooltip title={formatMsg('pages.flowList.editor.zoomReset', '重置')} placement="bottom">
        <span className="command" onClick={() => handle('zoom-reset')}>
          <IconFont type="icon-lx-zoomReset" style={iconStyle} />
        </span>
      </Tooltip>
    </div>
  );
};

export default ToolBar;
