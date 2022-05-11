// @ts-ignore
import { FormattedMessage } from 'umi';
import { ExclamationCircleOutlined, PlusOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-layout';
import { Button, message, Modal, Space } from 'antd';
import type { Key } from 'react';
import React, { useRef, useState } from 'react';
import type { ActionType, ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { addFlow, deleteFlow, getFlowList, updateFlow } from '@/services/api';
import { ModalForm, ProFormText, ProFormTextArea } from '@ant-design/pro-form';
import { generateUniqueID, useFormatMessage } from '@/utils/util';

const FlowList: React.FC = () => {
  const [modalFormVisible, handleModalFormVisible] = useState<boolean>(false);
  const [modalFormType, setModalFormType] = useState<'add' | 'edit'>('add');
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.Flow>();
  const [selectedRowKeys, setSelectedRows] = useState<Key[]>([]);
  const { formatMsg } = useFormatMessage();

  /** 新增流程 */
  const handleAdd = async (body: API.Flow) => {
    const hide = message.loading('Operating');
    try {
      body.id = generateUniqueID(8);
      await addFlow(body);
      hide();
      message.success('Successful operation');
      return true;
    } catch (error) {
      hide();
      return false;
    }
  };

  /** 更新流程 */
  const handleUpdate = async (body: API.Flow) => {
    const hide = message.loading('operating');
    try {
      await updateFlow(body);
      hide();
      message.success('Successful operation');
      return true;
    } catch (error) {
      hide();
      return false;
    }
  };

  /** 删除流程 */
  const handleRemove = async (selectedRows: Key[]) => {
    const hide = message.loading('operating');
    try {
      await deleteFlow(selectedRows);
      hide();
      message.success('Successful operation');
      setSelectedRows([]);
      actionRef.current?.reloadAndRest?.();
    } catch (error) {
      hide();
    }
  };

  /**删除二次确认*/
  const showDeleteConfirm = async (selectedRows: Key[]) => {
    Modal.confirm({
      title: formatMsg('component.modalForm.confirm.title'),
      icon: <ExclamationCircleOutlined />,
      okType: 'danger',
      okText: formatMsg('component.modalForm.confirm'),
      cancelText: formatMsg('component.modalForm.cancel'),
      onOk() {
        handleRemove(selectedRows);
      },
    });
  };

  const columns: ProColumns<API.Flow>[] = [
    {
      title: formatMsg('pages.flowList.flows.name', '名称'),
      dataIndex: 'name',
      ellipsis: true,
    },
    {
      title: formatMsg('pages.flowList.flows.status', '状态'),
      dataIndex: 'status',
      filters: true,
      onFilter: true,
      valueType: 'select',
      valueEnum: {
        INIT: {
          text: formatMsg('pages.flowList.flows.status.init'),
          status: 'INIT',
        },
        RUNNING: {
          text: formatMsg('pages.flowList.flows.status.running'),
          status: 'RUNNING',
        },
        FINISHED: {
          text: formatMsg('pages.flowList.flows.status.finished'),
          status: 'FINISHED',
        },
        FAILED: {
          text: formatMsg('pages.flowList.flows.status.failed'),
          status: 'FAILED',
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
      search: false,
    },
    {
      title: formatMsg('pages.flowList.flows.updateDate', '更新时间'),
      dataIndex: 'updateDate',
      valueType: 'dateTime',
      search: false,
    },
    {
      title: formatMsg('pages.flowList.flows.operate', '操作'),
      valueType: 'option',
      render: (text, record) => [
        <a
          key="show"
          onClick={() => {
            // 打开流编辑器
          }}
        >
          <FormattedMessage id="pages.flowList.flows.show" defaultMessage="查看" />
        </a>,
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
            <FormattedMessage id="pages.flowList.flows.chosen" defaultMessage="已选择" />
            &nbsp;{selectedRowKeys.length}&nbsp;
            <FormattedMessage id="pages.flowList.flows.item" defaultMessage="项" />
          </span>
        )}
        tableAlertOptionRender={() => {
          return (
            <Space size={16}>
              <a onClick={() => setSelectedRows([])}>
                <FormattedMessage id="pages.flowList.flows.clear" defaultMessage="取消选择" />
              </a>
              <a
                onClick={async () => {
                  await showDeleteConfirm(selectedRowKeys);
                }}
              >
                <FormattedMessage
                  id="pages.flowList.flows.batchDeletion"
                  defaultMessage="批量删除"
                />
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
              success = await handleUpdate(body);
            } else {
              success = await handleAdd(body);
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
