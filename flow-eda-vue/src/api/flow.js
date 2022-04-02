import request from '../utils/request';

// 查询工作流列表
export function listFlow(params) {
    return request({
        url: '/api/v1/flow',
        method: 'get',
        params: params
    });
}

// 新增工作流
export function addFlow(body) {
    return request({
        url: '/api/v1/flow',
        method: 'post',
        data: body
    });
}

// 修改/启用/禁用工作流
export function updateFlow(body) {
    return request({
        url: '/api/v1/flow',
        method: 'put',
        data: body
    });
}

// 删除工作流
export function deleteFlow(body) {
    return request({
        url: '/api/v1/flow',
        method: 'delete',
        data: body
    });
}
