import request from '../utils/request';

// 查询节点类型
export function getNodeTypes(params) {
    return request({
        url: '/api/v1/node/type',
        method: 'get',
        params: params
    });
}

// 新增节点类型
export function addNodeType(body) {
    return request({
        url: '/api/v1/node/type',
        method: 'post',
        data: body
    });
}

// 修改节点类型
export function updateNodeType(body) {
    return request({
        url: '/api/v1/node/type',
        method: 'put',
        data: body
    });
}

// 删除节点类型
export function deleteNodeType(body) {
    return request({
        url: '/api/v1/node/type',
        method: 'delete',
        data: body
    });
}
