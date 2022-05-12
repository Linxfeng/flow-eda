import { ExclamationCircleOutlined, PlusOutlined } from '@ant-design/icons';
import type { ActionType, ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { ModalForm, ProFormText, ProFormTextArea } from '@ant-design/pro-form';
import { PageContainer } from '@ant-design/pro-layout';
import { Button, Modal, Space } from 'antd';
import type { Key } from 'react';
import React, { useRef, useState } from 'react';
import { FormattedMessage, Link } from 'umi';
import { addFlow, deleteFlow, getFlowList, updateFlow } from '@/services/api';
import { useFormatMessage, useSubmit } from '@/hooks/index';
import { generateUniqueID } from '@/utils/util';

const FlowList: React.FC = () => {
  const [modalFormVisible, handleModalFormVisible] = useState<boolean>(false);
  const [modalFormType, setModalFormType] = useState<'add' | 'edit'>('add');
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.Flow>();
  const [selectedRowKeys, setSelectedRows] = useState<Key[]>([]);
  const { formatMsg } = useFormatMessage();
  const { submitRequest } = useSubmit();

  /** 删除流程，二次确认 */
  const showDeleteConfirm = async (selectedRows: Key[] | string[]) => {
    Modal.confirm({
      title: formatMsg('component.modalForm.confirm.title'),
      icon: <ExclamationCircleOutlined />,
      okType: 'danger',
      okText: formatMsg('component.modalForm.confirm'),
      cancelText: formatMsg('component.modalForm.cancel'),
      async onOk() {
        const success = await submitRequest(deleteFlow, selectedRows);
        if (success) {
          setSelectedRows([]);
          actionRef.current?.reloadAndRest?.();
        }
      },
    });
  };

  const columns: ProColumns<API.Flow>[] = [
    {
      title: formatMsg('pages.flowList.flows.name', '名称'),
      dataIndex: 'name',
      width: '18%',
      ellipsis: true,
    },
    {
      title: formatMsg('pages.flowList.flows.status', '状态'),
      dataIndex: 'status',
      width: '8%',
      filters: true,
      onFilter: true,
      valueType: 'select',
      valueEnum: {
        INIT: {
          text: formatMsg('pages.flowList.flows.status.init'),
          status: 'Default',
        },
        RUNNING: {
          text: formatMsg('pages.flowList.flows.status.running'),
          status: 'Processing',
        },
        FINISHED: {
          text: formatMsg('pages.flowList.flows.status.finished'),
          status: 'Success',
        },
        FAILED: {
          text: formatMsg('pages.flowList.flows.status.failed'),
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
        <a key="logs" onClick={() => {}}>
          <FormattedMessage id="pages.flowList.flows.logs" defaultMessage="日志" />
        </a>,
        <a
          key="delete"
          style={{ color: 'red' }}
          onClick={async () => {
            if (record?.id) {
              await showDeleteConfirm([record.id]);
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
