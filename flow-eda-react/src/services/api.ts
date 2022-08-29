// @ts-ignore
import { request } from 'umi';
import type { Key } from 'react';

/** 获取流程列表 */
export async function getFlowList(params: any) {
  const { result, total } = await request('/api/v1/flow', {
    method: 'GET',
    params: {
      limit: params?.pageSize,
      page: params?.current,
      status: params?.status,
      name: params?.name,
    },
  });
  return { data: result, total };
}

/** 新增流程 */
export async function addFlow(body: API.Flow) {
  return request<API.ApiResult>('/api/v1/flow', {
    method: 'POST',
    data: body,
  });
}

/** 更新流程 */
export async function updateFlow(body: API.Flow) {
  return request<API.ApiResult>('/api/v1/flow', {
    method: 'PUT',
    data: body,
  });
}

/** 删除流程 */
export async function deleteFlow(body: Key[]) {
  return request<API.ApiResult>('/api/v1/flow', {
    method: 'DELETE',
    data: body,
  });
}

/** 获取日志列表 */
export async function getLogList(params: any) {
  const { result, total } = await request('/api/v1/logs', {
    method: 'GET',
    params: {
      limit: params?.pageSize,
      page: params?.current,
      type: params?.type,
    },
  });
  return { data: result, total };
}

/** 删除日志 */
export async function deleteLogs(body: Key[]) {
  return request<API.ApiResult>('/api/v1/logs', {
    method: 'DELETE',
    data: body,
  });
}

/** 获取节点类型 */
export async function getNodeTypes() {
  const { result } = await request('/api/v1/node/type', {
    method: 'GET',
  });
  return result;
}

/** 获取流程节点数据 */
export async function getFlowData(params: any) {
  return request<API.ApiResult>('/api/v1/node/data', {
    method: 'GET',
    params: params,
  });
}

/** 获取流程数据版本 */
export async function getVersion(id: string) {
  return request<API.ApiResult>('/api/v1/node/data/version', {
    method: 'GET',
    params: { flowId: id },
  });
}

/** 更新流程节点数据 */
export async function setFlowData(body: any) {
  return request<API.ApiResult>('/api/v1/node/data', {
    method: 'POST',
    data: body,
  });
}

/** 保存流程数据版本 */
export async function saveVersion(version: string, body: any) {
  return request<API.ApiResult>('/api/v1/node/data/version?version=' + version, {
    method: 'POST',
    data: body,
  });
}

/** 运行流程 */
export async function runFlow(id: string) {
  return request<API.ApiResult>('/api/v1/node/data/run?flowId=' + id, {
    method: 'POST',
  });
}

/** 停止流程 */
export async function stopFlow(id: string) {
  return request<API.ApiResult>('/api/v1/node/data/stop?flowId=' + id, {
    method: 'POST',
  });
}

/** 获取客户端信息 */
async function getClientInfo() {
  const clientId = localStorage.getItem('client_id');
  const clientSecret = localStorage.getItem('client_secret');
  if (!clientId || !clientSecret) {
    const res = await request('/oauth/client', {
      method: 'GET',
    });
    if (res && res.result) {
      localStorage.setItem('client_id', res.result.clientId);
      localStorage.setItem('client_secret', res.result.clientSecret);
    }
  }
}

/** 获取Basic认证信息 */
async function getBasicInfo() {
  await getClientInfo();
  const clientId = localStorage.getItem('client_id');
  const clientSecret = localStorage.getItem('client_secret');
  return 'Basic ' + btoa(clientId + ':' + clientSecret);
}

/** 获取token */
async function getOauthToken(body: any) {
  const basic = await getBasicInfo();
  const res = await request('/oauth/token', {
    method: 'POST',
    headers: {
      Authorization: basic,
      'Content-Type': 'application/x-www-form-urlencoded',
    },
    data: body,
  });
  if (res && res.access_token && res.refresh_token) {
    localStorage.setItem('access_token', res.access_token);
    localStorage.setItem('refresh_token', res.refresh_token);
    return true;
  }
  return false;
}

/** 用户注册 */
export async function userRegister(body: any) {
  return request('/oauth/register', {
    method: 'POST',
    data: body,
  });
}

/** 用户登录 */
export async function userLogin(form: API.LoginForm) {
  const params = new URLSearchParams();
  params.append('grant_type', 'password');
  params.append('username', form.username);
  params.append('password', form.password);
  return await getOauthToken(params);
}

/** 用户登出 */
export async function userLogout() {
  const res = await request('/oauth/logout', {
    method: 'POST',
  });
  if (res) {
    localStorage.removeItem('access_token');
    localStorage.removeItem('refresh_token');
  }
}

/** 获取当前用户信息 */
export async function userInfo() {
  const { result } = await request('/oauth/me', {
    method: 'GET',
  });
  return result;
}

/** 刷新token */
export async function refreshToken() {
  const refresh_token = localStorage.getItem('refresh_token');
  if (refresh_token) {
    const params = new URLSearchParams();
    params.append('grant_type', 'refresh_token');
    params.append('refresh_token', refresh_token);
    return await getOauthToken(params);
  }
  return false;
}
