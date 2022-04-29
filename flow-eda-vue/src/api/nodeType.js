import request from '../utils/request';

// 查询节点类型
export function getNodeTypes(params) {
    return request({
        url: '/api/v1/node/type',
        method: 'get',
        params: params
    });
}
