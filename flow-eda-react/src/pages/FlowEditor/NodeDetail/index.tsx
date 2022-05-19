import React, { useEffect } from 'react';
import './index.less';
import { Button, Form, Input, Select } from 'antd';
const { Option } = Select;

const FlowDetail: React.FC<{
  node: API.Node;
  showNodeDetail: (node: API.Node) => void;
  updateNode: (node: API.Node, params: object) => void;
}> = (props) => {
  const [form] = Form.useForm();

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
    console.log(detail);
    form.setFieldsValue(detail);
  }, []);

  const onFinish = (values: any) => {
    console.log(values);
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
          保存
        </Button>
      </div>
      <div className="detail-body">
        {props.node.output && (
          <div className="item">
            {/* <json-viewer :expand-depth=3 :value="node.output" copyable/> */}
            <hr />
          </div>
        )}

        <Form form={form} className="row" layout="vertical" onFinish={onFinish}>
          <Form.Item className="item" label="名称：" name="name" rules={[{ required: true }]}>
            <Input className="input" />
          </Form.Item>
          {props.node.nodeType?.params?.map((p) => {
            return (
              <div key={p.key}>
                {p.inType === 'input' && (
                  <Form.Item
                    label={p.name + '：'}
                    className="item"
                    name={p.key}
                    key={p.key}
                    rules={[{ required: p.required }]}
                  >
                    <Input placeholder={p.placeholder} className="input" />
                  </Form.Item>
                )}
                {p.inType === 'select' && p.placeholder && (
                  <Form.Item
                    label={p.name + '：'}
                    key={p.key}
                    className="item"
                    required={p.required}
                  >
                    <Input.Group compact>
                      <Form.Item name={p.key} noStyle>
                        <Input placeholder={p.placeholder.split(',')[0]} className="input-left" />
                      </Form.Item>
                      <Form.Item name={p.key + '-o'} noStyle>
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
                    label={p.name + '：'}
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
            label="自定义参数："
            tooltip="参数为json格式，可传递至下一节点，使用${xx}接收，例如${a,httpResult.$0.name}"
          >
            <Input.TextArea autoSize={true} className="input" placeholder="{'a':'xx','b':'123'}" />
          </Form.Item>
          <Form.Item className="item" label="备注：" name="remark">
            <Input.TextArea autoSize={true} className="input" />
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default FlowDetail;
