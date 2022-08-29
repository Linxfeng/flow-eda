import { newWebSocket } from "../utils/websocket";

let ws = {};
let logWs = {};
let logContentWs = {};

// 建立连接，监听消息并进行回调
export function onOpen(id, callback) {
  if (Object.keys(ws).length === 0 || !ws[id]) {
    const url = ":8088/ws/flow/" + id + "/nodes";
    ws[id] = newWebSocket(url, callback);
  }
}

// 关闭连接
export function onClose(id) {
  close(ws, id);
}

// 监听日志消息
export function onOpenLogs(id, callback) {
  if (Object.keys(logWs).length === 0 || !logWs[id]) {
    const url = ":8082/ws/flow/" + id + "/logs";
    logWs[id] = newWebSocket(url, callback);
  }
}

export function onCloseLogs(id) {
  close(logWs, id);
}

// 接收日志文件内容
export function onOpenLogDetail(path, callback) {
  if (Object.keys(logContentWs).length === 0 || !logContentWs[path]) {
    const url = ":8082/ws/logs/content/" + path.replaceAll("/", ":");
    logContentWs[path] = newWebSocket(url, callback);
  }
}

export function onCloseLogDetail(path) {
  close(logContentWs, path);
}

function close(ws_obj, id) {
  if (ws_obj && ws_obj[id]) {
    try {
      ws_obj[id].close();
    } catch (ignore) {}
    ws_obj[id] = null;
  }
}
