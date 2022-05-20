import React, { useEffect } from 'react';
import { Button, Form, Input, Select } from 'antd';
const { Option } = Select;
import './index.less';
import ReactJson from 'react-json-view';
import { useFormatMessage } from '@/hooks';
import { FormattedMessage } from '@@/plugin-locale/localeExports';

const FlowDetail: React.FC<{
  node: API.Node;
  updateNode: (node: API.Node, params: object) => void;
}> = (props) => {
  const { formatMsg } = useFormatMessage();
  const [form] = Form.useForm();

  /** 根据节点信息初始化表单内容*/
  useEffect(() => {
    const detail = { name: props.node.nodeName, remark: props.node.remark };
    if (props.node.payload) {
      // @ts-ignore
      detail.payload = JSON.stringify(props.node.payload);
    }
    const pv = props.node.params;
    if (pv) {
      Object.keys(pv).forEach((k) => (detail[k] = pv[k]));
    }
    const ps = props.node.nodeType?.params;
    if (ps && ps.length > 0) {
      ps.forEach((p) => {
        // 处理输入框+选择框=单个参数的情况，需要在form中拆成两个参数
        if (p.inType === 'select' && p.placeholder) {
          if (detail[p.key]) {
            detail[p.key + '-o'] = detail[p.key].split(',')[1];
            detail[p.key] = detail[p.key].split(',')[0];
          } else {
            detail[p.key + '-o'] = p.placeholder.split(',')[1];
          }
        }
      });
    }
    form.setFieldsValue(detail);
  }, []); // eslint-disable-line react-hooks/exhaustive-deps

  /** 校验json格式 */
  const checkJson = (rule: any, value: string, callback: (error?: string) => void) => {
    if (value) {
      if (value.startsWith('{') && value.endsWith('}')) {
        try {
          JSON.parse(value);
          callback();
        } catch (ignore) {}
      }
      callback(formatMsg('pages.flowList.editor.checkJson'));
    }
    callback();
  };

  /** 提交表单，更新节点信息 */
  const onFinish = (values: any) => {
    const newNode = JSON.parse(JSON.stringify(props.node));
    newNode.nodeName = values.name;
    if (values.remark) {
      newNode.remark = values.remark;
    }
    if (values.payload) {
      newNode.payload = JSON.parse(values.payload);
    } else {
      newNode.payload = null;
    }
    let params: any = {};
    Object.keys(values).forEach((k) => {
      if (k !== 'name' && k !== 'remark' && k !== 'payload' && values[k] !== null) {
        if (values[k + '-o']) {
          params[k] = values[k] + ',' + values[k + '-o'];
        } else if (!k.endsWith('-o')) {
          params[k] = values[k];
        }
      }
    });
    if (Object.keys(params).length === 0) {
      params = undefined;
    }
    props.updateNode(newNode, params);
  };

  return (
    <div className="node-detail">
      <div className="detail-header">
        <div className="title">{props.node.nodeType?.typeName}</div>
        <Button
          type="primary"
          className="button"
          style={{ float: 'right' }}
          onClick={form.submit}
          htmlType="submit"
        >
          <FormattedMessage id="component.option.save" defaultMessage="保存" />
        </Button>
      </div>
      <div className="detail-body">
        {props.node.output && (
          <div className="item">
            {/* @ts-ignore */}
            <ReactJson
              src={props.node.output}
              collapsed={3}
              name={false}
              displayDataTypes={false}
              displayObjectSize={false}
              quotesOnKeys={false}
              style={{ fontFamily: 'consolas', backgroundColor: '#ffffff' }}
            />
            <hr />
          </div>
        )}

        <Form form={form} className="row" layout="vertical" onFinish={onFinish}>
          <Form.Item
            className="item"
            label={formatMsg('pages.flowList.flows.name', '名称')}
            name="name"
            rules={[{ required: true }]}
          >
            <Input className="input" />
          </Form.Item>
          {props.node.nodeType?.params?.map((p) => {
            return (
              <div key={p.key}>
                {p.inType === 'input' && (
                  <Form.Item
                    label={p.name}
                    className="item"
                    name={p.key}
                    key={p.key}
                    rules={[{ required: p.required }]}
                  >
                    <Input placeholder={p.placeholder} className="input" />
                  </Form.Item>
                )}
                {p.inType === 'select' && p.placeholder && (
                  <Form.Item label={p.name} key={p.key} className="item" required={p.required}>
                    <Input.Group compact>
                      <Form.Item name={p.key} noStyle rules={[{ required: p.required }]}>
                        <Input placeholder={p.placeholder.split(',')[0]} className="input-left" />
                      </Form.Item>
                      <Form.Item name={p.key + '-o'} noStyle rules={[{ required: p.required }]}>
                        <Select className="input-right">
                          {p.option?.split(',')?.map((op: string) => {
                            return (
                              <Option value={op} key={op}>
                                {op}
                              </Option>
                            );
                          })}
                        </Select>
                      </Form.Item>
                    </Input.Group>
                  </Form.Item>
                )}
                {p.inType === 'select' && !p.placeholder && (
                  <Form.Item
                    label={p.name}
                    className="item"
                    name={p.key}
                    key={p.key}
                    rules={[{ required: p.required }]}
                  >
                    <Select className="input">
                      {p.option?.split(',')?.map((op: string) => {
                        return (
                          <Option value={op} key={op}>
                            {op}
                          </Option>
                        );
                      })}
                    </Select>
                  </Form.Item>
                )}
              </div>
            );
          })}

          <Form.Item
            className="item"
            name="payload"
            label={formatMsg('pages.flowList.editor.parameters', '自定义参数')}
            tooltip="参数为json格式，可传递至下一节点，使用${xx}接收，例如${a,httpResult.$0.name}"
            rules={[{ validator: checkJson }]}
          >
            <Input.TextArea autoSize={true} className="input" placeholder="{'a':'xx','b':'123'}" />
          </Form.Item>
          <Form.Item
            className="item"
            label={formatMsg('pages.flowList.editor.remark', '备注')}
            name="remark"
          >
            <Input.TextArea autoSize={true} className="input" />
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default FlowDetail;
