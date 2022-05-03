import request from '../utils/request';

// 查询流程列表
export function listFlow(params) {
    return request({
        url: '/api/v1/flow',
        method: 'get',
        params: params
    });
}

// 新增流程
export function addFlow(body) {
    return request({
        url: '/api/v1/flow',
        method: 'post',
        data: body
    });
}

// 更新流程
export function updateFlow(body) {
    return request({
        url: '/api/v1/flow',
        method: 'put',
        data: body
    });
}

// 删除流程
export function deleteFlow(body) {
    return request({
        url: '/api/v1/flow',
        method: 'delete',
        data: body
    });
}
