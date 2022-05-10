// @ts-ignore
import { FormattedMessage, useIntl } from 'umi';
import { PlusOutlined } from '@ant-design/icons';
import { Button, message } from 'antd';
import React, { useRef, useState } from 'react';
import { PageContainer } from '@ant-design/pro-layout';
import type { ActionType, ProColumns } from '@ant-design/pro-table';
import ProTable from '@ant-design/pro-table';
import { addFlow, deleteFlow, getFlowList, updateFlow } from '@/services/api';
import {ModalForm, ProFormText, ProFormTextArea} from "@ant-design/pro-form";


/**新增流程*/
const handleAdd = async (fields: API.Flow) => {
  const hide = message.loading('Operating');
  try {
    await addFlow(fields);
    hide();
    message.success('Successful operation');
    return true;
  } catch (error) {
    hide();
    return false;
  }
};

/**
 * @en-US Update Flow
 * @zh-CN 更新流程
 * @param fields
 */
const handleUpdate = async (fields: API.Flow) => {
  const hide = message.loading('operating');
  try {
    await updateFlow({
      name: fields.name,
      desc: fields.desc,
    });
    hide();
    message.success('Successful operation');
    return true;
  } catch (error) {
    hide();
    message.error('Configuration failed, please try again!');
    return false;
  }
};

/**
 * @en-US Delete Flow
 * @zh-CN 删除流程
 * @param selectedRows
 */
const handleRemove = async (selectedRows: API.Flow[]) => {
  const hide = message.loading('operating');
  if (!selectedRows) return true;
  try {
    await deleteFlow({
      ids: selectedRows.map((row) => row.id),
    });
    hide();
    message.success('Successful operation');
    return true;
  } catch (error) {
    hide();
    message.error('Delete failed, please try again');
    return false;
  }
};

const FlowList: React.FC = () => {
  const [createModalVisible, handleModalVisible] = useState<boolean>(false);
  const actionRef = useRef<ActionType>();
  const [currentRow, setCurrentRow] = useState<API.Flow>();
  const [selectedRowsState, setSelectedRows] = useState<API.RuleListItem[]>([]);
  /**
   * @en-US International configuration
   * @zh-CN 国际化配置
   * */
  const { formatMessage } = useIntl();

  const columns: ProColumns<API.Flow>[] = [
    {
      title: formatMessage({ id: "pages.flowList.flows.name", defaultMessage: "名称"}),
      dataIndex: 'name',
      ellipsis: true,
    },
    {
      title: formatMessage({ id:"pages.flowList.flows.status", defaultMessage: "状态"}),
      dataIndex: 'status',
      filters: true,
      onFilter: true,
      valueType: 'select',
      valueEnum: {
        init: {
          text: formatMessage({ id:"pages.flowList.flows.status.init"}),
          status: 'INIT',
        },
        RUNNING: {
          text: formatMessage({ id:"pages.flowList.flows.status.running"}),
          status: 'RUNNING',
        },
        FINISHED: {
          text: formatMessage({ id:"pages.flowList.flows.status.finished"}),
          status: 'FINISHED',
        },
        FAILED: {
          text: formatMessage({ id:"pages.flowList.flows.status.failed"}),
          status: 'FAILED',
        },
      },
    },
    {
      title: formatMessage({ id:"pages.flowList.flows.desc", defaultMessage: "描述"}),
      dataIndex: 'description',
      ellipsis: true,
      search: false
    },
    {
      title: formatMessage({ id:"pages.flowList.flows.createDate", defaultMessage: "创建时间"}),
      dataIndex: 'createDate',
      valueType: 'dateTime',
      search: false
    },
    {
      title: formatMessage({ id:"pages.flowList.flows.updateDate", defaultMessage: "更新时间"}),
      dataIndex: 'updateDate',
      valueType: 'dateTime',
      search: false
    },
    {
      title: formatMessage({ id:"pages.flowList.flows.operate", defaultMessage: "操作"}),
      valueType: 'option',
      render: (text, record, _, action) => [
        <a
          key="editable"
          onClick={() => {
            action?.startEditable?.(record.id);
          }}
        >
          查看
        </a>,
        <a href={record.url} target="_blank" rel="noopener noreferrer" key="view">
          编辑
        </a>,
      ],
    },
  ];

  return (
    <PageContainer>
      <ProTable<API.Flow>
        headerTitle={formatMessage({
          id: 'pages.flowList.flows.title',
          defaultMessage: '流程列表',
        })}
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
          onChange: (_, selectedRows) => {
            setSelectedRows(selectedRows);
          },
        }}
        toolBarRender={() => [
          <Button
            type="primary"
            key="primary"
            onClick={() => {
              handleModalVisible(true);
            }}
          >
            <PlusOutlined />
            <FormattedMessage id="pages.flowList.flows.add" defaultMessage="新增" />
          </Button>,
          <Button
            onClick={async () => {
              await handleRemove(selectedRowsState);
              setSelectedRows([]);
              actionRef.current?.reloadAndRest?.();
            }}
          >
            <FormattedMessage id="pages.flowList.flows.batchDeletion" defaultMessage="批量删除" />
          </Button>,
        ]}
      />

      <ModalForm
        title={formatMessage({
          id: 'pages.flowList.flows.addFlow',
          defaultMessage: '新增流程',
        })}
        width="600px"
        visible={createModalVisible}
        onVisibleChange={handleModalVisible}
        onFinish={async (value) => {
          const success = await handleAdd(value as API.Flow);
          if (success) {
            handleModalVisible(false);
            if (actionRef.current) {
              actionRef.current.reload();
            }
          }
        }}
      >
        <ProFormText
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id="pages.searchTable.ruleName"
                  defaultMessage="Rule name is required"
                />
              ),
            },
          ]}
          width="md"
          name="name"
        />
        <ProFormTextArea width="md" name="desc" />
      </ModalForm>
    </PageContainer>
  );
};

export default FlowList;
