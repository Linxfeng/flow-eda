import { useFormatMessage } from '@/hooks';
import { userLogin } from '@/services/api';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { LoginForm, ProFormText } from '@ant-design/pro-form';
import { message } from 'antd';
import React from 'react';
import { FormattedMessage, history, Link, useModel } from 'umi';
import './index.less';

const Login: React.FC = () => {
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

  const handleSubmit = async (values: API.LoginForm) => {
    // 用户登录
    const pass = await userLogin(values);
    if (pass) {
      message.success(formatMsg('pages.login.success'));
      await fetchUserInfo();
      // 登陆成功，跳转首页
      history.push('/flows');
    }
  };

  return (
    <div className="login-body">
      <LoginForm
        title={formatMsg('menu.logo.title')}
        subTitle={formatMsg('menu.list.login')}
        initialValues={{
          username: 'test',
          password: 'test',
        }}
        actions={
          <div className="tips">
            <p>
              <FormattedMessage id="pages.login.form.tips1" />
            </p>
            <p>
              <FormattedMessage id="pages.login.form.tips2" />
              &nbsp;
              <Link className="link" to="/register">
                <FormattedMessage id="pages.login.form.tips.register" defaultMessage="注册账号" />
              </Link>
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

export default Login;
