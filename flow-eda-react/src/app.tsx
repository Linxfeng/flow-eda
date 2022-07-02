import RightContent from '@/components/RightContent';
import { refreshToken, userInfo } from '@/services/api';
// @ts-ignore
import type { RequestConfig } from '@@/plugin-request/request';
import { message } from 'antd';
import type { RunTimeLayoutConfig } from 'umi';
import { history, request as req } from 'umi';
import type { RequestOptionsInit } from 'umi-request';

/** 请求拦截器，在请求头添加AccessToken */
const authHeaderInterceptor = (url: string, options: RequestOptionsInit) => {
  // 统一配置请求头
  let headers = options.headers;
  if (!headers) {
    headers = { 'Content-Type': 'application/json;charset=utf-8' };
  }
  headers['Access-Control-Allow-Origin'] = '*';
  // 带上token
  const token = localStorage.getItem('access_token');
  if (token && url !== '/oauth/token') {
    // @ts-ignore
    headers.Authorization = 'Bearer ' + token;
  }
  return {
    url: `${url}`,
    options: { ...options, interceptors: true, headers: headers },
  };
};

/** 响应拦截器，状态401时RefreshToken */
const authResponseInterceptor = async (response: Response, options: RequestOptionsInit) => {
  // 未授权
  if (response.status === 401) {
    // 刷新token
    const pass = await refreshToken();
    if (pass) {
      // 刷新token成功，重新之前的请求
      return await req(options.url, options);
    }
    // 登录过期，跳转登陆页
    await message.error('登录过期', 1);
    history.push('/login');
  }
  return response;
};

/**配置request adaptor，统一异常处理*/
export const request: RequestConfig = {
  errorConfig: {
    adaptor: (resData: any) => {
      return {
        success: true,
        errorMessage: resData.message ? resData.message : resData.error,
        errorCode: resData.status,
      };
    },
  },
  // 配置请求拦截器
  requestInterceptors: [authHeaderInterceptor],
  // 配置响应拦截器
  responseInterceptors: [authResponseInterceptor],
};

// 配置initialState，存储当前用户信息
export async function getInitialState(): Promise<{
  currentUser?: API.CurrentUser;
  loading?: boolean;
  fetchUserInfo?: () => Promise<API.CurrentUser | undefined>;
}> {
  const fetchUserInfo = async () => {
    return await userInfo();
  };
  // 如果不是登录/注册页面，需要重新获取当前用户
  if (
    history.location.pathname !== '/' &&
    history.location.pathname !== '/login' &&
    history.location.pathname !== '/register'
  ) {
    const currentUser = await fetchUserInfo();
    return {
      fetchUserInfo,
      currentUser,
    };
  }
  return {
    fetchUserInfo,
  };
}

// 配置ProLayout
export const layout: RunTimeLayoutConfig = () => {
  return {
    rightContentRender: () => <RightContent />,
    disableContentMargin: false,
    menuHeaderRender: undefined,
  };
};
