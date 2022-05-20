// @ts-ignore
import ReactCodeMirror from 'react-cmirror';
import React, { useEffect, useState } from 'react';
import 'codemirror/mode/javascript/javascript.js';
import 'codemirror/theme/dracula.css';
import './index.less';
import { Card } from 'antd';
import { PageContainer } from '@ant-design/pro-layout';
import { onCloseLogContent, onOpenLogContent } from '@/services/ws';

const LogDetail: React.FC = (props: any) => {
  const [logContent, setLogContent] = useState<string[]>([]);

  /** 加载日志内容 */
  useEffect(() => {
    const path = props.location?.state?.path;
    if (path) {
      onOpenLogContent(path, (s) => {
        // 更新拼接日志内容
        setLogContent((arr) => {
          return [...arr, s];
        });
      });
    }

    // 销毁组件时关闭ws连接
    return () => {
      onCloseLogContent(path);
    };
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  return (
    <PageContainer title={false}>
      <Card>
        <div id="log-detail" className="log-detail">
          <ReactCodeMirror
            value={logContent.join('')}
            options={{
              mode: 'javascript',
              styleActiveLine: true,
              theme: 'dracula',
              readOnly: true,
              lineNumbers: true,
            }}
          />
        </div>
      </Card>
    </PageContainer>
  );
};

export default LogDetail;
