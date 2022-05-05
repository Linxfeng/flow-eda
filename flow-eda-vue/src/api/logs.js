import request from '../utils/request';

// 查询日志列表
export function listLogs(params) {
    return request({
        url: '/api/v1/logs',
        method: 'get',
        params: params
    });
}

// 删除日志文件
export function deleteLogs(body) {
    return request({
        url: '/api/v1/logs',
        method: 'delete',
        data: body
    });
}
