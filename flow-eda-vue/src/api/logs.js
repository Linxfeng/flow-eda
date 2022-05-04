import request from '../utils/request';

// 查询日志列表
export function listLogs(params) {
    return request({
        url: '/api/v1/logs',
        method: 'get',
        params: params
    });
}
