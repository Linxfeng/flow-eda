import React from 'react';
import { Col, Row, Tooltip } from 'antd';
import { Divider } from 'antd';
import {
  CopyOutlined,
  DeleteOutlined,
  ExpandOutlined,
  FileTextOutlined,
  FullscreenOutlined,
  PlayCircleOutlined,
  SaveOutlined,
  SnippetsOutlined,
  ZoomInOutlined,
  ZoomOutOutlined,
} from '@ant-design/icons';

const ToolBar: React.FC = () => {
  const dividerStyle = { height: '1.5em' };

  const handle = (command: string) => {
    console.log(command);
  };

  return (
    <Row gutter={16} justify="end">
      <Col span={1} flex="none">
        <Tooltip placement="bottom" title="日志">
          <FileTextOutlined onClick={() => handle('logs')} />
        </Tooltip>
      </Col>
      <Divider type="vertical" style={dividerStyle} />
      <Col span={1} flex="none">
        <Tooltip placement="bottom" title="保存">
          <SaveOutlined onClick={() => handle('save')} />
        </Tooltip>
      </Col>
      <Divider type="vertical" style={dividerStyle} />
      <Col span={1} flex="none">
        <Tooltip placement="bottom" title="运行">
          <PlayCircleOutlined onClick={() => handle('run')} />
        </Tooltip>
      </Col>
      <Divider type="vertical" style={dividerStyle} />
      <Col span={1} flex="none">
        <Tooltip placement="bottom" title="复制">
          <CopyOutlined onClick={() => handle('copy')} />
        </Tooltip>
      </Col>
      <Divider type="vertical" style={dividerStyle} />
      <Col span={1} flex="none">
        <Tooltip placement="bottom" title="粘贴">
          <SnippetsOutlined onClick={() => handle('paste')} />
        </Tooltip>
      </Col>
      <Divider type="vertical" style={dividerStyle} />
      <Col span={1} flex="none">
        <Tooltip placement="bottom" title="删除">
          <DeleteOutlined onClick={() => handle('del')} />
        </Tooltip>
      </Col>
      <Divider type="vertical" style={dividerStyle} />
      <Col span={1} flex="none">
        <Tooltip placement="bottom" title="放大">
          <ZoomInOutlined onClick={() => handle('zoom-in')} />
        </Tooltip>
      </Col>
      <Divider type="vertical" style={dividerStyle} />
      <Col span={1} flex="none">
        <Tooltip placement="bottom" title="缩小">
          <ZoomOutOutlined onClick={() => handle('zoom-out')} />
        </Tooltip>
      </Col>
      <Divider type="vertical" style={dividerStyle} />
      <Col span={1} flex="none">
        <Tooltip placement="bottom" title="全屏">
          <FullscreenOutlined onClick={() => handle('zoom-full')} />
        </Tooltip>
      </Col>
      <Divider type="vertical" style={dividerStyle} />
      <Col span={1} flex="none">
        <Tooltip placement="bottom" title="重置">
          <ExpandOutlined onClick={() => handle('zoom-reset')} />
        </Tooltip>
      </Col>
    </Row>
  );
};

export default ToolBar;
