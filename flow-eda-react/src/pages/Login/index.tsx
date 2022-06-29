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
        logo={<img alt="logo" src="/logo.svg" />}
        title="流程管理系统"
        subTitle="用户登录"
        initialValues={{
          username: 'test',
          password: 'test',
        }}
        actions={
          <div className="tips">
            <p>提示：默认使用体验账号 test 登录，体验账号的数据会每日重置。</p>
            <p>
              若需要永久保存用户下的流程数据，请先&nbsp;
              <Link className="link" to="/register">
                注册账号
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
          }}
          placeholder={'用户名: admin or user'}
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id="pages.login.username.required"
                  defaultMessage="请输入用户名!"
                />
              ),
            },
          ]}
        />
        <ProFormText.Password
          name="password"
          fieldProps={{
            size: 'large',
            prefix: <LockOutlined className={'prefixIcon'} />,
          }}
          placeholder={'密码: ant.design'}
          rules={[
            {
              required: true,
              message: (
                <FormattedMessage
                  id="pages.login.password.required"
                  defaultMessage="请输入密码！"
                />
              ),
            },
          ]}
        />
      </LoginForm>
    </div>
  );
};

export default Login;
