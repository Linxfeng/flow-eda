// @ts-ignore
import { FormattedMessage, history } from 'umi';
import { ExclamationCircleOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { PageContainer } from '@ant-design/pro-layout';
import { Modal, Space } from 'antd';
import type { Key } from 'react';
import React, { useRef, useState } from 'react';
import { deleteLogs, getLogList } from '@/services/api';
import { useFormatMessage, useSubmit } from '@/hooks/index';

const LogList: React.FC = () => {
  const [selectedRowKeys, setSelectedRows] = useState<Key[]>([]);
  const actionRef = useRef<ActionType>();
  const { formatMsg } = useFormatMessage();
  const { submitRequest } = useSubmit();

  /** 删除日志，二次确认 */
  const showDeleteConfirm = async (selectedRows: Key[] | string[]) => {
    Modal.confirm({
      title: formatMsg('component.modalForm.confirm.title'),
      icon: <ExclamationCircleOutlined />,
      okType: 'danger',
      okText: formatMsg('component.modalForm.confirm'),
      cancelText: formatMsg('component.modalForm.cancel'),
      async onOk() {
        const success = await submitRequest(deleteLogs, selectedRows);
        if (success) {
          setSelectedRows([]);
          actionRef.current?.reloadAndRest?.();
        }
      },
    });
  };

  const columns: ProColumns<API.Log>[] = [
    {
      title: formatMsg('pages.logList.logs.type', '日志类型'),
      dataIndex: 'type',
      width: '18%',
      valueType: 'select',
      initialValue: 'RUNNING',
      valueEnum: {
        RUNNING: { text: formatMsg('pages.logList.logs.type.running') },
        OPERATION: { text: formatMsg('pages.logList.logs.type.operation') },
      },
    },
    {
      title: formatMsg('pages.logList.logs.flowName', '流程名称'),
      dataIndex: 'flow',
      ellipsis: true,
      search: false,
    },
    {
      title: formatMsg('pages.logList.logs.date', '日志日期'),
      dataIndex: 'date',
      width: '14%',
      search: false,
    },
    {
      title: formatMsg('pages.logList.logs.fileSize', '文件大小（单位：kb）'),
      dataIndex: 'size',
      width: '14%',
      search: false,
    },
    {
      title: formatMsg('component.option', '操作'),
      valueType: 'option',
      width: '20%',
      render: (text, record) => [
        <a
          key="show"
          onClick={() => {
            history.push({
              pathname: '/logs/detail',
              state: {
                path: record.path,
              },
            });
          }}
        >
          <FormattedMessage id="component.option.show" defaultMessage="查看" />
        </a>,
        <a
          key="delete"
          style={{ color: 'red' }}
          onClick={async () => {
            if (record?.path) {
              await showDeleteConfirm([record.path]);
            }
          }}
        >
          <FormattedMessage id="component.option.delete" defaultMessage="删除" />
        </a>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.Log>
        headerTitle={formatMsg('pages.logList.logs.title', '日志列表')}
        columns={columns}
        actionRef={actionRef}
        rowKey="path"
        search={{ labelWidth: 'auto' }}
        pagination={{
          pageSize: 10,
          current: 1,
        }}
        request={async (params) => {
          const res = await getLogList(params);
          res.data.forEach((v: API.Log) => {
            v.type = params.type;
          });
          return res;
        }}
        rowSelection={{
          selectedRowKeys,
          onChange: (selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
        tableAlertRender={() => (
          <span>
            <FormattedMessage id="component.option.chosen" defaultMessage="已选择" />
            &nbsp;{selectedRowKeys.length}&nbsp;
            <FormattedMessage id="component.option.item" defaultMessage="项" />
          </span>
        )}
        tableAlertOptionRender={() => {
          return (
            <Space size={16}>
              <a onClick={() => setSelectedRows([])}>
                <FormattedMessage id="component.option.clear" defaultMessage="取消选择" />
              </a>
              <a
                onClick={async () => {
                  await showDeleteConfirm(selectedRowKeys);
                }}
              >
                <FormattedMessage id="component.option.batchDeletion" defaultMessage="批量删除" />
              </a>
            </Space>
          );
        }}
      />
    </PageContainer>
  );
};

export default LogList;
