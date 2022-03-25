import request from '../utils/request'

// 查询工作流列表
export function listFlows(params) {
    return request({
        url: '/api/v1/flows',
        method: 'get',
        params: params
    })
}