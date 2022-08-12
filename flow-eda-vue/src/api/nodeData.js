import request from "../utils/request";

// 获取节点数据
export function getNodeData(params) {
  return request({
    url: "/api/v1/node/data",
    method: "get",
    params: params,
  });
}

// 更新节点数据
export function setNodeData(body) {
  return request({
    url: "/api/v1/node/data",
    method: "post",
    data: body,
  });
}

// 保存流程数据版本
export function saveVersion(version, body) {
  return request({
    url: "/api/v1/node/data/version?version=" + version,
    method: "post",
    data: body,
  });
}

// 运行本流程
export function executeNodeData(flowId) {
  return request({
    url: "/api/v1/node/data/run?flowId=" + flowId,
    method: "post",
  });
}

// 停止本流程
export function stopNodeData(flowId) {
  return request({
    url: "/api/v1/node/data/stop?flowId=" + flowId,
    method: "post",
  });
}
