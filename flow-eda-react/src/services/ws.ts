import { wsIp } from '@/global';
import { refreshToken } from '@/services/api';
import { history } from '@@/core/history';
import { message } from 'antd';

const nodeWs = {};
const logWs = {};
const logContentWs = {};

function openWs(url: string, callback: (res: string) => void) {
  const urlWs = wsIp + url + '?access_token=';
  // websocket请求url带上token认证
  const socket = new WebSocket(urlWs + localStorage.getItem('access_token'));
  socket.onerror = async function () {
    // 刷新token
    const pass = await refreshToken();
    if (pass) {
      // 刷新token成功，重新之前的请求
      const ws = new WebSocket(urlWs + localStorage.getItem('access_token'));
      ws.onmessage = function (msg) {
        callback(msg.data);
      };
      return ws;
    }
    // 登录过期，跳转登陆页
    await message.error('登录过期', 1);
    history.push('/login');
    return null;
  };
  socket.onmessage = function (msg) {
    callback(msg.data);
  };
  return socket;
}

/** 监听流程运行时的节点信息 */
export function onOpenNode(id: string, callback: (res: string) => void) {
  if (Object.keys(nodeWs).length === 0 || !nodeWs[id]) {
    const url = ':8088/ws/flow/' + id + '/nodes';
    nodeWs[id] = openWs(url, callback);
  }
}

/** 监听流程运行时的日志信息 */
export function onOpenLogs(id: string, callback: (res: string) => void) {
  if (Object.keys(logWs).length === 0 || !logWs[id]) {
    const url = ':8082/ws/flow/' + id + '/logs';
    logWs[id] = openWs(url, callback);
  }
}

/** 接收日志文件内容 */
export function onOpenLogContent(path: string, callback: (res: string) => void) {
  if (Object.keys(logContentWs).length === 0 || !logContentWs[path]) {
    const url = ':8082/ws/logs/content/' + path.replaceAll('/', ':');
    logContentWs[path] = openWs(url, callback);
  }
}

function closeWs(ws: any, key: string) {
  if (ws && ws[key]) {
    try {
      ws[key].close();
    } catch (ignore) {}
    ws[key] = undefined;
  }
}

/** 关闭 nodeWs 连接 */
export function onCloseNode(id: string) {
  closeWs(nodeWs, id);
}

/** 关闭 logWs 连接 */
export function onCloseLogs(id: string) {
  closeWs(logWs, id);
}

/** 关闭 logContentWs 连接 */
export function onCloseLogContent(path: string) {
  closeWs(logContentWs, path);
}
