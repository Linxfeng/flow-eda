import RightContent from '@/components/RightContent';
import { userInfo } from '@/services/api';
import type { RequestConfig } from '@@/plugin-request/request';
import type { RunTimeLayoutConfig } from 'umi';
import { history } from 'umi';
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
  if (history.location.pathname !== '/login' && history.location.pathname !== '/register') {
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
