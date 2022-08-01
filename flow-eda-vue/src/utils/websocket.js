import { ElMessage } from "element-plus";
import { refreshToken } from "../api/oauth2";

/** 创建WebSocket连接 */
export function newWebSocket(url, callback) {
  const socket = new WebSocket(urlWithToken(url));
  socket.onerror = async function (e) {
    // 刷新token
    const pass = await refreshToken();
    if (pass) {
      // 刷新token成功，重新请求
      const ws = new WebSocket(urlWithToken(url));
      ws.onmessage = function (msg) {
        callback(msg.data);
      };
      return ws;
    } else {
      // 刷新token失败，退出登录
      ElMessage.error("登录过期");
      location.href = "/";
    }
  };
  socket.onmessage = function (msg) {
    callback(msg.data);
  };
  return socket;
}

function urlWithToken(url) {
  return (
    window.$wsIp + url + "?access_token=" + localStorage.getItem("access_token")
  );
}
