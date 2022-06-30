import { useFormatMessage } from '@/hooks';
import { userLogin, userRegister } from '@/services/api';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { LoginForm, ProFormText } from '@ant-design/pro-form';
import { Button, message } from 'antd';
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
        // 登陆成功，跳转首页
        await fetchUserInfo();
        history.push('/flows');
      }
    }
  };

  return (
    <div className="register-body">
      <LoginForm
        title={formatMsg('menu.logo.title')}
        subTitle={formatMsg('menu.list.register')}
        submitter={{
          render: () => {
            return (
              <Button type="primary" htmlType="submit" className="register-form-button">
                <FormattedMessage id="pages.register.form.submit" defaultMessage="注册" />
              </Button>
            );
          },
        }}
        actions={
          <div className="tips">
            <p>
              <FormattedMessage id="pages.register.form.tips" />
            </p>
          </div>
        }
        onFinish={async (values) => {
          await handleSubmit(values as API.LoginForm);
        }}
      >
        <ProFormText
          name="username"
          fieldProps={{
            size: 'large',
            prefix: <UserOutlined className={'prefixIcon'} />,
            autoComplete: 'on',
          }}
          placeholder={formatMsg('pages.login.username', '用户名')}
          rules={[
            {
              required: true,
              message: formatMsg('pages.login.username.required', '请输入用户名!'),
            },
          ]}
        />
        <ProFormText.Password
          name="password"
          fieldProps={{
            size: 'large',
            prefix: <LockOutlined className={'prefixIcon'} />,
            autoComplete: 'off',
          }}
          placeholder={formatMsg('pages.login.password', '密码')}
          rules={[
            {
              required: true,
              message: formatMsg('pages.login.password.required', '请输入密码!'),
            },
          ]}
        />
      </LoginForm>
    </div>
  );
};

export default Register;
