// @ts-ignore
import { FormattedMessage, Link, history } from 'umi';
import { PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import moment from 'moment';
import { ModalForm, ProFormText, ProFormTextArea } from '@ant-design/pro-form';
import { PageContainer } from '@ant-design/pro-layout';
import { Button, Space } from 'antd';
import type { Key } from 'react';
import React, { useRef, useState } from 'react';
import { addFlow, deleteFlow, getFlowList, updateFlow } from '@/services/api';
import { useFormatMessage, useSubmit } from '@/hooks/index';
import { generateUniqueID } from '@/utils/util';
import ConfirmModal from '@/components/ConfirmModal';

const FlowList: React.FC = () => {
  const [modalFormVisible, handleModalFormVisible] = useState<boolean>(false);
  const [modalFormType, setModalFormType] = useState<'add' | 'edit'>('add');
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.Flow>();
  const [selectedRowKeys, setSelectedRows] = useState<Key[]>([]);
  const { formatMsg } = useFormatMessage();
  const { submitRequest } = useSubmit();

  const columns: ProColumns<API.Flow>[] = [
    {
      title: formatMsg('pages.flowList.flows.name', '名称'),
      dataIndex: 'name',
      width: '18%',
      ellipsis: true,
    },
    {
      title: formatMsg('component.flows.status', '状态'),
      dataIndex: 'status',
      width: '8%',
      filters: true,
      onFilter: true,
      valueType: 'select',
      valueEnum: {
        INIT: {
          text: formatMsg('component.flows.status.init'),
          status: 'Default',
        },
        RUNNING: {
          text: formatMsg('component.flows.status.running'),
          status: 'Processing',
        },
        FINISHED: {
          text: formatMsg('component.flows.status.finished'),
          status: 'Success',
        },
        FAILED: {
          text: formatMsg('component.flows.status.failed'),
          status: 'Error',
        },
      },
    },
    {
      title: formatMsg('pages.flowList.flows.desc', '描述'),
      dataIndex: 'description',
      ellipsis: true,
      search: false,
    },
    {
      title: formatMsg('pages.flowList.flows.createDate', '创建时间'),
      dataIndex: 'createDate',
      valueType: 'dateTime',
      width: '14%',
      search: false,
    },
    {
      title: formatMsg('pages.flowList.flows.updateDate', '更新时间'),
      dataIndex: 'updateDate',
      valueType: 'dateTime',
      width: '14%',
      search: false,
    },
    {
      title: formatMsg('component.option', '操作'),
      valueType: 'option',
      width: '15%',
      render: (text, record) => [
        <Link key="show" to={`/flows/editor/${record.id}`}>
          {formatMsg('component.option.show', '查看')}
        </Link>,
        <a
          key="edit"
          onClick={() => {
            setCurrentRow(record);
            setModalFormType('edit');
            handleModalFormVisible(true);
          }}
        >
          <FormattedMessage id="pages.flowList.flows.edit" defaultMessage="编辑" />
        </a>,
        <a
          key="logs"
          onClick={() => {
            const date = moment().format('YYYY-MM-DD');
            const path = `/logs/running/${record.id}/${date}.log`;
            history.push({
              pathname: '/logs/detail',
              state: {
                path: path,
              },
            });
          }}
        >
          <FormattedMessage id="pages.flowList.flows.logs" defaultMessage="日志" />
        </a>,
        <ConfirmModal
          key="delete"
          title={formatMsg('component.modalForm.confirm.title')}
          danger={true}
          onConfirm={async () => {
            if (record?.id) {
              const success = await submitRequest(deleteFlow, [record.id]);
              if (success) {
                setSelectedRows([]);
                actionRef.current?.reloadAndRest?.();
              }
            }
          }}
        >
          <a key="delete" style={{ color: 'red' }}>
            <FormattedMessage id="component.option.delete" defaultMessage="删除" />
          </a>
        </ConfirmModal>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.Flow>
        headerTitle={formatMsg('pages.flowList.flows.title', '流程列表')}
        columns={columns}
        actionRef={actionRef}
        rowKey="id"
        search={{ labelWidth: 'auto' }}
        dateFormatter="string"
        pagination={{
          pageSize: 10,
          current: 1,
        }}
        request={getFlowList}
        rowSelection={{
          selectedRowKeys,
          onChange: (selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="add"
            onClick={() => {
              setModalFormType('add');
              handleModalFormVisible(true);
            }}
          >
            <PlusOutlined />
            <FormattedMessage id="pages.flowList.flows.add" defaultMessage="新增" />
          </Button>,
        ]}
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
              <ConfirmModal
                key="batchDel"
                title={formatMsg('component.modalForm.confirm.title')}
                danger={true}
                onConfirm={async () => {
                  const success = await submitRequest(deleteFlow, selectedRowKeys);
                  if (success) {
                    setSelectedRows([]);
                    actionRef.current?.reloadAndRest?.();
                  }
                }}
              >
                <a key="batchDel">
                  <FormattedMessage id="component.option.batchDeletion" defaultMessage="批量删除" />
                </a>
              </ConfirmModal>
            </Space>
          );
        }}
      />

      {modalFormVisible && (
        <ModalForm<API.Flow>
          title={
            modalFormType === 'edit'
              ? formatMsg('pages.flowList.flows.editFlow')
              : formatMsg('pages.flowList.flows.addFlow')
          }
          width="496px"
          initialValues={currentRow}
          visible={modalFormVisible}
          onVisibleChange={handleModalFormVisible}
          submitter={{
            render: (props) => {
              return [
                <Button
                  key="cancel"
                  onClick={() => {
                    setCurrentRow(undefined);
                    handleModalFormVisible(false);
                  }}
                >
                  <FormattedMessage id="component.modalForm.cancel" defaultMessage="取消" />
                </Button>,
                <Button type="primary" key="confirm" onClick={() => props.submit()}>
                  <FormattedMessage id="component.modalForm.confirm" defaultMessage="确认" />
                </Button>,
              ];
            },
          }}
          onFinish={async (body) => {
            let success;
            if (modalFormType === 'edit') {
              body.id = currentRow?.id;
              success = await submitRequest(updateFlow, body);
            } else {
              body.id = generateUniqueID(8);
              success = await submitRequest(addFlow, body);
            }
            if (success) {
              setCurrentRow(undefined);
              handleModalFormVisible(false);
              if (actionRef.current) {
                actionRef.current.reload();
              }
            }
          }}
        >
          <ProFormText
            name="name"
            label={formatMsg('pages.flowList.flows.name', '名称')}
            rules={[
              {
                required: true,
                message: formatMsg('pages.flowList.flows.addFlow.nameRule', '请输入流程名称'),
              },
            ]}
          />
          <ProFormTextArea
            name="description"
            label={formatMsg('pages.flowList.flows.desc', '描述')}
          />
        </ModalForm>
      )}
    </PageContainer>
  );
};

export default FlowList;
