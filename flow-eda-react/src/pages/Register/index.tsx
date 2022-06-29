import { useFormatMessage } from '@/hooks';
import { userLogin, userRegister } from '@/services/api';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { Button, Form, Input, message } from 'antd';
import React from 'react';
import { FormattedMessage, history, useModel } from 'umi';
import './index.less';

const Register: React.FC = () => {
  const { formatMsg } = useFormatMessage();
  const { initialState, setInitialState } = useModel('@@initialState');

  const fetchUserInfo = async () => {
    const user = await initialState?.fetchUserInfo?.();
    if (user) {
      await setInitialState((s) => ({
        ...s,
        currentUser: user,
      }));
    }
  };

  const handleSubmit = async (form: API.LoginForm) => {
    // 用户注册
    const res = await userRegister(form);
    if (res) {
      message.success(formatMsg('pages.register.success', '注册成功'));
      // 自动登录
      const pass = await userLogin(form);
      if (pass) {
        await fetchUserInfo();
        // 登陆成功，跳转首页
        history.push('/flows');
      }
    }
  };

  return (
    <div className="register-body">
      <Form name="register" className="register-form" onFinish={handleSubmit}>
        <Form.Item>
          <div className="register-form-title">
            <FormattedMessage id="menu.logo.title" defaultMessage="流程管理系统" />
          </div>
        </Form.Item>
        <Form.Item>
          <div className="register-form-subTitle">
            <FormattedMessage id="menu.list.register" defaultMessage="用户注册" />
          </div>
        </Form.Item>
        <Form.Item
          name="username"
          rules={[
            {
              required: true,
              message: formatMsg('pages.login.username.required', '请输入用户名!'),
            },
          ]}
        >
          <Input
            prefix={<UserOutlined className={'prefixIcon'} />}
            placeholder={formatMsg('pages.login.username', '用户名')}
          />
        </Form.Item>
        <Form.Item
          name="password"
          rules={[
            {
              required: true,
              message: formatMsg('pages.login.password.required', '请输入密码!'),
            },
          ]}
        >
          <Input
            prefix={<LockOutlined className={'prefixIcon'} />}
            type="password"
            autoComplete="off"
            placeholder={formatMsg('pages.login.password', '密码')}
          />
        </Form.Item>
        <Form.Item>
          <Button type="primary" htmlType="submit" className="register-form-button">
            <FormattedMessage id="pages.register.form.submit" defaultMessage="注册" />
          </Button>
          <div className="tips">
            <p>
              <FormattedMessage id="pages.register.form.tips" />
            </p>
          </div>
        </Form.Item>
      </Form>
    </div>
  );
};

export default Register;
