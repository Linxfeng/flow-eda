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

/** 获取流程数据 */
export async function getFlowData(id: string) {
  return request<API.ApiResult>('/api/v1/node/data', {
    method: 'GET',
    params: { flowId: id },
  });
}

/** 获取当前的用户 GET /api/currentUser */
export async function currentUser(options?: { [key: string]: any }) {
  return request<{
    data: API.CurrentUser;
  }>('/api/currentUser', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 退出登录接口 POST /api/login/outLogin */
export async function outLogin(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/login/outLogin', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 登录接口 POST /api/login/account */
export async function login(body: API.LoginParams, options?: { [key: string]: any }) {
  return request<API.LoginResult>('/api/login/account', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /api/notices */
export async function getNotices(options?: { [key: string]: any }) {
  return request<API.NoticeIconList>('/api/notices', {
    method: 'GET',
    ...(options || {}),
  });
}

/** 获取规则列表 GET /api/rule */
export async function rule(
  params: {
    // query
    /** 当前的页码 */
    current?: number;
    /** 页面的容量 */
    pageSize?: number;
  },
  options?: { [key: string]: any },
) {
  return request<API.RuleList>('/api/rule', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 新建规则 PUT /api/rule */
export async function updateRule(options?: { [key: string]: any }) {
  return request<API.RuleListItem>('/api/rule', {
    method: 'PUT',
    ...(options || {}),
  });
}

/** 新建规则 POST /api/rule */
export async function addRule(options?: { [key: string]: any }) {
  return request<API.RuleListItem>('/api/rule', {
    method: 'POST',
    ...(options || {}),
  });
}

/** 删除规则 DELETE /api/rule */
export async function removeRule(options?: { [key: string]: any }) {
  return request<Record<string, any>>('/api/rule', {
    method: 'DELETE',
    ...(options || {}),
  });
}
