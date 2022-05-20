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
    params: { limit: 1000 },
  });
  return result;
}

/** 获取流程节点数据 */
export async function getFlowData(id: string) {
  return request<API.ApiResult>('/api/v1/node/data', {
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
